// src/main/java/net/amaha/habitmod/gui/ChronicleScreen.java
package net.amaha.habitmod.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.amaha.habitmod.HabitMod; // Import your minimal main mod class
import net.amaha.habitmod.data.Task; // Import the Task data class
import net.amaha.habitmod.data.TaskManager; // Import the TaskManager
import net.amaha.habitmod.network.PacketHandler;
import net.amaha.habitmod.network.ApplyEffectPacket;
import net.minecraft.client.Minecraft; // Import Minecraft client for player access
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player; // Correct import for Player
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

@OnlyIn(Dist.CLIENT)
public class ChronicleScreen extends Screen {
    // A simple placeholder texture. For a real mod, you'd create your own.
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(HabitMod.MOD_ID, "textures/gui/chronicle_background.png");

    // Page constants
    private static final int OVERVIEW_TASKS_PAGE = 0;
    private static final int HEALTH_AURA_PAGE = 1;
    private static final int INTELLIGENCE_AURA_PAGE = 2;
    private static final int SOCIAL_AURA_PAGE = 3;
    private static final int ORGANIZATION_PRODUCTIVITY_AURA_PAGE = 4;
    private static final int CREATIVE_HOBBY_AURA_PAGE = 5;
    private static final int SKILL_TREES_PAGE = 6;
    private static final int SETTINGS_PAGE = 7;

    // Current page being displayed
    private int currentPage = OVERVIEW_TASKS_PAGE;

    private EditBox taskInputField;
    private Button addTaskButton;
    private List<Task> tasks = new ArrayList<>(); // Use our Task data class
    private List<Checkbox> taskCheckboxes = new ArrayList<>(); // To manage checkboxes

    // Navigation buttons for left panel
    private List<Button> navigationButtons = new ArrayList<>();

    // Top header components
    private Button confessCheatButton;

    // Add aura display
    private net.amaha.habitmod.data.AuraManager auraManager;

    private int xSize = 512;
    private int ySize = 200; // Make GUI slightly taller to fit more tasks
    private int guiLeft;
    private int guiTop;

    // Scrolling variables for tasks
    private int scrollOffset = 0; // Current scroll position
    private int maxVisibleTasks = 6; // Maximum number of tasks visible at once
    private Button scrollUpButton;
    private Button scrollDownButton;

    // Scrolling variables for navigation panel
    private int navScrollOffset = 0; // Current navigation scroll position
    private int maxVisibleNavButtons = 5; // Maximum number of navigation buttons visible at once
    private Button navScrollUpButton;
    private Button navScrollDownButton;

    // Scrolling variables for aura category content
    private int auraCategoryScrollOffset = 0; // Current aura category content scroll position
    private Button auraCategoryScrollUpButton;
    private Button auraCategoryScrollDownButton;

    // Task editing variables
    private int editingTaskIndex = -1; // Index of the task being edited, -1 if none
    private EditBox taskEditField;

    // Tier buttons
    private Button tier0Button;
    private Button tier1Button;
    private Button tier2Button;
    private Button tier3Button;
    private Button tier4Button;

    public ChronicleScreen() {
        super(Component.translatable("gui.habitmod.chronicle.title"));
        // Load tasks from TaskManager
        tasks = new ArrayList<>(TaskManager.getTasks());
    }

    @Override
    protected void init() {
        super.init();
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;

        // --- Add Top Header Components ---
        // "Confess Cheat" button in top-right corner
        confessCheatButton = Button.builder(Component.literal("Confess Cheat"), (button) -> {
            // TODO: Implement anti-abuse consequences as detailed in PRD.md
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                player.sendSystemMessage(Component.literal("You confessed to cheating! Consequences will follow..."));
                // For now, just remove some aura points
                net.amaha.habitmod.data.AuraManager.removeAura(50);
            }
        }).pos(guiLeft + xSize - 100, guiTop + 5).size(90, 20).build();
        this.addRenderableWidget(confessCheatButton);

        // --- Add Left-Side Navigation Panel ---
        // Clear existing navigation buttons
        navigationButtons.forEach(this::removeWidget);
        navigationButtons.clear();

        // Add navigation buttons for different views
        int buttonHeight = 20;
        int buttonSpacing = 5;
        int navPanelStartY = guiTop + 40;
        int navPanelHeight = ySize - 50; // Leave space for header and some padding
        int navButtonsAreaHeight = navPanelHeight - 50; // Leave space for scroll buttons

        // Calculate how many buttons can be visible at once
        maxVisibleNavButtons = navButtonsAreaHeight / (buttonHeight + buttonSpacing);

        // Add navigation scroll buttons
        navScrollUpButton = Button.builder(Component.literal("▲"), (button) -> {
            if (navScrollOffset > 0) {
                navScrollOffset--;
                clearWidgets();
                init();
            }
        }).pos(guiLeft + 10, navPanelStartY).size(20, 20).build();
        this.addRenderableWidget(navScrollUpButton);

        navScrollDownButton = Button.builder(Component.literal("▼"), (button) -> {
            if (navScrollOffset < navigationButtons.size() - maxVisibleNavButtons) {
                navScrollOffset++;
                clearWidgets();
                init();
            }
        }).pos(guiLeft + 10, navPanelStartY + navButtonsAreaHeight + 5).size(20, 20).build();
        this.addRenderableWidget(navScrollDownButton);

        // Create all navigation buttons (even if not all will be visible)
        List<Button> allNavButtons = new ArrayList<>();

        // Overview / Tasks button (default)
        Button overviewButton = Button.builder(Component.literal("Overview / Tasks"), (button) -> {
            currentPage = OVERVIEW_TASKS_PAGE;
            clearWidgets();
            init();
        }).pos(guiLeft + 35, 0).size(75, buttonHeight).build(); // Y position will be set later
        allNavButtons.add(overviewButton);

        // Health Aura button
        Button healthButton = Button.builder(Component.literal("Health Aura"), (button) -> {
            currentPage = HEALTH_AURA_PAGE;
            clearWidgets();
            init();
        }).pos(guiLeft + 35, 0).size(75, buttonHeight).build(); // Y position will be set later
        allNavButtons.add(healthButton);

        // Intelligence Aura button
        Button intelligenceButton = Button.builder(Component.literal("Intelligence Aura"), (button) -> {
            currentPage = INTELLIGENCE_AURA_PAGE;
            clearWidgets();
            init();
        }).pos(guiLeft + 35, 0).size(75, buttonHeight).build(); // Y position will be set later
        allNavButtons.add(intelligenceButton);

        // Social Aura button
        Button socialButton = Button.builder(Component.literal("Social Aura"), (button) -> {
            currentPage = SOCIAL_AURA_PAGE;
            clearWidgets();
            init();
        }).pos(guiLeft + 35, 0).size(75, buttonHeight).build(); // Y position will be set later
        allNavButtons.add(socialButton);

