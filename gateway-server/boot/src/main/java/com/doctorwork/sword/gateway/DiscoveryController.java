package com.doctorwork.sword.gateway;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenzhiqiang
 * @date 2019/7/4
 */
@RequestMapping("/discovery")
@RestController
public class DiscoveryController {
//
//    @Autowired
//    private IRespositoryManagerApi respositoryManagerApi;
//
//    @RequestMapping("conn/load")
//    @ResponseBody
//    public void connectionLoad(String registryId) {
//        respositoryManagerApi.connectionLoad(registryId);
//    }
//
//    @RequestMapping("conn/close")
//    @ResponseBody
//    public void connectionClose(String registryId) {
//        respositoryManagerApi.connectionClose(registryId);
//    }
//
//    @RequestMapping("svc/get")
//    @ResponseBody
//    public ServiceWrapper serviceWrapper(String serviceId) {
//        ServiceWrapper serviceWrapper = respositoryManagerApi.serviceWrapper(serviceId);
//        return serviceWrapper;
//    }
//
//    @RequestMapping("svc/discover/get")
//    @ResponseBody
//    public ServiceDiscoveryWrapper serviceDisovery(String serviceId) {
//        ServiceWrapper serviceWrapper = respositoryManagerApi.serviceWrapper(serviceId);
//        return respositoryManagerApi.serviceDisovery(serviceWrapper);
//    }
//
//    @RequestMapping("svc/load")
//    @ResponseBody
//    public void loadService(String serviceId) {
//        respositoryManagerApi.loadService(serviceId);
//    }
//
//    @RequestMapping("svc/discover/load")
//    @ResponseBody
//    public void loadDiscovery(String dscrId) {
//        respositoryManagerApi.loadDiscovery(dscrId);
//    }
//
//    @RequestMapping("reg/load")
//    @ResponseBody
//    void loadRegistry(String registryId) {
//        respositoryManagerApi.loadRegistry(registryId);
//    }
//
//    @RequestMapping("svc/del")
//    @ResponseBody
//    void serviceDelete(String serviceId) {
//        respositoryManagerApi.serviceDelete(serviceId);
//    }
}
