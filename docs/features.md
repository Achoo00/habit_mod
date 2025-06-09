1. Introduction

This Product Requirements Document (PRD) outlines the features and functionalities of the "Productivity & Decay" Minecraft mod, aiming to gamify real-world productivity through in-game progression and maintenance. This mod transforms Minecraft into a dynamic representation of the player's discipline, where in-game capabilities are directly tied to real-life task completion.

2. Product Overview

The "Productivity & Decay" mod is a total conversion Minecraft mod designed for single-player use (with potential for future multiplayer compatibility) that gates all major in-game progression and abilities behind real-world tasks. The mod introduces a "Productivity Aura" that constantly decays over real-world time, requiring consistent task completion to maintain in-game power and progression. It operates on an "honor system" with built-in mechanisms to discourage abuse.

3. Goals & Objectives

* Primary Goal: To motivate players to complete real-world tasks by linking their in-game Minecraft progression and abilities directly to their productivity.
* Objective 1: Create a robust "Productivity Aura" system that accurately tracks real-world time and provides a clear in-game consequence for inactivity.
* Objective 2: Implement a tiered ability system where in-game actions, crafting, and progression are gated by current Aura level.
* Objective 3: Develop an intuitive "Chronicle of Deeds" GUI for managing real-world tasks and tracking Aura.
* Objective 4: Incorporate anti-abuse mechanisms to maintain the integrity of the "honor system."
* Objective 5: Provide visual and auditory feedback to enhance player immersion and understanding of their Aura status.

4. Core "Productivity Aura" System

* **Real-world Time-Based Decay**: "Productivity Aura" constantly decreases over real-world time (configurable rate).
* **Task-Based Aura Gain**: Completing defined real-world tasks grants "Productivity Aura" in relevant categories.
* **Categorized Aura**: Aura is gained in different categories (e.g., Health, Intelligence, Social, Organization, Creative), influencing specific in-game buffs and debuffs.
* **Daily Reset Penalty**: A configurable significant Aura decay if no tasks are completed within a 24-hour real-world period.
* **Persistent Data**: All Aura levels and task data save and load correctly with the Minecraft world.

5. "Chronicle of Deeds" (GUI)

* **Hotkeyed Access**: Opens via a dedicated hotkey or unloseable item.
* **Task Management**: Allows players to add, edit, delete, and mark real-world tasks as complete.
* **Task Details**: Tasks can be assigned names, categories, and "Effort Levels" (Small, Medium, Large).
* **Aura Display**: Shows current "Productivity Aura" level and currently unlocked ability tiers for all categories.
* **Confess Cheat Button**: A self-reporting mechanism for acknowledging and penalizing cheating.

6. Tiered Abilities & Progression Gating

In-game abilities and progression are directly tied to your Aura levels across different categories. As your Aura increases, you unlock beneficial effects and remove debuffs. Conversely, low Aura levels impose significant challenges.

### Productivity Aura (Overall Progression)

* **0-20 (Incapable)**:
    * XP Gain reduced by 25%.
    * Max Health limited to 5 hearts. - done
    * Player cannot skip the night.
    * Player can still set spawn by clicking on the bed.
* **21-50 (Struggling)**:
    * XP gain reduced by 10%.
    * Max Health limited to 7 hearts. - done
    * Player can sleep in bed and skip the night.
* **51-100 (Awakened)**:
    * Player can enter the Nether.
    * Max Health is 10 hearts. - done
* **101-150 (Proficient)**:
    * Player can enter the Nether.
    * Max Health is 15 hearts. - done
* **151+ (Luminary)**:
    * Player can enter the Nether and End.
    * Max Health is 20 hearts. - done

### Health Aura (Physical Well-being)

* **0-20 (Weakened)**:
    * Permanent Weakness I, Mining Fatigue III, Slowness I, Hunger I - done
    * 25% more fall damage.
    * Food has a 50% chance to cause Hunger (Rotten Flesh/Raw Chicken 100% Hunger/Poison I).
* **21-50 (Recovering)**:
    * Removes Weakness I, Slowness I.
    * Normal hunger drain.
    * Fall damage returns to default.
    * Food has a 15% chance to cause Hunger.
