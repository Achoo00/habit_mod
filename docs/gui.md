# GUI Design Specification - Productivity & Decay Mod

This document details the user interface (GUI) design for the "Productivity & Decay" Minecraft mod, specifically focusing on the "Chronicle of Deeds" and the Heads-Up Display (HUD) elements.

## 1. Core GUI Principles

* **Clarity:** Information is easy to understand at a glance, using clear text and intuitive visuals.
* **Hierarchy:** The most critical information (e.g., overall Productivity Aura) is prominently displayed.
* **Usability:** Common actions (e.g., completing a task, navigating between aura categories) are simple and require minimal clicks.
* **Consistency:** Visual elements (buttons, icons, text styles) and interaction patterns are consistent throughout the GUI.
* **Feedback:** Player actions and Aura changes are immediately and clearly communicated.

## 2. "Chronicle of Deeds" GUI

The "Chronicle of Deeds" is the central hub for players to manage their real-life tasks and monitor their Aura status. It is designed as a multi-tabbed interface to manage the complexity of multiple Aura categories.

### 2.1. Accessing the GUI

* **Method:** Accessed via an uncraftable, unloseable in-game item (e.g., a "Chronicle Book") or a configurable hotkey.

### 2.2. Overall Layout - Main Screen with Tabs

The GUI will feature a main content area with a left-side navigation panel for different sections/aura categories.

+-------------------------------------------------------------------+
| [MOD LOGO/TITLE]                                                  |
+-------------------------------------------------------------------+
| Productivity Aura: [█████████----] 125/151+                       |
+-------------------------------------------------------------------+
| [  Health ]                                                       |
| [  Intell ]      [Filter: All] [Sort: Freshness]                  |
| [  Social ]      +-------------------------------------------+    |
| [  Org&Prod]     | Task: Workout          [♥] [M] [███--] [Done]|
| [  Creative]     | Task: Read Book        [🧠] [L] [████-] [Done]|
| [  Skill Trees]  | Task: Declutter Desk   [⚙️] [S] [--] [Done]|
| [ Settings ]     | ... (scrollable list)                     |    |
|                  +-------------------------------------------+    |
|                  |                                           |    |
|                  +-------------------------------------------+    |
| [Confess Cheat]                         [ Add New Task ]          |
+-------------------------------------------------------------------+

#### 2.2.1. Top Header Area

* **Mod Title:** "Chronicle of Deeds" prominently displayed.
* **"Confess Cheat" Button:** Small, distinctively styled button (e.g., red or with a skull icon) in the top-right corner.
    * **Functionality:** Triggers the anti-abuse consequences detailed in `PRD.md`.
* **Overall Productivity Aura Display:**
    * **Text:** "Productivity Aura: [Current Level] / [Max Level for Current Tier]" (e.g., "125 / 151+").
    * **Progress Bar:** A horizontal bar visually representing the player's overall Productivity Aura progress within the current tier.
    * **(Optional) Small Icon/Indicator:** A subtle visual cue that indicates the current overall Productivity Aura tier's most impactful debuff or buff (e.g., a small skull for low Aura, a star for high Aura).

#### 2.2.2. Left-Side Navigation Panel (Tabs/Buttons)

This panel allows players to switch between different views of their Aura system. Each tab will clearly indicate the section it leads to.

* **"Overview / Tasks" (Default Tab):** Displays the main task management interface.
* **Individual Aura Categories:**
    * "Health Aura"
    * "Intelligence Aura"
    * "Social Aura"
    * "Organization & Productivity Aura"
    * "Creative & Hobby Aura"
    * *(Future Consideration: "Financial & Resource Management Aura", "Mindfulness & Well-being Aura")*
* **"Skill Trees":** For managing custom skill point allocation (referencing FR-ST1, FR-ST2, FR-ST3).
* **"Settings":** For mod configuration options (e.g., Aura decay rates, visual preferences).

#### 2.2.3. Central Content Area - "Overview / Tasks" Tab

