package grill24.learningtheropes.network;

import grill24.learningtheropes.LearningTheRopes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.MainThreadPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = LearningTheRopes.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class Network {
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");

        registrar.playToClient(
                ClientboundImpulsePacket.TYPE,
                ClientboundImpulsePacket.STREAM_CODEC,
                new MainThreadPayloadHandler<>(
                        ClientboundImpulsePacket.ClientPacketHandler::handle
                )
        );
    }
}
