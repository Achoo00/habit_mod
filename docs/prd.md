1. Introduction

This Product Requirements Document (PRD) outlines the features and functionalities of the "Productivity & Decay" Minecraft mod, aiming to gamify real-world productivity through in-game progression and maintenance. This mod transforms Minecraft into a dynamic representation of the player's discipline, where in-game capabilities are directly tied to real-life task completion.

2. Product Overview

The "Productivity & Decay" mod is a total conversion Minecraft mod designed for single-player use (with potential for future multiplayer compatibility) that gates all major in-game progression and abilities behind real-world tasks. The mod introduces a "Productivity Aura" that constantly decays over real-world time, requiring consistent task completion to maintain in-game power and progression. It operates on an "honor system" with built-in mechanisms to discourage abuse.

3. Goals & Objectives

   Primary Goal: To motivate players to complete real-world tasks by linking their in-game Minecraft progression and abilities directly to their productivity.
   Objective 1: Create a robust "Productivity Aura" system that accurately tracks real-world time and provides a clear in-game consequence for inactivity.
   Objective 2: Implement a tiered ability system where in-game actions, crafting, and progression are gated by current Aura level.
   Objective 3: Develop an intuitive "Chronicle of Deeds" GUI for managing real-world tasks and tracking Aura.
   Objective 4: Integrate anti-abuse mechanisms to encourage honesty within the honor system.
   Objective 5: Provide clear visual and auditory feedback for Aura level changes and associated in-game effects.

4. Target Audience

Single-player Minecraft users who:

    Are looking for external motivation to be more productive in real life.
    Enjoy gamified self-improvement.
    Are comfortable with a challenging and potentially restrictive Minecraft experience initially.
    Are willing to engage with an "honor system" for self-accountability.

5. Functional Requirements
   5.1. The "Productivity Aura" System

   FR-A1: The mod shall track a "Productivity Aura" value for each player, persistent across game sessions.
   FR-A2: "Productivity Aura" shall constantly decay over real-world time, regardless of whether the player is in-game. The decay rate shall be configurable (default: 1 point per minute, possibly faster at higher Aura levels).
   FR-A3: A configurable daily "Aura Reset" shall occur, decaying a larger chunk of Aura if no tasks are completed within a real-world 24-hour period.
   FR-A4: "Productivity Aura" shall increase upon marking a task complete in the "Chronicle of Deeds." The amount gained shall be dependent on task difficulty/effort level.
   FR-A5: "Productivity Aura" shall be categorized (e.g., Physical, Mental, Organizational, Creative), with different categories potentially contributing to specific in-game buffs or unlocks.

5.2. "Chronicle of Deeds" (GUI)

    FR-CD1: The mod shall provide an uncraftable, unloseable in-game item or hotkey that opens a custom GUI called the "Chronicle of Deeds."
    FR-CD2: The "Chronicle of Deeds" GUI shall display key information and functionality as detailed in the **GUI Design Specification (GUI.md)**, including:
        A list of active tasks.
        Current "Aura Level" (numerical and/or bar indicator) for overall Productivity and individual Aura categories.
        Currently unlocked abilities/tiers.
        Task management features (add, edit, delete, complete).
        A "Confess Cheat" button.
    FR-CD3: Players shall be able to add tasks with a name, category, and an assigned "Effort Level" (e.g., Small, Medium, Large).
    FR-CD4: Marking a task as complete shall update the player's Aura and record the real-world timestamp.
    FR-CD5: The GUI shall load and save task data persistently.

5.3. Aura Category Tiers & Progression Gating

    FR-AT1: The mod shall define distinct "Aura Tiers" (0-20, 21-50, 51-100, 101-150, 151+) for each individual Aura category, as well as for an aggregate "Productivity Aura." In-game abilities and progression shall be dynamically enabled, disabled, or modified based on these Aura levels. If an Aura level drops below a tier threshold, abilities shall revert to the lower tier.