* **Task List Filters & Sorters:**
    * **"Filter by Category" Dropdown/Buttons:** Allows players to display tasks belonging only to a specific Aura category (e.g., only Health tasks).
    * **"Sort By" Dropdown/Buttons:** Allows sorting tasks by freshness (ascending/descending), effort level, or name.
* **Active Tasks List (Scrollable):** Each entry in the list will clearly represent a single task.
    * **Task Name:** Prominently displayed (e.g., "Workout," "Read a book").
    * **Category Icon:** A small, distinct icon next to the task name, representing its associated Aura category (e.g., Red Heart for Health, Blue Brain for Intelligence, Green Gear for Organization & Productivity, Purple Music Note for Creative & Hobby, Orange People for Social).
    * **Effort Level Indicator:** A visual representation of the task's effort (e.g., 1-5 stars, or "S" for Small, "M" for Medium, "L" for Large).
    * **Freshness Indicator:** A small, colored bar or icon that visually represents how long it has been since the task was completed.
        * Green: Very fresh (recently completed, full Aura gain).
        * Yellow: Nearing expiry (still full Aura gain).
        * Red: Stale (not completed in a long time, potential bonus Aura gain).
        * Grey/Empty: Recently completed too frequently (diminished/no Aura gain).
    * **"Complete" Button:** The primary action button for a task. Marks the task as complete, updates Aura, and resets its freshness timer.
    * **Edit/Delete Task (via context menu or smaller buttons):** When a task is selected or hovered over, smaller buttons for "Edit" and "Delete" appear, or these options are available via a right-click context menu.
        * **Edit Task:** Opens a small pop-up or switches to an "Edit Task" view allowing modification of name, category, and effort.
        * **Delete Task:** Prompts for confirmation before removal.
* **"Add New Task" Button:** Clearly visible at the bottom of the central area.
    * **Functionality:** Opens a new overlay or switches to a dedicated "Add Task" screen.
        * **Input Fields:** Text input for Task Name, dropdown for Category selection, dropdown/slider for Effort Level.
        * **"Create Task" Button:** Saves the new task.
        * **"Cancel" Button:** Discards the new task.

### 2.3. Individual Aura Category Tab Content (e.g., "Health Aura" Tab)

When a player clicks on one of the individual Aura category tabs (e.g., "Health"), the central content area updates to show detailed information for that specific Aura.

+-------------------------------------------------------------------+
| [MOD LOGO/TITLE]                                                  |
+-------------------------------------------------------------------+
| Health Aura: [███████----] 75                                     |
+-------------------------------------------------------------------+
| [  Health ] <--- ACTIVE                                           |
| [  Intell ]      ** Health Aura Status: Tier 51-100 (Awakened) ** |
| [  Social ]      -----------------------------------------------  |
| [  Org&Prod]     ** Benefits: **                                  |
| [  Creative]     | [♥] Permanent Regeneration I                   |
| [  Skill Trees]  | [❤︎❤︎] +2 Max Health                            |
| [ Settings ]     | [🪶] Reduced Fall Damage (Feather Falling I)   |
|                  | [🔥] Permanent Fire Protection I               |
|                  | [🍔] Food 0% Hunger Chance                     |
|                  -----------------------------------------------  |
|                  ** Next Unlock at 101 Health Aura: **            |
|                  | [⚡] Permanent Haste I                          |
|                  | [❤︎❤︎❤︎❤︎] +4 Max Health                         |
|                  | [💧] Significantly Reduced Environmental Damage|
|                  -----------------------------------------------  |
|                  **Associated Tasks:**                            |
|                  | Task: Workout          [M] [███--] [Done]      |
|                  | Task: Floss            [S] [████-] [Done]      |
|                  | ...                                            |
+-------------------------------------------------------------------+

#### 2.3.1. Top Header Area (Individual Aura)

* **Specific Aura Name & Level:** E.g., "Health Aura: 75."
* **Progress Bar:** A bar showing progress for *this specific aura* within its current tier.
* **Aura Icon:** A larger, prominent icon representing the current aura category (e.g., a heart for Health).

