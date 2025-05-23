package grill24.learningtheropes;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = LearningTheRopes.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class CreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, LearningTheRopes.MOD_ID);

    public static final Holder<CreativeModeTab> LTR_TAB = TABS.register("ltr", () -> CreativeModeTab.builder()
            .title(Component.translatable("tab.learningtheropes.title"))
            .icon(() -> new ItemStack(Blocks.BLUE_STONE.value()))
            .displayItems((params, output) -> {
                output.accept(Blocks.BLUE_STONE.value());
                output.accept(Blocks.TOGGLER.value());
                output.accept(Blocks.BOX_FAN.value());
            })
            .build());

    @SubscribeEvent
    public static void onCreativeModeTabs(final BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            event.accept(new ItemStack(Blocks.BLUE_STONE.value()));
        }
    }
}