### 5.3.1. Health Aura Progression

    FR-HA1.0-20: If Health Aura is 0-20:
        FR-HA1.1: Player shall have permanent Weakness I and Slowness I debuffs.
        FR-HA1.2: Hunger bar shall drain 10% faster.
        FR-HA1.3: Player shall take 25% more fall damage.
        FR-HA1.4: Any food (excluding rotten flesh/raw chicken) shall have a 50% chance of inflicting the `Hunger` effect.
        FR-HA1.5: Rotten flesh and raw chicken shall always inflict `Hunger` and `Poison I`.
    FR-HA2.21-50: If Health Aura is 21-50:
        FR-HA2.1: Weakness I and Slowness I debuffs shall be removed.
        FR-HA2.2: Hunger bar shall drain at a normal rate.
        FR-HA2.3: Fall damage shall be set to default.
        FR-HA2.4: Any food shall have a 15% chance of inflicting the `Hunger` effect.
        FR-HA2.5: Rotten flesh and raw chicken shall revert to their default effects.
    FR-HA3.51-100: If Health Aura is 51-100:
        FR-HA3.1: Player shall have permanent Regeneration I.
        FR-HA3.2: Max health shall be increased by 2 hearts.
        FR-HA3.3: Player shall have reduced fall damage (equivalent to Feather Falling I).
        FR-HA3.4: Player shall have permanent Fire Protection I.
        FR-HA3.5: Any food (excluding inherent `Hunger`/`Poison` foods like rotten flesh, raw chicken, pufferfish) shall have a 0% chance to inflict `Hunger`.
    FR-HA4.101-150: If Health Aura is 101-150:
        FR-HA4.1: Player shall have permanent Haste I.
        FR-HA4.2: Max health shall be increased by 4 hearts.
        FR-HA4.3: Player shall have significantly reduced environmental damage (equivalent to Fire Protection II, Respiration II).
        FR-HA4.4: Player shall have permanent Regeneration I.
        FR-HA4.5: Player shall have reduced fall damage (equivalent to Feather Falling I).
        FR-HA4.6: Food shall provide 15% more saturation.
        FR-HA4.7: Player shall be immune to `Poison` effects.
    FR-HA5.151+: If Health Aura is 151+:
        FR-HA5.1: Player shall have permanent Regeneration II.
        FR-HA5.2: Max health shall be increased by 6 hearts.
        FR-HA5.3: Player shall gain temporary Resistance I after taking damage.
        FR-HA5.4: Player shall have permanent Haste I.
        FR-HA5.5: Player shall have significantly reduced environmental damage (equivalent to Fire Protection II, Respiration II).
        FR-HA5.6: Player shall have reduced fall damage (equivalent to Feather Falling I).
        FR-HA5.7: Food shall provide 30% more saturation.
        FR-HA5.8: Player shall be immune to `Poison` and `Slowness` effects.

### 5.3.2. Creative & Hobby Aura Progression

    FR-CA1.0-20: If Creative & Hobby Aura is 0-20:
        FR-CA1.1: Player shall not be able to craft any block variants (stairs, slabs, fences, walls, chiseled).
        FR-CA1.2: Player shall not be able to craft decorative blocks (item frames, paintings, flower pots, armor stands, banners, carpets).
        FR-CA1.3: Player shall not be able to use an anvil to rename or repair tools.
        FR-CA1.4: Player shall not be able to use a stonecutter to get variants.
    FR-CA2.21-50: If Creative & Hobby Aura is 21-50:
        FR-CA2.1: Player shall not be able to craft certain block variants (fence, chiseled).
        FR-CA2.2: Player shall not be able to craft decorative blocks (item frames, paintings, flower pots, armor stands, banners, carpets).
        FR-CA2.3: Anvil costs for renaming or repairing tools shall be doubled.
        FR-CA2.4: Stonecutter shall only give access to half the variants.
    FR-CA3.51-100: If Creative & Hobby Aura is 51-100:
        FR-CA3.1: Player shall be able to craft any block variant.
        FR-CA3.2: Player shall not be able to craft decorative blocks (item frames or paintings).
        FR-CA3.3: Anvil repair and rename shall cost 50% less than default.
        FR-CA3.4: Stonecutter shall give full access to all variants.
        FR-CA3.5: Rare item rates for mob drops and fishing shall be increased by 3%.
    FR-CA4.101-150: If Creative & Hobby Aura is 101-150:
        FR-CA4.1: Player shall be able to craft any block variant.
        FR-CA4.2: Player shall not be able to craft decorative blocks (item frames or paintings).
        FR-CA4.3: Anvil repair and rename costs shall revert to default.
        FR-CA4.4: Stonecutter shall give full access to all variants.
        FR-CA4.5: Rare item rates for mob drops and fishing shall be increased by 5%.
    FR-CA5.151+: If Creative & Hobby Aura is 151+:
        FR-CA5.1: Player shall be able to craft any block variant.
        FR-CA5.2: Player shall be able to craft any decorative block.
        FR-CA5.3: Anvil repair and rename costs shall be 50% less than default.
        FR-CA5.4: Stonecutter shall give full access to all variants.
        FR-CA5.5: Mobs shall have a small chance to drop their mob head.
        FR-CA5.6: Player shall be able to apply trims to armor.

