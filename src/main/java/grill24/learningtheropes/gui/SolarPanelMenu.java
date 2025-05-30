package grill24.learningtheropes.gui;

import grill24.learningtheropes.Menus;
import grill24.learningtheropes.block.blockentity.SolarPanelBlockEntity;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SolarPanelMenu extends AbstractContainerMenu {
    public static class DisplayData {
        public static final int ENERGY_STORAGE = 0;
        public static final int LAST_ENERGY_DELTA = 1;

        public static ContainerData createServer(Container container) {
            if (container instanceof SolarPanelBlockEntity solarPanelBlockEntity) {
                return new SimpleContainerData(2) {
                    @Override
                    public int get(int index) {
                        return (int) switch (index) {
                            // Return percentage of energy stored
                            case ENERGY_STORAGE -> solarPanelBlockEntity.getStoredEnergy() / ((float) solarPanelBlockEntity.getMaxStoredEnergy()) * 100;
                            case LAST_ENERGY_DELTA -> solarPanelBlockEntity.getLastEnergyDelta();
                            default -> 0;
                        };
                    }

                    @Override
                    public void set(int index, int value) {
                        // No-op, as this data is read-only from the server side
                    }

                    @Override
                    public int getCount() {
                        return 2;
                    }
                };
            }

            return new SimpleContainerData(2);
        }
    }

    private static final int CONTAINER_SIZE = 1; // Size of the container, can be adjusted as needed
    private Container container;
    private final ContainerData containerData;

    // Client menu constructor
    public SolarPanelMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(CONTAINER_SIZE), new SimpleContainerData(2));
    }

    // Server menu constructor
    public SolarPanelMenu(int containerId, Inventory playerInventory, Container container) {
        this(containerId, playerInventory, container, DisplayData.createServer(container));
    }

    private SolarPanelMenu(int containerId, Inventory playerInventory, Container container, ContainerData containerData) {
        super(Menus.SOLAR_PANEL.value(), containerId);
        checkContainerSize(container, CONTAINER_SIZE);
        this.container = container;
        container.startOpen(playerInventory.player);
        this.addSlot(new Slot(container, 0, 80, 36));
        this.addStandardInventorySlots(playerInventory, 8, 84);

        this.containerData = containerData;
        this.addDataSlots(this.containerData);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        int containerSlots = 1;
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < containerSlots) {
                // If the item is in the container slots, try to move it to the player's inventory
                if (!this.moveItemStackTo(itemstack1, containerSlots, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, containerSlots, false)) {
                // If the item is in the player's inventory, try to move it to the container slots
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;

    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    public int getEnergyStorage() {
        return this.containerData.get(DisplayData.ENERGY_STORAGE);
    }

    public int getLastEnergyDelta() {
        return this.containerData.get(DisplayData.LAST_ENERGY_DELTA);
    }
}
