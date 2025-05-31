This roadmap outlines the planned development phases for the "Productivity & Decay" Minecraft mod, focusing on a solo developer approach with AI assistance. It prioritizes a Minimum Viable Product (MVP) first, followed by deep integration and refinement.
Phase 0: Foundations & Learning (Optional, but factored into overall estimate)

    Goal: Establish development environment and foundational modding knowledge.
    Timeline: 1.5 - 4 months (if starting from scratch with Java/modding)
    Activities:
        Learn Java fundamentals (if needed).
        Complete basic Forge modding tutorials (e.g., custom item, block, GUI).
        Set up a stable Forge development environment for Minecraft 1.20.1.
        Initial research into Forge's Capability system and Recipe APIs.

Phase 1: Core Systems - The Minimum Viable Product (MVP)

    Goal: Deliver a functional core loop: complete task -> gain Aura -> experience basic tiered abilities -> Aura decays. Prove the core concept.
    Timeline: 6 - 11 weeks (approx. 1.5 - 2.5 months)
    Key Deliverables:
        Mod Setup: Initial Forge project structure.
        Productivity Aura:
            Basic Aura data storage (e.g., using player capabilities).
            Constant real-world time-based Aura decay.
            Basic saving/loading of Aura level.
        Chronicle of Deeds (GUI):
            Functional GUI accessible by hotkey/item.
            Ability to add, delete, and mark tasks complete.
            Link task completion to basic Aura gain.
            Basic display of current Aura.
        Basic Tiered Abilities:
            Define 2-3 simple Aura tiers.
            Apply/remove basic vanilla potion effects (e.g., Slowness, Haste, Mining Fatigue) based on Aura tier.
        Version Control: Regular Git commits.

Phase 2: Deep Integration & Polish

    Goal: Implement the "total conversion" aspects, integrate Aura deeper into core Minecraft mechanics, and add advanced features.
    Timeline: 12 - 21 weeks (approx. 3 - 5 months)
    Key Deliverables:
        Advanced Tiered Abilities & Consequences:
            Implement Aura-gated vanilla tool/armor crafting recipes (disable/enable).
            Modify block breaking/interaction mechanics based on Aura tiers.
            Integrate full health, movement speed, and hunger changes based on Aura.
        Farm & Resource Automation Gating:
            Gate specific Redstone components and automated farm recipes by Aura tiers.
            (Optional, if feasible) Implement "Productivity Core" block.
        Dimensional Progression Gating:
            Modify Nether portal activation based on Aura/tasks.
            Modify End Portal activation and Eye of Ender mechanics.
        Task Repetition & "Freshness" Logic:
            Implement full task freshness timers and diminishing returns.
            Implement bonus Aura for "stale" tasks.
        Visual & Auditory Feedback:
            Implement HUD Aura indicator.
            World desaturation and player model visual decay.
            Custom sound cues and dynamic music changes.
        Robust Save/Load System: Ensure all custom data (tasks, timestamps, Integrity Score, skill points) saves and loads perfectly.
        Custom Skill Trees (Knowledge & Effort):
            System for earning Skill Points from task completion.
            Basic implementation of spending points and unlocking passive abilities.

Phase 3: Refinement, Balancing & Bug Fixing (V1.0 Release Candidate)

    Goal: Achieve a stable, balanced, and enjoyable V1.0 experience.
    Timeline: 6 - 10 weeks (approx. 1.5 - 2.5 months)
    Key Deliverables:
        Extensive Playtesting: Thorough testing of all features and interactions.
        Balancing: Fine-tune all Aura gain/decay rates, task values, and ability thresholds. Iterative adjustments.
        Comprehensive Bug Fixing: Address all identified bugs and crashes.
        Anti-Abuse System Refinement: Test and refine the "Confess Cheat" and "Integrity Score" mechanisms.
        User Configuration: Implement in-game options for key configurable parameters (e.g., decay rate, task values).
        Documentation: Create clear installation guide, user manual for "Chronicle of Deeds," and mod mechanics explanations.
        Final Build & Deployment Preparation.

Overall Estimated Timeframe (Solo Dev, Intermediate Skill, AI-Assisted)

    Realistic Total: 7.5 - 10 months

Maintenance & Future Updates (Post V1.0)

    Goal: Keep the mod compatible, fix post-release bugs, and introduce new features.
    Activities:
        Bug fixes based on user feedback.
        Compatibility updates for newer Minecraft/Forge versions.
        Implement "Nice-to-have" features from the initial planning (e.g., advanced visual decay, more skill tree branches).
        Continued re-balancing and minor enhancements.