### 5.3.3. Organization & Productivity Aura Progression

    FR-OP1.0-20: If Organization & Productivity Aura is 0-20:
        FR-OP1.1: Player’s main inventory (excluding hotbar) shall be locked.
        FR-OP1.2: Player shall not be able to craft any storage blocks (chests, barrels, hoppers, shulker boxes, item frames for storage).
        FR-OP1.3: Furnace fuel shall be 50% inefficient.
    FR-OP2.21-50: If Organization & Productivity Aura is 21-50:
        FR-OP2.1: 2 full rows of inventory shall be unlocked.
        FR-OP2.2: Player shall be able to craft storage blocks.
        FR-OP2.3: Furnace fuel shall be 25% inefficient.
    FR-OP3.51-100: If Organization & Productivity Aura is 51-100:
        FR-OP3.1: Full inventory shall be accessible.
        FR-OP3.2: Furnace fuel shall have default efficiency.
        FR-OP3.3: The compass shall serve as an item magnet that can be toggled on/off (glowing when active). The magnet radius shall be 5 blocks.
    FR-OP4.101-150: If Organization & Productivity Aura is 101-150:
        FR-OP4.1: Smelting in furnaces shall be 25% faster.
        FR-OP4.2: Placing a crafting table next to chests shall allow interaction with items in that chest for crafting.
        FR-OP4.3: Inventory shall expand to have 4 extra slots.
        FR-OP4.4: The compass magnet shall have a radius of 7 blocks.
    FR-OP5.151+: If Organization & Productivity Aura is 151+:
        FR-OP5.1: Furnaces/smokers/blast furnaces shall work 50% faster.
        FR-OP5.2: Placing a crafting table next to chests shall allow interaction with items in that chest for crafting.
        FR-OP5.3: Inventory shall expand to have 8 extra slots.
        FR-OP5.4: The compass magnet shall have a radius of 9 blocks.
        FR-OP5.5: When interacting with a chest, hovering over an item in the inventory and using the scroll wheel shall insert the item into the chest one block at a time.

### 5.3.4. Social Aura Progression

    FR-SA1.0-20: If Social Aura is 0-20:
        FR-SA1.1: Player shall not be able to interact with villagers and wandering traders.
        FR-SA1.2: Tameable animals shall be 100% harder to tame.
        FR-SA1.3: Piglins shall not barter with the player.
    FR-SA2.21-50: If Social Aura is 21-50:
        FR-SA2.1: Player shall be able to interact with select villagers (wandering trader, butcher, farmer, fisherman, leatherworker, shepherd).
        FR-SA2.2: Tameable animals shall be 50% harder to tame.
        FR-SA2.3: Piglins shall not barter with the player.
    FR-SA3.51-100: If Social Aura is 51-100:
        FR-SA3.1: Player shall be able to interact with select villagers (wandering trader, butcher, farmer, fisherman, leatherworker, shepherd, cartographer, fletcher).
        FR-SA3.2: Villager trades shall be 10% cheaper.
        FR-SA3.3: Tameable animals shall be 25% harder to tame.
        FR-SA3.4: Piglins shall barter only select items (excluding enchanted books, iron boots, ender pearl, obsidian, nether quartz).
    FR-SA4.101-150: If Social Aura is 101-150:
        FR-SA4.1: Player shall be able to interact with almost all villagers (except armorer, toolsmith, weaponsmith).
        FR-SA4.2: Villager trades shall be 25% cheaper.
        FR-SA4.3: Tameable animal tame rates shall be set to default.
        FR-SA4.4: Piglins shall barter only select items (excluding enchanted books, ender pearl, obsidian).
    FR-SA5.151+: If Social Aura is 151+:
        FR-SA5.1: Player shall be able to interact with all villagers.
        FR-SA5.2: Villager trades shall be 30% cheaper.
        FR-SA5.3: Player shall have permanent Hero of the Village effect.
        FR-SA5.4: Tameable animal tame rates shall be 50% easier to tame.
        FR-SA5.5: Piglin bartering shall no longer have any restrictions.

