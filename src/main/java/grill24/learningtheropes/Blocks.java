package grill24.learningtheropes;

import grill24.learningtheropes.block.BoxFanBlock;
import grill24.learningtheropes.block.TogglerBlock;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DeferredRegister;

public class Blocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.createBlocks(LearningTheRopes.MOD_ID);

    public static final Holder<Block> BLUE_STONE = BLOCKS.register("blue_stone", (resourceLocation) -> new Block(
            BlockBehaviour.Properties.ofFullCopy(net.minecraft.world.level.block.Blocks.STONE)
                    .requiresCorrectToolForDrops()
                    .setId(ResourceKey.create(Registries.BLOCK, resourceLocation))
    ));

    public static final Holder<Block> TOGGLER = BLOCKS.register("toggler", (resourceLocation) -> new TogglerBlock(BlockBehaviour.Properties.of()
            .setId(ResourceKey.create(Registries.BLOCK, resourceLocation))
    ));

    public static final Holder<Block> BOX_FAN = BLOCKS.register("box_fan", (resourceLocation) -> new BoxFanBlock(
            BlockBehaviour.Properties.ofFullCopy(net.minecraft.world.level.block.Blocks.DISPENSER)
                    .setId(ResourceKey.create(Registries.BLOCK, resourceLocation))
                    .requiresCorrectToolForDrops()
    ));

    public static final Holder<Block> SOLAR_PANEL = BLOCKS.register("solar_panel", (resourceLocation) -> new grill24.learningtheropes.block.SolarPanelBlock(
            BlockBehaviour.Properties.ofFullCopy(net.minecraft.world.level.block.Blocks.IRON_BLOCK)
                    .setId(ResourceKey.create(Registries.BLOCK, resourceLocation))
                    .requiresCorrectToolForDrops()
                    .noOcclusion()
    ));
}