#### 2.3.2. Central Content Area (Individual Aura)

* **Current Tier Status:** Clearly states the current tier and its level range (e.g., "Current Tier: 51-100 Health Aura").
* **Active Benefits (Collapsible/Categorized):**
    * A list of all buffs, unlocks, and modified mechanics currently active due to this Aura's level.
    * Use small, recognizable icons (Minecraft potion effects, custom icons) next to each benefit.
    * Benefits should be dynamically updated and clearly state their effects (e.g., "Permanent Regeneration I," "Max Health +2 hearts").
* **Next Tier Benefits (Collapsible/Categorized):**
    * Clearly states the level required for the next tier (e.g., "Next Unlock at 101 Health Aura").
    * Lists the benefits that will be gained upon reaching that next tier. This serves as strong motivation.
* **Previous Tier Debuffs/Reversion (Collapsible/Categorized):**
    * Clearly states the threshold below which these effects will activate (e.g., "Risk if Aura drops below 51 Health Aura").
    * Lists the negative effects or restrictions that would be re-imposed if the Aura drops to the previous tier. This provides clear consequences.
* **Associated Tasks (Optional, but Recommended):**
    * A filtered list showing only the tasks the player has created that belong to this specific Aura category. This helps players understand how to improve the current Aura. Includes "Complete" button.

### 2.4. "Skill Trees" Tab (Conceptual)

* This tab would display the custom skill trees (Productivity, Focus, Mindfulness).
* It would show available Skill Points and allow players to allocate them into various passive abilities on the skill trees.
* Visual representation of the tree, unlocked nodes, and unspent points.

### 2.5. "Settings" Tab (Conceptual)

* Allows players to configure mod settings (e.g., overall Aura decay rate, notification preferences, visual style of HUD).

## 3. Heads-Up Display (HUD) Element

A persistent, non-intrusive HUD element to provide real-time Aura status without requiring the player to open the "Chronicle of Deeds."

* **Location:** Suggested top-left corner, similar to hunger/health bars, but potentially slightly offset or smaller.
* **Primary Display:**
    * **Overall "Productivity Aura" Level:** A numerical value (e.g., "PA: 125").
    * **Overall Progress Bar:** A small, horizontal progress bar that fills/drains with the Productivity Aura.
* **Quick Category Status Indicators (Optional, for higher levels of visual feedback):**
    * A very small, discreet set of icons (e.g., Heart, Brain, Gear, Music Note, People) could be displayed.
    * These icons could subtly change color or have a very minor visual effect (e.g., glow briefly) if their respective Aura category is in a particularly low (danger) or high (excellent) tier. This provides at-a-glance information for all categories without clutter.

## 4. Iconography and Color Coding

* **Aura Category Icons:** Develop unique, clear icons for each Aura category (e.g., Heart for Health, Brain for Intelligence, Gear for Organization & Productivity, Music Note/Palette for Creative & Hobby, People for Social).
* **Color Palette:** Assign a distinct color to each Aura category and use it consistently across the GUI (e.g., Health = Red/Pink, Intelligence = Blue/Purple, Org & Prod = Green/Yellow, Creative & Hobby = Cyan/Orange, Social = Magenta/Brown). This helps with visual differentiation.
* **Status Indicators:** Use a consistent color scheme for freshness (Green: Good, Yellow: Warning, Red: Danger/Stale) and progress bars.

## 5. User Flow Examples

* **Checking Aura:** Player presses hotkey -> Chronicle GUI opens to "Overview" tab, showing overall Aura. Player clicks "Health Aura" tab to see detailed Health Aura status.
* **Completing a Task:** Player opens GUI -> Navigates to "Overview" tab -> Clicks "Complete" next to "Workout" task -> Aura updates, task timestamp resets, visual/auditory feedback given.
* **Adding a Task:** Player opens GUI -> Clicks "Add New Task" -> Enters details in overlay -> Clicks "Create Task" -> New task appears in the list.

This detailed GUI design specification provides a robust framework for implementing the "Chronicle of Deeds" and related UI elements.