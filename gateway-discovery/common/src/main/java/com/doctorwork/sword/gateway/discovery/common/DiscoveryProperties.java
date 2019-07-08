package com.doctorwork.sword.gateway.discovery.common;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @Author:czq
 * @Description:
 * @Date: 10:18 2019/5/31
 * @Modified By:
 */
public class DiscoveryProperties {

    private HostInfo hostInfo;

    private boolean enabled = true;

    private String zkRoot = Constants.DEFAULT_DISCOVER_BASEPATH;

    private String uriSpec = Constants.DEFAULT_URI_SPEC;

    private String appId;

    private String appName;

    private boolean preferIpAddress = false;

    private Integer appPort;

    private Integer appSslPort;

    private boolean register = true;

    private Map<String, String> metadata = new HashMap<>();

    private String initStatus = AppStatusEnum.ON.name();

    private ExecutorService executorService;

    public DiscoveryProperties() {
    }

    public DiscoveryProperties(boolean findIp) {
        this();
        if (findIp) {
            this.executorService = Executors.newSingleThreadExecutor();
            this.hostInfo = findFirstNonLoopbackHostInfo();
        }
    }

    public HostInfo getHostInfo() {
        return hostInfo;
    }

    public void setHostInfo(HostInfo hostInfo) {
        this.hostInfo = hostInfo;
    }

    public String getHost() {
        if (hostInfo == null)
            return null;
        if (this.preferIpAddress) {
            return this.hostInfo.getIpAddress();
        }
        return this.hostInfo.getHostname();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getZkRoot() {
        return zkRoot;
    }

    public void setZkRoot(String zkRoot) {
        this.zkRoot = zkRoot;
    }

    public String getUriSpec() {
        return uriSpec;
    }

    public void setUriSpec(String uriSpec) {
        this.uriSpec = uriSpec;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public boolean isPreferIpAddress() {
        return preferIpAddress;
    }

    public void setPreferIpAddress(boolean preferIpAddress) {
        this.preferIpAddress = preferIpAddress;
    }

    public Integer getAppPort() {
        return appPort;
    }

    public void setAppPort(Integer appPort) {
        this.appPort = appPort;
    }

    public Integer getAppSslPort() {
        return appSslPort;
    }

    public void setAppSslPort(Integer appSslPort) {
        this.appSslPort = appSslPort;
    }

    public boolean isRegister() {
        return register;
    }

    public void setRegister(boolean register) {
        this.register = register;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public String getInitStatus() {
        return initStatus;
    }

    public void setInitStatus(String initStatus) {
        this.initStatus = initStatus;
    }

    private HostInfo findFirstNonLoopbackHostInfo() {
        InetAddress address = findFirstNonLoopbackAddress();
        if (address != null) {
            return convertAddress(address);
        }
        HostInfo hostInfo = new HostInfo();
        hostInfo.setHostname(Constants.DEFAULT_HOST);
        hostInfo.setIpAddress(Constants.DEFAULT_IP_ADDRESS);
        return hostInfo;
    }

    private HostInfo convertAddress(final InetAddress address) {
        HostInfo hostInfo = new HostInfo();
        Future<String> result = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return address.getHostName();
            }
        });

        String hostname;
        try {
            hostname = result.get(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            hostname = "localhost";
        }
        hostInfo.setHostname(hostname);
        hostInfo.setIpAddress(address.getHostAddress());
        return hostInfo;
    }

    private InetAddress findFirstNonLoopbackAddress() {
        InetAddress result = null;
        try {
            int lowest = Integer.MAX_VALUE;
            for (Enumeration<NetworkInterface> nics = NetworkInterface
                    .getNetworkInterfaces(); nics.hasMoreElements(); ) {
                NetworkInterface ifc = nics.nextElement();

                if (ifc.isUp()) {
                    if (ifc.getIndex() < lowest || result == null) {
                        lowest = ifc.getIndex();
                    } else if (result != null) {
                        continue;
                    }

                    // @formatter:off
                    for (Enumeration<InetAddress> addrs = ifc
                            .getInetAddresses(); addrs.hasMoreElements(); ) {
                        InetAddress address = addrs.nextElement();
                        if (address instanceof Inet4Address
                                && !address.isLoopbackAddress()) {
                            result = address;
                        }
                    }
                    // @formatter:on
                }
            }
        } catch (IOException ex) {
        }

        if (result != null) {
            return result;
        }

        try {
            return InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
        }

        return null;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
