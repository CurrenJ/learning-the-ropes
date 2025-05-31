package grill24.learningtheropes;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

public class Items {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems(LearningTheRopes.MOD_ID);

    public static final Holder<Item> BLUE_STONE = ITEMS.register("blue_stone", (resourceLocation) -> new BlockItem(Blocks.BLUE_STONE.value(), new Item.Properties()
            .setId(ResourceKey.create(Registries.ITEM, resourceLocation))
    ));

    public static final Holder<Item> TOGGLER = ITEMS.register("toggler", (resourceLocation) -> new BlockItem(Blocks.TOGGLER.value(), new Item.Properties()
            .setId(ResourceKey.create(Registries.ITEM, resourceLocation))
    ));

    public static final Holder<Item> BOX_FAN = ITEMS.register("box_fan", (resourceLocation) -> new BlockItem(Blocks.BOX_FAN.value(), new Item.Properties()
            .setId(ResourceKey.create(Registries.ITEM, resourceLocation))
    ));

    public static final Holder<Item> SOLAR_PANEL = ITEMS.register("solar_panel", (resourceLocation -> new BlockItem(Blocks.SOLAR_PANEL.value(), new Item.Properties()
            .setId(ResourceKey.create(Registries.ITEM, resourceLocation))
    )));
}
