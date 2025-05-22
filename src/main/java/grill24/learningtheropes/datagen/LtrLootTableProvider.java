package grill24.learningtheropes.datagen;

import grill24.learningtheropes.LearningTheRopes;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Set;

@EventBusSubscriber(modid = LearningTheRopes.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class LtrLootTableProvider {
    @SubscribeEvent
    public static void onGatherDataEvent(final GatherDataEvent.Client event) {
        event.createProvider((packOutput, lookupProvider) -> new LootTableProvider(
                packOutput,
                Set.of(),
                List.of(new LootTableProvider.SubProviderEntry(LtrBlockLootTableSubProvider::new, LootContextParamSets.BLOCK)),
                lookupProvider
        ));
    }
}
