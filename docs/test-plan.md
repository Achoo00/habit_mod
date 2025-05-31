1. Introduction

This Test Plan outlines the strategy and scope for testing the "Productivity & Decay" Minecraft mod. The primary goal is to ensure the mod functions as intended, is stable, and delivers a motivating and consistent player experience according to the defined requirements.
2. Test Objectives

    Verify all functional requirements related to Aura gain, decay, and persistence.
    Validate correct behavior of all ability tiers and associated in-game gating (crafting, interaction, movement).
    Ensure the "Chronicle of Deeds" GUI is fully functional and user-friendly.
    Confirm anti-abuse mechanisms correctly detect and respond to suspicious activity.
    Assess the performance impact of the mod on Minecraft.
    Verify proper saving and loading of all mod-specific data.
    Evaluate the overall balance and playability of the mod.

3. Test Scope

The scope covers all features defined in the Product Requirements Document (PRD) for V1.0. This includes:

    Productivity Aura system (gain, decay, persistence).
    Chronicle of Deeds GUI (CRUD for tasks, Aura display, unlock display).
    All 5 Ability Tiers and their specific gating mechanisms (crafting recipes, block interactions, movement, health, hunger, redstone, dimensional portals).
    Task repetition and freshness logic.
    Visual and auditory feedback (HUD, world desaturation, player model, sounds, music, debuffs).
    Anti-abuse mechanisms (Confess Cheat, Truth Quests, Integrity Score, hidden penalties, long-term milestones).
    Custom Skill Trees (point gain, point spending, ability unlocks).
    Mod data saving and loading across sessions.
    Performance under various scenarios.

4. Test Environment

    Operating System: Windows 10/11 (Primary), macOS (Secondary, if resources allow).
    Minecraft Version: 1.20.1.
    Mod Loader: Forge (latest recommended build for 1.20.1).
    Java Version: Latest compatible Java Development Kit (JDK) for Minecraft 1.20.1.
    Hardware: Typical gaming PC specifications (various RAM allocations for performance testing).
    Network: Disconnected environment for testing non-API functionality.

5. Test Types

    Functional Testing: Verifying that each feature works as specified in the PRD.
    Integration Testing: Ensuring different components of the mod (e.g., task completion -> Aura gain -> recipe unlock) interact correctly.
    System Testing: Testing the mod as a whole within the Minecraft environment for stability, performance, and overall user experience.
    Regression Testing: Re-running previous tests after code changes or bug fixes to ensure no new defects have been introduced.
    Balance Testing: Iterative playtesting to fine-tune Aura gain/decay rates, task values, and ability thresholds for a fair and motivating experience.
    "Abuse" Testing: Actively attempting to exploit the honor system to identify weaknesses in the anti-abuse mechanisms.
    Persistence Testing: Verifying that all mod data saves and loads correctly after game restarts and crashes.

6. Test Cases (Examples)
6.1. Productivity Aura System

    TC-A01: Verify Aura decay occurs correctly over real-world time while the player is offline.
    TC-A02: Verify Aura decay occurs correctly over real-world time while the player is online.
    TC-A03: Verify Aura increases by the specified amount when a task is marked complete.
    TC-A04: Verify the daily Aura reset occurs and applies the specified decay.

6.2. Chronicle of Deeds (GUI)

    TC-CD01: Verify the "Chronicle of Deeds" GUI opens with the designated hotkey/item.
    TC-CD02: Verify a new task can be added with name, category, and effort level.
    TC-CD03: Verify an existing task can be edited.
    TC-CD04: Verify a task can be deleted.
    TC-CD05: Verify marking a task complete updates the Aura display and the task status.
    TC-CD06: Verify the "Confess Cheat" button is present and triggers its associated penalty.

6.3. Ability Tiers & Gating

    TC-AT01: Start a new world; verify initial player abilities are restricted to Tier 0 (e.g., cannot mine wood, limited inventory, movement debuffs).
    TC-AT02: Gain enough Aura to reach Tier 1; verify ability to mine wood, 3x3 crafting, and basic furnace functionality are unlocked.
    TC-AT03: Let Aura decay from Tier 1 to Tier 0; verify abilities revert correctly.
    TC-AT04: Gain enough Aura to reach Tier 2; verify Iron tool recipes are craftable, basic Redstone components are enabled.
    TC-AT05: Verify that if Aura is below a specific tier, corresponding recipes are uncraftable/hidden.
    TC-AT06: Verify Nether portal activation is gated until Tier 3 conditions are met.
    TC-AT07: Verify End Portal activation and Eye of Ender usage is gated until Tier 4 conditions are met.

6.4. Task Repetition & Freshness

    TC-TR01: Complete a task within 1 hour; verify diminished Aura gain.
    TC-TR02: Complete a task after 24 hours; verify full Aura gain.
    TC-TR03: Complete a "stale" task (not completed in 3+ days); verify bonus Aura gain.

6.5. Anti-Abuse Mechanisms

    TC-AA01: Mark 5 "Large Effort" tasks complete in 10 seconds; verify Integrity Score drops and/or Aura gain is penalized.
    TC-AA02: Repeatedly answer "Yes" to "Truth Quests" without credible task completion; verify hidden penalties trigger over time.
    TC-AA03: Test the "Confess Cheat" button and observe all associated penalties.
    TC-AA04: Verify that a Tier 4 unlock (e.g., Netherite) is not granted just by spiking Aura, but requires cumulative effort over time.

6.6. Data Persistence

    TC-DP01: Start a new world, complete a task, gain Aura, then quit the game. Relaunch and verify Aura and task data are correctly loaded.
    TC-DP02: Simulate a game crash while Aura is decaying; relaunch and verify data integrity.

7. Test Strategy

    Phased Testing: Testing will be integrated into the SDLC.
        Phase 1 (MVP): Focus on core Aura, GUI, and basic tier functionality.
        Phase 2 (Deep Integration): Test all recipe gating, dimensional gating, and advanced mechanics.
        Phase 3 (Refinement): Intensive balance, abuse, and regression testing.
    Iterative Testing: After each feature implementation or bug fix, relevant test cases will be executed.
    Manual Testing: Due to the nature of game modding and visual/feel elements, most testing will be manual.
    Issue Tracking: All bugs, anomalies, and balance issues will be documented in a dedicated issue tracker (e.g., a simple spreadsheet for solo dev).

8. Exit Criteria

    All critical bugs (crashes, save corruption, game-breaking issues) are resolved.
    All high-priority functional requirements are met.
    The mod performs within acceptable limits (no significant lag).
    Balance feels fair and motivating to the primary tester (developer).
    All known issues are documented.
    Documentation (installation, usage) is complete.