package grill24.learningtheropes.datagen;

import grill24.learningtheropes.Blocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.Set;

public class LtrBlockLootTableSubProvider extends BlockLootSubProvider {
    protected LtrBlockLootTableSubProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.DEFAULT_FLAGS, registries);
    }

    @Override
    public Iterable<Block> getKnownBlocks() {
        return List.of(
                Blocks.BLUE_STONE.value(),
                Blocks.TOGGLER.value(),
                Blocks.BOX_FAN.value()
        );
    }

    @Override
    protected void generate() {
        this.dropSelf(Blocks.BLUE_STONE.value());
        this.dropSelf(Blocks.TOGGLER.value());
        this.dropSelf(Blocks.BOX_FAN.value());
    }
}
