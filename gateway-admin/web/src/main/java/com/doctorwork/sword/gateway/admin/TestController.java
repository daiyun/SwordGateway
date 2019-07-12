package com.doctorwork.sword.gateway.admin;

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
@RequestMapping("test")
public class TestController {
    @RequestMapping("ww")
    @ResponseBody
    public String test() {
        return "IJ";
    }
}
