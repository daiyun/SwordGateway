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
@RequestMapping("loadbalance")
public class TestController {
    @Autowired
    private GatewayAdminService gatewayAdminService;

    @RequestMapping("/pubish")
    @ResponseBody
    public void test(String lbmark) throws Exception {
        gatewayAdminService.publishLoadBalanceConfig(lbmark);
    }
}
