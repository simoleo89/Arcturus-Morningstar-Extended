package com.eu.habbo.networking.restapi;

import com.eu.habbo.Emulator;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Embedded REST API server built on Netty.
 * Provides HTTP endpoints for external integrations (Discord bots, websites, mobile apps).
 *
 * Configuration (config.ini):
 *   api.enabled=true
 *   api.host=0.0.0.0
 *   api.port=8081
 *   api.token=your-secret-token
 *
 * Endpoints:
 *   GET  /api/status         - Server status (uptime, version, users, rooms)
 *   GET  /api/online         - Online user count
 *   GET  /api/rooms          - Active rooms count
 *   GET  /api/user?name=xxx  - User info by username
 *
 * All requests require Authorization header: Bearer {api.token}
 */
public class RestApiServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestApiServer.class);
    private static final Gson GSON = new Gson();

    private final String host;
    private final int port;
    private final String apiToken;
    private final Map<String, Function<QueryStringDecoder, JsonObject>> routes = new HashMap<>();
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private Channel serverChannel;

    public RestApiServer(String host, int port, String apiToken) {
        this.host = host;
        this.port = port;
        this.apiToken = apiToken;
        registerRoutes();
    }

    private void registerRoutes() {
        routes.put("/api/status", this::handleStatus);
        routes.put("/api/online", this::handleOnline);
        routes.put("/api/rooms", this::handleRooms);
        routes.put("/api/user", this::handleUser);
    }

    public void start() {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup(2);

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new HttpServerCodec());
                            p.addLast(new HttpObjectAggregator(65536));
                            p.addLast(new RestApiHandler());
                        }
                    });

            serverChannel = bootstrap.bind(host, port).sync().channel();
            LOGGER.info("REST API Server started on {}:{}", host, port);
        } catch (Exception e) {
            LOGGER.error("Failed to start REST API server", e);
            stop();
        }
    }

    public void stop() {
        if (serverChannel != null) serverChannel.close();
        if (bossGroup != null) bossGroup.shutdownGracefully();
        if (workerGroup != null) workerGroup.shutdownGracefully();
        LOGGER.info("REST API Server stopped.");
    }

    private JsonObject handleStatus(QueryStringDecoder query) {
        JsonObject json = new JsonObject();
        json.addProperty("version", Emulator.version);
        json.addProperty("build", Emulator.build);
        json.addProperty("uptime_seconds", Emulator.getOnlineTime());
        json.addProperty("users_online", Emulator.getGameEnvironment().getHabboManager().getOnlineCount());
        json.addProperty("rooms_active", Emulator.getGameEnvironment().getRoomManager().getActiveRooms().size());

        Runtime rt = Emulator.getRuntime();
        long usedMB = (rt.totalMemory() - rt.freeMemory()) / (1024 * 1024);
        long maxMB = rt.maxMemory() / (1024 * 1024);
        json.addProperty("ram_used_mb", usedMB);
        json.addProperty("ram_max_mb", maxMB);
        json.addProperty("threads_active", Thread.activeCount());

        int seconds = Emulator.getOnlineTime();
        long days = TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - (days * 24);
        long minutes = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds) * 60);
        json.addProperty("uptime_formatted", days + "d " + hours + "h " + minutes + "m");

        return json;
    }

    private JsonObject handleOnline(QueryStringDecoder query) {
        JsonObject json = new JsonObject();
        json.addProperty("users_online", Emulator.getGameEnvironment().getHabboManager().getOnlineCount());
        return json;
    }

    private JsonObject handleRooms(QueryStringDecoder query) {
        JsonObject json = new JsonObject();
        json.addProperty("rooms_active", Emulator.getGameEnvironment().getRoomManager().getActiveRooms().size());
        return json;
    }

    private JsonObject handleUser(QueryStringDecoder query) {
        JsonObject json = new JsonObject();
        var params = query.parameters();

        if (!params.containsKey("name") || params.get("name").isEmpty()) {
            json.addProperty("error", "Missing 'name' parameter");
            return json;
        }

        String username = params.get("name").get(0);
        var habbo = Emulator.getGameEnvironment().getHabboManager().getHabbo(username);

        if (habbo == null) {
            json.addProperty("error", "User not found or offline");
            json.addProperty("username", username);
            json.addProperty("online", false);
            return json;
        }

        json.addProperty("username", habbo.getHabboInfo().getUsername());
        json.addProperty("id", habbo.getHabboInfo().getId());
        json.addProperty("online", true);
        json.addProperty("motto", habbo.getHabboInfo().getMotto());
        json.addProperty("rank", habbo.getHabboInfo().getRank().getId());
        json.addProperty("credits", habbo.getHabboInfo().getCredits());
        if (habbo.getRoomUnit() != null && habbo.getRoomUnit().getRoom() != null) {
            json.addProperty("room_id", habbo.getRoomUnit().getRoom().getRoomInfo().getId());
            json.addProperty("room_name", habbo.getRoomUnit().getRoom().getRoomInfo().getName());
        }

        return json;
    }

    private class RestApiHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) {
            // Auth check
            String authHeader = request.headers().get(HttpHeaderNames.AUTHORIZATION);
            if (apiToken != null && !apiToken.isEmpty()) {
                if (authHeader == null || !authHeader.equals("Bearer " + apiToken)) {
                    sendJson(ctx, HttpResponseStatus.UNAUTHORIZED, errorJson("Invalid or missing API token"));
                    return;
                }
            }

            if (!Emulator.isReady) {
                sendJson(ctx, HttpResponseStatus.SERVICE_UNAVAILABLE, errorJson("Server is starting up"));
                return;
            }

            QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
            String path = decoder.path();

            Function<QueryStringDecoder, JsonObject> handler = routes.get(path);
            if (handler != null) {
                try {
                    JsonObject result = handler.apply(decoder);
                    sendJson(ctx, HttpResponseStatus.OK, result);
                } catch (Exception e) {
                    LOGGER.error("Error handling REST request: {}", path, e);
                    sendJson(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR, errorJson("Internal server error"));
                }
            } else {
                JsonObject notFound = errorJson("Unknown endpoint: " + path);
                sendJson(ctx, HttpResponseStatus.NOT_FOUND, notFound);
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            LOGGER.error("REST API error", cause);
            ctx.close();
        }
    }

    private static JsonObject errorJson(String message) {
        JsonObject json = new JsonObject();
        json.addProperty("error", message);
        return json;
    }

    private static void sendJson(ChannelHandlerContext ctx, HttpResponseStatus status, JsonObject body) {
        byte[] content = GSON.toJson(body).getBytes(CharsetUtil.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, status, Unpooled.wrappedBuffer(content));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=UTF-8");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.length);
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