* **51-100 (Regenerative)**:
    * Permanent Regeneration I.
    * Max Health +2 hearts.
    * Reduced fall damage (Feather Falling I equivalent).
    * Permanent Fire Protection I.
    * Food has 0% chance to cause Hunger.
* **101-150 (Vigorous)**:
    * Permanent Haste I.
    * Max Health +4 hearts.
    * Reduced environmental damage (Fire Prot. II, Respiration II equivalent).
    * Permanent Regeneration I.
    * Reduced fall damage (Feather Falling I equivalent).
    * Food provides 15% more saturation.
    * Immune to Poison effects.
* **151+ (Resilient)**:e
    * Permanent Regeneration II.
    * Max Health +6 hearts.
    * Temporary Resistance I after damage.
    * Permanent Haste I.
    * Reduced environmental damage (Fire Prot. II, Respiration II equivalent).
    * Reduced fall damage (Feather Falling I equivalent).
    * Food provides 30% more saturation.
    * Immune to Poison, Slowness effects.

### Intelligence Aura (Mental Acuity)

* **0-20 (Dull)**:
    * Permanent Mining Fatigue I.
    * Only wooden tools can be used.
    * Cannot craft redstone items/blocks.
* **21-50 (Learning)**:
    * Removes Mining Fatigue I.
    * Can craft wood, stone, and gold tools.
    * Limited redstone crafting (torches, blocks, levers, buttons, pressure plates only).
* **51-100 (Knowledgeable)**:
    * Permanent Haste I.
    * Recipe book shows complex recipes (brewing, advanced redstone, potion/enchanting options).
    * Can craft wood, stone, gold, and iron tools.
    * Can craft all redstone items/blocks.
    * Enchantment table does not show what enchantments are applied.
* **101-150 (Insightful)**:
    * Permanent Haste II.
    * XP from all sources +15%.
    * Can craft wood, stone, gold, iron, and diamond tools.
    * Enchantment table shows 1 enchantment applied.
    * All redstone items/blocks craftable.
* **151+ (Mastermind)**:
    * Permanent Haste III.
    * XP from all sources +30%.
    * Enchantment table shows all enchantments.
    * Can craft wood, stone, gold, iron, diamond, and netherite tools.
    * All redstone items/blocks craftable.
    * Mending and Infinity enchantments are combinable.

### Social Aura (Interpersonal Skills)

* **0-20 (Isolated)**:
    * Cannot interact with villagers/wandering traders.
    * Tameable animals are 100% harder to tame.
    * Piglins will not barter.
* **21-50 (Awkward)**:
    * Can interact with select villagers (wandering trader, butcher, farmer, fisherman, leatherworker, shepherd).
    * Tameable animals are 50% harder to tame.
    * Piglins will not barter.
* **51-100 (Engaging)**:
    * Can interact with more villagers (cartographer, fletcher).
    * Villager trades are 10% cheaper.
    * Tameable animals are 25% harder to tame.
    * Piglins barter select items (excluding enchanted books, iron boots, ender pearl, obsidian, nether quartz).
* **101-150 (Connected)**:
    * Can interact with almost all villagers (except armorer, toolsmith, weaponsmith).
    * Villager trades are 25% cheaper.
    * Tameable animal tame rates return to default.
    * Piglins barter select items (excluding enchanted books, ender pearl, obsidian).
* **151+ (Charismatic)**:
    * Can interact with all villagers.
    * Villager trades are 30% cheaper.
    * Permanent Hero of the Village effect.
    * Tameable animals are 50% easier to tame.
    * Piglins bartering is unrestricted.

### Organization Aura (Order & Efficiency)

* **0-20 (Chaotic)**:
    * Main inventory locked (toolbar only).
    * Cannot craft storage blocks (chests, barrels, shulker boxes).
    * Furnace fuel is 50% inefficient.
* **21-50 (Disorganized)**:
    * 2 full rows of inventory unlocked.
    * Can craft storage blocks.
    * Furnace fuel is 25% inefficient.
