package grill24.learningtheropes;

import grill24.learningtheropes.block.entity.SolarPanelBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Set;

public class BlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, LearningTheRopes.MOD_ID);

    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<SolarPanelBlockEntity>> SOLAR_PANEL = BLOCK_ENTITIES.register(
            "solar_panel",
            () -> new BlockEntityType<>(SolarPanelBlockEntity::new, Set.of(Blocks.SOLAR_PANEL.value()))
    );
}
