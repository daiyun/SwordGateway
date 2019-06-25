package com.doctorwork.sword.gateway.loadbalance.param.ping;

import com.doctorwork.sword.gateway.common.Constants;
import com.doctorwork.sword.gateway.common.JacksonUtil;
import com.doctorwork.sword.gateway.loadbalance.param.PingParam;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.loadbalancer.DummyPing;
import org.reflections.Reflections;

import java.io.IOException;
import java.util.Set;

/**
 * @author chenzhiqiang
 * @date 2019/6/21
 */
@JsonTypeName(Constants.PINGMODE_DUMMY)
public class DummyPingParam extends RibbonPingParam<DummyPing> {

    public DummyPingParam() {
        super(Constants.PINGMODE_DUMMY, null);
    }

    @Override
    public DummyPing ping() {
        return new DummyPing();
    }

    public static void main(String[] args) {
        DummyPingParam dummyPingParam = new DummyPingParam();
        String json = JacksonUtil.toJSon(dummyPingParam);
        System.out.println(json);
        Reflections reflections = new Reflections("com.doctorwork.gateway.loadbalance.param.ping");
        Set<Class<? extends RibbonPingParam>> classSet = reflections.getSubTypesOf(RibbonPingParam.class);
        ObjectMapper mapper = new ObjectMapper();
        classSet.stream().forEach(clazz -> mapper.registerSubtypes(clazz));
        try {
            PingParam pingParam = mapper.readValue("{\"lbType\":\"ribbon\",\"pingMode\":\"dummy\",\"pingIntervalTime\":20,\"maxTotalPingTime\":2,\"pingStrategy\":\"serial\"}", RibbonPingParam.class);
            System.out.println(pingParam.ping());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
