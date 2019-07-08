package com.doctorwork.sword.gateway.loadbalance;

import com.doctorwork.sword.gateway.dal.model.LoadbalanceInfo;
import com.doctorwork.sword.gateway.loadbalance.param.PingParam;
import com.doctorwork.sword.gateway.loadbalance.param.ext.LoadbalanceParam;
import com.doctorwork.sword.gateway.loadbalance.param.ext.RibbonLoadBalanceParam;
import com.doctorwork.sword.gateway.loadbalance.param.ping.RibbonPingParam;
import com.doctorwork.sword.gateway.loadbalance.param.rule.RuleParam;
import com.netflix.loadbalancer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.LongAdder;

/**
 * @Author:czq
 * @Description:
 * @Date: 16:53 2019/6/28
 * @Modified By:
 */
public class DynamicLoadBalancer<T extends Server> extends BaseLoadBalancer {
    private static final Logger logger = LoggerFactory.getLogger(DynamicLoadBalancer.class);

    private RibbonLoadBalanceConfig ribbonLoadBalanceConfig;

    protected AtomicBoolean serverListUpdateInProgress = new AtomicBoolean(false);

    volatile ServerList<T> serverListImpl;

    volatile ServerListFilter<T> filter;

    private volatile LongAdder count = new LongAdder();

    private volatile long lastUpdated = System.currentTimeMillis();

    protected final ServerListUpdater.UpdateAction updateAction = new ServerListUpdater.UpdateAction() {
        @Override
        public void doUpdate() {
            updateListOfServers(false);
        }
    };

    protected volatile ServerListUpdater serverListUpdater;

    public DynamicLoadBalancer(ServerList<T> serverListImpl) {
        super();
        this.serverListImpl = serverListImpl;
    }

