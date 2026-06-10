package com.eu.habbo.habbohotel.gameclients;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class GameClientManagerContractTest {

    @Test
    void exposesExplicitForcedDisposePath() {
        assertDoesNotThrow(() -> GameClient.class.getDeclaredMethod("dispose", boolean.class));
        assertDoesNotThrow(() -> GameClientManager.class.getDeclaredMethod("forceDisposeClient", GameClient.class));
    }

    @Test
    void disposeMethodsIgnoreNullClient() {
        GameClientManager manager = new GameClientManager();

        assertDoesNotThrow(() -> manager.disposeClient(null));
        assertDoesNotThrow(() -> manager.forceDisposeClient(null));
    }
}
