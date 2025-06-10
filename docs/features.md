# 1. Introduction

This Product Requirements Document (PRD) outlines the features and functionalities of the "Productivity & Decay" Minecraft mod, aiming to gamify real-world productivity through in-game progression and maintenance. This mod transforms Minecraft into a dynamic representation of the player's discipline, where in-game capabilities are directly tied to real-life task completion.

# 2. Product Overview

The "Productivity & Decay" mod is a total conversion Minecraft mod designed for single-player use (with potential for future multiplayer compatibility) that gates all major in-game progression and abilities behind real-world tasks. The mod introduces a "Productivity Aura" that constantly decays over real-world time, requiring consistent task completion to maintain in-game power and progression. It operates on an "honor system" with built-in mechanisms to discourage abuse.

# 3. Goals & Objectives

* **Primary Goal:** To motivate players to complete real-world tasks by linking their in-game Minecraft progression and abilities directly to their productivity.
* **Objective 1:** Create a robust "Productivity Aura" system that accurately tracks real-world time and provides a clear in-game consequence for inactivity.
* **Objective 2:** Implement a tiered ability system where in-game actions, crafting, and progression are gated by current Aura level.
* **Objective 3:** Develop an intuitive "Chronicle of Deeds" GUI for managing real-world tasks and tracking Aura.
* **Objective 4:** Incorporate anti-abuse mechanisms to maintain the integrity of the "honor system."
* **Objective 5:** Provide visual and auditory feedback to enhance player immersion and understanding of their Aura status.

# 4. Core "Productivity Aura" System

* **Real-world Time-Based Decay**: "Productivity Aura" constantly decreases over real-world time (configurable rate).
* **Task-Based Aura Gain**: Completing defined real-world tasks grants "Productivity Aura" in relevant categories.
* **Categorized Aura**: Aura is gained in different categories (e.g., Health, Intelligence, Social, Organization, Creative), influencing specific in-game buffs and debuffs.
* **Daily Reset Penalty**: A configurable significant Aura decay if no tasks are completed within a 24-hour real-world period.
* **Persistent Data**: All Aura levels and task data save and load correctly with the Minecraft world.

# 5. "Chronicle of Deeds" (GUI)

* **Hotkeyed Access**: Opens via a dedicated hotkey or unloseable item.
* **Task Management**: Allows players to add, edit, delete, and mark real-world tasks as complete.
* **Task Details**: Tasks can be assigned names, categories, and "Effort Levels" (Small, Medium, Large).
* **Aura Display**: Shows current "Productivity Aura" level and currently unlocked ability tiers for all categories.
* **Confess Cheat Button**: A self-reporting mechanism for acknowledging and penalizing cheating.

# 6. Tiered Abilities & Progression Gating

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

* **Real-Life Focus:** Workout, journaling, scalp massage, rosemary oil, floss, stretch
* **Health Aura Level Unlocks:**
  * **0-20 (Weakened)**:
    * Permanent Weakness I, Mining Fatigue III, Slowness I, Hunger I. - done
    * 25% more fall damage. - done
    * Food has a 50% chance to cause Hunger (Rotten Flesh/Raw Chicken 100% Hunger/Poison I). - done
  * **21-50 (Recovering)**:
    * Removes Weakness I, Slowness I. - done
    * Normal hunger drain. - done
    * Fall damage returns to default. - done
    * Food has a 15% chance to cause Hunger. - done
  * **51-100 (Regenerative)**:
    * Permanent Regeneration I. - done
    * Reduced fall damage (Feather Falling I equivalent). - done
    * Permanent Fire Protection I. - done
    * Food has 0% chance to cause Hunger.
  * **101-150 (Vigorous)**:
    * Permanent Haste I. - done
    * Reduced environmental damage (Fire Prot. II, Respiration II equivalent). - done
    * Permanent Regeneration I. - done
    * Reduced fall damage (Feather Falling I equivalent). - done
    * Food provides 15% more saturation.
    * Immune to Poison effects.
    * Holding blue ice gives you Frost Walker.
  * **151+ (Resilient)**:
    * Permanent Regeneration II. - done
    * Strength II.
    * Temporary Resistance I after damage. - done
    * Permanent Haste I. - done
    * Reduced environmental damage (Fire Prot. II, Respiration II equivalent). - done
    * Reduced fall damage (Feather Falling I equivalent). - done
    * Food provides 30% more saturation.
    * Immune to Poison, and Slowness effects.
    * Carrot on a stick automatically feeds you.
    * Holding blue ice gives you Frost Walker.

### Intelligence Aura (Mental Acuity)

* **Real-Life Focus:** Coding, read, weekly meeting or self reflecting, practice French.
* **Intelligence Aura Level Unlocks:**
  * **0-20 (Dull)**:
    * Only wooden tools can be used.
    * Cannot craft redstone items/blocks.
  * **21-50 (Learning)**:
    * Can craft wood, stone, and gold tools.
    * Limited redstone crafting (torches, blocks, levers, buttons, pressure plates only).
  * **51-100 (Knowledgeable)**:
    * Recipe book shows complex recipes (Suggestion: Specify: e.g., brewing recipes, complex redstone components like comparators/repeaters, or advanced potion recipes/enchanting options.).
    * Can craft wood, stone, gold, and iron tools.
    * Can craft all redstone items/blocks.
    * Enchantment table does not show what enchantments are applied.
  * **101-150 (Insightful)**:
    * XP from all sources +15%.
    * Can craft wood, stone, gold, iron, and diamond tools.
    * Enchantment table shows 1 enchantment applied.
    * All redstone items/blocks craftable.
  * **151+ (Mastermind)**:
    * Permanent Haste II.
    * XP from all sources +30%.
    * Enchantment table shows all enchantments.
    * Can craft wood, stone, gold, iron, diamond, and netherite tools.
    * All redstone items/blocks craftable.
    * Mending and Infinity enchantments are combinable.

