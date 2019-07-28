package com.doctorwork.sword.gateway.admin;

import com.doctorwork.sword.gateway.admin.core.GatewayAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author:czq
 * @Description:
 * @Date: 16:17 2019/7/12
 * @Modified By:
 */
@RestController
@RequestMapping("config")
public class PublishController {
    @Autowired
    private GatewayAdminService gatewayAdminService;

    @RequestMapping("/pubish/loadbalance")
    @ResponseBody
    public void publishLoadBalanceConfig(String lbmark) throws Exception {
        gatewayAdminService.publishLoadBalanceConfig(lbmark);
    }

    @RequestMapping("/pubish/discovery")
    @ResponseBody
    public void publishDiscoveryConfig(String dscrId) throws Exception {
        gatewayAdminService.publishDiscoveryConfig(dscrId);
    }

    @RequestMapping("/pubish/loadbalanceservers")
    @ResponseBody
    public void publishLoadBalanceServer(String lbmark) throws Exception {
        gatewayAdminService.publishLoadBalanceServer(lbmark);
    }

    @RequestMapping("/pubish/registry")
    @ResponseBody
    public void publishRegistryConfig(String registryId) throws Exception {
        gatewayAdminService.publishRegistryConfig(registryId);
    }

    @RequestMapping("/pubish/route")
    @ResponseBody
    public void publishRouteConfig(String routeMark) throws Exception {
        gatewayAdminService.publishRouteConfig(routeMark);
    }
}
