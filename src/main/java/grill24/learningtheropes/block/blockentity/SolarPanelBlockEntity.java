package grill24.learningtheropes.block.blockentity;

import grill24.learningtheropes.BlockEntities;
import grill24.learningtheropes.LearningTheRopes;
import grill24.learningtheropes.gui.SolarPanelMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.List;

@EventBusSubscriber(modid = LearningTheRopes.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class SolarPanelBlockEntity extends RandomizableContainerBlockEntity {
    private NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);

    private final EnergyStorage energyStorageCapability;
    private final IEnergyStorage energyOutputCapability;
    private static final int MAX_STORED_ENERGY = 1000;

    private int energyStoredLastTick = 0; // Used to track energy changes
    private int lastTickEnergyDelta = 0;

    public SolarPanelBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntities.SOLAR_PANEL_BLOCK_ENTITY.value(), pos, blockState);
        energyStorageCapability = new EnergyStorage(MAX_STORED_ENERGY);
        energyOutputCapability = new IEnergyStorage() {
            @Override
            public int receiveEnergy(int i, boolean b) {
                return 0; // This block entity does not receive energy, it only outputs
            }

            @Override
            public int extractEnergy(int i, boolean b) {
                return energyStorageCapability.extractEnergy(i, b); // Extract energy from the storage
            }

            @Override
            public int getEnergyStored() {
                return energyStorageCapability.getEnergyStored();
            }

            @Override
            public int getMaxEnergyStored() {
                return energyStorageCapability.getMaxEnergyStored();
            }

            @Override
            public boolean canExtract() {
                return true; // This block entity can output energy
            }

            @Override
            public boolean canReceive() {
                return false;
            }
        };

    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.learningtheropes.solar_panel");
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> nonNullList) {
        this.items = nonNullList;
    }

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return new SolarPanelMenu(i, inventory, this);
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, SolarPanelBlockEntity blockEntity) {
        if (level instanceof ServerLevel serverLevel) {
            // Check if it's daytim
            if (serverLevel.isDay() && serverLevel.canSeeSky(pos)) {
                blockEntity.energyStorageCapability.receiveEnergy(2, false); // Generate 10 energy units per tick
            }

            blockEntity.energyStorageCapability.extractEnergy(1, false); // Lossy :(        >:)

            final int maxEnergySend = 10;
            // Push energy to the block below if it can receive energy
            IEnergyStorage energyStorage = serverLevel.getCapability(Capabilities.EnergyStorage.BLOCK, pos.below(), Direction.UP);
            if (energyStorage != null && energyStorage.canReceive()) {
                // Transfer energy from the block entity to the block below
                pushEnergy(blockEntity, maxEnergySend, energyStorage);
            }

            List<ItemStack> items = blockEntity.getItems();
            if (!items.isEmpty()) {
                ItemStack stack = items.getFirst();
                IEnergyStorage itemEnergyStorage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
                if (itemEnergyStorage != null && itemEnergyStorage.canReceive()) {
                    // Transfer energy from the block entity to the item
                    pushEnergy(blockEntity, maxEnergySend, itemEnergyStorage);
                }
            }

            int postTickEnergy = blockEntity.getStoredEnergy();
            blockEntity.lastTickEnergyDelta = postTickEnergy - blockEntity.energyStoredLastTick;
            blockEntity.energyStoredLastTick = postTickEnergy;
        }
    }

    private static void pushEnergy(SolarPanelBlockEntity blockEntity, int maxEnergySend, IEnergyStorage energyStorage) {
        int energyToTransfer = blockEntity.energyStorageCapability.extractEnergy(maxEnergySend, true);
        if (energyToTransfer > 0) {
            int sent = energyStorage.receiveEnergy(energyToTransfer, false);
            blockEntity.energyStorageCapability.extractEnergy(sent, false);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(tag)) {
            ContainerHelper.loadAllItems(tag, this.items, provider);
        }

        Tag energyStorageTag = tag.get("EnergyStorage");
        if (energyStorageTag != null) {
            energyStorageCapability.deserializeNBT(provider, energyStorageTag);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        if (!this.trySaveLootTable(tag)) {
            ContainerHelper.saveAllItems(tag, this.items, provider);
        }

        tag.put("EnergyStorage", energyStorageCapability.serializeNBT(provider));
    }

    /**
     * DataSlot to allow us to update the client GUI with the current energy level.
     */
    public static class EnergyStorageSlot extends DataSlot {
        private final SolarPanelBlockEntity blockEntity;

        private EnergyStorageSlot(SolarPanelBlockEntity blockEntity) {
            this.blockEntity = blockEntity;
        }

        public static EnergyStorageSlot standalone(SolarPanelBlockEntity blockEntity) {
            return new EnergyStorageSlot(blockEntity);
        }

        @Override
        public int get() {
            // Return the current energy level from the block entity
            return Math.round(((float)blockEntity.getStoredEnergy()) / blockEntity.getMaxStoredEnergy() * 100);
        }

        @Override
        public void set(int value) {
            return; // No need to set energy directly, it's managed by the block entity
        }
    }

    public static IEnergyStorage getEnergyStorageCapability(SolarPanelBlockEntity blockEntity, Direction side) {
        if (side == Direction.DOWN) {
            return blockEntity.energyOutputCapability; // Output energy to the block below
        }
        return null; // No energy output or input on other sides
    }

    public int getStoredEnergy() {
        return energyStorageCapability.getEnergyStored();
    }

    public int getLastEnergyDelta() {
        return lastTickEnergyDelta;
    }

    public int getMaxStoredEnergy() {
        return energyStorageCapability.getMaxEnergyStored();
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(final RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                BlockEntities.SOLAR_PANEL_BLOCK_ENTITY.value(),
                SolarPanelBlockEntity::getEnergyStorageCapability
        );
    }
}
