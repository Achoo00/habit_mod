package net.amaha.habitmod.effect;

import net.amaha.habitmod.data.AuraManager;
import net.amaha.habitmod.network.ApplyEffectPacket;
import net.amaha.habitmod.network.PacketHandler;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;
import java.util.Random;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private static final int EFFECT_DURATION = Integer.MAX_VALUE; // 10 seconds (longer than check interval to ensure continuous effect)
    private static final int SLOWNESS_AMPLIFIER = 0; // Amplifier 0 = 10% slower
    private static final int WEAKNESS_AMPLIFIER = 0; // Amplifier 0 = 0.5 less damage
    private static final int HUNGER_AMPLIFIER = 0; // Amplifier 0 = hunger depletes faster
    private static final int MINING_FATIGUE_AMPLIFIER = 2; // Amplifier 2 = 45% slower mining speed

    // Health Aura effect amplifiers
    private static final int REGENERATION_AMPLIFIER_TIER_2 = 0; // Regeneration I
    private static final int REGENERATION_AMPLIFIER_TIER_4 = 1; // Regeneration II
    private static final int HASTE_AMPLIFIER = 0; // Haste I
    private static final int FIRE_RESISTANCE_AMPLIFIER = 0; // Fire Resistance I
    private static final int RESISTANCE_AMPLIFIER = 0; // Resistance I

    // Random number generator for chance-based effects
    private static final Random random = new Random();

    // Counter for timing effect checks
    private static int tickCounter = 0;

    // Counter for timing aura decay
    private static int auraDecayCounter = 0;

    // Track the previous tier to detect tier changes
    private static int previousTier = -1;

    // Track the previous health aura tier to detect tier changes
    private static int previousHealthTier = -1;

    // Map to store effects per health aura tier
    private static final Map<Integer, List<MobEffectInstance>> HEALTH_AURA_TIER_EFFECTS;
    // List of all unique MobEffect types managed by this system, for efficient removal
    private static final List<net.minecraft.world.effect.MobEffect> ALL_MANAGED_HEALTH_AURA_EFFECTS;

    // Static initializer block to populate the maps once when the class is loaded
    static {
        Map<Integer, List<MobEffectInstance>> effectsMap = new HashMap<>();

        // Tier 0 effects (Incapable Builder Debuffs)
        effectsMap.put(0, Arrays.asList(
                new MobEffectInstance(net.minecraft.world.effect.MobEffects.MOVEMENT_SLOWDOWN, EFFECT_DURATION, SLOWNESS_AMPLIFIER, false, false),
                new MobEffectInstance(net.minecraft.world.effect.MobEffects.WEAKNESS, EFFECT_DURATION, WEAKNESS_AMPLIFIER, false, false),
                new MobEffectInstance(net.minecraft.world.effect.MobEffects.HUNGER, EFFECT_DURATION, HUNGER_AMPLIFIER, false, false),
                new MobEffectInstance(net.minecraft.world.effect.MobEffects.DIG_SLOWDOWN, EFFECT_DURATION, MINING_FATIGUE_AMPLIFIER, false, false)
        ));

        // Tier 1 effects (No specific mob effects, primarily removal of Tier 0 debuffs)
        effectsMap.put(1, Collections.emptyList());

        // Tier 2 effects
        effectsMap.put(2, Arrays.asList(
                new MobEffectInstance(net.minecraft.world.effect.MobEffects.REGENERATION, EFFECT_DURATION, REGENERATION_AMPLIFIER_TIER_2, false, false),
                new MobEffectInstance(net.minecraft.world.effect.MobEffects.FIRE_RESISTANCE, EFFECT_DURATION, FIRE_RESISTANCE_AMPLIFIER, false, false)
        ));

        // Tier 3 effects
        effectsMap.put(3, Arrays.asList(
                new MobEffectInstance(net.minecraft.world.effect.MobEffects.DIG_SPEED, EFFECT_DURATION, HASTE_AMPLIFIER, false, false), // Haste
                new MobEffectInstance(net.minecraft.world.effect.MobEffects.REGENERATION, EFFECT_DURATION, REGENERATION_AMPLIFIER_TIER_2, false, false), // Regen I
                new MobEffectInstance(net.minecraft.world.effect.MobEffects.FIRE_RESISTANCE, EFFECT_DURATION, FIRE_RESISTANCE_AMPLIFIER, false, false)
        ));

        // Tier 4 effects
        effectsMap.put(4, Arrays.asList(
                new MobEffectInstance(net.minecraft.world.effect.MobEffects.REGENERATION, EFFECT_DURATION, REGENERATION_AMPLIFIER_TIER_4, false, false), // Regen II
                new MobEffectInstance(net.minecraft.world.effect.MobEffects.DIG_SPEED, EFFECT_DURATION, HASTE_AMPLIFIER, false, false), // Haste
                new MobEffectInstance(net.minecraft.world.effect.MobEffects.FIRE_RESISTANCE, EFFECT_DURATION, FIRE_RESISTANCE_AMPLIFIER, false, false)
        ));

        HEALTH_AURA_TIER_EFFECTS = Collections.unmodifiableMap(effectsMap); // Make the map immutable

        // List all unique MobEffect types that this system manages
        ALL_MANAGED_HEALTH_AURA_EFFECTS = Arrays.asList(
                net.minecraft.world.effect.MobEffects.MOVEMENT_SLOWDOWN,
                net.minecraft.world.effect.MobEffects.WEAKNESS,
                net.minecraft.world.effect.MobEffects.HUNGER,
                net.minecraft.world.effect.MobEffects.DIG_SLOWDOWN,
                net.minecraft.world.effect.MobEffects.REGENERATION,
                net.minecraft.world.effect.MobEffects.FIRE_RESISTANCE,
                net.minecraft.world.effect.MobEffects.DIG_SPEED
        );
    }

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
     * Max Health Working
     */
    private static void applyTierEffects(Player player) {
        int currentTier = AuraManager.getCurrentTier(); // This will return 0, 1, 2, 3, 4 based on aura level

        // Check for tier changes to update health
        if (previousTier != currentTier) {
            double newMaxHealth = switch (currentTier) {
                case 0 -> 10.0D; // 5 hearts (Tier 0 effects are applied later)
                case 1 -> 14.0D; // 7 hearts
                case 2 -> 20.0D; // 10 hearts (normal)
                case 3 -> 30.0D; // 15 hearts
                case 4 -> 40.0D; // 20 hearts
                default ->
                    // Handle unexpected tier, default to normal health
                        20.0D;
            }; // Default to normal health (10 hearts)

            // Apply the new max health
            Objects.requireNonNull(player.getAttribute(Attributes.MAX_HEALTH))
                    .setBaseValue(newMaxHealth);

            // Ensure current health doesn't exceed new max, and heal if max health increased
            // If max health increased, set health to new max. If decreased, cap it.
            if (newMaxHealth > player.getMaxHealth()) {
                player.setHealth((float) newMaxHealth); // Heal to new max
                player.sendSystemMessage(net.minecraft.network.chat.Component.literal("Your vitality has increased!"));
            } else if (newMaxHealth < player.getMaxHealth()) {
                player.setHealth(Math.min(player.getHealth(), (float) newMaxHealth)); // Cap current health to new max
                player.sendSystemMessage(net.minecraft.network.chat.Component.literal("Your vitality has decreased!"));
            }

            // Update previous tier
            previousTier = currentTier;
        }
        applyHealthAuraEffects(player);
    }

    /**
     * Applies effects based on the player's current Health Aura tier.
     * @param player The player to apply effects to.
     */
    private static void applyHealthAuraEffects(Player player) {
        int currentHealthTier = AuraManager.getHealthAuraTier();

        System.out.println("Current Health Aura Tier: " + currentHealthTier);

        // Only apply/remove effects if the tier has actually changed
        if (previousHealthTier != currentHealthTier) {

            // 1. Remove all previously applied effects that this system manages
            for (net.minecraft.world.effect.MobEffect effectType : ALL_MANAGED_HEALTH_AURA_EFFECTS) {
                if (player.hasEffect(effectType)) {
                    player.removeEffect(effectType);
                }
            }

            // 2. Apply effects for the new current tier
            List<MobEffectInstance> effectsToApply = HEALTH_AURA_TIER_EFFECTS.getOrDefault(currentHealthTier, Collections.emptyList());
            for (MobEffectInstance effect : effectsToApply) {
                // Create a new instance to avoid modifying the shared static MobEffectInstance object
                player.addEffect(new MobEffectInstance(effect.getEffect(), effect.getDuration(), effect.getAmplifier(), effect.isAmbient(), effect.isVisible()));
            }

            // 3. Update the previous tier for the next tick
            previousHealthTier = currentHealthTier;
        }
    }

    /**
     * Handles player login to apply initial tier effects.
     * @param event The player logged in event.
     */
    // In your PlayerEffectManager class (or wherever these event handlers are)

    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();

        if (!player.level().isClientSide()) {
            // Initialize previous overall tier to current tier
            previousTier = AuraManager.getCurrentTier();

            // IMPORTANT FIX: Force previousHealthTier to a different value to ensure
            // applyHealthAuraEffects runs its effect application logic on first login.
            // It will then correctly update previousHealthTier to the actual tier.
            previousHealthTier = -1; // Any value outside of 0-4 range works

            // Apply tier effects (this will trigger applyHealthAuraEffects)
            applyTierEffects(player);
        }
    }

    // Bug: Player does not keep effects after respawn
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();

        if (!player.level().isClientSide()) {
            // Initialize previous overall tier to current tier
            previousTier = AuraManager.getCurrentTier();

            // IMPORTANT FIX: Force previousHealthTier to a different value to ensure
            // applyHealthAuraEffects runs its effect application logic on first respawn.
            previousHealthTier = -1; // Any value outside of 0-4 range works

            // Apply tier effects (this will trigger applyHealthAuraEffects)
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
     * Handles block breaking speed modification for Tier 0 players.
     * This allows normal mining speed for dirt and leaves, while keeping the mining fatigue effect for other blocks.
     * @param event The break speed event.
     */
    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
        BlockState blockState = event.getState();

        // Only process if player is in Tier 0
        if (AuraManager.isInTier0()) {
            // Allow normal mining speed for dirt and leaves
            if (blockState.is(Blocks.DIRT) || 
                blockState.is(Blocks.GRASS_BLOCK) || 
                blockState.is(Blocks.PODZOL) || 
                blockState.is(Blocks.COARSE_DIRT) || 
                blockState.is(Blocks.ROOTED_DIRT) || 
                blockState.is(Blocks.OAK_LEAVES) || 
                blockState.is(Blocks.SPRUCE_LEAVES) || 
                blockState.is(Blocks.BIRCH_LEAVES) || 
                blockState.is(Blocks.JUNGLE_LEAVES) || 
                blockState.is(Blocks.ACACIA_LEAVES) || 
                blockState.is(Blocks.DARK_OAK_LEAVES) ||
                blockState.is(Blocks.AZALEA_LEAVES) ||
                blockState.is(Blocks.FLOWERING_AZALEA_LEAVES) ||
                blockState.is(Blocks.MANGROVE_LEAVES)) {

                // If player has mining fatigue, compensate for it by increasing break speed
                if (player.hasEffect(MobEffects.DIG_SLOWDOWN)) {
                    int amplifier = player.getEffect(MobEffects.DIG_SLOWDOWN).getAmplifier();
                    float speedMultiplier = 1.0f;

                    // Calculate the inverse of the mining fatigue effect
                    // Mining Fatigue I: 0.3x speed -> multiply by 3.33
                    // Mining Fatigue II: 0.09x speed -> multiply by 11.11
                    // Mining Fatigue III: 0.027x speed -> multiply by 37.04
                    if (amplifier == 0) {
                        speedMultiplier = 3.33f;
                    } else if (amplifier == 1) {
                        speedMultiplier = 11.11f;
                    } else if (amplifier == 2) {
                        speedMultiplier = 37.04f;
                    } else {
                        speedMultiplier = (float) Math.pow(3.33, amplifier + 1);
                    }

                    // Apply the speed multiplier to counteract mining fatigue
                    event.setNewSpeed(event.getOriginalSpeed() * speedMultiplier);
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

    /**
     * Handles food consumption to apply Health Aura effects.
     * @param event The living entity use item finish event.
     */
    @SubscribeEvent
    public static void onFoodConsumption(LivingEntityUseItemEvent.Finish event) {
        // Only process for players and food items
        if (!(event.getEntity() instanceof Player player) || 
            !(event.getItem().isEdible())) {
            return;
        }

        // Only process on server side
        if (player.level().isClientSide()) {
            return;
        }

        // Get the food item
        ItemStack foodStack = event.getItem();
        Item foodItem = foodStack.getItem();

        // Get the Health Aura tier
        int healthTier = AuraManager.getHealthAuraTier();

        // Apply effects based on Health Aura tier
        if (healthTier == 0) {
            // Tier 0 (0-20): Any food (excluding rotten flesh/raw chicken) shall have a 50% chance of inflicting the `Hunger` effect.
            // Rotten flesh and raw chicken shall always inflict `Hunger` and `Poison I`.

            if (foodItem == Items.ROTTEN_FLESH || foodItem == Items.CHICKEN) {
                // Rotten flesh and raw chicken always inflict Hunger and Poison I
                player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 600, 0)); // 30 seconds of Hunger
                player.addEffect(new MobEffectInstance(MobEffects.POISON, 200, 0)); // 10 seconds of Poison I
                player.sendSystemMessage(Component.literal("The food makes you feel sick!"));
            } else {
                // 50% chance of Hunger effect for other foods
                if (random.nextFloat() < 0.5f) {
                    player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 300, 0)); // 15 seconds of Hunger
                    player.sendSystemMessage(Component.literal("The food doesn't sit well with you."));
                }
            }

        } else if (healthTier == 1) {
            // Tier 1 (21-50): Any food shall have a 15% chance of inflicting the `Hunger` effect.
            // Rotten flesh and raw chicken shall revert to their default effects.

            // 15% chance of Hunger effect for all foods
            if (random.nextFloat() < 0.15f) {
                player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 200, 0)); // 10 seconds of Hunger
                player.sendSystemMessage(Component.literal("The food doesn't sit well with you."));
            }

        } else if (healthTier == 2) {
            // Tier 2 (51-100): Any food (excluding inherent `Hunger`/`Poison` foods like rotten flesh, raw chicken, pufferfish) shall have a 0% chance to inflict `Hunger`.

            // No additional effects for food consumption

        } else if (healthTier == 3) {
            // Tier 3 (101-150): Food shall provide 15% more saturation.
            // Player shall be immune to `Poison` effects.

            // Increase saturation by 15%
            FoodProperties foodProperties = foodItem.getFoodProperties();
            if (foodProperties != null) {
                float saturation = foodProperties.getSaturationModifier();
                float bonusSaturation = saturation * 0.15f;

                // Add bonus saturation
                player.getFoodData().setSaturation(player.getFoodData().getSaturationLevel() + bonusSaturation);
            }

            // Remove Poison effect if present
            if (player.hasEffect(MobEffects.POISON)) {
                player.removeEffect(MobEffects.POISON);
                player.sendSystemMessage(Component.literal("Your body quickly neutralizes the poison!"));
            }

        } else if (healthTier == 4) {
            // Tier 4 (151+): Food shall provide 30% more saturation.
            // Player shall be immune to `Poison` and `Slowness` effects.

            // Increase saturation by 30%
            FoodProperties foodProperties = foodItem.getFoodProperties();
            if (foodProperties != null) {
                float saturation = foodProperties.getSaturationModifier();
                float bonusSaturation = saturation * 0.3f;

                // Add bonus saturation
                player.getFoodData().setSaturation(player.getFoodData().getSaturationLevel() + bonusSaturation);
            }

            // Remove Poison effect if present
            if (player.hasEffect(MobEffects.POISON)) {
                player.removeEffect(MobEffects.POISON);
                player.sendSystemMessage(Component.literal("Your body quickly neutralizes the poison!"));
            }

            // Remove Slowness effect if present
            if (player.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
                player.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
                player.sendSystemMessage(Component.literal("Your body quickly overcomes the slowness!"));
            }
        }
    }

    /**
     * Handles fall damage to apply Health Aura effects.
     * @param event The living fall event.
     */
    @SubscribeEvent
   public static void onFallDamage(LivingFallEvent event) {
          // Only process for players
    if (!(event.getEntity() instanceof Player player)) {
        return;
     }

          // Only process on server side
     if (player.level().isClientSide()) {
     return;
     }

          // --- DEBUGGING OUTPUT START ---
     System.out.println("DEBUG: onFallDamage event triggered for player: " + player.getName().getString());
     float originalDamageMultiplier = event.getDamageMultiplier();
     System.out.println("DEBUG: Original Fall Damage Multiplier: " + originalDamageMultiplier);
          // --- DEBUGGING OUTPUT END ---

          // Get the Health Aura tier
     int healthTier = AuraManager.getHealthAuraTier();

          // --- DEBUGGING OUTPUT START ---
     System.out.println("DEBUG: Player's Health Aura Tier: " + healthTier);
          // --- DEBUGGING OUTPUT END ---

          // Apply effects based on Health Aura tier
     if (healthTier == 0) {
              // Tier 0 (0-20): Player shall take 25% more fall damage.
     float newDamage = originalDamageMultiplier * 1.25f;
     event.setDamageMultiplier(newDamage);
              // --- DEBUGGING OUTPUT START ---
     System.out.println("DEBUG: Applying Tier 0 effect: Fall damage increased by 25%. New Multiplier: " + newDamage);
              // --- DEBUGGING OUTPUT END ---

     } else if (healthTier == 1) {
              // Tier 1 (21-50): Fall damage shall be set to default.
              // No modification needed
             // --- DEBUGGING OUTPUT START ---
     System.out.println("DEBUG: Applying Tier 1 effect: Fall damage is default. Multiplier: " + originalDamageMultiplier);
              // --- DEBUGGING OUTPUT END ---

     } else if (healthTier >= 2) {
              // Tier 2+ (51+): Player shall have reduced fall damage (equivalent to Feather Falling I).
              // Feather Falling I reduces fall damage by 12%
     float newDamage = originalDamageMultiplier * 0.88f;
     event.setDamageMultiplier(newDamage);
              // --- DEBUGGING OUTPUT START ---
     System.out.println("DEBUG: Applying Tier 2+ effect: Fall damage reduced by 12%. New Multiplier: " + newDamage);
              // --- DEBUGGING OUTPUT END ---
     }
     }
    /**
     * Handles damage to apply Health Aura effects.
     * @param event The living hurt event.
     */
    @SubscribeEvent
    public static void onDamage(LivingHurtEvent event) {
        // Only process for players
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        // Only process on server side
        if (player.level().isClientSide()) {
            return;
        }

        // Get the Health Aura tier
        int healthTier = AuraManager.getHealthAuraTier();

        // Apply effects based on Health Aura tier
        if (healthTier == 3 || healthTier == 4) {
            // Tier 3-4 (101+): Player shall have significantly reduced environmental damage (equivalent to Fire Protection II, Respiration II).

            // Check if damage is from fire or drowning
            if (event.getSource().is(net.minecraft.world.damagesource.DamageTypes.IN_FIRE) || 
                event.getSource().is(net.minecraft.world.damagesource.DamageTypes.ON_FIRE) || 
                event.getSource().is(net.minecraft.world.damagesource.DamageTypes.LAVA) || 
                event.getSource().is(net.minecraft.world.damagesource.DamageTypes.HOT_FLOOR) || 
                event.getSource().is(net.minecraft.world.damagesource.DamageTypes.DROWN)) {

                // Reduce damage by 40% (equivalent to Protection II)
                float newDamage = event.getAmount() * 0.6f;
                event.setAmount(newDamage);
            }
        }

        // Apply Resistance I after taking damage for Tier 4
        if (healthTier == 4) {
            // Tier 4 (151+): Player shall gain temporary Resistance I after taking damage.
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, RESISTANCE_AMPLIFIER)); // 5 seconds of Resistance I
        }
    }
}
