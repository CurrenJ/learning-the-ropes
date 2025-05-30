package grill24.learningtheropes.datagen;

import grill24.learningtheropes.Blocks;
import grill24.learningtheropes.Items;
import grill24.learningtheropes.LearningTheRopes;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.BlockStateGenerator;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Optional;

@EventBusSubscriber(modid = LearningTheRopes.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class LtrModelProvider extends ModelProvider {
    public LtrModelProvider(PackOutput output) {
        super(output, LearningTheRopes.MOD_ID);
    }

    public static final ModelTemplate BOX_FAN = new ModelTemplate(
            Optional.of(ResourceLocation.fromNamespaceAndPath(LearningTheRopes.MOD_ID, "block/box_fan")),
            Optional.empty(),
            TextureSlot.FAN
    );

    @Override
    protected void registerModels(BlockModelGenerators blockModelGenerators, ItemModelGenerators itemModelGenerators) {
        // Blue Stone
        blockModelGenerators.createTrivialCube(Blocks.BLUE_STONE.value());

        // Toggler Block
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

        // Box Fan Block
        TextureMapping boxFanTextureIdle = new TextureMapping().put(TextureSlot.FAN, ResourceLocation.fromNamespaceAndPath(LearningTheRopes.MOD_ID, "block/box_fan_idle"));
        ResourceLocation boxFanModelIdle = BOX_FAN.create(
                ResourceLocation.fromNamespaceAndPath(LearningTheRopes.MOD_ID, "block/box_fan_idle"),
                boxFanTextureIdle,
                blockModelGenerators.modelOutput);

        TextureMapping boxFanTextureActive = new TextureMapping().put(TextureSlot.FAN, ResourceLocation.fromNamespaceAndPath(LearningTheRopes.MOD_ID, "block/box_fan"));
        ResourceLocation boxFanModelActive = BOX_FAN.create(
                ResourceLocation.fromNamespaceAndPath(LearningTheRopes.MOD_ID, "block/box_fan_active"),
                boxFanTextureActive,
                blockModelGenerators.modelOutput
        );

        BlockStateGenerator boxFanGenerator = MultiVariantGenerator.multiVariant(Blocks.BOX_FAN.value())
                .with(BlockModelGenerators.createBooleanModelDispatch(BlockStateProperties.LIT, boxFanModelActive, boxFanModelIdle))
                .with(BlockModelGenerators.createFacingDispatch());
        blockModelGenerators.blockStateOutput.accept(boxFanGenerator);

        ItemModel.Unbaked boxFanItemModel = ItemModelUtils.plainModel(boxFanModelIdle);
        itemModelGenerators.itemModelOutput.accept(Items.BOX_FAN.value(), boxFanItemModel);

        // Solar Panel Block
        ResourceLocation solarPanelModel = ResourceLocation.fromNamespaceAndPath(LearningTheRopes.MOD_ID, "block/solar_panel");
        BlockStateGenerator solarPanelGenerator = BlockModelGenerators.createSimpleBlock(Blocks.SOLAR_PANEL.value(), solarPanelModel);
        blockModelGenerators.blockStateOutput.accept(solarPanelGenerator);

        ItemModel.Unbaked solarPanelItemModel = ItemModelUtils.plainModel(solarPanelModel);
        itemModelGenerators.itemModelOutput.accept(Items.SOLAR_PANEL.value(), solarPanelItemModel);
    }

    @SubscribeEvent
    public static void onGatherData(final GatherDataEvent.Client event) {
        event.createProvider(LtrModelProvider::new);
    }
}
