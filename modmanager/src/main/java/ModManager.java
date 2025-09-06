import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

@Mod("modmanager")
public class ModManager {

	private static final String PROTOCOL_VERSION = "1.0";
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
		new ResourceLocation("modmanager", "modlist"),
		() -> PROTOCOL_VERSION,
		PROTOCOL_VERSION::equals,
		PROTOCOL_VERSION::equals
	);

	public ModManager() {
		MinecraftForge.EVENT_BUS.register(this);
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		final int id = 0;
		// New builder-based definition
		INSTANCE.messageBuilder(MessagePacket.class, id)
			.encoder(MessagePacket::encoder)
			.decoder(MessagePacket::decoder)
			.consumerMainThread(MessagePacket::messageConsumer)
			.add();
		// You can use consumerMainThread or consumerNetworkThread.
		// If you use consumerMainThread, the builder will take care of the enqueueWork and setPacketHandled.
		// With consumerNetworkThread, you can return a value instead of calling setPacketHandled.
	}

	public static void handle(MyMessage msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			// Work that needs to be threadsafe (most work)
			ServerPlayer sender = ctx.get().getSender(); // the client that sent this packet
			// do stuff
		});
		ctx.get().setPacketHandled(true);
	}
}
