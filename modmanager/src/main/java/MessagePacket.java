import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessagePacket {
	public void encoder(FriendlyByteBuf buffer) {
		// Write to buffer
	}

	public static MessagePacket decoder(FriendlyByteBuf buffer) {
		// Create packet from buffer data
		return null;
	}

	public void messageConsumer(Supplier<NetworkEvent.Context> ctx) {
		// Handle message
	}
}
