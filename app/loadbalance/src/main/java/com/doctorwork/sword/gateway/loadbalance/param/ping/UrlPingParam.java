package com.doctorwork.sword.gateway.loadbalance.param.ping;

import com.doctorwork.sword.gateway.common.Constants;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.netflix.loadbalancer.PingUrl;
import org.springframework.util.StringUtils;

/**
 * @author chenzhiqiang
 * @date 2019/6/21
 */
@JsonTypeName(Constants.PINGMODE_URL)
public class UrlPingParam extends RibbonPingParam<PingUrl> {
    private String uri;
    private Boolean secure;

    public UrlPingParam() {
        super(Constants.PINGMODE_URL, null);
    }

    @Override
    public PingUrl ping() {
        if (!StringUtils.isEmpty(uri)) {
            if (secure == null || !secure)
                return new PingUrl(Boolean.FALSE, uri);
            return new PingUrl(Boolean.TRUE, uri);
        }
        return null;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Boolean getSecure() {
        return secure;
    }

    public void setSecure(Boolean secure) {
        this.secure = secure;
    }
}
