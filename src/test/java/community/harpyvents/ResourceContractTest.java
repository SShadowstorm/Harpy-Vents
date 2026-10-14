package community.harpyvents;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.metadata.version.VersionPredicate;
import org.junit.jupiter.api.Test;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ResourceContractTest {
    private JsonObject resource(String path) {
        try (var input = new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream(path), path),
                StandardCharsets.UTF_8)) {
            return JsonParser.parseReader(input).getAsJsonObject();
        } catch (java.io.IOException e) {
            throw new AssertionError(path, e);
        }
    }

    @Test
    void declaredVersionsAcceptTheActualInstalledPack() throws Exception {
        var dependencies = resource("/fabric.mod.json").getAsJsonObject("depends");
        assertTrue(VersionPredicate.parse(dependencies.get("wathe").getAsString())
                .test(Version.parse("1.4.1-1.21.1")));
        assertTrue(VersionPredicate.parse(dependencies.get("noellesroles").getAsString())
                .test(Version.parse("1.7.5-h1.4")));
        assertTrue(VersionPredicate.parse(dependencies.get("minecraft").getAsString())
                .test(Version.parse("1.21.1")));
        assertFalse(VersionPredicate.parse(dependencies.get("minecraft").getAsString())
                .test(Version.parse("1.21.2")));
    }

    @Test
    void everyOrientationAndOpenStateHasARealModel() {
        var variants = resource("/assets/harpyvents/blockstates/vent.json").getAsJsonObject("variants");
        assertEquals(12, variants.size());
        for (String side : new String[]{"down", "up", "north", "south", "west", "east"}) {
            for (boolean open : new boolean[]{false, true}) {
                String model = variants.getAsJsonObject("facing=" + side + ",open=" + open).get("model").getAsString();
                assertTrue(model.startsWith("harpyvents:block/"));
                var json = resource("/assets/harpyvents/models/" + model.substring("harpyvents:".length()) + ".json");
                assertFalse(json.getAsJsonArray("elements").isEmpty());
                for (var element : json.getAsJsonArray("elements")) {
                    var bounds = element.getAsJsonObject();
                    assertEquals(3, bounds.getAsJsonArray("from").size());
                    assertEquals(3, bounds.getAsJsonArray("to").size());
                    for (int axis = 0; axis < 3; axis++) {
                        var from = bounds.getAsJsonArray("from").get(axis);
                        var to = bounds.getAsJsonArray("to").get(axis);
                        assertTrue(from.isJsonPrimitive() && from.getAsJsonPrimitive().isNumber(), model);
                        assertTrue(to.isJsonPrimitive() && to.getAsJsonPrimitive().isNumber(), model);
                        assertTrue(Double.isFinite(from.getAsDouble()) && Double.isFinite(to.getAsDouble()), model);
                        assertTrue(from.getAsDouble() >= 0 && to.getAsDouble() <= 16, model);
                        assertTrue(from.getAsDouble() < to.getAsDouble(), model);
                    }
                }
                assertEquals("wathe:block/stainless_steel_vent_hatch",
                        json.getAsJsonObject("textures").get("grille").getAsString());
            }
        }
    }
}
