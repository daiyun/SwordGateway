package com.doctorwork.sword.gateway.loadbalance;

import com.google.common.base.Strings;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import com.netflix.util.Pair;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerRequest;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * @Author:czq
 * @Description:
 * @Date: 18:14 2019/6/28
 * @Modified By:
 */
public abstract class AbstractLoadBalanceClient implements LoadBalancerClient {
    private static List<Integer> securePorts = Arrays.asList(443, 8443);
    private static final Map<String, String> unsecureSchemeMapping;

    static {
        unsecureSchemeMapping = new HashMap<>();
        unsecureSchemeMapping.put("http", "https");
        unsecureSchemeMapping.put("ws", "wss");
    }


    @Override
    public <T> T execute(String serviceId, LoadBalancerRequest<T> request) throws IOException {
        Server server = getServer(serviceId);
        if (server == null) {
            throw new IllegalStateException("No instances available for " + serviceId);
        }
        AbstractLoadBalanceClient.LoadBalanceServer ribbonServer = new AbstractLoadBalanceClient.LoadBalanceServer(serviceId, server,
                isSecure(server.getPort()), Collections.emptyMap());

        return execute(serviceId, ribbonServer, request);
    }

    @Override
    public <T> T execute(String serviceId, ServiceInstance serviceInstance, LoadBalancerRequest<T> request) throws IOException {
        Server server = null;
        if (serviceInstance instanceof AbstractLoadBalanceClient.LoadBalanceServer) {
            server = ((AbstractLoadBalanceClient.LoadBalanceServer) serviceInstance).getServer();
        }
        if (server == null) {
            throw new IllegalStateException("No instances available for " + serviceId);
        }

        try {
            T returnVal = request.apply(serviceInstance);
            return returnVal;
        }
        // catch IOException and rethrow so RestTemplate behaves correctly
        catch (IOException ex) {
            throw ex;
        } catch (Exception ex) {
            ReflectionUtils.rethrowRuntimeException(ex);
        }
        return null;
    }

    @Override
    public URI reconstructURI(ServiceInstance instance, URI original) {
        Assert.notNull(instance, "instance can not be null");
        URI uri;
        Server server;
        if (instance instanceof AbstractLoadBalanceClient.LoadBalanceServer) {
            AbstractLoadBalanceClient.LoadBalanceServer loadBalanceServer = (AbstractLoadBalanceClient.LoadBalanceServer) instance;
            server = loadBalanceServer.getServer();
            uri = updateToSecureConnectionIfNeeded(original, loadBalanceServer.getPort());
        } else {
            server = new Server(instance.getScheme(), instance.getHost(), instance.getPort());
            uri = updateToSecureConnectionIfNeeded(original, server.getPort());
        }
        return reconstructURIWithServer(server, uri);
    }

    protected URI reconstructURIWithServer(Server server, URI original) {
        String host = server.getHost();
        int port = server.getPort();
        String scheme = server.getScheme();

        if (host.equals(original.getHost())
                && port == original.getPort()
                && scheme == original.getScheme()) {
            return original;
        }
        if (scheme == null) {
            scheme = original.getScheme();
        }
        if (scheme == null) {
            scheme = deriveSchemeAndPortFromPartialUri(original).first();
        }

        try {
            StringBuilder sb = new StringBuilder();
            sb.append(scheme).append("://");
            if (!Strings.isNullOrEmpty(original.getRawUserInfo())) {
                sb.append(original.getRawUserInfo()).append("@");
            }
            sb.append(host);
            if (port >= 0) {
                sb.append(":").append(port);
            }
            sb.append(original.getRawPath());
            if (!Strings.isNullOrEmpty(original.getRawQuery())) {
                sb.append("?").append(original.getRawQuery());
            }
            if (!Strings.isNullOrEmpty(original.getRawFragment())) {
                sb.append("#").append(original.getRawFragment());
            }
            URI newURI = new URI(sb.toString());
            return newURI;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    protected Pair<String, Integer> deriveSchemeAndPortFromPartialUri(URI uri) {
        boolean isSecure = false;
        String scheme = uri.getScheme();
        if (scheme != null) {
            isSecure = scheme.equalsIgnoreCase("https");
        }
        int port = uri.getPort();
        if (port < 0 && !isSecure) {
            port = 80;
        } else if (port < 0 && isSecure) {
            port = 443;
        }
        if (scheme == null) {
            if (isSecure) {
                scheme = "https";
            } else {
                scheme = "http";
            }
        }
        return new Pair<String, Integer>(scheme, port);
    }

    public static URI updateToSecureConnectionIfNeeded(URI uri, Integer port) {
        String scheme = uri.getScheme();
        if (StringUtils.isEmpty(scheme)) {
            scheme = "http";
        }
        if (!StringUtils.isEmpty(uri.toString())
                && unsecureSchemeMapping.containsKey(scheme)
                && isSecure(port)) {
            return upgradeConnection(uri, unsecureSchemeMapping.get(scheme));
        }
        return uri;
    }

    private static URI upgradeConnection(URI uri, String scheme) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUri(uri).scheme(scheme);
        if (uri.getRawQuery() != null) {
            // When building the URI, UriComponentsBuilder verify the allowed characters and does not
            // support the '+' so we replace it for its equivalent '%20'.
            // See issue https://jira.spring.io/browse/SPR-10172
            uriComponentsBuilder.replaceQuery(uri.getRawQuery().replace("+", "%20"));
        }
        return uriComponentsBuilder.build(true).toUri();
    }

    @Override
    public ServiceInstance choose(String serviceId) {
        Server server = getServer(serviceId);
        if (server == null) {
            return null;
        }
        return new AbstractLoadBalanceClient.LoadBalanceServer(serviceId, server, isSecure(server.getPort()), Collections.emptyMap());
    }

    private static boolean isSecure(Integer port) {
        return securePorts.contains(port);
    }

    protected abstract Server getServer(String serviceId);

    protected abstract ILoadBalancer getLoadBalancer(String serviceId);

    public static class LoadBalanceServer implements ServiceInstance {
        private final String serviceId;
        private final Server server;
        private final boolean secure;
        private Map<String, String> metadata;

        public LoadBalanceServer(String serviceId, Server server) {
            this(serviceId, server, false, Collections.<String, String>emptyMap());
        }

        public LoadBalanceServer(String serviceId, Server server, boolean secure,
                                 Map<String, String> metadata) {
            this.serviceId = serviceId;
            this.server = server;
            this.secure = secure;
            this.metadata = metadata;
        }

        @Override
        public String getServiceId() {
            return this.serviceId;
        }

        @Override
        public String getHost() {
            return this.server.getHost();
        }

        @Override
        public int getPort() {
            return this.server.getPort();
        }

        @Override
        public boolean isSecure() {
            return this.secure;
        }

        @Override
        public URI getUri() {
            return DefaultServiceInstance.getUri(this);
        }

        @Override
        public Map<String, String> getMetadata() {
            return this.metadata;
        }

        public Server getServer() {
            return this.server;
        }

        @Override
        public String getScheme() {
            return this.server.getScheme();
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Server{");
            sb.append("serviceId='").append(serviceId).append('\'');
            sb.append(", server=").append(server);
            sb.append(", secure=").append(secure);
            sb.append(", metadata=").append(metadata);
            sb.append('}');
            return sb.toString();
        }
    }
}
