package com.doctorwork.sword.gateway.discovery.common;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

/**
 * @Author:czq
 * @Description:
 * @Date: 10:28 2019/5/31
 * @Modified By:
 */
public class HostInfo {
    public boolean override;
    private String ipAddress;
    private String hostname;

    public HostInfo(String hostname) {
        this.hostname = hostname;
    }

    public HostInfo() {
    }

    public int getIpAddressAsInt() {
        InetAddress inetAddress = null;
        String host = this.ipAddress;
        if (host == null) {
            host = this.hostname;
        }
        try {
            inetAddress = InetAddress.getByName(host);
        }
        catch (final UnknownHostException e) {
            throw new IllegalArgumentException(e);
        }
        return ByteBuffer.wrap(inetAddress.getAddress()).getInt();
    }

    public boolean isOverride() {
        return override;
    }

    public void setOverride(boolean override) {
        this.override = override;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
}
