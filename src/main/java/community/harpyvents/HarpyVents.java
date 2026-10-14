package community.harpyvents;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public final class HarpyVents implements ModInitializer {
    public static final String MOD_ID = "harpyvents";
    public static final Identifier VENT_ID = Identifier.of(MOD_ID, "vent");
    public static final Block VENT = new VentBlock(AbstractBlock.Settings.create()
            .mapColor(MapColor.IRON_GRAY)
            .strength(-1.0F, 3_600_000.0F)
            .sounds(BlockSoundGroup.METAL)
            .nonOpaque());

    @Override
    public void onInitialize() {
        Registry.register(Registries.BLOCK, VENT_ID, VENT);
        Item ventItem = Registry.register(Registries.ITEM, VENT_ID,
                new BlockItem(VENT, new Item.Settings()));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> entries.add(ventItem));
    }
}
