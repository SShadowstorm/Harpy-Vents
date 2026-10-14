package community.harpyvents;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class VentAccessTest {
    @ParameterizedTest
    @ValueSource(strings = {"wathe:lockpick", "noellesroles:master_key"})
    void eitherExistingKeyOpensFromEitherHand(String key) {
        assertTrue(VentAccess.canToggle(false, key, "minecraft:air"));
        assertTrue(VentAccess.canToggle(false, "minecraft:air", key));
        assertTrue(VentAccess.canToggle(false, "minecraft:stone", key));
    }

    @ParameterizedTest
    @ValueSource(strings = {"minecraft:air", "minecraft:tripwire_hook", "other:lockpick",
            "wathe:master_key", "noellesroles:lockpick", "Lockpick", "Master Key", ""})
    void ordinaryItemsAndNameLookalikesCannotOpen(String impostor) {
        assertFalse(VentAccess.canToggle(false, impostor, "minecraft:air"));
        assertFalse(VentAccess.canToggle(false, "minecraft:air", impostor));
    }

    @Test
    void emptyHandsCannotOpenButCanClose() {
        assertFalse(VentAccess.canToggle(false, "minecraft:air", "minecraft:air"));
        assertTrue(VentAccess.canToggle(true, "minecraft:air", "minecraft:air"));
    }

    @Test
    void losingTheKeyDoesNotPreventClosing() {
        assertTrue(VentAccess.canToggle(true, "minecraft:stone", "minecraft:stick"));
    }

    @Test
    void absentRegistryValuesNeverAuthorizeOpening() {
        assertFalse(VentAccess.canToggle(false, null, null));
    }
}