### 5.3.5. Intelligence Aura Progression

    FR-IA1.0-20: If Intelligence Aura is 0-20:
        FR-IA1.1: Player shall have permanent Mining Fatigue I.
        FR-IA1.2: Player shall only be able to craft wooden tools.
        FR-IA1.3: Player shall not be able to craft redstone items or blocks.
    FR-IA2.21-50: If Intelligence Aura is 21-50:
        FR-IA2.1: Mining Fatigue I shall be removed.
        FR-IA2.2: Player shall be able to craft wooden, stone, and gold tools.
        FR-IA2.3: Player shall not be able to craft redstone items or blocks, except redstone torches, redstone blocks, levers, buttons, and pressure plates.
    FR-IA3.51-100: If Intelligence Aura is 51-100:
        FR-IA3.1: Player shall have permanent Haste I.
        FR-IA3.2: Recipe book shall show more complex recipes (e.g., all brewing recipes, advanced redstone components like comparators/repeaters, or complex potion recipes/enchanting options).
        FR-IA3.3: Player shall be able to craft wooden, stone, gold, and iron tools.
        FR-IA3.4: When using the enchantment table, the player shall not be able to see what is being applied.
        FR-IA3.5: Player shall be able to craft all redstone items and blocks.
    FR-IA4.101-150: If Intelligence Aura is 101-150:
        FR-IA4.1: Player shall have permanent Haste II.
        FR-IA4.2: XP from all sources shall be increased by 15%.
        FR-IA4.3: Player shall be able to craft wooden, stone, gold, iron, and diamond tools.
        FR-IA4.4: When using the enchantment table, the player shall be able to see only 1 of the enchantments being applied.
        FR-IA4.5: All redstone items and blocks shall be craftable.
    FR-IA5.151+: If Intelligence Aura is 151+:
        FR-IA5.1: Player shall have permanent Haste III.
        FR-IA5.2: XP from all sources shall be increased by 30%.
        FR-IA5.3: In the enchantment table, the player shall see exactly what is going to be applied to the tool or armor.
        FR-IA5.4: Player shall be able to craft wooden, stone, gold, iron, diamond, and netherite tools.
        FR-IA5.5: All redstone items and blocks shall be craftable.
        FR-IA5.6: Enchantments that were previously unable to combine (e.g., Mending + Infinity) shall now be combinable.

### 5.3.6. Productivity Aura Progression (Aggregate)

    FR-PA1.0-20: If Productivity Aura is 0-20:
        FR-PA1.1: XP Gain shall be reduced by 25%.
        FR-PA1.2: Max health shall be limited to 5 hearts.
        FR-PA1.3: Player shall not be able to skip the night.
        FR-PA1.4: Player can still set spawn by clicking on the bed.
    FR-PA2.21-50: If Productivity Aura is 21-50:
        FR-PA2.1: XP gain shall be reduced by 10%.
        FR-PA2.2: Max health shall be limited to 7 hearts.
        FR-PA2.3: Player shall be able to sleep in bed and skip the night.
    FR-PA3.51-100: If Productivity Aura is 51-100:
        FR-PA3.1: Player shall be able to enter the Nether.
        FR-PA3.2: Max health shall be 10 hearts.
    FR-PA4.101-150: If Productivity Aura is 101-150:
        FR-PA4.1: Player shall be able to enter the Nether.
        FR-PA4.2: Max health shall be 15 hearts.
    FR-PA5.151+: If Productivity Aura is 151+:
        FR-PA5.1: Player shall be able to enter the Nether and End.
        FR-PA5.2: Max health shall be 20 hearts.

    FR-AT7: The mod shall dynamically enable/disable vanilla crafting recipes based on the player's current relevant Aura category level (e.g., Intelligence Aura for tool recipes, Creative & Hobby Aura for decorative blocks).
    FR-AT8: The mod shall modify block breaking/interaction mechanics (e.g., effective tools, break speed) based on the player's current relevant Aura category level.
    FR-AT9: The mod shall gate dimensional travel (Nether, End) based on the player's Productivity Aura level. This includes modifying Nether portal activation and Eye of Ender mechanics.

