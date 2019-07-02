package com.doctorwork.sword.gateway.loadbalance.param.rule;

import com.doctorwork.sword.gateway.loadbalance.server.DataBaseServer;
import com.doctorwork.sword.gateway.loadbalance.DatabaseLoadBalancer;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.RoundRobinRule;
import com.netflix.loadbalancer.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author chenzhiqiang
 * @date 2019/6/22
 */
public class WeightRule extends RoundRobinRule {

    private static Logger logger = LoggerFactory.getLogger(WeightRule.class);
    private AtomicInteger sequence = new AtomicInteger();

    public WeightRule() {
        super();
    }

    public WeightRule(ILoadBalancer lb) {
        super(lb);
    }

    public Server choose(ILoadBalancer lb, Object key) {
        if (lb == null) {
            logger.warn("no load balancer");
            return null;
        }
        if (lb.getAllServers().size() == 0) {
            logger.warn("no server");
            return null;
        }
        if (lb instanceof DatabaseLoadBalancer) {
            List<Server> allList = lb.getAllServers();
            int length = allList.size(); // Number of invokers
            int maxWeight = 0; // The maximum weight
            int minWeight = Integer.MAX_VALUE; // The minimum weight
            int weightSum = 0;
            final LinkedHashMap<Server, IntegerWrapper> serverToWeightMap = new LinkedHashMap<Server, IntegerWrapper>();
            for (int i = 0; i < length; i++) {
                DataBaseServer dataBaseServer = (DataBaseServer) allList.get(i);
                int weight = dataBaseServer.weight();
                maxWeight = Math.max(maxWeight, weight); // Choose the maximum weight
                minWeight = Math.min(minWeight, weight); // Choose the minimum weight
                if (weight > 0) {
                    serverToWeightMap.put(dataBaseServer, new IntegerWrapper(weight));
                    weightSum += weight;
                }
            }

            int currentSequence = squenceGetAndIncrement();
            if (maxWeight > 0 && minWeight < maxWeight) {
                long mod = currentSequence % weightSum;
                for (int i = 0; i < maxWeight; i++) {
                    for (Map.Entry<Server, IntegerWrapper> each : serverToWeightMap.entrySet()) {
                        final Server server = each.getKey();
                        final IntegerWrapper v = each.getValue();
                        if (mod == 0 && v.getValue() > 0) {
                            return server;
                        }
                        if (v.getValue() > 0) {
                            v.decrement();
                            mod--;
                        }
                    }
                }
            }
        }
        return super.choose(lb, key);//round robin
    }

    @Override
    public Server choose(Object key) {
        return choose(getLoadBalancer(), key);
    }

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {

    }

    private final int squenceGetAndIncrement() {
        for (; ; ) {
            int current = sequence.get();
            int next = (current >= Integer.MAX_VALUE ? 0 : current + 1);
            if (sequence.compareAndSet(current, next)) {
                return current;
            }
        }
    }

    private static final class IntegerWrapper {
        private int value;

        public IntegerWrapper(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public void decrement() {
            this.value--;
        }
    }
}