### Social Aura (Interpersonal Skills)

* **Real-Life Focus:** Socialize with friends, text a friend you haven’t spoken to in a while, socialize at work, extracurricular or activities, learning a language.
* **Social Aura Level Unlocks:**
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
    * Fisherman give special trades for fish (Todo: figure out items).
    * Villager trades are 10% cheaper.
    * Tameable animals are 25% harder to tame.
    * Piglins barter select items (excluding enchanted books, iron boots, ender pearl, obsidian, nether quartz).
  * **101-150 (Connected)**:
    * Can interact with almost all villagers (except armorer, toolsmith, weaponsmith).
    * Villager trades are 25% cheaper.
    * Tameable animal tame rates return to default.
    * Piglins barter select items (excluding enchanted books, ender pearl, obsidian).
  * **151+ (Charismatic)**:
    * Can interact with all villagers (unlock this one after completing language lessons X amount of times).
    * Villager trades are 30% cheaper.
    * Permanent Hero of the Village effect.
    * Tameable animals are 50% easier to tame.
    * Piglins bartering is unrestricted.

### Organization Aura (Order & Efficiency)

* **Real-Life Focus:** Plan your day, declutter a space, create or manage to-do list, make bed, clear digital clutter (emails, files)
* **Organization & Productivity Aura Level Unlocks:**
  * **0-20 (Chaotic)**:
    * Main inventory locked (toolbar only). - done
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
    * The magnet radius will be 5 blocks.
    * Blocks can stack up to 128.
    * Your crafting grid gets bigger and you can craft 3x3.
    * Backpack.
  * **101-150 (Efficient)**:
    * Smelting is 25% faster.
    * Crafting table interacts with adjacent chests.
    * Inventory gains +4 extra slots.
    * Compass magnet has a 7-block radius.
    * Blocks can stack up to 256.
    * Your crafting grid gets bigger and you can craft 3x3.
    * Backpack.
    * Slime boots, everything is bouncy, no fall damage when wearing them.
  * **151+ (Master Organizer)**:
    * Furnaces/smokers/blast furnaces are 50% faster.
    * Crafting table interacts with adjacent chests.
    * Inventory gains +8 extra slots.
    * Compass magnet has a 9-block radius.
    * When interacting with a chest, you can hover over an item in your inventory and use the scroll wheel to insert the item into the chest 1 block at a time for more control.
    * Blocks can stack up to 256.
    * Your crafting grid gets bigger and you can craft 3x3.
    * Backpack.
    * Slime boots, everything is bouncy, no fall damage when wearing them.
    * Torches and lanterns prevent mobs from spawning in a greater radius.

### Creative Aura (Innovation & Aesthetics)

* **Real-Life Focus:** Draw, play music, voice training.
* **Creative Aura Level Unlocks:**
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

# 7. Unlockable Mods

Certain popular Minecraft mods are integrated into the progression system and become available as players achieve specific Aura tiers, representing their increasing mastery and capability.

* **Vein Miner:** Unlocked at **Health Aura Tier 51-100**.
* **Waystones:** Unlocked at **Organization Aura Tier 101-150**.
* **Logical Zoom:** Unlocked at **Intelligence Aura Tier 51-100** (after doing eye exercises).
* **Xaero's MiniMap:** Unlocked at **Intelligence Aura Tier 51-100** (after driving for X amount of times).
* **Create Mod:** Unlocked at **Productivity Aura Tier 151+**.

# 8. Task Repetition & Maintenance

* **Task "Freshness" Timer**: Each task tracks when it was last completed (e.g., 24-48 hours).
* **Diminishing Returns**: Repeated completion of the same task too frequently yields less Aura.
* **Bonus Aura for "Stale" Tasks**: Old, uncompleted tasks give bonus Aura when finally completed.
* **Incentivizes Variety**: Encourages completing diverse tasks for balanced Aura gain.

# 9. Visual & Auditory Feedback

* **HUD Aura Indicator**: A visible bar or number on the HUD showing current Aura.
* **World Desaturation**: Subtle visual desaturation of the world as Aura decays.
* **Player Model Feedback**: Player model changes (vibrancy, transparency, particles) reflect Aura level.
* **Dynamic Audio Cues**: Subtle sounds for Aura gain/loss, dynamic music reflecting decay.
* **In-Game Debuffs**: Standard Minecraft debuff icons applied at low Aura levels.

# 10. Anti-Abuse ("Honor System") Mechanisms

* **Confess Cheat Penalty**: Explicit button to self-report cheating, leading to significant in-game consequences.
* **"Truth Quests"**: Periodic in-game prompts for self-reflection on honesty.
* **Hidden "Integrity Score"**: Invisible metric tracking player honesty based on task completion patterns.
* **Dynamic Penalties**: Reduced Aura gain or negative in-game events if "Integrity Score" drops due to suspicious activity.
* **Long-Term Milestone Gating**: Endgame unlocks require sustained, believable effort over time, not just peak Aura.
* **Time-Locked Progression**: Certain benefits are gated by real-world time elapsed, preventing rapid cheating.

# 11. Custom Skill Trees ("Knowledge & Effort" System)

* **Skill Point Earning**: Completing real-life tasks grants custom "Skill Points."
* **Unlockable Abilities**: Points can be spent in themed skill trees (e.g., Productivity, Focus, Mindfulness) to unlock passive in-game benefits.
* **Examples of Abilities**: Faster breaking speed, improved potion durations, slower hunger depletion, increased health regeneration, reduced fall damage.