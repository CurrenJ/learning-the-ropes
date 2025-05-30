package grill24.learningtheropes;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(LearningTheRopes.MOD_ID)
public class LearningTheRopes {
    public static final String MOD_ID = "learningtheropes";

    public LearningTheRopes(IEventBus eventBus, ModContainer modContainer) {
        Blocks.BLOCKS.register(eventBus);
        Items.ITEMS.register(eventBus);
        CreativeTabs.TABS.register(eventBus);
        BlockEntities.BLOCK_ENTITIES.register(eventBus);
        Menus.MENUS.register(eventBus);
    }
}