        // Organization & Productivity Aura button
        Button orgProdButton = Button.builder(Component.literal("Org & Prod Aura"), (button) -> {
            currentPage = ORGANIZATION_PRODUCTIVITY_AURA_PAGE;
            clearWidgets();
            init();
        }).pos(guiLeft + 35, 0).size(75, buttonHeight).build(); // Y position will be set later
        allNavButtons.add(orgProdButton);

        // Creative & Hobby Aura button
        Button creativeButton = Button.builder(Component.literal("Creative Aura"), (button) -> {
            currentPage = CREATIVE_HOBBY_AURA_PAGE;
            clearWidgets();
            init();
        }).pos(guiLeft + 35, 0).size(75, buttonHeight).build(); // Y position will be set later
        allNavButtons.add(creativeButton);

        // Skill Trees button
        Button skillTreesButton = Button.builder(Component.literal("Skill Trees"), (button) -> {
            currentPage = SKILL_TREES_PAGE;
            clearWidgets();
            init();
        }).pos(guiLeft + 35, 0).size(75, buttonHeight).build(); // Y position will be set later
        allNavButtons.add(skillTreesButton);

        // Settings button
        Button settingsButton = Button.builder(Component.literal("Settings"), (button) -> {
            currentPage = SETTINGS_PAGE;
            clearWidgets();
            init();
        }).pos(guiLeft + 35, 0).size(75, buttonHeight).build(); // Y position will be set later
        allNavButtons.add(settingsButton);

        // Add only the visible buttons to the screen
        int startIndex = Math.min(navScrollOffset, Math.max(0, allNavButtons.size() - maxVisibleNavButtons));
        int endIndex = Math.min(startIndex + maxVisibleNavButtons, allNavButtons.size());

        for (int i = startIndex; i < endIndex; i++) {
            Button button = allNavButtons.get(i);
            int buttonY = navPanelStartY + 25 + ((i - startIndex) * (buttonHeight + buttonSpacing));
            button.setY(buttonY);
            navigationButtons.add(button);
            this.addRenderableWidget(button);
        }

        // Update scroll button states
        navScrollUpButton.active = navScrollOffset > 0;
        navScrollDownButton.active = navScrollOffset < allNavButtons.size() - maxVisibleNavButtons;

        // Highlight the current page button if it's visible
        for (Button button : navigationButtons) {
            button.active = true; // Make all buttons active
        }

        // Find the current page button in the visible buttons and disable it
        for (int i = 0; i < navigationButtons.size(); i++) {
            if (startIndex + i == currentPage) {
                navigationButtons.get(i).active = false;
                break;
            }
        }

