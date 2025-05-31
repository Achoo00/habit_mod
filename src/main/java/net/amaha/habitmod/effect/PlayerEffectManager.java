package net.amaha.habitmod.effect;

import net.amaha.habitmod.data.AuraManager;
import net.amaha.habitmod.network.ApplyEffectPacket;
import net.amaha.habitmod.network.PacketHandler;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Manages the application of effects to players based on their aura tier.
 */
@Mod.EventBusSubscriber
public class PlayerEffectManager {
    // How often to check and apply tier effects (in ticks)
    private static final int EFFECT_CHECK_INTERVAL = 100; // About 5 seconds

    // How often to decay aura (in ticks)
    private static final int AURA_DECAY_INTERVAL = 600; // 30 seconds (20 ticks per second)

    // Effect durations and amplifiers
    private static final int EFFECT_DURATION = 200; // 10 seconds (longer than check interval to ensure continuous effect)
    private static final int SLOWNESS_AMPLIFIER = 0; // Amplifier 0 = 10% slower
    private static final int WEAKNESS_AMPLIFIER = 0; // Amplifier 0 = 0.5 less damage
    private static final int HUNGER_AMPLIFIER = 0; // Amplifier 0 = hunger depletes faster

    // Counter for timing effect checks
    private static int tickCounter = 0;

    // Counter for timing aura decay
    private static int auraDecayCounter = 0;

    // Track the previous tier to detect tier changes
    private static int previousTier = -1;

