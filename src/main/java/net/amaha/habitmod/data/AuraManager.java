package net.amaha.habitmod.data;

/**
 * Manages the player's Productivity Aura level and tier.
 */
public class AuraManager {
    // Aura tiers as defined in features.md
    public static final int TIER_0_MAX = 50;    // Incapable Builder: 0-50
    public static final int TIER_1_MAX = 150;   // Awakened Apprentice: 51-150
    public static final int TIER_2_MAX = 300;   // Diligent Crafter: 151-300
    public static final int TIER_3_MAX = 500;   // Master Builder: 301-500
    // Tier 4: 501+                             // End-Game Luminary
    
    // Current aura level
    private static int auraLevel = 0;
    
    /**
     * Gets the current aura level.
     * @return The current aura level.
     */
    public static int getAuraLevel() {
        return auraLevel;
    }
    
    /**
     * Sets the aura level to a specific value.
     * @param level The new aura level.
     */
    public static void setAuraLevel(int level) {
        auraLevel = Math.max(0, level); // Ensure aura level is not negative
    }
    
    /**
     * Adds aura points to the current level.
     * @param points The number of points to add.
     */
    public static void addAura(int points) {
        auraLevel = Math.max(0, auraLevel + points);
    }
    
    /**
     * Removes aura points from the current level.
     * @param points The number of points to remove.
     */
    public static void removeAura(int points) {
        auraLevel = Math.max(0, auraLevel - points);
    }
    
    /**
     * Gets the current tier based on the aura level.
     * @return The tier (0-4) based on the aura level.
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
     * Checks if the player is in Tier 0 (Incapable Builder).
     * @return True if the player is in Tier 0, false otherwise.
     */
    public static boolean isInTier0() {
        return getCurrentTier() == 0;
    }
}