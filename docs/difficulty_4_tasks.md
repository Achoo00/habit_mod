---

## Productivity & Decay Mod - Detailed Task List (Difficulty 4+)

| ID | Title | Score (Java Difficulty) | # of Subtasks | Description |
|---|---|---|---|---|
| FR-A1 | Implement Persistent Productivity Aura Tracking | 4 | 3 | Develop a system to track a player's Productivity Aura value that saves and loads across game sessions. This likely involves NBT data handling for player-specific data. |
| FR-A1.1 | Define Aura Data Structure | 2 | 1 | Create a data structure (e.g., custom class or map) to hold player Aura values, including total and categorical Auras. |
| FR-A1.2 | Implement NBT Serialization/Deserialization | 3 | 1 | Write methods to serialize and deserialize Aura data to and from NBT (Named Binary Tag) for persistent storage with player data. |
| FR-A1.3 | Integrate Aura Data with Player Events | 3 | 1 | Hook into Minecraft player login/logout/save events to ensure Aura data is loaded and saved correctly. |
| FR-A2 | Implement Real-World Aura Decay | 5 | 4 | Create a mechanism for Productivity Aura to constantly decay over real-world time, even when the player is offline. This will require robust time tracking and server-side processing. |
| FR-A2.1 | Implement Real-World Time Tracking | 4 | 1 | Develop a system to track real-world time (e.g., using `System.currentTimeMillis()`) associated with each player's Aura. |
| FR-A2.2 | Calculate Aura Decay Rate | 3 | 1 | Implement configurable decay rate logic, potentially adjusting based on current Aura level. |
| FR-A2.3 | Apply Decay Periodically (Offline) | 5 | 1 | Design and implement a server-side scheduled task or background process to apply Aura decay for all players, even when they are offline. This is complex as it needs to handle world loading/unloading. |
| FR-A2.4 | Apply Decay Continuously (Online) | 3 | 1 | Implement client-side or server-side tick-based updates to apply Aura decay when the player is actively in-game. |
| FR-CD2 | Develop "Chronicle of Deeds" GUI Content | 5 | 8 | Design and implement the "Chronicle of Deeds" GUI, displaying active tasks, current Aura levels (numerical and bar), unlocked abilities, and task management features (add, edit, delete, complete) and a "Confess Cheat" button. This is a significant GUI development effort. |
| FR-CD2.1 | Design GUI Layout (XML/Code) | 3 | 1 | Create the visual layout for the GUI, including panels for tasks, Aura display, and buttons. |
| FR-CD2.2 | Implement Task List Display | 4 | 1 | Populate the GUI with a dynamic, scrollable list of active tasks, showing name, category, and effort. |
| FR-CD2.3 | Implement Aura Level Display (Numerical/Bar) | 3 | 1 | Display current overall and categorical Aura levels numerically and as progress bars. |
| FR-CD2.4 | Implement Unlocked Abilities Section | 4 | 1 | Dynamically display currently unlocked in-game abilities or tiers based on Aura levels. |
| FR-CD2.5 | Implement Add Task Form/Pop-up | 3 | 1 | Create the UI for adding new tasks with input fields for name, category dropdown, and effort level. |
| FR-CD2.6 | Implement Edit/Delete Task Functionality | 3 | 1 | Add buttons and logic for editing existing tasks and permanently deleting them. |
| FR-CD2.7 | Implement Mark Complete Button | 3 | 1 | Create a button for each task to mark it as complete. |
| FR-CD2.8 | Implement "Confess Cheat" Button | 3 | 1 | Add the "Confess Cheat" button and its basic UI integration. |
| FR-CD5 | Implement Persistent Task Data Storage | 4 | 3 | Ensure all task data (name, category, effort, completion status, timestamps) is persistently saved and loaded. |
| FR-CD5.1 | Define Task Data Structure | 2 | 1 | Create a custom class to represent a task, including fields for its properties and metadata (last completion timestamp). |
| FR-CD5.2 | Implement Task List Serialization/Deserialization | 3 | 1 | Write methods to convert a list of task objects to NBT and back, for storage with player data. |
| FR-CD5.3 | Integrate Task Data with Player Events | 3 | 1 | Ensure task data is loaded and saved with the player's game data. |
| FR-AT1 | Define and Implement Aura Tiers & Gating | 5 | 5 | Establish distinct Aura Tiers for each category and an aggregate Productivity Aura. Implement dynamic enabling/disabling/modification of in-game abilities based on these levels, with effects reverting if Aura drops. This is a core mechanic. |
| FR-AT1.1 | Define Aura Tier Thresholds (Config) | 2 | 1 | Create a configurable system for defining the numerical thresholds for each Aura tier and category. |
| FR-AT1.2 | Create Aura Effect Management System | 5 | 1 | Develop a central system to manage and apply/remove in-game effects (debuffs, buffs, crafting restrictions, etc.) based on the player's current Aura levels. This will likely involve event handlers and capability systems. |
| FR-AT1.3 | Implement Dynamic Effect Application/Removal | 4 | 1 | Write logic to continuously check Aura levels and apply or remove corresponding potion effects or capability modifications. |
| FR-AT1.4 | Handle Aura Tier Reversion | 4 | 1 | Ensure that when an Aura level drops below a tier threshold, previously granted abilities or buffs are correctly reverted or removed. |
| FR-AT1.5 | Integrate with Game Event Hooks | 4 | 1 | Hook into relevant Minecraft events (e.g., player tick, crafting, dimension change) to trigger Aura-based checks and modifications. |
| FR-HA4.101-150 | Health Aura Tier 101-150 Effects | 4 | 7 | Grant permanent Haste I, +4 max hearts, significantly reduced environmental damage, permanent Regeneration I, reduced fall damage (Feather Falling I), 15% more food saturation, and immunity to Poison. |
| FR-HA4.1.1 | Apply Permanent Haste I | 2 | 1 | Code to apply the Haste I potion effect to the player. |
| FR-HA4.1.2 | Increase Max Health by 4 Hearts | 3 | 1 | Modify the player's max health attribute. |
| FR-HA4.1.3 | Implement Environmental Damage Reduction | 3 | 1 | Implement logic to reduce environmental damage (e.g., fire, drowning) based on equivalent armor enchantments. |
| FR-HA4.1.4 | Apply Permanent Regeneration I | 2 | 1 | Code to apply the Regeneration I potion effect. |
| FR-HA4.1.5 | Reduce Fall Damage (Feather Falling I) | 2 | 1 | Implement logic to modify fall damage taken. |
| FR-HA4.1.6 | Increase Food Saturation by 15% | 3 | 1 | Modify how much saturation food items provide. |
| FR-HA4.1.7 | Grant Poison Immunity | 2 | 1 | Implement logic to cancel incoming Poison effects. |
| FR-HA5.151+ | Health Aura Tier 151+ Effects | 4 | 8 | Grant permanent Regeneration II, +6 max hearts, temporary Resistance I after taking damage, permanent Haste I, significantly reduced environmental damage (equivalent to Fire Protection II, Respiration II), reduced fall damage (equivalent to Feather Falling I), 30% more food saturation, and immunity to `Poison` and `Slowness` effects. |
| FR-HA5.1.1 | Apply Permanent Regeneration II | 2 | 1 | Code to apply the Regeneration II potion effect. |
| FR-HA5.1.2 | Increase Max Health by 6 Hearts | 3 | 1 | Modify the player's max health attribute. |
| FR-HA5.1.3 | Apply Temporary Resistance I After Damage | 4 | 1 | Implement an event listener for damage taken to apply a temporary Resistance I effect. |
| FR-HA5.1.4 | Apply Permanent Haste I | 2 | 1 | Code to apply the Haste I potion effect. |
| FR-HA5.1.5 | Implement Environmental Damage Reduction (Enhanced) | 3 | 1 | Enhance logic to reduce environmental damage (e.g., fire, drowning) based on equivalent armor enchantments. |
| FR-HA5.1.6 | Reduce Fall Damage (Feather Falling I) | 2 | 1 | Implement logic to modify fall damage taken. |
| FR-HA5.1.7 | Increase Food Saturation by 30% | 3 | 1 | Modify how much saturation food items provide. |
| FR-HA5.1.8 | Grant Poison and Slowness Immunity | 2 | 1 | Implement logic to cancel incoming Poison and Slowness effects. |
| FR-OP5.151+ | Org & Prod Aura Tier 151+ Effects | 4 | 5 | Furnaces/smokers/blast furnaces shall work 50% faster. Placing a crafting table next to chests shall allow interaction with items in that chest for crafting. Inventory shall expand to have 8 extra slots. The compass magnet shall have a radius of 9 blocks. When interacting with a chest, hovering over an item in the inventory and using the scroll wheel shall insert the item into the chest one block at a time. |
| FR-OP5.1.1 | Implement Faster Furnace Processing | 4 | 1 | Modify furnace, smoker, and blast furnace tick rates to be 50% faster. |
| FR-OP5.1.2 | Implement Crafting Table Chest Interaction | 4 | 1 | Modify the crafting table GUI logic to allow accessing items from adjacent chests. |
| FR-OP5.1.3 | Expand Player Inventory by 8 Slots | 4 | 1 | Modify the player's inventory size, potentially using a custom capability or inventory handler. |
| FR-OP5.1.4 | Increase Compass Magnet Radius to 9 Blocks | 3 | 1 | Adjust the radius of the item magnet effect on the compass. |
| FR-OP5.1.5 | Implement Scroll-Wheel Item Transfer | 4 | 1 | Implement custom mouse wheel event handling in the chest GUI to allow single-item transfer. |
| FR-IA5.151+ | Intelligence Aura Tier 151+ Effects | 4 | 6 | Player shall have permanent Haste III. XP from all sources shall be increased by 30%. In the enchantment table, the player shall see exactly what is going to be applied to the tool or armor. Player shall be able to craft wooden, stone, gold, iron, diamond, and netherite tools. All redstone items and blocks shall be craftable. Enchantments that were previously unable to combine (e.g., Mending + Infinity) shall now be combinable. |
| FR-IA5.1.1 | Apply Permanent Haste III | 2 | 1 | Code to apply the Haste III potion effect. |
| FR-IA5.1.2 | Increase XP Gain by 30% | 3 | 1 | Modify the XP orb pickup event or XP calculations. |
| FR-IA5.1.3 | Reveal Enchantment Table Results | 4 | 1 | Modify the enchantment table GUI to display the exact enchantments. |
| FR-IA5.1.4 | Enable Netherite Tool Crafting | 3 | 1 | Ensure the Netherite tool crafting recipes are always available. |
| FR-IA5.1.5 | Ensure All Redstone Crafting is Enabled | 2 | 1 | Verify all redstone recipes are available. |
| FR-IA5.1.6 | Allow Mending + Infinity Combination | 4 | 1 | Override or modify the enchantment combination rules for the anvil. |
| FR-AT7 | Dynamic Recipe Gating | 4 | 2 | Implement dynamic enabling/disabling of vanilla crafting recipes based on relevant Aura levels. |
| FR-AT7.1 | Hook into Recipe Lookup/Crafting Event | 3 | 1 | Identify and hook into the appropriate Forge events for recipe lookup or crafting attempts. |
| FR-AT7.2 | Implement Aura-Based Recipe Filtering | 4 | 1 | Add logic to filter or remove recipes from the player's available recipes based on their current Aura levels. |
| FR-AT8 | Dynamic Block Interaction Modification | 4 | 3 | The mod shall modify block breaking/interaction mechanics (e.g., effective tools, break speed) based on the player's current relevant Aura category level. |
| FR-AT8.1 | Hook into Block Break Speed Calculation | 3 | 1 | Identify the Forge event or method related to calculating block break speed. |
| FR-AT8.2 | Modify Break Speed Based on Aura | 4 | 1 | Adjust the block break speed multiplier based on the player's relevant Aura level. |
| FR-AT8.3 | Modify Effective Tools Logic | 3 | 1 | Potentially modify whether a tool is considered "effective" for certain blocks based on Aura. |
| FR-AT9 | Dimensional Travel Gating | 4 | 3 | The mod shall gate dimensional travel (Nether, End) based on the player's Productivity Aura level. This includes modifying Nether portal activation and Eye of Ender mechanics. |
| FR-AT9.1 | Gate Nether Portal Activation | 4 | 1 | Intercept Nether portal activation attempts and prevent them if the Productivity Aura is too low. |
| FR-AT9.2 | Gate Eye of Ender Usage | 3 | 1 | Intercept Eye of Ender usage and prevent it if the Productivity Aura is too low. |
| FR-AT9.3 | Handle Player Teleportation to/from Dimensions | 3 | 1 | Ensure any attempts to teleport to/from gated dimensions are checked against Aura levels. |
| FR-VF1 | Persistent HUD Element for Aura | 4 | 3 | A persistent HUD element shall display the player's current "Productivity Aura Level," with an option for quick glances at individual Aura status (as detailed in **GUI.md**). |
| FR-VF1.1 | Create Custom HUD Overlay Renderer | 3 | 1 | Develop a custom HUD renderer to draw on the screen during the game. |
| FR-VF1.2 | Display Overall Productivity Aura | 3 | 1 | Render the numerical and bar indicator for the main Productivity Aura. |
| FR-VF1.3 | Implement Quick Glance for Category Auras | 4 | 1 | Develop a mechanism (e.g., hover, button press) to quickly display individual Aura category statuses on the HUD. |
| FR-VF3 | Player Model Decay Visuals | 4 | 2 | As Aura levels drop, the player model shall visually reflect decay (e.g., less vibrant, translucent, negative particle effects). |
| FR-VF3.1 | Implement Player Model Shaders/Render Layers | 4 | 1 | Research and apply custom shaders or render layers to the player model to achieve desaturation or transparency effects. |
| FR-VF3.2 | Apply Negative Particle Effects | 3 | 1 | Spawn and manage custom particle effects around the player based on low Aura levels. |
| FR-AA1 | "Confess Cheat" Button Implementation | 4 | 3 | Activating this shall result in a significant Aura loss, potential loss of recent progress, or severe temporary debuffs. |
| FR-AA1.1 | Implement Aura Loss for Confession | 2 | 1 | Apply a predefined significant reduction to the player's Aura. |
| FR-AA1.2 | Implement Recent Progress Loss (Optional) | 3 | 1 | Design and implement mechanics to undo recent Aura gains or task completions (more complex). |
| FR-AA1.3 | Apply Severe Temporary Debuffs | 3 | 1 | Apply strong, temporary potion effects or other in-game penalties. |
| FR-AA2 | Implement "Truth Quests" | 4 | 3 | The mod shall periodically present "Truth Quests" within the "Chronicle," posing reflective questions about honesty in task completion. |
| FR-AA2.1 | Define Truth Quest Questions/Mechanics | 2 | 1 | Create a pool of reflective questions and associated mechanics for answering. |
| FR-AA2.2 | Implement Periodic Truth Quest Trigger | 3 | 1 | Develop a system to randomly or periodically trigger a "Truth Quest" pop-up in the GUI. |
| FR-AA2.3 | Handle Truth Quest Responses | 3 | 1 | Process player responses to Truth Quests and integrate with Integrity Score. |
| FR-AA3 | Implement Invisible "Integrity Score" | 4 | 3 | The mod shall track an invisible "Integrity Score" for the player. |
| FR-AA3.1 | Define Integrity Score Data Structure | 2 | 1 | Create a persistent variable to store the Integrity Score for each player. |
| FR-AA3.2 | Implement NBT Serialization/Deserialization for Integrity | 3 | 1 | Ensure the Integrity Score is saved and loaded with player data. |
| FR-AA3.3 | Integrate Integrity Score with Player Events | 2 | 1 | Hook into player events for loading/saving. |
| FR-AA4 | Implement Integrity Score Decrease Logic | 4 | 4 | The "Integrity Score" shall decrease if: Too many tasks are marked complete too quickly. Very high-value tasks are marked complete within seconds of each other. A "Truth Quest" is falsely answered positively, followed by suspicious activity. |
| FR-AA4.1 | Monitor Task Completion Frequency | 3 | 1 | Track timestamps of task completions to detect rapid succession. |
| FR-AA4.2 | Monitor High-Value Task Completion Speed | 3 | 1 | Specifically check if high-effort tasks are completed immediately. |
| FR-AA4.3 | Implement Truth Quest Honesty Check | 3 | 1 | Logic to assess the honesty of a Truth Quest answer based on subsequent activity. |
| FR-AA4.4 | Apply Integrity Score Reduction | 3 | 1 | Reduce the Integrity Score based on detected suspicious activity. |
| FR-AA5 | Integrity Score Threshold Effects | 4 | 2 | If "Integrity Score" drops below a threshold, Aura gain from tasks shall be significantly reduced (e.g., 50%) or negative in-game events shall trigger (e.g., random mob spawns, debuffs, XP gain reduction). |
| FR-AA5.1 | Implement Aura Gain Reduction | 3 | 1 | Modify the Aura gain calculation based on the Integrity Score. |
| FR-AA5.2 | Trigger Negative In-Game Events | 4 | 1 | Implement a system to randomly trigger mob spawns, apply debuffs, or reduce XP gain. |
| FR-AA6 | Cumulative Aura/Streak Requirements for High Tiers | 4 | 2 | High-tier unlocks (Netherite, End Portal) shall require cumulative Aura earned over time or consistent task completion streaks, discouraging short-term cheating. |
| FR-AA6.1 | Track Cumulative Aura Earned | 3 | 1 | Keep a persistent record of all Aura earned over the player's lifetime. |
| FR-AA6.2 | Implement Task Completion Streak Tracking | 3 | 1 | Track consecutive days or periods with task completions. |
| FR-AA6.3 | Gate High-Tier Unlocks with Cumulative/Streak | 4 | 1 | Add checks to Netherite crafting/usage and End Portal activation based on these accumulated values. |
| FR-ST1 | Introduce Custom Skill Trees | 4 | 3 | The mod shall introduce custom skill trees (e.g., Productivity, Focus, Mindfulness). |
| FR-ST1.1 | Define Skill Tree Structure | 3 | 1 | Create data structures to define individual skill trees, their nodes, and associated abilities. |
| FR-ST1.2 | Implement Skill Point Storage | 3 | 1 | Create persistent storage for player skill points in each tree. |
| FR-ST1.3 | Design Skill Tree GUI | 4 | 1 | Create a separate GUI or section within the "Chronicle" to visualize and interact with the skill trees. |
| FR-ST3 | Allow Spending Skill Points for Abilities | 4 | 2 | Players shall be able to spend "Skill Points" to unlock specific in-game passive abilities (e.g., faster breaking speed, improved potion durations, slower hunger depletion). |
| FR-ST3.1 | Implement Skill Point Spending Logic | 3 | 1 | Add functionality within the GUI for players to spend their earned skill points. |
| FR-ST3.2 | Apply Unlocked Passive Abilities | 4 | 1 | Develop a system to apply persistent passive effects or modify game mechanics based on unlocked skill tree abilities. This might involve applying custom capabilities or potion effects. |

---