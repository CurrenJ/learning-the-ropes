package grill24.learningtheropes.datagen;

import grill24.learningtheropes.Blocks;
import grill24.learningtheropes.LearningTheRopes;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = LearningTheRopes.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class LtrModelProvider extends ModelProvider {
    public LtrModelProvider(PackOutput output) {
        super(output, LearningTheRopes.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModelGenerators, ItemModelGenerators itemModelGenerators) {
        blockModelGenerators.createTrivialCube(Blocks.BLUE_STONE.value());
    }

    @SubscribeEvent
    public static void onGatherData(final GatherDataEvent.Client event) {
        event.createProvider(LtrModelProvider::new);
    }
}
