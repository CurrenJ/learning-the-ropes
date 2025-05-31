package grill24.learningtheropes.block.entity;

import grill24.learningtheropes.BlockEntities;
import grill24.learningtheropes.LearningTheRopes;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;

@EventBusSubscriber(modid = LearningTheRopes.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class SolarPanelBlockEntity extends RandomizableContainerBlockEntity {
    private NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);
    private EnergyStorage energyStorage;
    private IEnergyStorage energyExtract;

    public SolarPanelBlockEntity(BlockPos blockPos, BlockState state) {
        super(BlockEntities.SOLAR_PANEL.value(), blockPos, state);

        energyStorage = new EnergyStorage(1000);
        energyExtract = new IEnergyStorage() {
            @Override
            public int receiveEnergy(int i, boolean b) {
                return 0;
            }

            @Override
            public int extractEnergy(int i, boolean b) {
                return energyStorage.extractEnergy(i, b);
            }

            @Override
            public int getEnergyStored() {
                return energyStorage.getEnergyStored();
            }

            @Override
            public int getMaxEnergyStored() {
                return energyStorage.getMaxEnergyStored();
            }

            @Override
            public boolean canExtract() {
                return energyStorage.canExtract();
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
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> nonNullList) {
        this.items = nonNullList;
    }

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return null;
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
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
            energyStorage.deserializeNBT(provider, energyStorageTag);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        if (!this.trySaveLootTable(tag)) {
            ContainerHelper.saveAllItems(tag, this.items, provider);
        }

        tag.put("EnergyStorage", energyStorage.serializeNBT(provider));
    }

    public static IEnergyStorage getCapabilities(SolarPanelBlockEntity solarPanelBlockEntity, Direction direction) {
        if (direction == Direction.DOWN) {
            return solarPanelBlockEntity.energyExtract;
        }

        return null;
    }

    public static void tickServer(Level level, BlockPos pos, BlockState state, SolarPanelBlockEntity blockEntity) {
        if (level instanceof ServerLevel serverLevel) {
            if (level.isDay() && level.canSeeSky(pos)) {
                blockEntity.energyStorage.receiveEnergy(1, false);
            }

            System.out.println(blockEntity.energyStorage.getEnergyStored());

            IEnergyStorage other = serverLevel.getCapability(Capabilities.EnergyStorage.BLOCK, pos.below(), Direction.UP);
            if (other != null) {
                int energyExtracted = blockEntity.energyStorage.extractEnergy(10, true);
                int energySent = other.receiveEnergy(energyExtracted, false);
                blockEntity.energyStorage.extractEnergy(energySent, false);
            }
        }
    }

    @SubscribeEvent
    public static void onCapabilitiesRegister(final RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                BlockEntities.SOLAR_PANEL.get(),
                SolarPanelBlockEntity::getCapabilities);
    }
}
