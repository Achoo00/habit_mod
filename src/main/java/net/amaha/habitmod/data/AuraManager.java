package net.amaha.habitmod.data;

/**
 * Manages the player's Productivity Aura level and tier, and Health Aura level and tier distinctly.
 */
public class AuraManager {
    // Aura tiers as defined in features.md
    public static final int TIER_0_MAX = 50;    // Incapable Builder: 0-50
    public static final int TIER_1_MAX = 150;   // Awakened Apprentice: 51-150
    public static final int TIER_2_MAX = 300;   // Diligent Crafter: 151-300
    public static final int TIER_3_MAX = 500;   // Master Builder: 301-500
    // Tier 4: 501+                             // End-Game Luminary

    // Health Aura tiers as defined in PRD.md
    public static final int HEALTH_TIER_0_MAX = 20;    // 0-20: Debuffs
    public static final int HEALTH_TIER_1_MAX = 50;    // 21-50: Normal
    public static final int HEALTH_TIER_2_MAX = 100;   // 51-100: Basic buffs
    public static final int HEALTH_TIER_3_MAX = 150;   // 101-150: Advanced buffs
    // Health Tier 4: 151+                             // Supreme buffs

    // Current Productivity Aura level
    private static int auraLevel = 0;

    // Current Health Aura level (now distinct from overall aura)
    private static int healthAuraLevel = 0;

    /**
     * Gets the current Productivity Aura level.
     * @return The current Productivity Aura level.
     */
    public static int getAuraLevel() {
        return auraLevel;
    }

    /**
     * Sets the Productivity Aura level to a specific value.
     * @param level The new Productivity Aura level.
     */
    public static void setAuraLevel(int level) {
        auraLevel = Math.max(0, level); // Ensure aura level is not negative
    }

    /**
     * Adds Productivity Aura points to the current level.
     * @param points The number of points to add.
     */
    public static void addAura(int points) {
        auraLevel = Math.max(0, auraLevel + points);
    }

    /**
     * Removes Productivity Aura points from the current level.
     * @param points The number of points to remove.
     */
    public static void removeAura(int points) {
        auraLevel = Math.max(0, auraLevel - points);
    }

    /**
     * Gets the current tier based on the Productivity Aura level.
     * @return The tier (0-4) based on the Productivity Aura level.
     */
    public static int getCurrentTier() {
        if (auraLevel <= TIER_0_MAX) {
            return 0; // Tier 0: Incapable Builder
        } else if (auraLevel <= TIER_1_MAX) {
            return 1; // Tier 1: Awakened Apprentice
        } else if (auraLevel <= TIER_2_MAX) {
            return 2; // Tier 2: Diligent Crafter
        } else if (auraLevel <= TIER_3_MAX) {
            return 3; // Tier 3: Master Builder
        } else {
            return 4; // Tier 4: End-Game Luminary
        }
    }

    /**
     * Checks if the player is in Tier 0 (Incapable Builder) based on Productivity Aura.
     * @return True if the player is in Tier 0, false otherwise.
     */
    public static boolean isInTier0() {
        return getCurrentTier() == 0;
    }

    /**
     * Gets the current Health Aura level.
     * @return The current Health Aura level.
     */
    public static int getHealthAuraLevel() {
        return healthAuraLevel;
    }

    /**
     * Sets the Health Aura level to a specific value.
     * @param level The new Health Aura level.
     */
    public static void setHealthAuraLevel(int level) {
        healthAuraLevel = Math.max(0, level); // Ensure health aura level is not negative
    }

    /**
     * Adds Health Aura points to the current level.
     * @param points The number of points to add.
     */
    public static void addHealthAura(int points) {
        healthAuraLevel = Math.max(0, healthAuraLevel + points);
    }

    /**
     * Removes Health Aura points from the current level.
     * @param points The number of points to remove.
     */
    public static void removeHealthAura(int points) {
        healthAuraLevel = Math.max(0, healthAuraLevel - points);
    }

    /**
     * Gets the current health aura tier based on the health aura level.
     * @return The health tier (0-4) based on the health aura level.
     */
    public static int getHealthAuraTier() {
        if (healthAuraLevel <= HEALTH_TIER_0_MAX) {
            return 0; // Health Tier 0: 0-20
        } else if (healthAuraLevel <= HEALTH_TIER_1_MAX) {
            return 1; // Health Tier 1: 21-50
        } else if (healthAuraLevel <= HEALTH_TIER_2_MAX) {
            return 2; // Health Tier 2: 51-100
        } else if (healthAuraLevel <= HEALTH_TIER_3_MAX) {
            return 3; // Health Tier 3: 101-150
        } else {
            return 4; // Health Tier 4: 151+
        }
    }
}