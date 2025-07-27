## Productivity & Decay Mod - Task List

| ID | Title | Score (Java Difficulty) | # of Subtasks | Description |
|---|---|---|---|---|
| FR-A1 | Implement Persistent Productivity Aura Tracking | 4 | 1 | Develop a system to track a player's Productivity Aura value that saves and loads across game sessions. This likely involves NBT data handling for player-specific data. |
| FR-A2 | Implement Real-World Aura Decay | 5 | 1 | Create a mechanism for Productivity Aura to constantly decay over real-world time, even when the player is offline. This will require robust time tracking and server-side processing. |
| FR-A3 | Implement Daily Aura Reset | 3 | 1 | Develop logic for a configurable daily "Aura Reset" that applies a larger decay if no tasks were completed within a 24-hour period. |
| FR-A4 | Implement Aura Gain from Task Completion | 2 | 1 | Integrate the system so that completing a task in the "Chronicle of Deeds" increases the player's Aura based on task difficulty. |
| FR-A5 | Implement Categorized Productivity Aura | 3 | 1 | Extend the Aura system to include distinct categories (Physical, Mental, Organizational, Creative) that can contribute to specific in-game buffs or unlocks. |
| FR-CD1 | Create "Chronicle of Deeds" GUI Item/Hotkey | 3 | 1 | Develop an uncraftable, unloseable in-game item or hotkey to open the custom "Chronicle of Deeds" GUI. This involves item registration and event handling for key presses. |
| FR-CD2 | Develop "Chronicle of Deeds" GUI Content | 5 | 5 | Design and implement the "Chronicle of Deeds" GUI, displaying active tasks, current Aura levels (numerical and bar), unlocked abilities, and task management features (add, edit, delete, complete) and a "Confess Cheat" button. This is a significant GUI development effort. |
| FR-CD3 | Implement Task Addition in GUI | 3 | 1 | Enable players to add new tasks within the GUI, including defining a name, category, and "Effort Level". |
| FR-CD4 | Implement Task Completion Logic | 3 | 1 | Develop the functionality to mark a task as complete, which updates the player's Aura and records a real-world timestamp. |
| FR-CD5 | Implement Persistent Task Data Storage | 4 | 1 | Ensure all task data (name, category, effort, completion status, timestamps) is persistently saved and loaded. |
| FR-AT1 | Define and Implement Aura Tiers & Gating | 5 | 1 | Establish distinct Aura Tiers for each category and an aggregate Productivity Aura. Implement dynamic enabling/disabling/modification of in-game abilities based on these levels, with effects reverting if Aura drops. This is a core mechanic. |
| FR-HA1.0-20 | Health Aura Tier 0-20 Effects | 3 | 5 | Apply permanent Weakness I and Slowness I, 10% faster hunger drain, 25% more fall damage, 50% chance of Hunger from food, and guaranteed Hunger/Poison from rotten flesh/raw chicken. |
| FR-HA2.21-50 | Health Aura Tier 21-50 Effects | 2 | 5 | Remove Weakness/Slowness, normalize hunger drain and fall damage, reduce Hunger chance from food to 15%, and normalize rotten flesh/raw chicken effects. |
| FR-HA3.51-100 | Health Aura Tier 51-100 Effects | 3 | 5 | Grant permanent Regeneration I, +2 max hearts, reduced fall damage (Feather Falling I), permanent Fire Protection I, and 0% Hunger chance from non-poisonous foods. |
| FR-HA4.101-150 | Health Aura Tier 101-150 Effects | 4 | 7 | Grant permanent Haste I, +4 max hearts, significantly reduced environmental damage, permanent Regeneration I, reduced fall damage (Feather Falling I), 15% more food saturation, and immunity to Poison. |
| FR-HA5.151+ | Health Aura Tier 151+ Effects | 4 | 8 | Grant permanent Regeneration II, +6 max hearts, temporary Resistance I after damage, permanent Haste I, significantly reduced environmental damage, reduced fall damage (Feather Falling I), 30% more food saturation, and immunity to Poison and Slowness. |
| FR-CA1.0-20 | Creative & Hobby Aura Tier 0-20 Effects | 3 | 4 | Restrict crafting of block variants, decorative blocks, anvil renaming/repair, and stonecutter variants. |
| FR-CA2.21-50 | Creative & Hobby Aura Tier 21-50 Effects | 2 | 4 | Partially lift crafting restrictions for block variants, keep decorative block restrictions, double anvil costs, and limit stonecutter variants. |
| FR-CA3.51-100 | Creative & Hobby Aura Tier 51-100 Effects | 3 | 5 | Allow all block variant crafting, restrict specific decorative blocks, reduce anvil costs by 50%, grant full stonecutter access, and increase rare item rates by 3%. |
| FR-CA4.101-150 | Creative & Hobby Aura Tier 101-150 Effects | 2 | 5 | Revert anvil costs to default, maintain other benefits from previous tier, and increase rare item rates by 5%. |
| FR-CA5.151+ | Creative & Hobby Aura Tier 151+ Effects | 4 | 6 | Allow all decorative block crafting, reduce anvil costs by 50%, add mob head drop chance, and enable armor trims. |
| FR-OP1.0-20 | Org & Prod Aura Tier 0-20 Effects | 3 | 3 | Lock main inventory, prevent crafting of storage blocks, and make furnace fuel 50% inefficient. |
| FR-OP2.21-50 | Org & Prod Aura Tier 21-50 Effects | 2 | 3 | Unlock 2 inventory rows, allow storage block crafting, and make furnace fuel 25% inefficient. |
| FR-OP3.51-100 | Org & Prod Aura Tier 51-100 Effects | 3 | 3 | Unlock full inventory, normalize furnace fuel efficiency, and implement a 5-block radius togglable item magnet compass. |
| FR-OP4.101-150 | Org & Prod Aura Tier 101-150 Effects | 3 | 4 | Make smelting 25% faster, allow chest interaction for crafting, expand inventory by 4 slots, and increase compass magnet radius to 7 blocks. |
| FR-OP5.151+ | Org & Prod Aura Tier 151+ Effects | 4 | 5 | Make furnaces 50% faster, maintain chest interaction for crafting, expand inventory by 8 slots, increase compass magnet radius to 9 blocks, and enable scroll-wheel item transfer to chests. |
| FR-SA1.0-20 | Social Aura Tier 0-20 Effects | 3 | 3 | Prevent villager/wandering trader interaction, make tameable animals 100% harder to tame, and prevent piglin bartering. |
| FR-SA2.21-50 | Social Aura Tier 21-50 Effects | 2 | 3 | Allow interaction with select villagers, make tameable animals 50% harder to tame, and maintain piglin barter restriction. |
| FR-SA3.51-100 | Social Aura Tier 51-100 Effects | 3 | 4 | Allow interaction with more villagers, make trades 10% cheaper, make tameable animals 25% harder to tame, and partially restrict piglin bartering. |
| FR-SA4.101-150 | Social Aura Tier 101-150 Effects | 3 | 4 | Allow interaction with almost all villagers, make trades 25% cheaper, normalize tame rates, and further reduce piglin barter restrictions. |
| FR-SA5.151+ | Social Aura Tier 151+ Effects | 4 | 5 | Allow interaction with all villagers, make trades 30% cheaper, grant permanent Hero of the Village, make taming 50% easier, and remove all piglin barter restrictions. |
| FR-IA1.0-20 | Intelligence Aura Tier 0-20 Effects | 3 | 3 | Apply permanent Mining Fatigue I, restrict crafting to wooden tools, and prevent redstone item crafting. |
| FR-IA2.21-50 | Intelligence Aura Tier 21-50 Effects | 2 | 3 | Remove Mining Fatigue, allow wooden, stone, and gold tools, and partially lift redstone crafting restrictions. |
| FR-IA3.51-100 | Intelligence Aura Tier 51-100 Effects | 3 | 5 | Grant permanent Haste I, show more complex recipes, allow wooden, stone, gold, and iron tools, hide enchantment table results, and allow all redstone crafting. |
| FR-IA4.101-150 | Intelligence Aura Tier 101-150 Effects | 3 | 5 | Grant permanent Haste II, increase XP gain by 15%, allow diamond tools, show 1 enchantment, and allow all redstone crafting. |
| FR-IA5.151+ | Intelligence Aura Tier 151+ Effects | 4 | 6 | Grant permanent Haste III, increase XP gain by 30%, show all enchantments, allow netherite tools, and allow Mending + Infinity. |
| FR-PA1.0-20 | Prod Aura Tier 0-20 Effects (Aggregate) | 3 | 4 | Reduce XP gain by 25%, limit max health to 5 hearts, prevent night skipping, but allow setting spawn. |
| FR-PA2.21-50 | Prod Aura Tier 21-50 Effects (Aggregate) | 2 | 3 | Reduce XP gain by 10%, limit max health to 7 hearts, and allow night skipping. |
| FR-PA3.51-100 | Prod Aura Tier 51-100 Effects (Aggregate) | 2 | 2 | Allow Nether entry, and set max health to 10 hearts. |
| FR-PA4.101-150 | Prod Aura Tier 101-150 Effects (Aggregate) | 2 | 2 | Allow Nether entry, and set max health to 15 hearts. |
| FR-PA5.151+ | Prod Aura Tier 151+ Effects (Aggregate) | 3 | 2 | Allow Nether and End entry, and set max health to 20 hearts. |
| FR-AT7 | Dynamic Recipe Gating | 4 | 1 | Implement dynamic enabling/disabling of vanilla crafting recipes based on relevant Aura levels. |
| FR-AT8 | Dynamic Block Interaction Modification | 4 | 1 | Modify block breaking/interaction mechanics (e.g., break speed, effective tools) based on relevant Aura levels. |
| FR-AT9 | Dimensional Travel Gating | 4 | 1 | Implement gating for Nether and End travel, affecting portal activation and Eye of Ender mechanics. |
| FR-TR1 | Implement Task Freshness Timer | 3 | 1 | Develop a system to track a "freshness" timer for each task from its last completion. |
| FR-TR2 | Refresh Task Timer on Completion | 2 | 1 | Ensure completing a task within its freshness period refreshes its timer. |
| FR-TR3 | Diminishing Returns for Frequent Completion | 3 | 1 | Implement logic for diminishing or no Aura returns when completing the same task too frequently. |
| FR-TR4 | Bonus Aura for Old Tasks | 3 | 1 | Implement a mechanism to grant bonus Aura for tasks not completed for a long period. |
| FR-VF1 | Persistent HUD Element for Aura | 4 | 1 | Create a persistent HUD element to display the player's current Productivity Aura Level and individual Aura status. |
| FR-VF2 | Visual World Desaturation on Aura Drop | 3 | 1 | Implement subtle visual desaturation of the in-game world as Aura levels drop. |
| FR-VF3 | Player Model Decay Visuals | 4 | 1 | Develop visual effects for the player model (e.g., less vibrant, translucent, negative particles) as Aura decays. |
| FR-VF4 | Auditory Cues for Aura Changes | 3 | 1 | Implement subtle auditory cues for Aura changes (draining for decay, positive hum for gain). |
| FR-VF5 | Dynamic Ominous Music | 3 | 1 | Integrate dynamic in-game music that becomes more ominous or sparse as Aura decays. |
| FR-VF6 | Display Standard Debuff Icons | 2 | 1 | Ensure standard Minecraft debuff icons are displayed when Aura drops below specific tiers. |
| FR-AA1 | "Confess Cheat" Button Implementation | 4 | 1 | Implement the "Confess Cheat" button, triggering significant Aura loss, potential loss of recent progress, or severe temporary debuffs. |
| FR-AA2 | Implement "Truth Quests" | 4 | 1 | Develop "Truth Quests" that periodically present reflective questions about honesty within the "Chronicle." |
| FR-AA3 | Implement Invisible "Integrity Score" | 4 | 1 | Create and track an invisible "Integrity Score" for the player. |
| FR-AA4 | Implement Integrity Score Decrease Logic | 4 | 3 | Implement logic for decreasing the "Integrity Score" based on too many tasks completed too quickly, high-value tasks completed instantly, or falsely answered "Truth Quests" followed by suspicious activity. |
| FR-AA5 | Integrity Score Threshold Effects | 4 | 2 | Implement effects for low "Integrity Score," such as reduced Aura gain or negative in-game events. |
| FR-AA6 | Cumulative Aura/Streak Requirements for High Tiers | 4 | 2 | Gate high-tier unlocks (Netherite, End Portal) behind cumulative Aura earned over time or consistent task completion streaks. |
| FR-AA7 | Time-Locked Benefits/Advancements | 3 | 1 | Implement certain in-game benefits or advancements that are "time-locked" regardless of Aura. |
| FR-ST1 | Introduce Custom Skill Trees | 4 | 1 | Implement custom skill trees (e.g., Productivity, Focus, Mindfulness) for the mod. |
| FR-ST2 | Grant Skill Points on Task Completion | 3 | 1 | Develop a system to grant "Skill Points" to custom trees upon completing real-life tasks. |
| FR-ST3 | Allow Spending Skill Points for Abilities | 4 | 1 | Implement the ability for players to spend "Skill Points" to unlock specific in-game passive abilities. |