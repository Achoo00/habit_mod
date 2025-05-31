package net.amaha.habitmod.network;

import net.amaha.habitmod.HabitMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
// import net.minecraftforge.network.SimpleChannel;
import net.minecraftforge.network.simple.SimpleChannel;
public class PacketHandler {
    private static SimpleChannel INSTANCE; // Our network channel
    private static int packetId = 0;      // Counter for unique packet IDs

    private static int id() {
        return packetId++;
    } // Helper to get unique IDs

    public static void register() {
        // Create the channel. The "main_channel" is a unique name for your channel.
        INSTANCE = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(HabitMod.MOD_ID, "main_channel"), // Your mod ID and channel name
                () -> "1.0", // Network Protocol Version for your mod (client)
                v -> true,   // Client accepts this protocol version
                v -> true    // Server accepts this protocol version
        );

        // Register our ApplyEffectPacket. It's sent from Client to Server.
        INSTANCE.registerMessage(
                id(), // Unique ID for this packet
                ApplyEffectPacket.class, // The packet class
                ApplyEffectPacket::encode, // Method to write packet data
                ApplyEffectPacket::new, // Method to read packet data
                ApplyEffectPacket::handle, // Method to process the packet
                // Specify the direction: FROM_CLIENT indicates it's sent from client to server
                // TO_CLIENT would be from server to client
                java.util.Optional.of(NetworkDirection.PLAY_TO_SERVER) // <--- CRUCIAL CHANGE
        );
    }

    // Helper method to send a packet from the client to the server
    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }
}