    public void init(LoadbalanceInfo loadbalanceInfo) {
        if (loadbalanceInfo == null)
            throw new RuntimeException("loadbalance info must not be null");
        this.name = loadbalanceInfo.getLbMark();
        this.ribbonLoadBalanceConfig = new RibbonLoadBalanceConfig(loadbalanceInfo);
        RibbonPingParam<IPing> pingParam;
        IPing iPing = null;
        IRule iRule = null;
        if ((pingParam = this.ribbonLoadBalanceConfig.pingParam()) != null) {
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
        if (this.ribbonLoadBalanceConfig != null) {
            iRule = this.ribbonLoadBalanceConfig.rule();
            logger.info("加载负载器{} RULE策略{}", name, iRule);
        }
        if (iRule != null) {
            iRule.setLoadBalancer(this);
            setRule(iRule);
        }
        this.setEnablePrimingConnections(false);
        serverListUpdateConfig(this.ribbonLoadBalanceConfig);
        setServersList(serverListImpl.getInitialListOfServers());

    }

    public void reloadPing(LoadbalanceInfo loadbalanceInfo) {
        RibbonPingParam<IPing> pingParam = PingParam.build(loadbalanceInfo);
        IPing iPing = null;
        if (pingParam != null) {
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
        this.ribbonLoadBalanceConfig.getLoadbalanceInfo().setPingParam(loadbalanceInfo.getPingParam());
        this.ribbonLoadBalanceConfig.setPingParam(pingParam);
    }

    public void reloadRule(LoadbalanceInfo loadbalanceInfo) {
        RuleParam ruleParam = RuleParam.build(loadbalanceInfo);
        IRule iRule = null;
        if (ruleParam != null) {
            iRule = RibbonLoadBalanceConfig.rule(ruleParam);
            logger.info("加载负载器{} RULE策略{}", name, iRule);
        }
        if (iRule != null) {
            iRule.setLoadBalancer(this);
            setRule(iRule);
        }
        this.ribbonLoadBalanceConfig.getLoadbalanceInfo().setPingParam(loadbalanceInfo.getPingParam());
        this.ribbonLoadBalanceConfig.getLoadbalanceInfo().setRuleParam(loadbalanceInfo.getRuleParam());
        this.ribbonLoadBalanceConfig.setRuleParam(ruleParam);
    }

    public void reloadAutoRefresh(LoadbalanceInfo loadbalanceInfo) {
        LoadbalanceParam loadbalanceParam = LoadbalanceParam.build(loadbalanceInfo);
        this.ribbonLoadBalanceConfig.getLoadbalanceInfo().setLbExtParam(loadbalanceInfo.getLbExtParam());
        this.ribbonLoadBalanceConfig.setLoadbalanceParam(loadbalanceParam);
        if (loadbalanceParam == null) {
            stopServerListRefreshing();
            return;
        }
        serverListUpdateConfig(this.ribbonLoadBalanceConfig);
    }

    private void serverListUpdateConfig(RibbonLoadBalanceConfig ribbonLoadBalanceConfig) {
        RibbonLoadBalanceParam param = null;
        if (ribbonLoadBalanceConfig != null && (param = ribbonLoadBalanceConfig.extParam()) != null) {
            if (param.getAutoRefresh() != null && !param.getAutoRefresh()) {
                logger.info("禁止负载器{} 服务自动刷新策略", this.name);
                stopServerListRefreshing();
                return;
            }
        }
        logger.info("启用负载器{} 服务自动刷新策略[{}](该策略会刷新服务状态)", this.name, PollingServerListUpdater.class.getSimpleName());
        stopServerListRefreshing();
        if (param != null) {
            if (param.getInitialDelayMs() != null && param.getRefreshIntervalMs() != null) {
                setServerListUpdater(new PollingServerListUpdater(param.getInitialDelayMs(), param.getRefreshIntervalMs()));
                logger.info("负载器{} 服务自动刷新时间已配置，执行延迟时间：{}ms,延迟时间间隔:{}ms", this.name, param.getInitialDelayMs(), param.getRefreshIntervalMs());
            }
        } else {
            setServerListUpdater(new PollingServerListUpdater());
        }
        startServerListRefreshing();
    }

    @Override
    public Server chooseServer(Object key) {
        count.increment();
        if (rule == null) {
            return null;
        } else {
            try {
                return rule.choose(key);
            } catch (Exception e) {
                logger.warn("LoadBalancer [{}]:  Error choosing server for key {}", name, key, e);
                return null;
            }
        }
    }

    @Override
    public void setServersList(List lsrv) {
        super.setServersList(lsrv);
        List<T> serverList = (List<T>) lsrv;
        Map<String, List<Server>> serversInZones = new HashMap<String, List<Server>>();
        for (Server server : serverList) {
            getLoadBalancerStats().getSingleServerStat(server);
            String zone = server.getZone();
            if (zone != null) {
                zone = zone.toLowerCase();
                List<Server> servers = serversInZones.get(zone);
                if (servers == null) {
                    servers = new ArrayList<Server>();
                    serversInZones.put(zone, servers);
                }
                servers.add(server);
            }
        }
        setServerListForZones(serversInZones);
    }

    protected void setServerListForZones(
            Map<String, List<Server>> zoneServersMap) {
        logger.debug("Setting server list for zones: {}", zoneServersMap);
        getLoadBalancerStats().updateZoneServerMapping(zoneServersMap);
    }

    public ServerList<T> getServerListImpl() {
        return serverListImpl;
    }

    public void setServerListImpl(ServerList<T> niwsServerList) {
        this.serverListImpl = niwsServerList;
    }

    public ServerListFilter<T> getFilter() {
        return filter;
    }

    public void setFilter(ServerListFilter<T> filter) {
        this.filter = filter;
    }

    public ServerListUpdater getServerListUpdater() {
        return serverListUpdater;
    }

    public void setServerListUpdater(ServerListUpdater serverListUpdater) {
        this.serverListUpdater = serverListUpdater;
    }

    @Override
    public void forceQuickPing() {
        // no-op
    }

    private void startServerListRefreshing() {
        serverListUpdater.start(updateAction);
    }

    private String getIdentifier() {
        return this.name;
    }

    public void stopServerListRefreshing() {
        if (serverListUpdater != null) {
            serverListUpdater.stop();
        }
    }

    public void updateListOfServers(boolean force) {
        //avoid to concurrent update service list
        if (!force && (System.currentTimeMillis() - lastUpdated) <= 5000) {
            return;
        }
        List<T> servers = new ArrayList<T>();
        if (serverListImpl != null) {
            servers = serverListImpl.getUpdatedListOfServers();
            logger.debug("List of Servers for {} obtained from Discovery client: {}",
                    getIdentifier(), servers);

            if (filter != null) {
                servers = filter.getFilteredListOfServers(servers);
                logger.debug("Filtered List of Servers for {} obtained from Discovery client: {}",
                        getIdentifier(), servers);
            }
        }
        updateAllServerList(servers);
        lastUpdated = System.currentTimeMillis();
    }

    private void updateAllServerList(List<T> ls) {
        if (serverListUpdateInProgress.compareAndSet(false, true)) {
            try {
                for (T s : ls) {
                    s.setAlive(true);
                }
                setServersList(ls);
                super.forceQuickPing();
            } finally {
                serverListUpdateInProgress.set(false);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("DynamicServerListLoadBalancer:");
        sb.append(super.toString());
        sb.append("ServerList:" + String.valueOf(serverListImpl));
        return sb.toString();
    }

    @Override
    public void shutdown() {
        super.shutdown();
        stopServerListRefreshing();
    }


    public String getLastUpdate() {
        return serverListUpdater.getLastUpdate();
    }

    public long getDurationSinceLastUpdateMs() {
        return serverListUpdater.getDurationSinceLastUpdateMs();
    }

    public int getNumberMissedCycles() {
        return serverListUpdater.getNumberMissedCycles();
    }

    public int getCoreThreads() {
        return serverListUpdater.getCoreThreads();
    }
}
