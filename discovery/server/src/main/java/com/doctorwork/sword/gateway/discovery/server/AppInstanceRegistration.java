package com.doctorwork.sword.gateway.discovery.server;

import com.doctorwork.sword.gateway.discovery.common.Constants;
import com.doctorwork.sword.gateway.discovery.common.ZookeeperInstance;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceInstanceBuilder;
import org.apache.curator.x.discovery.ServiceType;
import org.apache.curator.x.discovery.UriSpec;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

/**
 * @Author:czq
 * @Description:
 * @Date: 9:40 2019/5/31
 * @Modified By:
 */
public class AppInstanceRegistration implements ZookeeperRegistration {

    public static RegistrationBuilder builder() {
        try {
            return new RegistrationBuilder(ServiceInstance.builder());
        } catch (Exception e) {
            throw new RuntimeException("Error creating ServiceInstanceBuilder", e);
        }
    }

    public static class RegistrationBuilder {
        protected ServiceInstanceBuilder<ZookeeperInstance> builder;

        public RegistrationBuilder(ServiceInstanceBuilder<ZookeeperInstance> builder) {
            this.builder = builder;
        }

        public AppInstanceRegistration build() {
            return new AppInstanceRegistration(this.builder);
        }

        public RegistrationBuilder name(String name) {
            this.builder.name(name);
            return this;
        }

        public RegistrationBuilder address(String address) {
            this.builder.address(address);
            return this;
        }

        public RegistrationBuilder id(String id) {
            this.builder.id(id);
            return this;
        }

        public RegistrationBuilder port(int port) {
            this.builder.port(port);
            return this;
        }

        public RegistrationBuilder sslPort(int port) {
            this.builder.sslPort(port);
            return this;
        }

        public RegistrationBuilder payload(ZookeeperInstance payload) {
            this.builder.payload(payload);
            return this;
        }

        public RegistrationBuilder serviceType(ServiceType serviceType) {
            this.builder.serviceType(serviceType);
            return this;
        }

        public RegistrationBuilder registrationTimeUTC(long registrationTimeUTC) {
            this.builder.registrationTimeUTC(registrationTimeUTC);
            return this;
        }

        public RegistrationBuilder uriSpec(UriSpec uriSpec) {
            this.builder.uriSpec(uriSpec);
            return this;
        }

        public RegistrationBuilder uriSpec(String uriSpec) {
            this.builder.uriSpec(new UriSpec(uriSpec));
            return this;
        }

        public RegistrationBuilder defaultUriSpec() {
            this.builder.uriSpec(new UriSpec(Constants.DEFAULT_URI_SPEC));
            return this;
        }
    }

    private ServiceInstance<ZookeeperInstance> serviceInstance;
    private ServiceInstanceBuilder<ZookeeperInstance> builder;

    public AppInstanceRegistration(ServiceInstanceBuilder<ZookeeperInstance> builder) {
        this.builder = builder;
    }

    private void build() {
        this.serviceInstance = this.builder.build();
    }

    @Override
    public ServiceInstance<ZookeeperInstance> getServiceInstance() {
        if (this.serviceInstance == null) {
            build();
        }
        return this.serviceInstance;
    }

    @Override
    public String getAppId() {
        if (this.serviceInstance == null) {
            return null;
        }
        return this.serviceInstance.getName();
    }

    @Override
    public Integer getPort() {
        if (this.serviceInstance == null) {
            return null;
        }
        return this.serviceInstance.getPort();
    }

    @Override
    public String getHost() {
        if (this.serviceInstance == null) {
            return null;
        }
        return this.serviceInstance.getAddress();
    }

    @Override
    public boolean isSecure() {
        if (this.serviceInstance == null) {
            return false;
        }
        return this.serviceInstance.getSslPort() != null;
    }

    public void setPort(int port) {
        this.builder.port(port);
    }

    @Override
    public URI getUri() {
        if (this.serviceInstance == null) {
            return null;
        }
        return URI.create(this.serviceInstance.buildUriSpec());
    }

    @Override
    public Map<String, String> getMetadata() {
        if (this.serviceInstance == null || this.serviceInstance.getPayload() == null) {
            return Collections.EMPTY_MAP;
        }
        return this.serviceInstance.getPayload().getMetadata();
    }
}
