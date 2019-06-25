package com.doctorwork.sword.gateway.common;

/**
 * @Author:czq
 * @Description:
 * @Date: 16:59 2019/6/20
 * @Modified By:
 */
public class Constants {
    public static final String GATEWAY_ROUTES = "DOCTORWORK:GETEWAY:ROUTES";

    public static final String PINGMODE_URL = "url";
    public static final String PINGMODE_DUMMY = "dummy";
    public static final String PINGMODE_NOOP = "noop";
    public static final String PINGMODE_CONSTANT = "constant";

    public static final String PINGSTRATEGY_SERIAL= "serial";

    public static final String LBTYPE_RIBBON = "ribbon";

    //轮询
    public static final String LBRULE_ROUNDROBIN = "roundrobin";
    //随机
    public static final String LBRULE_RANDOM = "random";
    //过滤掉由于多次访问故障而处于断路器跳闸状态的服务，还有并发的连接数量超过阈值的服务，然后对剩余的服务列表按轮询策略进行访问
    public static final String LBRULE_AVAILABILITYFILTERING = "availabilityfiltering";
    //根据平均响应时间计算所有服务的权重，响应时间越快服务权重越大被选中的概率越高。刚启动时如果统计信息不足，则使用RoundTobinRule策略，等统计信息足够会切换到WeightedResponseTimeRule
    public static final String LBRULE_WEIGHTEDRESPONSETIME = "weightedresponsetime";
    //按照RoudRobinRule的策略获取服务，如果获取服务失败则在指定时间内进行重试，获取可用服务,如果还是失败的就会自动把这个失败的服务隔离掉
    public static final String LBRULE_RETRY = "retry";
    //会先过滤掉由于多次访问故障而处于断路器跳闸状态的服务，然后选择一个并发量小的服务
    public static final String LBRULE_BESTAVAILABLE = "bestavailable";
    //复合判断server所在区域的性能和server的可用性选择服务器
    public static final String LBRULE_ZONEAVOIDANCE = "zoneavoidance";
    //权重轮训调度
    public static final String LBRULE_WEIGHTROUNDROBIN = "weightroundrobin";
}
