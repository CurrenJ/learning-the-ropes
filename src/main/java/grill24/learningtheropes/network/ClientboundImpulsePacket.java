package grill24.learningtheropes.network;

import grill24.learningtheropes.LearningTheRopes;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ClientboundImpulsePacket(Vec3 impulse) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ClientboundImpulsePacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(LearningTheRopes.MOD_ID, "impulse"));

    public static final StreamCodec<ByteBuf, ClientboundImpulsePacket> STREAM_CODEC = StreamCodec.composite(
            Vec3.STREAM_CODEC,
            ClientboundImpulsePacket::impulse,
            ClientboundImpulsePacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static class ClientPacketHandler {
        public static void handle(final ClientboundImpulsePacket packet, final IPayloadContext context) {
            context.player().addDeltaMovement(packet.impulse());
        }
    }
}
