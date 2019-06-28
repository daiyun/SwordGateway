package com.doctorwork.sword.gateway.loadbalance;

import com.doctorwork.sword.gateway.dal.model.LoadbalanceInfo;
import com.doctorwork.sword.gateway.loadbalance.param.ext.RibbonLoadBalanceParam;
import com.doctorwork.sword.gateway.loadbalance.param.ping.RibbonPingParam;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author:czq
 * @Description:
 * @Date: 9:56 2019/6/11
 * @Modified By:
 */
@Deprecated
public class DatabaseLoadBalancer extends DynamicServerListLoadBalancer<DataBaseServer> {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseLoadBalancer.class);

    private RibbonLoadBalanceConfig ribbonLoadBalanceConfig;

    public DatabaseLoadBalancer(ServerList<DataBaseServer> serverList, IClientConfig config, IPing iPing) {
        this(serverList, null, config, iPing, null);
    }

    public DatabaseLoadBalancer(ServerList<DataBaseServer> serverList, IClientConfig config, LoadbalanceInfo loadbalanceInfo) {
        this(serverList, null, config, null, loadbalanceInfo);
    }

    public DatabaseLoadBalancer(ServerList<DataBaseServer> serverList, IRule iRule, IClientConfig config, IPing iPing, LoadbalanceInfo loadbalanceInfo) {
        super(config);
        setServerListImpl(serverList);
        setServersList(serverList.getInitialListOfServers());
        if (loadbalanceInfo != null)
            this.ribbonLoadBalanceConfig = new RibbonLoadBalanceConfig(loadbalanceInfo);
        RibbonPingParam<IPing> pingParam;
        if (iPing == null && this.ribbonLoadBalanceConfig != null && (pingParam = this.ribbonLoadBalanceConfig.pingParam()) != null) {
            iPing = pingParam.ping();
            logger.info("加载负载器{} PING策略{}", name, iPing);
            Integer pingIntervalTime;
            Integer maxTotalPingTime;
            if ((maxTotalPingTime = pingParam.getMaxTotalPingTime()) != null)
                setMaxTotalPingTime(maxTotalPingTime);
            if ((pingIntervalTime = pingParam.getPingIntervalTime()) != null)
                setPingInterval(pingIntervalTime);
        }
        if (iPing != null)
            setPing(iPing);
        if (iRule == null && this.ribbonLoadBalanceConfig != null) {
            iRule = this.ribbonLoadBalanceConfig.rule();
            logger.info("加载负载器{} RULE策略{}", name, iRule);
        }
        setRule(iRule);
    }

    @Override
    public void enableAndInitLearnNewServersFeature() {
        RibbonLoadBalanceParam param = null;
        if (this.ribbonLoadBalanceConfig != null && (param = this.ribbonLoadBalanceConfig.extParam()) != null) {
            if (param.getAutoRefresh() != null && !param.getAutoRefresh()){
                logger.info("禁止负载器{} 服务自动刷新策略", name);
                return;
            }
        }
        logger.info("启用负载器{} 服务自动刷新策略[{}](该策略会刷新服务状态)", name, serverListUpdater);
        if (param != null && getServerListUpdater() instanceof PollingServerListUpdater) {
            if (param.getInitialDelayMs() != null && param.getRefreshIntervalMs() != null) {
                stopServerListRefreshing();
                setServerListUpdater(new PollingServerListUpdater(param.getInitialDelayMs(), param.getRefreshIntervalMs()));
                logger.info("负载器{} 服务自动刷新时间已配置，初始延迟时间：{}ms,延迟时间间隔:{}ms", name, param.getInitialDelayMs(), param.getRefreshIntervalMs());
            }
        }
        super.enableAndInitLearnNewServersFeature();
    }
}
