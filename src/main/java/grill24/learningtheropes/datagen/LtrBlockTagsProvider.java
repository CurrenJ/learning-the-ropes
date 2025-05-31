package grill24.learningtheropes.datagen;

import grill24.learningtheropes.Blocks;
import grill24.learningtheropes.LearningTheRopes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = LearningTheRopes.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class LtrBlockTagsProvider extends BlockTagsProvider {
    public LtrBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, LearningTheRopes.MOD_ID);
    }

    @SubscribeEvent
    public static void onGatherDataEvent(final GatherDataEvent.Client event) {
        event.createProvider(LtrBlockTagsProvider::new);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(Blocks.BLUE_STONE.value(), Blocks.TOGGLER.value(), Blocks.BOX_FAN.value(), Blocks.SOLAR_PANEL.value());
    }
}
