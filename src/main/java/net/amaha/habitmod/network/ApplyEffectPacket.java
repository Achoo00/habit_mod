package net.amaha.habitmod.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects; // For MobEffects.byId

import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public class ApplyEffectPacket {
    private final int effectId;
    private final int duration;
    private final int amplifier;
    private final boolean ambient;
    private final boolean showParticles;

    // Constructor to create the packet
    public ApplyEffectPacket(MobEffect effect, int duration, int amplifier, boolean ambient, boolean showParticles) {
        this.effectId = MobEffect.getId(effect); // Get the integer ID of the MobEffect
        this.duration = duration;
        this.amplifier = amplifier;
        this.ambient = ambient;
        this.showParticles = showParticles;
    }

    // Constructor for decoding the packet (used by Forge)
    public ApplyEffectPacket(FriendlyByteBuf buffer) {
        this.effectId = buffer.readInt();
        this.duration = buffer.readInt();
        this.amplifier = buffer.readInt();
        this.ambient = buffer.readBoolean();
        this.showParticles = buffer.readBoolean();
    }

    // Method to encode (write) the packet data into the buffer
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.effectId);
        buffer.writeInt(this.duration);
        buffer.writeInt(this.amplifier);
        buffer.writeBoolean(this.ambient);
        buffer.writeBoolean(this.showParticles);
    }

    // Method to handle (process) the packet when it's received
    public static void handle(ApplyEffectPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // This code runs on the Server thread!
            ServerPlayer player = ctx.get().getSender(); // Get the player who sent the packet
            if (player != null) {
                MobEffect effect = MobEffect.byId(msg.effectId); // Get the MobEffect object from its ID
                // MobEffect effect = MobEffects.class // Get the MobEffect object from its ID
                if (effect != null) {
                    player.addEffect(new MobEffectInstance(effect, msg.duration, msg.amplifier, msg.ambient, msg.showParticles));
                }
            }
        });
        ctx.get().setPacketHandled(true); // Mark the packet as handled
    }
}