        // Initialize the appropriate page based on the current page
        switch (currentPage) {
            case OVERVIEW_TASKS_PAGE:
                initTasksPage();
                break;
            case HEALTH_AURA_PAGE:
            case INTELLIGENCE_AURA_PAGE:
            case SOCIAL_AURA_PAGE:
            case ORGANIZATION_PRODUCTIVITY_AURA_PAGE:
            case CREATIVE_HOBBY_AURA_PAGE:
                // Initialize the aura category page
                initAuraCategoryPage();
                break;
            case SKILL_TREES_PAGE:
                // TODO: Implement skill trees page
                initTiersPage(); // Temporary fallback
                break;
            case SETTINGS_PAGE:
                // TODO: Implement settings page
                initTiersPage(); // Temporary fallback
                break;
        }
    }

    /**
     * Initializes the tasks page with task management widgets
     */
    private void initTasksPage() {
        // Define content area bounds
        int contentAreaStartY = guiTop + 85;
        int contentAreaHeight = ySize - 95; // Leave space for header and filter controls
        int contentAreaEndY = contentAreaStartY + contentAreaHeight;

        // Calculate maximum visible tasks to fit within the content area
        maxVisibleTasks = Math.min(6, contentAreaHeight / 24);

        // --- Add New Task Button at the bottom of the content area ---
        // Make sure it fits within the GUI bounds
        int addTaskButtonY = Math.min(guiTop + ySize - 25, contentAreaStartY + (maxVisibleTasks * 24) + 5);

        addTaskButton = Button.builder(Component.literal("Add New Task"), (button) -> {
            // TODO: Open a dialog or overlay to add a new task
            // For now, just add a placeholder task
            Task newTask = new Task("New Task", false);
            tasks.add(newTask);
            TaskManager.addTask(newTask); // Add to TaskManager
            rebuildTaskCheckboxes(); // Rebuild the checkboxes after adding a task
        }).pos(guiLeft + 180, addTaskButtonY).size(100, 20).build();
        this.addRenderableWidget(addTaskButton);

        // --- Add Scroll Buttons ---
        scrollUpButton = Button.builder(Component.literal("▲"), (button) -> {
            if (scrollOffset > 0) {
                scrollOffset--;
                rebuildTaskCheckboxes();
            }
        }).pos(guiLeft + xSize - 30, contentAreaStartY).size(20, 20).build();
        this.addRenderableWidget(scrollUpButton);

        scrollDownButton = Button.builder(Component.literal("▼"), (button) -> {
            if (scrollOffset < Math.max(0, tasks.size() - maxVisibleTasks)) {
                scrollOffset++;
                rebuildTaskCheckboxes();
            }
        }).pos(guiLeft + xSize - 30, contentAreaStartY + (maxVisibleTasks * 24) - 20).size(20, 20).build();
        this.addRenderableWidget(scrollDownButton);

        // Initially set visibility based on task count
        boolean needsScrolling = tasks.size() > maxVisibleTasks;
        scrollUpButton.visible = needsScrolling;
        scrollDownButton.visible = needsScrolling;

        // --- Initialize and Rebuild Task Checkboxes ---
        rebuildTaskCheckboxes();
    }

    /**
     * Initializes the tiers page with tier management widgets
     */
    private void initTiersPage() {
        // Add title for the tiers page
        // (This will be rendered in the render method)

        // --- Add Tier Testing Buttons ---
        // These buttons allow setting the aura level to test different tiers

        // Tier 0 button (0-50 aura)
        tier0Button = Button.builder(Component.literal("Set Tier 0"), (button) -> {
            net.amaha.habitmod.data.AuraManager.setAuraLevel(25);
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                player.sendSystemMessage(Component.literal("Aura set to 25 (Tier 0: Incapable Builder)"));
            }
        }).pos(guiLeft + xSize/2 - 45, guiTop + 50).size(90, 20).build();
        this.addRenderableWidget(tier0Button);

        // Tier 1 button (51-150 aura)
        tier1Button = Button.builder(Component.literal("Set Tier 1"), (button) -> {
            net.amaha.habitmod.data.AuraManager.setAuraLevel(100);
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                player.sendSystemMessage(Component.literal("Aura set to 100 (Tier 1: Awakened Apprentice)"));
            }
        }).pos(guiLeft + xSize/2 - 45, guiTop + 80).size(90, 20).build();
        this.addRenderableWidget(tier1Button);

        // Tier 2 button (151-300 aura)
        tier2Button = Button.builder(Component.literal("Set Tier 2"), (button) -> {
            net.amaha.habitmod.data.AuraManager.setAuraLevel(200);
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                player.sendSystemMessage(Component.literal("Aura set to 200 (Tier 2: Diligent Crafter)"));
            }
        }).pos(guiLeft + xSize/2 - 45, guiTop + 110).size(90, 20).build();
        this.addRenderableWidget(tier2Button);

        // Tier 3 button (301-500 aura)
        tier3Button = Button.builder(Component.literal("Set Tier 3"), (button) -> {
            net.amaha.habitmod.data.AuraManager.setAuraLevel(400);
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                player.sendSystemMessage(Component.literal("Aura set to 400 (Tier 3: Master Builder)"));
            }
        }).pos(guiLeft + xSize/2 - 45, guiTop + 140).size(90, 20).build();
        this.addRenderableWidget(tier3Button);

        // Tier 4 button (501+ aura)
        tier4Button = Button.builder(Component.literal("Set Tier 4"), (button) -> {
            net.amaha.habitmod.data.AuraManager.setAuraLevel(600);
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                player.sendSystemMessage(Component.literal("Aura set to 600 (Tier 4: End-Game Luminary)"));
            }
        }).pos(guiLeft + xSize/2 - 45, guiTop + 170).size(90, 20).build();
        this.addRenderableWidget(tier4Button);
    }

    /**
     * Initializes the aura category page with category-specific widgets
     */
    private void initAuraCategoryPage() {
        // Reset scroll offset when switching pages
        if (currentPage != HEALTH_AURA_PAGE && 
            currentPage != INTELLIGENCE_AURA_PAGE && 
            currentPage != SOCIAL_AURA_PAGE && 
            currentPage != ORGANIZATION_PRODUCTIVITY_AURA_PAGE && 
            currentPage != CREATIVE_HOBBY_AURA_PAGE) {
            auraCategoryScrollOffset = 0;
        }

        // Add scroll buttons for aura category content
        int contentAreaStartY = guiTop + 50;
        int contentAreaHeight = ySize - 60; // Leave space for header

        auraCategoryScrollUpButton = Button.builder(Component.literal("▲"), (button) -> {
            if (auraCategoryScrollOffset > 0) {
                auraCategoryScrollOffset--;
                clearWidgets();
                init();
            }
        }).pos(guiLeft + xSize - 30, contentAreaStartY).size(20, 20).build();
        this.addRenderableWidget(auraCategoryScrollUpButton);

        auraCategoryScrollDownButton = Button.builder(Component.literal("▼"), (button) -> {
            // The max scroll offset depends on the content height, which we don't know yet
            // We'll update the button's active state in the render method
            auraCategoryScrollOffset++;
            clearWidgets();
            init();
        }).pos(guiLeft + xSize - 30, contentAreaStartY + contentAreaHeight - 25).size(20, 20).build();
        this.addRenderableWidget(auraCategoryScrollDownButton);

        // Add buttons at positions that respect the scroll offset
        int buttonY = guiTop + 170 - (auraCategoryScrollOffset * 30);

        // Only add buttons if they would be visible
        if (buttonY >= contentAreaStartY && buttonY <= contentAreaStartY + contentAreaHeight - 25) {
            // For now, we'll just add a button to increase the aura level for testing
            Button increaseAuraButton = Button.builder(Component.literal("Increase Aura"), (button) -> {
                net.amaha.habitmod.data.AuraManager.addAura(10);
                Player player = Minecraft.getInstance().player;
                if (player != null) {
                    player.sendSystemMessage(Component.literal("Aura increased by 10. Current Aura: " + 
                        net.amaha.habitmod.data.AuraManager.getAuraLevel()));
                }
            }).pos(guiLeft + xSize/2 - 45, buttonY).size(90, 20).build();
            this.addRenderableWidget(increaseAuraButton);
        }

        buttonY = guiTop + 140 - (auraCategoryScrollOffset * 30);

        // Only add buttons if they would be visible
        if (buttonY >= contentAreaStartY && buttonY <= contentAreaStartY + contentAreaHeight - 25) {
            // Add a button to filter tasks by the current category
            Button filterTasksButton = Button.builder(Component.literal("Show Related Tasks"), (button) -> {
                // This would filter tasks by category in a real implementation
                Player player = Minecraft.getInstance().player;
                if (player != null) {
                    player.sendSystemMessage(Component.literal("Showing tasks for this category"));
                }
            }).pos(guiLeft + xSize/2 - 45, buttonY).size(90, 20).build();
            this.addRenderableWidget(filterTasksButton);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        // Handle scrolling based on the current page
        if (currentPage == OVERVIEW_TASKS_PAGE) {
            // Handle scrolling for the tasks page
            // Only handle scrolling if mouse is over the task list area
            int taskListStartX = guiLeft + 120;
            int taskListEndX = guiLeft + xSize - 35;
            int taskListStartY = guiTop + 85;
            int taskListEndY = guiTop + 85 + (maxVisibleTasks * 24);

            if (mouseX >= taskListStartX && mouseX <= taskListEndX && 
                mouseY >= taskListStartY && mouseY <= taskListEndY) {

                // Handle mouse wheel scrolling
                if (delta > 0 && scrollOffset > 0) {
                    // Scroll up
                    scrollOffset--;
                    rebuildTaskCheckboxes();
                    return true;
                } else if (delta < 0 && scrollOffset < Math.max(0, tasks.size() - maxVisibleTasks)) {
                    // Scroll down
                    scrollOffset++;
                    rebuildTaskCheckboxes();
                    return true;
                }
            }
        } else if (currentPage == HEALTH_AURA_PAGE || 
                  currentPage == INTELLIGENCE_AURA_PAGE || 
                  currentPage == SOCIAL_AURA_PAGE || 
                  currentPage == ORGANIZATION_PRODUCTIVITY_AURA_PAGE || 
                  currentPage == CREATIVE_HOBBY_AURA_PAGE) {
            // Handle scrolling for the aura category pages
            // Only handle scrolling if mouse is over the content area
            int contentAreaStartX = guiLeft + 120;
            int contentAreaEndX = guiLeft + xSize - 35;
            int contentAreaStartY = guiTop + 50;
            int contentAreaEndY = guiTop + ySize - 10;

            if (mouseX >= contentAreaStartX && mouseX <= contentAreaEndX && 
                mouseY >= contentAreaStartY && mouseY <= contentAreaEndY) {

                // Calculate total content height and max scroll offset
                int contentY = contentAreaStartY;
                int lineCount = 0;

                // Count lines based on the current page
                if (currentPage == HEALTH_AURA_PAGE) {
                    lineCount = 20; // Approximate number of lines for Health Aura page
                } else {
                    lineCount = 15; // Approximate number of lines for other aura pages
                }

                int totalContentHeight = lineCount * 15;
                int contentAreaHeight = contentAreaEndY - contentAreaStartY;
                int maxScrollOffset = Math.max(0, (totalContentHeight - contentAreaHeight) / 15);

                // Handle mouse wheel scrolling
                if (delta > 0 && auraCategoryScrollOffset > 0) {
                    // Scroll up
                    auraCategoryScrollOffset--;
                    clearWidgets();
                    init();
                    return true;
                } else if (delta < 0 && auraCategoryScrollOffset < maxScrollOffset) {
                    // Scroll down
                    auraCategoryScrollOffset++;
                    clearWidgets();
                    init();
                    return true;
                }
            }
        } else if (mouseX >= guiLeft + 10 && mouseX <= guiLeft + 110 && 
                  mouseY >= guiTop + 40 && mouseY <= guiTop + ySize - 10) {
            // Handle scrolling for the navigation panel
            // Only handle scrolling if mouse is over the navigation panel area

            // Calculate max scroll offset
            int maxScrollOffset = Math.max(0, 8 - maxVisibleNavButtons);

            // Handle mouse wheel scrolling
            if (delta > 0 && navScrollOffset > 0) {
                // Scroll up
                navScrollOffset--;
                clearWidgets();
                init();
                return true;
            } else if (delta < 0 && navScrollOffset < maxScrollOffset) {
                // Scroll down
                navScrollOffset++;
                clearWidgets();
                init();
                return true;
            }
        }

        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    // Helper method to rebuild the list of checkboxes
    /**
     * Renders task icons, effort level, and freshness indicators for a task
     * @param graphics The GuiGraphics instance
     * @param task The task to render icons for
     * @param x The x position to start rendering
     * @param y The y position to start rendering
     */
    private void renderTaskIcons(GuiGraphics graphics, Task task, int x, int y) {
        // Render category icon
        String categoryIcon = "♥"; // Default to health
        int categoryColor = 0xFF0000; // Red for health

        // Determine category based on task name (in a real implementation, Task would have a category field)
        String taskName = task.getName().toLowerCase();
        if (taskName.contains("read") || taskName.contains("study") || taskName.contains("learn")) {
            categoryIcon = "🧠"; // Intelligence
            categoryColor = 0x0000FF; // Blue
        } else if (taskName.contains("meet") || taskName.contains("call") || taskName.contains("social")) {
            categoryIcon = "👥"; // Social
            categoryColor = 0xFF00FF; // Magenta
        } else if (taskName.contains("clean") || taskName.contains("organize") || taskName.contains("desk")) {
            categoryIcon = "⚙️"; // Organization
            categoryColor = 0x00FF00; // Green
        } else if (taskName.contains("music") || taskName.contains("art") || taskName.contains("hobby")) {
            categoryIcon = "🎵"; // Creative
            categoryColor = 0x00FFFF; // Cyan
        }

        // Draw category icon
        graphics.drawString(this.font, categoryIcon, x, y, categoryColor, false);

        // Render effort level
        String effortLevel = "M"; // Default to medium

        // Determine effort level based on task name (in a real implementation, Task would have an effort field)
        if (taskName.contains("quick") || taskName.contains("small") || taskName.contains("brief")) {
            effortLevel = "S"; // Small
        } else if (taskName.contains("big") || taskName.contains("large") || taskName.contains("major")) {
            effortLevel = "L"; // Large
        }

        // Draw effort level
        graphics.drawString(this.font, "[" + effortLevel + "]", x + 20, y, 0x808080, false);

        // Render freshness indicator (in a real implementation, Task would track completion time)
        // For now, just draw a random freshness level
        int freshness = task.hashCode() % 5; // 0-4 freshness level
        String freshnessBar = "";
        int freshnessColor = 0x808080; // Gray

        switch (freshness) {
            case 0: // Very fresh
                freshnessBar = "[███--]";
                freshnessColor = 0x00FF00; // Green
                break;
            case 1: // Fresh
                freshnessBar = "[████-]";
                freshnessColor = 0x88FF00; // Yellow-green
                break;
            case 2: // Neutral
                freshnessBar = "[█████]";
                freshnessColor = 0xFFFF00; // Yellow
                break;
            case 3: // Stale
                freshnessBar = "[█----]";
                freshnessColor = 0xFF8800; // Orange
                break;
            case 4: // Very stale
                freshnessBar = "[--]";
                freshnessColor = 0xFF0000; // Red
                break;
        }

        // Draw freshness bar
        graphics.drawString(this.font, freshnessBar, x + 40, y, freshnessColor, false);

        // Draw "Done" button (this is just visual, the actual button is the checkbox)
        if (task.isCompleted()) {
            graphics.drawString(this.font, "[Done]", x + 90, y, 0x00FF00, false);
        } else {
            graphics.drawString(this.font, "[Done]", x + 90, y, 0x808080, false);
        }
    }

    private void rebuildTaskCheckboxes() {
        // Only rebuild if we're on the overview/tasks page
        if (currentPage != OVERVIEW_TASKS_PAGE) return;

        // Clear existing checkboxes and buttons from the screen
        taskCheckboxes.forEach(this::removeWidget);
        taskCheckboxes.clear();

        // Remove task edit field if it exists
        if (taskEditField != null) {
            this.removeWidget(taskEditField);
            taskEditField = null;
        }

        // Reset editing state
        editingTaskIndex = -1;

        // Calculate visible tasks range
        int startIndex = scrollOffset;
        int endIndex = Math.min(startIndex + maxVisibleTasks, tasks.size());

        int yOffset = guiTop + 85; // Starting Y position for task list in the central content area

        // Update scroll buttons state
        boolean needsScrolling = tasks.size() > maxVisibleTasks;
        scrollUpButton.visible = needsScrolling;
        scrollDownButton.visible = needsScrolling;
        scrollUpButton.active = scrollOffset > 0;
        scrollDownButton.active = scrollOffset < Math.max(0, tasks.size() - maxVisibleTasks);

        // Only show tasks in the visible range
        for (int i = startIndex; i < endIndex; i++) {
            final int taskIndex = i; // Need final for lambda
            Task task = tasks.get(taskIndex);

            // Checkbox takes (x, y, width, height, message, checked)
            // Use a shorter width to make room for category icon, effort level, and freshness indicators
            Checkbox checkbox = new Checkbox(
                    guiLeft + 120, yOffset,
                    xSize - 200, 20, // Reduced width to make room for icons and buttons
                    Component.literal("Task: " + task.getName()),
                    task.isCompleted()
            ) {
                // Override onClick to handle our custom logic
                @Override
                public void onClick(double pMouseX, double pMouseY) {
                    super.onClick(pMouseX, pMouseY); // Let the checkbox handle its own checked state change
                    boolean newCheckedState = this.selected(); // Get the new state after click

                    // Only process if the state changed from unchecked to checked
                    if (newCheckedState && !task.isCompleted()) {
                        task.setCompleted(true); // Mark task as completed in our data
                        // Update TaskManager (it shares the same task objects, but just to be safe)
                        TaskManager.setTasks(tasks);

                        // Add aura points when completing a task
                        int auraGain = 20; // Default aura gain
                        net.amaha.habitmod.data.AuraManager.addAura(auraGain);

                        // Get player for messaging
                        Player player = Minecraft.getInstance().player;
                        if (player != null) {
                            player.sendSystemMessage(Component.literal("You gained " + auraGain + " Aura points!"));
                            player.sendSystemMessage(Component.literal("Current Aura: " + net.amaha.habitmod.data.AuraManager.getAuraLevel()));
                        }

                        // --- Specific logic for "Workout" task ---
                        if (task.getName().equalsIgnoreCase("Workout")) {
                            System.out.println("Workout task checked! Attempting to apply speed boost.");
                            if (player != null) {
                                // Send packet to server to apply effect instead of applying directly on client
                                // This ensures proper synchronization and allows effects to be removed by milk
                                PacketHandler.sendToServer(new ApplyEffectPacket(MobEffects.JUMP, 60, 4, false, false));
                                player.sendSystemMessage(Component.literal("You feel invigorated from your workout!"));
                            }
                        }
                    } else if (!newCheckedState && task.isCompleted()) {
                        // Optional: if you want to allow unchecking and revoking effects
                        task.setCompleted(false); // Mark task as uncompleted
                        // Update TaskManager (it shares the same task objects, but just to be safe)
                        TaskManager.setTasks(tasks);
                        // No effect revocation for simplicity in this basic concept
                    }
                }
            };
            taskCheckboxes.add(checkbox);
            this.addRenderableWidget(checkbox); // Add the checkbox to the screen

            // Add rename button
            final int finalYOffset = yOffset; // Create a final copy for the lambda
            Button renameButton = Button.builder(Component.literal("✎"), (button) -> {
                // Start editing this task
                editingTaskIndex = taskIndex;

                // Create edit field
                taskEditField = new EditBox(this.font, guiLeft + 120, finalYOffset, xSize - 150, 20, Component.empty());
                taskEditField.setMaxLength(50);
                taskEditField.setValue(task.getName());
                this.addRenderableWidget(taskEditField);

                // Rebuild to hide the checkbox and show the edit field
                rebuildTaskCheckboxes();
            }).pos(guiLeft + xSize - 60, yOffset).size(25, 20).build();
            this.addRenderableWidget(renameButton);

            // Add delete button
            Button deleteButton = Button.builder(Component.literal("✖"), (button) -> {
                // Remove the task
                tasks.remove(taskIndex);
                TaskManager.setTasks(tasks);

                // Adjust scroll offset if needed
                if (scrollOffset > 0 && scrollOffset >= tasks.size() - maxVisibleTasks) {
                    scrollOffset = Math.max(0, tasks.size() - maxVisibleTasks);
                }

                // Rebuild the checkboxes
                rebuildTaskCheckboxes();
            }).pos(guiLeft + xSize - 30, yOffset).size(25, 20).build();
            this.addRenderableWidget(deleteButton);

            yOffset += 24; // Spacing for next task (checkbox height + padding)
        }

        // If we're in edit mode, show the edit field and hide the checkbox
        if (editingTaskIndex >= 0 && editingTaskIndex < tasks.size()) {
            // Only create the edit field if it doesn't exist
            if (taskEditField == null) {
                int editYOffset = guiTop + 85 + ((editingTaskIndex - scrollOffset) * 24); // Adjusted for central content area

                // Create edit field
                taskEditField = new EditBox(this.font, guiLeft + 120, editYOffset, xSize - 150, 20, Component.empty());
                taskEditField.setMaxLength(50);
                taskEditField.setValue(tasks.get(editingTaskIndex).getName());
                this.addRenderableWidget(taskEditField);

                // Add save button
                Button saveButton = Button.builder(Component.literal("✓"), (button) -> {
                    // Save the edited task name
                    String newName = taskEditField.getValue();
                    if (!newName.isEmpty()) {
                        // Create a new task with the updated name and same completion status
                        Task oldTask = tasks.get(editingTaskIndex);
                        Task newTask = new Task(newName, oldTask.isCompleted());

                        // Replace the old task with the new one
                        tasks.set(editingTaskIndex, newTask);
                        TaskManager.setTasks(tasks);
                    }

                    // Exit edit mode
                    editingTaskIndex = -1;

                    // Rebuild the checkboxes
                    rebuildTaskCheckboxes();
                }).pos(guiLeft + xSize - 60, editYOffset).size(25, 20).build();
                this.addRenderableWidget(saveButton);

                // Add cancel button
                Button cancelButton = Button.builder(Component.literal("✖"), (button) -> {
                    // Exit edit mode without saving
                    editingTaskIndex = -1;

                    // Rebuild the checkboxes
                    rebuildTaskCheckboxes();
                }).pos(guiLeft + xSize - 30, editYOffset).size(25, 20).build();
                this.addRenderableWidget(cancelButton);

                // Hide the checkbox for this task
                for (Checkbox checkbox : taskCheckboxes) {
                    if (checkbox.getY() == editYOffset) {
                        checkbox.visible = false;
                    }
                }
            }
        }
    }


    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics); // Renders the darkened background

        // Render the GUI background texture
        RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
        // Blit a 256x200 texture
        graphics.blit(BACKGROUND_TEXTURE, guiLeft, guiTop, 0, 0, xSize, ySize, xSize, ySize);

        // Render widgets (input field, buttons, checkboxes)
        super.render(graphics, mouseX, mouseY, partialTick);

        // --- Render Top Header Area ---
        // Render mod title
        String modTitle = "Chronicle of Deeds";
        graphics.drawString(this.font, modTitle, guiLeft + 10, guiTop + 10, 0x404040, false);

        // Render Productivity Aura display
        int auraLevel = net.amaha.habitmod.data.AuraManager.getAuraLevel();
        int tier = net.amaha.habitmod.data.AuraManager.getCurrentTier();
        int maxTierLevel = 0;

        // Determine max level for current tier
        switch (tier) {
            case 0:
                maxTierLevel = net.amaha.habitmod.data.AuraManager.TIER_0_MAX;
                break;
            case 1:
                maxTierLevel = net.amaha.habitmod.data.AuraManager.TIER_1_MAX;
                break;
            case 2:
                maxTierLevel = net.amaha.habitmod.data.AuraManager.TIER_2_MAX;
                break;
            case 3:
                maxTierLevel = net.amaha.habitmod.data.AuraManager.TIER_3_MAX;
                break;
            case 4:
                maxTierLevel = Integer.MAX_VALUE; // No upper limit for tier 4
                break;
        }

        // Display aura level text
        String auraText = "Productivity Aura: " + auraLevel + " / ";
        if (tier == 4) {
            auraText += "151+"; // For tier 4, show the minimum threshold
        } else {
            auraText += maxTierLevel;
        }
        graphics.drawString(this.font, auraText, guiLeft + 10, guiTop + 25, 0x00FF00, false);

        // Draw progress bar
        int barWidth = 150;
        int barHeight = 5;
        int barX = guiLeft + 10;
        int barY = guiTop + 35;

        // Draw background (empty) bar
        graphics.fill(barX, barY, barX + barWidth, barY + barHeight, 0x33000000);

        // Calculate filled portion
        float fillPercentage = 0;
        if (tier < 4) {
            // For tiers 0-3, calculate based on current tier's range
            int minTierLevel = 0;
            switch (tier) {
                case 1:
                    minTierLevel = net.amaha.habitmod.data.AuraManager.TIER_0_MAX + 1;
                    break;
                case 2:
                    minTierLevel = net.amaha.habitmod.data.AuraManager.TIER_1_MAX + 1;
                    break;
                case 3:
                    minTierLevel = net.amaha.habitmod.data.AuraManager.TIER_2_MAX + 1;
                    break;
            }
            fillPercentage = (float)(auraLevel - minTierLevel) / (maxTierLevel - minTierLevel);
        } else {
            // For tier 4, just show full
            fillPercentage = 1.0f;
        }

        // Ensure fill percentage is between 0 and 1
        fillPercentage = Math.max(0, Math.min(1, fillPercentage));

        // Draw filled portion
        int fillWidth = (int)(barWidth * fillPercentage);
        graphics.fill(barX, barY, barX + fillWidth, barY + barHeight, getTierColor(tier));

        // Render page-specific content
        if (currentPage == OVERVIEW_TASKS_PAGE) {
            renderTasksPage(graphics, mouseX, mouseY, partialTick);
        } else if (currentPage == HEALTH_AURA_PAGE || 
                  currentPage == INTELLIGENCE_AURA_PAGE || 
                  currentPage == SOCIAL_AURA_PAGE || 
                  currentPage == ORGANIZATION_PRODUCTIVITY_AURA_PAGE || 
                  currentPage == CREATIVE_HOBBY_AURA_PAGE) {
            renderAuraCategoryPage(graphics, mouseX, mouseY, partialTick);
        } else {
            renderTiersPage(graphics, mouseX, mouseY, partialTick);
        }
    }

    /**
     * Renders content specific to the tasks page
     */
    private void renderTasksPage(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // Draw central content area title
        String tasksTitle = "Task Management";
        graphics.drawString(this.font, tasksTitle, guiLeft + 120, guiTop + 50, 0x404040, false);

        // Draw filter and sort controls
        String filterLabel = "Filter: All";
        graphics.drawString(this.font, filterLabel, guiLeft + 120, guiTop + 65, 0x808080, false);

        String sortLabel = "Sort: Freshness";
        graphics.drawString(this.font, sortLabel, guiLeft + 220, guiTop + 65, 0x808080, false);

        // Draw task list header
        int taskListX = guiLeft + 120;
        int taskListY = guiTop + 85;
        int taskListWidth = xSize - 130;

        // Draw task list background
        graphics.fill(taskListX, taskListY, taskListX + taskListWidth, taskListY + (maxVisibleTasks * 24), 0x22000000);

        // Render task icons for each visible task
        int startIndex = scrollOffset;
        int endIndex = Math.min(startIndex + maxVisibleTasks, tasks.size());

        for (int i = startIndex; i < endIndex; i++) {
            Task task = tasks.get(i);
            int taskY = taskListY + ((i - startIndex) * 24) + 2; // +2 to center vertically with checkbox

            // Render task icons at the appropriate position
            renderTaskIcons(graphics, task, taskListX + xSize - 190, taskY);
        }

        // Render scroll indicators if needed
        if (tasks.size() > maxVisibleTasks) {
            // Show scroll position indicator
            int totalTasks = tasks.size();
            int visibleTasks = Math.min(maxVisibleTasks, totalTasks);
            float percentVisible = (float) visibleTasks / totalTasks;
            float percentScrolled = totalTasks <= visibleTasks ? 0 : (float) scrollOffset / (totalTasks - visibleTasks);

            // Draw scroll track
            int trackHeight = maxVisibleTasks * 24;
            int trackX = guiLeft + xSize - 10;
            int trackY = taskListY;

            // Draw track background
            graphics.fill(trackX, trackY, trackX + 5, trackY + trackHeight, 0x33000000);

            // Draw scroll thumb
            int thumbHeight = Math.max(20, (int)(percentVisible * trackHeight));
            int thumbY = trackY + (int)((trackHeight - thumbHeight) * percentScrolled);
            graphics.fill(trackX, thumbY, trackX + 5, thumbY + thumbHeight, 0x66FFFFFF);

            // Show task count
            String taskCountText = scrollOffset + 1 + "-" + Math.min(scrollOffset + maxVisibleTasks, tasks.size()) + " of " + tasks.size();
            graphics.drawString(this.font, taskCountText, taskListX, taskListY + (maxVisibleTasks * 24) + 5, 0x808080, false);
        }

        // Draw category icons legend
        int legendX = guiLeft + 120;
        int legendY = taskListY + (maxVisibleTasks * 24) + 20;

        // Health icon
        graphics.drawString(this.font, "♥", legendX, legendY, 0xFF0000, false);
        graphics.drawString(this.font, "Health", legendX + 15, legendY, 0x808080, false);

        // Intelligence icon
        graphics.drawString(this.font, "🧠", legendX + 70, legendY, 0x0000FF, false);
        graphics.drawString(this.font, "Intelligence", legendX + 85, legendY, 0x808080, false);

        // Social icon
        graphics.drawString(this.font, "👥", legendX + 170, legendY, 0xFF00FF, false);
        graphics.drawString(this.font, "Social", legendX + 185, legendY, 0x808080, false);

        // Organization icon
        graphics.drawString(this.font, "⚙️", legendX, legendY + 15, 0x00FF00, false);
        graphics.drawString(this.font, "Organization", legendX + 15, legendY + 15, 0x808080, false);

        // Creative icon
        graphics.drawString(this.font, "🎵", legendX + 120, legendY + 15, 0x00FFFF, false);
        graphics.drawString(this.font, "Creative", legendX + 135, legendY + 15, 0x808080, false);
    }

    /**
     * Renders content specific to the tiers page
     */
    private void renderTiersPage(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // Render tier information
        String tiersTitle = "Aura Tier Management";
        graphics.drawString(this.font, tiersTitle, guiLeft + xSize / 2 - font.width(tiersTitle) / 2, guiTop + 30, 0x404040, false);

        // Render tier descriptions
        int descY = guiTop + 50;
        int descX = guiLeft + 10;
        int descColor = 0x808080;

        // Only render descriptions if there's space (not overlapping with buttons)
        if (descX + 120 < guiLeft + xSize/2 - 45) {
            graphics.drawString(this.font, "Tier 0: 0-50", descX, descY, descColor, false);
            graphics.drawString(this.font, "Tier 1: 51-150", descX, descY + 30, descColor, false);
            graphics.drawString(this.font, "Tier 2: 151-300", descX, descY + 60, descColor, false);
            graphics.drawString(this.font, "Tier 3: 301-500", descX, descY + 90, descColor, false);
            graphics.drawString(this.font, "Tier 4: 501+", descX, descY + 120, descColor, false);
        }
    }

    /**
     * Renders content specific to the aura category page
     */
    private void renderAuraCategoryPage(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // Get the current aura level (for now, we'll use the overall aura level)
        int auraLevel = net.amaha.habitmod.data.AuraManager.getAuraLevel();

        // Determine the category name and color based on the current page
        String categoryName = "";
        int categoryColor = 0xFFFFFF;
        String categoryIcon = "";

        switch (currentPage) {
            case HEALTH_AURA_PAGE:
                categoryName = "Health";
                categoryColor = 0xFF0000; // Red
                categoryIcon = "♥";
                break;
            case INTELLIGENCE_AURA_PAGE:
                categoryName = "Intelligence";
                categoryColor = 0x0000FF; // Blue
                categoryIcon = "🧠";
                break;
            case SOCIAL_AURA_PAGE:
                categoryName = "Social";
                categoryColor = 0xFF00FF; // Magenta
                categoryIcon = "👥";
                break;
            case ORGANIZATION_PRODUCTIVITY_AURA_PAGE:
                categoryName = "Organization & Productivity";
                categoryColor = 0x00FF00; // Green
                categoryIcon = "⚙️";
                break;
            case CREATIVE_HOBBY_AURA_PAGE:
                categoryName = "Creative & Hobby";
                categoryColor = 0x00FFFF; // Cyan
                categoryIcon = "🎵";
                break;
        }

        // For demonstration, we'll simulate different aura levels for each category
        // In a real implementation, each category would have its own aura level
        int categoryAuraLevel = auraLevel;
        if (currentPage == HEALTH_AURA_PAGE) {
            categoryAuraLevel = 75; // Fixed value for demonstration
        }

        // --- Render Top Header Area (Individual Aura) ---
        // Specific Aura Name & Level
        String auraText = categoryName + " Aura: " + categoryAuraLevel;
        graphics.drawString(this.font, auraText, guiLeft + 10, guiTop + 25, categoryColor, false);

        // Progress Bar for this specific aura
        int barWidth = 150;
        int barHeight = 5;
        int barX = guiLeft + 10;
        int barY = guiTop + 35;

        // Draw background (empty) bar
        graphics.fill(barX, barY, barX + barWidth, barY + barHeight, 0x33000000);

        // Calculate filled portion (simplified for demonstration)
        float fillPercentage = categoryAuraLevel / 100.0f; // Assuming max is 100
        fillPercentage = Math.max(0, Math.min(1, fillPercentage));

        // Draw filled portion
        int fillWidth = (int)(barWidth * fillPercentage);
        graphics.fill(barX, barY, barX + fillWidth, barY + barHeight, categoryColor);

        // --- Define content area bounds ---
        int contentAreaStartY = guiTop + 50;
        int contentAreaHeight = ySize - 60; // Leave space for header
        int contentAreaEndY = contentAreaStartY + contentAreaHeight;

        // --- Render Central Content Area (Individual Aura) ---
        int contentX = guiLeft + 120;
        // Adjust contentY based on scroll offset
        int contentY = contentAreaStartY - (auraCategoryScrollOffset * 15);

        // Draw content area background
        graphics.fill(guiLeft + 120, contentAreaStartY, guiLeft + xSize - 35, contentAreaEndY, 0x22000000);

        // Only render content if it would be visible
        if (contentY >= contentAreaStartY - 30 && contentY <= contentAreaEndY) {
            // Current Tier Status
            String tierStatus = categoryName + " Aura Status: Tier 51-100 (Awakened)";
            graphics.drawString(this.font, tierStatus, contentX, contentY, 0x404040, false);
        }
        contentY += 20;

        // Draw separator line if visible
        if (contentY >= contentAreaStartY - 5 && contentY <= contentAreaEndY) {
            graphics.fill(contentX, contentY, contentX + xSize - 150, contentY + 1, 0x33000000);
        }
        contentY += 10;

        // Active Benefits
        if (contentY >= contentAreaStartY - 15 && contentY <= contentAreaEndY) {
            graphics.drawString(this.font, "Benefits:", contentX, contentY, 0x404040, false);
        }
        contentY += 15;

        // List of benefits (example for Health Aura)
        if (currentPage == HEALTH_AURA_PAGE) {
            if (contentY >= contentAreaStartY - 15 && contentY <= contentAreaEndY) {
                graphics.drawString(this.font, categoryIcon + " Permanent Regeneration I", contentX, contentY, categoryColor, false);
            }
            contentY += 15;

            if (contentY >= contentAreaStartY - 15 && contentY <= contentAreaEndY) {
                graphics.drawString(this.font, "❤︎❤︎ +2 Max Health", contentX, contentY, categoryColor, false);
            }
            contentY += 15;

            if (contentY >= contentAreaStartY - 15 && contentY <= contentAreaEndY) {
                graphics.drawString(this.font, "🪶 Reduced Fall Damage (Feather Falling I)", contentX, contentY, categoryColor, false);
            }
            contentY += 15;

            if (contentY >= contentAreaStartY - 15 && contentY <= contentAreaEndY) {
                graphics.drawString(this.font, "🔥 Permanent Fire Protection I", contentX, contentY, categoryColor, false);
            }
            contentY += 15;

            if (contentY >= contentAreaStartY - 15 && contentY <= contentAreaEndY) {
                graphics.drawString(this.font, "🍔 Food 0% Hunger Chance", contentX, contentY, categoryColor, false);
            }
            contentY += 15;
        } else {
            // Generic benefits for other categories
            if (contentY >= contentAreaStartY - 15 && contentY <= contentAreaEndY) {
                graphics.drawString(this.font, categoryIcon + " Benefit 1", contentX, contentY, categoryColor, false);
            }
            contentY += 15;

            if (contentY >= contentAreaStartY - 15 && contentY <= contentAreaEndY) {
                graphics.drawString(this.font, categoryIcon + " Benefit 2", contentX, contentY, categoryColor, false);
            }
            contentY += 15;

            if (contentY >= contentAreaStartY - 15 && contentY <= contentAreaEndY) {
                graphics.drawString(this.font, categoryIcon + " Benefit 3", contentX, contentY, categoryColor, false);
            }
            contentY += 15;
        }

        // Draw separator line
        contentY += 5;
        if (contentY >= contentAreaStartY - 5 && contentY <= contentAreaEndY) {
            graphics.fill(contentX, contentY, contentX + xSize - 150, contentY + 1, 0x33000000);
        }
        contentY += 10;

        // Next Tier Benefits
        if (contentY >= contentAreaStartY - 15 && contentY <= contentAreaEndY) {
            String nextTierText = "Next Unlock at 101 " + categoryName + " Aura:";
            graphics.drawString(this.font, nextTierText, contentX, contentY, 0x404040, false);
        }
        contentY += 15;

        // List of next tier benefits (example for Health Aura)
        if (currentPage == HEALTH_AURA_PAGE) {
            if (contentY >= contentAreaStartY - 15 && contentY <= contentAreaEndY) {
                graphics.drawString(this.font, "⚡ Permanent Haste I", contentX, contentY, 0x808080, false);
            }
            contentY += 15;

            if (contentY >= contentAreaStartY - 15 && contentY <= contentAreaEndY) {
                graphics.drawString(this.font, "❤︎❤︎❤︎❤︎ +4 Max Health", contentX, contentY, 0x808080, false);
            }
            contentY += 15;

            if (contentY >= contentAreaStartY - 15 && contentY <= contentAreaEndY) {
                graphics.drawString(this.font, "💧 Significantly Reduced Environmental Damage", contentX, contentY, 0x808080, false);
            }
            contentY += 15;
        } else {
            // Generic next tier benefits for other categories
            if (contentY >= contentAreaStartY - 15 && contentY <= contentAreaEndY) {
                graphics.drawString(this.font, "⚡ Next Tier Benefit 1", contentX, contentY, 0x808080, false);
            }
            contentY += 15;

            if (contentY >= contentAreaStartY - 15 && contentY <= contentAreaEndY) {
                graphics.drawString(this.font, "⚡ Next Tier Benefit 2", contentX, contentY, 0x808080, false);
            }
            contentY += 15;
        }

        // Draw separator line
        contentY += 5;
        if (contentY >= contentAreaStartY - 5 && contentY <= contentAreaEndY) {
            graphics.fill(contentX, contentY, contentX + xSize - 150, contentY + 1, 0x33000000);
        }
        contentY += 10;

        // Associated Tasks
        if (contentY >= contentAreaStartY - 15 && contentY <= contentAreaEndY) {
            graphics.drawString(this.font, "Associated Tasks:", contentX, contentY, 0x404040, false);
        }
        contentY += 15;

        // List of associated tasks (example for Health Aura)
        if (currentPage == HEALTH_AURA_PAGE) {
            if (contentY >= contentAreaStartY - 15 && contentY <= contentAreaEndY) {
                graphics.drawString(this.font, "Task: Workout          [M] [███--] [Done]", contentX, contentY, 0x808080, false);
            }
            contentY += 15;

            if (contentY >= contentAreaStartY - 15 && contentY <= contentAreaEndY) {
                graphics.drawString(this.font, "Task: Floss            [S] [████-] [Done]", contentX, contentY, 0x808080, false);
            }
            contentY += 15;
        } else {
            // Generic tasks for other categories
            if (contentY >= contentAreaStartY - 15 && contentY <= contentAreaEndY) {
                graphics.drawString(this.font, "Task: Category Task 1  [M] [███--] [Done]", contentX, contentY, 0x808080, false);
            }
            contentY += 15;

            if (contentY >= contentAreaStartY - 15 && contentY <= contentAreaEndY) {
                graphics.drawString(this.font, "Task: Category Task 2  [S] [████-] [Done]", contentX, contentY, 0x808080, false);
            }
            contentY += 15;
        }

        // Calculate total content height and update scroll button states
        int totalContentHeight = contentY - (contentAreaStartY - (auraCategoryScrollOffset * 15));
        int maxScrollOffset = Math.max(0, (totalContentHeight - contentAreaHeight) / 15);

        // Update scroll button states
        auraCategoryScrollUpButton.active = auraCategoryScrollOffset > 0;
        auraCategoryScrollDownButton.active = auraCategoryScrollOffset < maxScrollOffset;

        // Draw scroll position indicator
        if (maxScrollOffset > 0) {
            float percentScrolled = (float) auraCategoryScrollOffset / maxScrollOffset;
            int trackHeight = contentAreaHeight - 50; // Leave space for scroll buttons
            int thumbHeight = Math.max(20, (int)(trackHeight / (maxScrollOffset + 1)));
            int thumbY = contentAreaStartY + 25 + (int)((trackHeight - thumbHeight) * percentScrolled);

            // Draw scroll track
            graphics.fill(guiLeft + xSize - 25, contentAreaStartY + 25, guiLeft + xSize - 20, contentAreaStartY + trackHeight + 25, 0x33000000);

            // Draw scroll thumb
            graphics.fill(guiLeft + xSize - 25, thumbY, guiLeft + xSize - 20, thumbY + thumbHeight, 0x66FFFFFF);
        }
    }

    /**
     * Gets the name of the tier based on the tier number.
     * @param tier The tier number (0-4).
     * @return The name of the tier.
     */
    private String getTierName(int tier) {
        switch (tier) {
            case 0: return "Incapable Builder";
            case 1: return "Awakened Apprentice";
            case 2: return "Diligent Crafter";
            case 3: return "Master Builder";
            case 4: return "End-Game Luminary";
            default: return "Unknown";
        }
    }

    /**
     * Gets the color to use for displaying the tier text.
     * @param tier The tier number (0-4).
     * @return The color to use for the tier text.
     */
    private int getTierColor(int tier) {
        switch (tier) {
            case 0: return 0xFF0000; // Red for Tier 0
            case 1: return 0xFFAA00; // Orange for Tier 1
            case 2: return 0xFFFF00; // Yellow for Tier 2
            case 3: return 0x00AAFF; // Light Blue for Tier 3
            case 4: return 0x00FF00; // Green for Tier 4
            default: return 0xFFFFFF; // White for unknown
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false; // Allows game to continue while GUI is open
    }

    @Override
    public void onClose() {
        // This is called when the GUI is closed (e.g., by pressing ESC)
        // Save tasks to TaskManager
        TaskManager.setTasks(tasks);

        // Save tasks to player's persistent data
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            // We need to send a packet to the server to save the tasks
            // For simplicity, we'll just call saveTasks directly, but in a real mod
            // you'd want to send a packet to the server to save the tasks
            TaskManager.saveTasks(player);
        }

        super.onClose();
    }
}
