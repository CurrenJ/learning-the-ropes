package grill24.learningtheropes.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.redstone.Orientation;

import javax.annotation.Nullable;

public class TogglerBlock extends Block {
    public TogglerBlock(Properties prop) {
        super(prop);
        this.registerDefaultState(this.defaultBlockState().setValue(BlockStateProperties.LIT, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.LIT);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(BlockStateProperties.LIT, false);
    }

    @Override
    protected void neighborChanged(BlockState p_55666_, Level p_55667_, BlockPos p_55668_, Block p_55669_, @Nullable Orientation p_364297_, boolean p_55671_) {
        if (!p_55667_.isClientSide) {
            boolean flag = p_55666_.getValue(BlockStateProperties.LIT);
            if (flag != p_55667_.hasNeighborSignal(p_55668_)) {
                if (flag) {
                    p_55667_.scheduleTick(p_55668_, this, 4);
                } else {
                    p_55667_.setBlock(p_55668_, p_55666_.cycle(BlockStateProperties.LIT), 2);
                }
            }
        }
    }

    @Override
    protected void tick(BlockState p_221937_, ServerLevel p_221938_, BlockPos p_221939_, RandomSource p_221940_) {
        if (p_221937_.getValue(BlockStateProperties.LIT) && !p_221938_.hasNeighborSignal(p_221939_)) {
            p_221938_.setBlock(p_221939_, p_221937_.cycle(BlockStateProperties.LIT), 2);
        }
    }
}
