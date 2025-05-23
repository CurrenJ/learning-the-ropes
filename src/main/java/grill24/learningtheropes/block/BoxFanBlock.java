package grill24.learningtheropes.block;

import grill24.learningtheropes.Blocks;
import grill24.learningtheropes.network.ClientboundImpulsePacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

public class BoxFanBlock extends TogglerBlock {
    public BoxFanBlock(Properties prop) {
        super(prop);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(BlockStateProperties.LIT, false)
                .setValue(BlockStateProperties.FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.LIT).add(BlockStateProperties.FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(BlockStateProperties.LIT, false)
                .setValue(BlockStateProperties.FACING, context.getClickedFace());
    }

    @Override
    protected void tick(BlockState state, ServerLevel serverLevel, BlockPos pos, RandomSource random) {
        super.tick(state, serverLevel, pos, random);

        if (state.getValue(BlockStateProperties.LIT)) {
            Direction facing = state.getValue(BlockStateProperties.FACING);
            AABB volume = createAabbExtendingInDirection(facing, pos, 8);
            for (Entity entity : serverLevel.getEntitiesOfClass(Entity.class, volume)) {
                Vec3 impulse = Vec3.ZERO.relative(facing, 0.25F);
                entity.push(impulse);

                if (entity instanceof ServerPlayer serverPlayer) {
                    PacketDistributor.sendToPlayer(serverPlayer, new ClientboundImpulsePacket(impulse));
                }
            }
        }

        serverLevel.scheduleTick(pos, Blocks.BOX_FAN.value(), 2);
    }

    private static AABB createAabbExtendingInDirection(Direction direction, BlockPos origin, int length) {
        Vec3 area = Vec3.ZERO.relative(direction, length); // 1d volume extended in direction
        return AABB.ofSize(
                // shift origin to end of volume rather than center
                origin.getCenter().add(area.multiply(new Vec3(0.5, 0.5, 0.5))),
                // change 0 sized dimensions to 1 block size instead; inflate 1d volume to 3d
                area.x() == 0 ? 1 : area.x(), area.y() == 0 ? 1 : area.y(), area.z() == 0 ? 1 : area.z()
        );
    }
}
