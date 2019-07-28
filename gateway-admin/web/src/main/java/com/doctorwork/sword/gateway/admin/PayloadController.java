package com.doctorwork.sword.gateway.admin;

import com.doctorwork.doctorwork.admin.api.req.*;
import com.doctorwork.sword.gateway.admin.core.GatewayPayloadService;
import com.doctorwork.sword.gateway.common.BusinessException;
import com.doctorwork.sword.gateway.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author:czq
 * @Description:
 * @Date: 11:46 2019/7/26
 * @Modified By:
 */
@RestController
@RequestMapping("payload")
public class PayloadController {
    @Autowired
    private GatewayPayloadService gatewayPayloadService;

    @PostMapping("/list")
    @ResponseBody
    public Result payloadSearch(@RequestBody PayloadSearchReq req) {
        return Result.result(gatewayPayloadService.payloadSearch(req));
    }

    @GetMapping("/get")
    @ResponseBody
    public Result payloadGet(String lbMark) throws BusinessException {
        return Result.result(gatewayPayloadService.payloadGet(lbMark));
    }

    @PostMapping("/edit")
    @ResponseBody
    public Result payloadUpdate(@RequestBody PayloadEdit edit) throws BusinessException {
        gatewayPayloadService.payloadUpdate(edit);
        return Result.result(null);
    }

    @PostMapping("/add")
    @ResponseBody
    public Result payloadAdd(@RequestBody PayloadEdit edit) throws BusinessException {
        gatewayPayloadService.payloadAdd(edit);
        return Result.result(null);
    }

    @PostMapping("/del")
    @ResponseBody
    public Result payloadDel(@RequestBody PayloadDel edit) throws BusinessException {
        gatewayPayloadService.payloadDel(edit);
        return Result.result(null);
    }

    @PostMapping("/enable")
    @ResponseBody
    public Result payloadEnable(String lbMark) throws BusinessException {
        gatewayPayloadService.payloadEnable(lbMark);
        return Result.result(null);
    }

    @PostMapping("/disable")
    @ResponseBody
    public Result payloadDisable(String lbMark) throws BusinessException {
        gatewayPayloadService.payloadDisable(lbMark);
        return Result.result(null);
    }

    @PostMapping("/server/list")
    @ResponseBody
    public Result payloadServerList(PayloadServerSearchReq req) throws BusinessException {
        return Result.result(gatewayPayloadService.payloadServerList(req));
    }

    @PostMapping("/server/add")
    @ResponseBody
    public Result payloadServerAdd(@RequestBody PayloadServerEdit edit) throws BusinessException {
        gatewayPayloadService.payloadServerAdd(edit);
        return Result.result(null);
    }

    @PostMapping("/server/edit")
    @ResponseBody
    public Result payloadServerUpdate(@RequestBody PayloadServerEdit edit) throws BusinessException {
        gatewayPayloadService.payloadServerUpdate(edit);
        return Result.result(null);
    }

    @PostMapping("/server/del")
    @ResponseBody
    public Result payloadServerDel(String id) throws BusinessException {
        gatewayPayloadService.payloadServerDel(id);
        return Result.result(null);
    }

    @PostMapping("/server/on")
    @ResponseBody
    public Result payloadServerOn(String id) throws BusinessException {
        gatewayPayloadService.payloadServerOn(id);
        return Result.result(null);
    }

    @PostMapping("/server/off")
    @ResponseBody
    public Result payloadServerOff(String id) throws BusinessException {
        gatewayPayloadService.payloadServerOff(id);
        return Result.result(null);
    }

    @PostMapping("/server/enable")
    @ResponseBody
    public Result payloadServerEnable(String id) throws BusinessException {
        gatewayPayloadService.payloadServerEnable(id);
        return Result.result(null);
    }

    @PostMapping("/server/disable")
    @ResponseBody
    public Result payloadServerDisable(String id) throws BusinessException {
        gatewayPayloadService.payloadServerDisable(id);
        return Result.result(null);
    }

    @PostMapping("/server/discover/list")
    @ResponseBody
    public Result payloadDiscoverServerList(PayloadServerSearchReq req) throws Exception {
        return Result.result(gatewayPayloadService.payloadDiscoverServerList(req));
    }
}
