package com.ibacker.gateway.filters.global;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http2.Http2StreamChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;
import reactor.netty.ConnectionObserver;
import reactor.netty.NettyPipeline;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Netty编码配置
 * @author Atomicyo
 * @version 1.0
 * @date 2021/11/3 14:22
 */
@Component
@Slf4j
public class EncodeQueryNettyWebServerCustomizer implements WebServerFactoryCustomizer<NettyReactiveWebServerFactory> {

    /**
     * 需要encode的特殊字符
     */
    private static final List<Character> charList = new ArrayList<Character>() {
        {
            this.add('{');
            this.add('}');
            this.add('[');
            this.add(']');
        }
    };

    @Override
    public void customize(NettyReactiveWebServerFactory factory) {
        factory.addServerCustomizers(httpServer ->
                httpServer.childObserve((conn, state) -> {
                    Channel channel = conn.channel();
                    if (state == ConnectionObserver.State.CONNECTED) {
                        if (channel instanceof Http2StreamChannel) {
                            channel.pipeline().addAfter(NettyPipeline.H2ToHttp11Codec, "", new QueryHandler());
                        } else {
                            channel.pipeline().addAfter(NettyPipeline.HttpCodec, "", new QueryHandler());
                        }
                    }
                })
        );
    }


    static class QueryHandler extends ChannelInboundHandlerAdapter {

        public QueryHandler() {
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {
            if (msg instanceof HttpRequest) {
                HttpRequest request = (HttpRequest) msg;
                String url = request.uri();
                // fix url
                log.info("url: {}", url);
                String[] split = url.split("\\?");
                StringBuilder fixUrl = new StringBuilder(split[0]);
                if (split.length > 1) {
                    fixUrl.append("?");
                    char[] chars = split[1].toCharArray();
                    for (char aChar : chars) {
                        if (charList.contains(aChar)) {
                            fixUrl.append(URLEncoder.encode(String.valueOf(aChar), "UTF-8"));
                        }else {
                            fixUrl.append(aChar);
                        }
                    }
                }
                log.info("fixUrl: {}", fixUrl);
                request.setUri(fixUrl.toString());
            }
            ctx.fireChannelRead(msg);
        }
    }
}