5.4. Task Repetition & Freshness

    FR-TR1: Each task shall track a "freshness" timer (configurable, default 24-48 hours) from its last completion.
    FR-TR2: Completing a task within its "freshness" period shall refresh its timer.
    FR-TR3: Completing the same task too frequently (e.g., within 6 hours) shall result in diminishing Aura returns or no Aura gain for that completion.
    FR-TR4: Tasks that haven't been completed for a configurable long period (e.g., 3+ days) shall grant bonus Aura upon completion as a "catch-up" mechanism.

5.5. Visual & Auditory Feedback

    FR-VF1: A persistent HUD element shall display the player's current "Productivity Aura Level," with an option for quick glances at individual Aura status (as detailed in **GUI.md**).
    FR-VF2: As Aura levels drop, the in-game world shall subtly desaturate visually.
    FR-VF3: As Aura levels drop, the player model shall visually reflect decay (e.g., less vibrant, translucent, negative particle effects).
    FR-VF4: Aura changes shall be accompanied by subtle auditory cues (e.g., draining sound for decay, positive hum for gain).
    FR-VF5: Dynamic in-game music shall become more ominous or sparse as Aura decays.
    FR-VF6: Standard Minecraft debuff icons (e.g., Weakness, Mining Fatigue, Slowness) shall be displayed when Aura drops below specific tiers.

5.6. Anti-Abuse ("Honor System") Mechanisms

    FR-AA1: The "Chronicle of Deeds" GUI shall include a "Confess Cheat" button. Activating this shall result in a significant Aura loss, potential loss of recent progress, or severe temporary debuffs.
    FR-AA2: The mod shall periodically present "Truth Quests" within the "Chronicle," posing reflective questions about honesty in task completion.
    FR-AA3: The mod shall track an invisible "Integrity Score" for the player.
    FR-AA4: The "Integrity Score" shall decrease if:
        Too many tasks are marked complete too quickly.
        Very high-value tasks are marked complete within seconds of each other.
        A "Truth Quest" is falsely answered positively, followed by suspicious activity.
    FR-AA5: If "Integrity Score" drops below a threshold, Aura gain from tasks shall be significantly reduced (e.g., 50%) or negative in-game events shall trigger (e.g., random mob spawns, debuffs, XP gain reduction).
    FR-AA6: High-tier unlocks (Netherite, End Portal) shall require cumulative Aura earned over time or consistent task completion streaks, discouraging short-term cheating.
    FR-AA7: Certain in-game benefits or advancements may be "time-locked," only becoming available after a specific real-world duration has passed since the last successful task completion, regardless of Aura.

5.7. Custom Skill Trees (Knowledge & Effort)

    FR-ST1: The mod shall introduce custom skill trees (e.g., Productivity, Focus, Mindfulness).
    FR-ST2: Completing real-life tasks shall grant "Skill Points" for these custom trees.
    FR-ST3: Players shall be able to spend "Skill Points" to unlock specific in-game passive abilities (e.g., faster breaking speed, improved potion durations, slower hunger depletion).

6. Non-Functional Requirements

   NFR-P1 (Performance): The mod shall not cause significant client-side or server-side lag. Aura decay calculations and event handling must be optimized.
   NFR-U1 (Usability): The "Chronicle of Deeds" GUI shall be intuitive and easy to navigate for task management.
   NFR-M1 (Maintainability): The codebase shall be modular, well-commented, and adhere to Forge best practices for ease of future updates and bug fixes.
   NFR-C1 (Compatibility): The mod shall be compatible with Minecraft 1.20.1 using Forge. Future compatibility with newer Minecraft versions will be addressed in maintenance phases.
   NFR-D1 (Persistence): All mod data (Aura, tasks, timestamps, Integrity Score, skill points) shall be correctly saved and loaded with the Minecraft world and player data.
   NFR-S1 (Security - Internal): The internal data storage should be resistant to simple user tampering (though a dedicated cheat is acknowledged).
   NFR-I1 (Installation): Installation shall follow standard Forge mod installation procedures (dropping the .jar file into the mods folder).

7. Future Considerations (Out of Scope for V1.0 MVP)

   Multiplayer compatibility.
   Integration with external productivity APIs (e.g., Pomodoro timers, habit trackers).
   More complex custom mobs or world generation specific to Aura tiers.
   Advanced cheat-proofing mechanisms that involve network verification (not feasible without external APIs).