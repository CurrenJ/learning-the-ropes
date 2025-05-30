package grill24.learningtheropes;

import grill24.learningtheropes.gui.SolarPanelMenu;
import grill24.learningtheropes.gui.SolarPanelScreen;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = LearningTheRopes.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class Menus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, LearningTheRopes.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<SolarPanelMenu>> SOLAR_PANEL = MENUS.register("solar_panel",
            () -> new MenuType<>(SolarPanelMenu::new, FeatureFlags.DEFAULT_FLAGS)
    );

    @SubscribeEvent
    public static void onRegisterScreens(final RegisterMenuScreensEvent event) {
        event.register(SOLAR_PANEL.value(), SolarPanelScreen::new);
    }
}
