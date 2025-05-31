package grill24.learningtheropes.block;

import com.mojang.serialization.MapCodec;
import grill24.learningtheropes.BlockEntities;
import grill24.learningtheropes.block.entity.SolarPanelBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class SolarPanelBlock extends BaseEntityBlock {
    public static final MapCodec<SolarPanelBlock> CODEC = simpleCodec(SolarPanelBlock::new);

    public static final VoxelShape shape = box(2, 0, 2, 14, 8, 14);

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
        return shape;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return shape;
    }

    @javax.annotation.Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (level instanceof ServerLevel serverLevel) {
            return createTickerHelper(
                    blockEntityType,
                    BlockEntities.SOLAR_PANEL.value(),
                    SolarPanelBlockEntity::tickServer
            );
        }

        return null;
    }
}
