package grill24.learningtheropes.block;

import com.mojang.serialization.MapCodec;
import grill24.learningtheropes.BlockEntities;
import grill24.learningtheropes.block.blockentity.SolarPanelBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class SolarPanelBlock extends BaseEntityBlock {
    public static final MapCodec<SolarPanelBlock> CODEC = simpleCodec(SolarPanelBlock::new);

    private final VoxelShape shape = box(2, 0, 2, 14, 8, 14); // A simple flat shape for the solar panel

    public SolarPanelBlock(Properties prop) {
        super(prop);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SolarPanelBlockEntity(blockPos, blockState);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return this.shape;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return this.shape;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
        if (level instanceof ServerLevel serverlevel) {
            MenuProvider menuprovider = this.getMenuProvider(state, level, blockPos);
            if (menuprovider != null) {
                player.openMenu(menuprovider);
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    protected InteractionResult useItemOn(
            ItemStack stack, BlockState state, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        return useWithoutItem(state, level, blockPos, player, blockHitResult);
    }

    @javax.annotation.Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> createTicker(
            Level level, BlockEntityType<T> serverType, BlockEntityType<? extends SolarPanelBlockEntity> clientType
    ) {
        return level instanceof ServerLevel serverlevel
                ? createTickerHelper(serverType, clientType,
                (level1, blockPos, blockState, solarPanelBlockEntity) ->
                        SolarPanelBlockEntity.serverTick(serverlevel, blockPos, blockState, solarPanelBlockEntity))
                : null;
    }

    @javax.annotation.Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTicker(level, blockEntityType, BlockEntities.SOLAR_PANEL_BLOCK_ENTITY.value());
    }
}
