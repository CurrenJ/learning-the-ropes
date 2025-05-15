package grill24.learningtheropes.datagen;

import grill24.learningtheropes.Blocks;
import grill24.learningtheropes.Items;
import grill24.learningtheropes.LearningTheRopes;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.BlockStateGenerator;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
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

        TextureMapping textureMappingOff = TextureMapping.cube(ResourceLocation.fromNamespaceAndPath(LearningTheRopes.MOD_ID, "block/toggler_off"));
        ResourceLocation togglerModelOff = ModelTemplates.CUBE_ALL.create(ResourceLocation.fromNamespaceAndPath(LearningTheRopes.MOD_ID, "block/toggler_off"),
                textureMappingOff, blockModelGenerators.modelOutput);

        TextureMapping textureMappingOn = TextureMapping.cube(ResourceLocation.fromNamespaceAndPath(LearningTheRopes.MOD_ID, "block/toggler_on"));
        ResourceLocation togglerModelOn = ModelTemplates.CUBE_ALL.create(ResourceLocation.fromNamespaceAndPath(LearningTheRopes.MOD_ID, "block/toggler_on"),
                textureMappingOn, blockModelGenerators.modelOutput);

        BlockStateGenerator togglerGenerator = MultiVariantGenerator.multiVariant(Blocks.TOGGLER.value())
                .with(BlockModelGenerators.createBooleanModelDispatch(BlockStateProperties.LIT, togglerModelOn, togglerModelOff));
        blockModelGenerators.blockStateOutput.accept(togglerGenerator);

        ItemModel.Unbaked togglerItemModel = ItemModelUtils.plainModel(togglerModelOff);
        itemModelGenerators.itemModelOutput.accept(Items.TOGGLER.value(), togglerItemModel);
    }

    @SubscribeEvent
    public static void onGatherData(final GatherDataEvent.Client event) {
        event.createProvider(LtrModelProvider::new);
    }
}