* **51-100 (Tidy)**:
    * Full inventory accessible.
    * Furnace fuel returns to default efficiency.
    * Compass acts as a 5-block item magnet (toggleable, glows when active).
* **101-150 (Efficient)**:
    * Smelting is 25% faster.
    * Crafting table interacts with adjacent chests.
    * Inventory gains +4 extra slots.
    * Compass magnet has a 7-block radius.
* **151+ (Master Organizer)**:
    * Furnaces/smokers/blast furnaces are 50% faster.
    * Crafting table interacts with adjacent chests.
    * Inventory gains +8 extra slots.
    * Compass magnet has a 9-block radius.
    * Scroll wheel item insertion into chests (1 block at a time).

### Creative Aura (Innovation & Aesthetics)

* **0-20 (Uninspired)**:
    * Cannot craft block variants (stairs, slabs, fences, walls, chiseled).
    * Cannot craft decorative blocks (item frames, paintings, flower pots, armor stands, banners, carpets).
    * Cannot use anvil to rename/repair items.
    * Cannot use stonecutter.
* **21-50 (Developing)**:
    * Cannot craft block variants (fence, chiseled).
    * Cannot craft decorative blocks (item frames, paintings, flower pots, armor stands, banners, carpets).
    * Anvil costs are doubled.
    * Stonecutter provides only half variants.
* **51-100 (Artisan)**:
    * Can craft any block variant.
    * Anvil repair/rename costs 50% less.
    * Stonecutter provides full access.
    * Rare mob drop/fishing rates +3%.
* **101-150 (Visionary)**:
    * Can craft any block variant.
    * Anvil repair/rename costs return to default.
    * Stonecutter provides full access.
    * Rare mob drop/fishing rates +5%.
* **151+ (Maestro)**:
    * Can craft any block variant.
    * Can craft any decorative block.
    * Anvil repair/rename costs 50% less.
    * Stonecutter provides full access.
    * Mobs have a small chance to drop their head.
    * Can apply armor trims.

7. Task Repetition & Maintenance

* **Task "Freshness" Timer**: Each task tracks when it was last completed (e.g., 24-48 hours).
* **Diminishing Returns**: Repeated completion of the same task too frequently yields less Aura.
* **Bonus Aura for "Stale" Tasks**: Old, uncompleted tasks give bonus Aura when finally completed.
* **Incentivizes Variety**: Encourages completing diverse tasks for balanced Aura gain.

8. Visual & Auditory Feedback

* **HUD Aura Indicator**: A visible bar or number on the HUD showing current Aura.
* **World Desaturation**: Subtle visual desaturation of the world as Aura decays.
* **Player Model Feedback**: Player model changes (vibrancy, transparency, particles) reflect Aura level.
* **Dynamic Audio Cues**: Subtle sounds for Aura gain/loss, dynamic music reflecting decay.
* **In-Game Debuffs**: Standard Minecraft debuff icons applied at low Aura levels.

9. Anti-Abuse ("Honor System") Mechanisms

* **Confess Cheat Penalty**: Explicit button to self-report cheating, leading to significant in-game consequences.
* **"Truth Quests"**: Periodic in-game prompts for self-reflection on honesty.
* **Hidden "Integrity Score"**: Invisible metric tracking player honesty based on task completion patterns.
* **Dynamic Penalties**: Reduced Aura gain or negative in-game events if "Integrity Score" drops due to suspicious activity.
* **Long-Term Milestone Gating**: Endgame unlocks require sustained, believable effort over time, not just peak Aura.
* **Time-Locked Progression**: Certain benefits are gated by real-world time elapsed, preventing rapid cheating.

10. Custom Skill Trees ("Knowledge & Effort" System)

* **Skill Point Earning**: Completing real-life tasks grants custom "Skill Points."
* **Unlockable Abilities**: Points can be spent in themed skill trees (e.g., Productivity, Focus, Mindfulness) to unlock passive in-game benefits.
* **Examples of Abilities**: Faster breaking speed, improved potion durations, slower hunger depletion, increased health regeneration, reduced fall damage.
