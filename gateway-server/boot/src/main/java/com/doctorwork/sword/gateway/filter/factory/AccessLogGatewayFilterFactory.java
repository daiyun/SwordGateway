package com.doctorwork.sword.gateway.filter.factory;

import com.doctorwork.sword.gateway.common.JacksonUtil;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @Author:czq
 * @Description:
 * @Date: 16:40 2019/6/26
 * @Modified By:
 */
public class AccessLogGatewayFilterFactory extends AbstractGatewayFilterFactory<AccessLogGatewayFilterFactory.Config> {

    public AccessLogGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return new AccessLogGatewayFilter(config);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("enabled");
    }

    public static class AccessLogGatewayFilter implements GatewayFilter, Ordered {

        private static final Logger logger = LoggerFactory.getLogger(AccessLogGatewayFilter.class);
        private Config config;

        AccessLogGatewayFilter(Config config) {
            this.config = config;
        }

        @Override
        public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
            if (!config.isEnabled()) {
                return chain.filter(exchange);
            }
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();
            String traceId = UUID.randomUUID().toString();
            long start = System.currentTimeMillis();
            ServerHttpRequestDecorator requestDecorator = new ServerHttpRequestDecorator(request) {
                private ByteArrayOutputStream stream = new ByteArrayOutputStream(0);

                @Override
                public Flux<DataBuffer> getBody() {
                    return super.getBody()
                            .doOnNext(buffer -> recordBytes(stream, buffer))
                            .doOnComplete(() -> {
                                logger.info("\n----Request[TraceId:{}]:\n" +
                                                "\tMethod:[{}]\n" +
                                                "\tHeads:[{}]\n" +
                                                "\tPath:[{}]\n" +
                                                "\tUri:[{}]\n" +
                                                "\tQueryParams:[{}]\n" +
                                                "\tBody:[{}]\n" +
                                                "---------",
                                        traceId,
                                        getMethod(),
                                        JacksonUtil.toJSon(getHeaders()),
                                        getPath(),
                                        getURI(),
                                        JacksonUtil.toJSon(getQueryParams()),
                                        stream.size() > 0 ? new String(stream.toByteArray(), StandardCharsets.UTF_8) : null
                                );
                                stream = null;
                            });
                }
            };

            ServerHttpResponseDecorator responseDecorator = new ServerHttpResponseDecorator(response) {
                private ByteArrayOutputStream stream = new ByteArrayOutputStream(0);

                @Override
                public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                    Flux<? extends DataBuffer> flux = Flux.from(body)
                            .doOnNext(buffer -> recordBytes(stream, buffer))
                            .doOnComplete(() -> {
                                logger.info("\n----Response[TraceId:{}]:\n" +
                                                "\tStatusCode:[{}]\n" +
                                                "\tCost:[{}ms]\n" +
                                                "\tHeads:[{}]\n" +
                                                "\tBody:[{}]\n" +
                                                "---------",
                                        traceId,
                                        getStatusCode(),
                                        System.currentTimeMillis() - start,
                                        JacksonUtil.toJSon(getHeaders()),
                                        stream.size() > 0 ? new String(stream.toByteArray(), StandardCharsets.UTF_8) : null);
                                stream = null;
                            });
                    return super.writeWith(flux);
                }
            };

            return chain.filter(exchange.mutate().request(requestDecorator).response(responseDecorator).build());
        }

        private static void recordBytes(ByteArrayOutputStream stream, DataBuffer buffer) {
            byte[] bs = new byte[buffer.readableByteCount()];
            buffer.read(bs);
            try {
                stream.write(bs);
            } catch (IOException e) {
                e.printStackTrace();
            }
            buffer.readPosition(0);
        }

        @Override
        public int getOrder() {
            return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER - 2;
        }
    }

    public static class Config {
        private Boolean enabled;

        public Config() {
        }

        public Boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }
    }
}