    /**
     * Handles player tick events to apply tier effects periodically.
     * @param event The player tick event.
     */
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        // Only process on server side and at the end of the tick
        if (event.phase == TickEvent.Phase.END && !event.player.level().isClientSide()) {
            tickCounter++;
            auraDecayCounter++;

            // Check and apply effects periodically
            if (tickCounter >= EFFECT_CHECK_INTERVAL) {
                tickCounter = 0;
                applyTierEffects(event.player);
            }

            // Decay aura periodically
            if (auraDecayCounter >= AURA_DECAY_INTERVAL) {
                auraDecayCounter = 0;
                // Decrease aura by 1 (AuraManager.removeAura already ensures aura doesn't go below 0)
                AuraManager.removeAura(1);

                // Notify player about aura decay (optional)
                if (event.player != null) {
                    event.player.sendSystemMessage(net.minecraft.network.chat.Component.literal("Your aura has decayed by 1. Current Aura: " + AuraManager.getAuraLevel()));
                }
            }
        }
    }

    /**
     * Applies effects based on the player's current aura tier.
     * @param player The player to apply effects to.
     */
    private static void applyTierEffects(Player player) {
        // Get current tier
        int currentTier = AuraManager.getCurrentTier();

        // Check for tier changes to update health
        if (previousTier != currentTier) {
            // Update max health based on current tier
            if (currentTier == 0) {
                // Set max health to half (10 hearts -> 5 hearts)
                player.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH)
                      .setBaseValue(10.0D); // 10 = 5 hearts (half of normal 20 = 10 hearts)
                player.setHealth(Math.min(player.getHealth(), 10.0F)); // Ensure health doesn't exceed new max
            } else {
                // Restore normal max health when moving to Tier 1 or higher
                player.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH)
                      .setBaseValue(20.0D); // 20 = 10 hearts (normal)

                // If coming from Tier 0, heal the player to full health
                if (previousTier == 0) {
                    player.setHealth(player.getMaxHealth()); // Set to full health
                    player.sendSystemMessage(net.minecraft.network.chat.Component.literal("Your vitality has been restored!"));
                }
            }

            // Update previous tier
            previousTier = currentTier;
        }

        // Check if player is in Tier 0 (Incapable Builder)
        if (currentTier == 0) {
            // Apply Tier 0 effects

            // 10% slower movement (Slowness I)
            player.addEffect(new net.minecraft.world.effect.MobEffectInstance(
                    MobEffects.MOVEMENT_SLOWDOWN, 
                    EFFECT_DURATION, 
                    SLOWNESS_AMPLIFIER, 
                    false, // ambient
                    false  // show particles
            ));

            // Weakness effect
            player.addEffect(new net.minecraft.world.effect.MobEffectInstance(
                    MobEffects.WEAKNESS,
                    EFFECT_DURATION,
                    WEAKNESS_AMPLIFIER,
                    false, // ambient
                    false  // show particles
            ));

            // Hunger effect
            player.addEffect(new net.minecraft.world.effect.MobEffectInstance(
                    MobEffects.HUNGER,
                    EFFECT_DURATION,
                    HUNGER_AMPLIFIER,
                    false, // ambient
                    false  // show particles
            ));
        } else {
            // If player is not in Tier 0, remove the effects if they have them
            if (player.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
                player.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
            }
            if (player.hasEffect(MobEffects.WEAKNESS)) {
                player.removeEffect(MobEffects.WEAKNESS);
            }
            if (player.hasEffect(MobEffects.HUNGER)) {
                player.removeEffect(MobEffects.HUNGER);
            }
        }
    }

    /**
     * Handles player login to apply initial tier effects.
     * @param event The player logged in event.
     */
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();

        // Apply initial tier effects when player logs in
        if (!player.level().isClientSide()) {
            // Initialize the previous tier to the current tier
            previousTier = AuraManager.getCurrentTier();

            // Apply max health modification based on tier
            if (AuraManager.isInTier0()) {
                // Set max health to half (10 hearts -> 5 hearts)
                player.setHealth(player.getHealth() / 2);
                player.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH)
                      .setBaseValue(10.0D); // 10 = 5 hearts (half of normal 20 = 10 hearts)
            } else {
                // Restore normal max health
                player.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH)
                      .setBaseValue(20.0D); // 20 = 10 hearts (normal)
            }

            // Apply tier effects after setting health
            applyTierEffects(player);
        }
    }

    /**
     * Handles player respawn to ensure tier effects are applied after respawning.
     * @param event The player respawn event.
     */
    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();

        // Apply tier effects when player respawns
        if (!player.level().isClientSide()) {
            // Apply max health modification based on tier
            if (AuraManager.isInTier0()) {
                // Set max health to half (10 hearts -> 5 hearts)
                player.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH)
                      .setBaseValue(10.0D); // 10 = 5 hearts (half of normal 20 = 10 hearts)
                player.setHealth(Math.min(player.getHealth(), 10.0F)); // Ensure health doesn't exceed new max
            } else {
                // Restore normal max health
                player.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH)
                      .setBaseValue(20.0D); // 20 = 10 hearts (normal)
            }

            // Apply tier effects after setting health
            applyTierEffects(player);
        }
    }

    /**
     * Handles player inventory open events to restrict inventory access for Tier 0 players.
     * @param event The player container event.
     */
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onPlayerOpenContainer(net.minecraftforge.event.entity.player.PlayerContainerEvent.Open event) {
        Player player = event.getEntity();

        // Only process on server side
        if (!player.level().isClientSide()) {
            // Check if player is in Tier 0
            if (AuraManager.isInTier0()) {
                // Check if the container is not the player's inventory hotbar
                if (event.getContainer() instanceof CraftingMenu) {
                    // Cancel the event to prevent opening the crafting table
                    event.setCanceled(true);

                    // Notify the player
                    player.sendSystemMessage(Component.literal("You cannot use a crafting table in Tier 0!"));
                } else if (!(event.getContainer() instanceof net.minecraft.world.inventory.InventoryMenu)) {
                    // This is not the player's inventory, so allow it
                    return;
                } else {
                    // This is the player's inventory
                    // We'll handle this in the GUI screen event
                }
            }
        }
    }

    /**
     * Handles player right-click block events to block crafting table interactions for Tier 0 players.
     * @param event The player interact event.
     */
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();

        // Only process on server side
        if (!player.level().isClientSide()) {
            // Check if player is in Tier 0
            if (AuraManager.isInTier0()) {
                // Check if the block is a crafting table
                if (event.getLevel().getBlockState(event.getPos()).getBlock() == Blocks.CRAFTING_TABLE) {
                    // Cancel the event to prevent interaction
                    event.setCanceled(true);

                    // Notify the player
                    player.sendSystemMessage(Component.literal("You cannot use a crafting table in Tier 0!"));
                }
            }
        }
    }

    /**
     * Handles inventory screen initialization to restrict inventory access for Tier 0 players.
     * This event is fired on the client side when the inventory screen is opened.
     * @param event The screen init event.
     */
    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init event) {
        // Check if the screen is the inventory screen
        if (event.getScreen() instanceof InventoryScreen) {
            // Check if player is in Tier 0
            if (AuraManager.isInTier0()) {
                // Add an overlay to block access to the inventory slots (except hotbar)
                int screenWidth = event.getScreen().width;
                int screenHeight = event.getScreen().height;

                // Create a button that covers the inventory area (except hotbar)
                // The button doesn't do anything when clicked, it just blocks access
                Button inventoryBlocker = Button.builder(
                        Component.literal("Inventory Locked in Tier 0"),
                        button -> {
                            // Do nothing when clicked
                            net.minecraft.client.Minecraft.getInstance().player.sendSystemMessage(
                                    Component.literal("You can only access your hotbar in Tier 0!")
                            );
                        })
                        .pos(screenWidth / 2 - 80, screenHeight / 2 - 76)
                        .size(160, 76)
                        .build();

                // Add the button to the screen
                event.addListener(inventoryBlocker);
            }
        }
    }
}
