package com.eu.arcturus.networking.gameserver;

import com.eu.arcturus.crypto.HabboRC4;
import com.eu.arcturus.habbohotel.gameclients.GameClient;
import io.netty.util.AttributeKey;

public class GameServerAttributes {

    public static final AttributeKey<GameClient> CLIENT = AttributeKey.valueOf("GameClient");
    public static final AttributeKey<HabboRC4> CRYPTO_CLIENT = AttributeKey.valueOf("CryptoClient");
    public static final AttributeKey<HabboRC4> CRYPTO_SERVER = AttributeKey.valueOf("CryptoServer");
    public static final AttributeKey<String> WS_IP = AttributeKey.valueOf("WebSocketIP");
}
