# Productivity & Decay (Habit Mod)

**Productivity & Decay** is a Minecraft Forge mod that motivates players to complete real-world tasks by directly tying in-game progression and player abilities to their productivity outside the game. Your character grows stronger, gains new abilities, and unlocks powerful features as you complete tasks in real life—while inactivity causes your powers to wane.

## Features

- **Productivity Aura:** Your in-game power level ("Aura") increases as you log completed real-life tasks. Aura constantly decays over real-world time, requiring consistent productivity to maintain your abilities.
- **Tiered Progression:** All major in-game abilities and recipe unlocks are gated behind your current Aura tier. Higher tiers grant access to more powerful Minecraft features, abilities, and even integration with other mods.
- **Chronicle of Deeds GUI:** Accessed by a hotkey (default: `O`), this in-game interface lets you log, manage, and categorize real-world tasks. Track your progress, see your Aura status, and mark tasks as complete.
- **Anti-Abuse System:** "Honor system" with built-in integrity features (confession button, hidden penalties, etc.) to discourage false task reporting and keep the challenge meaningful.
- **Visual & Auditory Feedback:** Clear HUD elements and sound cues keep you informed about your Aura changes and unlocks.
- **Unlockable Mod Features:** Popular mods like Vein Miner, Waystones, Xaero's Minimap, and more become available as you reach specific Aura milestones.

## Example Ability Tiers

- **Health Aura:** Increases max health, reduces hunger drain, unlocks healing powers, and more as you complete physical tasks.
- **Organization Aura:** Expands inventory, speeds up crafting, and unlocks special storage features when you tackle organizational tasks.
- **Social, Intelligence, and Productivity Auras:** Each focused on a different real-life skill area, unlocking related in-game perks.

*(For a full breakdown of Aura tiers and their effects, see the [`docs/features.md`](docs/features.md) file.)*

## How It Works

1. **Complete real-life tasks** (chores, work, study, exercise, etc.).
2. **Log them in-game** using the Chronicle of Deeds GUI (`O` key by default).
3. **Gain Aura** based on the number, type, and difficulty of tasks completed.
4. **Maintain your Aura**—it decays over time, so keep up your productivity!
5. **Unlock new powers** and features in Minecraft as your Aura grows.

## Installation & Running

1. **Requirements:**
   - **Minecraft** 1.20.1
   - **Forge Mod Loader** (latest recommended build for 1.20.1)
   - **Java** (latest compatible JDK for Minecraft 1.20.1)

2. **Installation:**
   1. Download the latest `habitmod-x.x.x.jar` from the [releases](https://github.com/Achoo00/habit_mod/releases) page or build from source.
   2. Place the `.jar` file in your Minecraft installation's `mods` folder.
   3. Launch Minecraft with the Forge profile.

3. **Usage:**
   - Start a single-player world.
   - Press `O` to open the Chronicle of Deeds.
   - Add, edit, and complete real-world tasks.
   - Watch your Aura grow or decay, and discover new powers as you stay productive!

## Running & Debugging in IntelliJ IDEA

### 1. Open the Project
- Open IntelliJ IDEA and select **Open**.
- Navigate to your project folder (where `build.gradle` and `gradlew` are located) and open it as a Gradle project.

### 2. Setup and Refresh Gradle
- If prompted, import the Gradle project.
- Let IntelliJ finish indexing and resolving dependencies.

### 3. Run Minecraft Client for Debugging
- Open the terminal (bottom panel in IntelliJ) or use IntelliJ’s Gradle tool window.
- Run the following command to start Minecraft with the mod in development mode:
  ```sh
  ./gradlew runClient
  ```
  - On Windows, use `gradlew runClient` (without the `./`).

### 4. Debug Mode
- To run with debugging enabled (so you can set breakpoints, step through code, etc.), use:
  ```sh
  ./gradlew runClient --debug-jvm
  ```
  - This will wait for a debugger to attach (IntelliJ will prompt you).

### 5. Additional Useful Commands
- **Recompile and re-run after code changes:**
  ```sh
  ./gradlew build
  ./gradlew runClient
  ```
- **Run the server (for testing server-side logic):**
  ```sh
  ./gradlew runServer
  ```
- **Clean build outputs (for troubleshooting):**
  ```sh
  ./gradlew clean
  ```

### 6. Tips
- You can also set up a "Run Configuration" in IntelliJ that points to the `runClient` Gradle task for easier repeated testing.

## Roadmap & Issues

- See [`docs/roadmap.md`](docs/roadmap.md) for planned features and future updates.
- Known issues and troubleshooting can be found in [`docs/known-issues.md`](docs/known-issues.md).

## Contributing

Contributions are welcome! Feel free to open issues, submit pull requests, or suggest features.

---

*Stay productive—the world (and your Minecraft world) depends on it!*
