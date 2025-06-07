// src/main/java/net/amaha/habitmod/gui/ChronicleScreen.java
package net.amaha.habitmod.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.amaha.habitmod.HabitMod; // Import your minimal main mod class
import net.amaha.habitmod.data.Task; // Import the Task data class
import net.amaha.habitmod.data.TaskManager; // Import the TaskManager
import net.amaha.habitmod.network.PacketHandler;
import net.amaha.habitmod.network.ApplyEffectPacket;
import net.minecraft.client.Minecraft; // Import Minecraft client for player access
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player; // Correct import for Player
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Arrays;
import net.minecraft.world.item.ItemStack; // Import for ItemStack
import java.util.stream.Collectors; // Import for stream operations


@OnlyIn(Dist.CLIENT)
public class ChronicleScreen extends Screen {
    // A simple placeholder texture. For a real mod, you'd create your own.
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(HabitMod.MOD_ID, "textures/gui/chronicle_background.png");

    // Inside your ChronicleScreen class (e.g., at the top, after other fields)
    private List<net.minecraft.client.gui.components.Button> allNavButtons = new ArrayList<>(); // To store ALL navigation buttons
    private List<net.minecraft.client.gui.components.Button> navigationButtons = new ArrayList<>(); // To store CURRENTLY VISIBLE navigation buttons
    private int navScrollOffset; // Current scroll position for navigation panel
    private int maxVisibleNavButtons; // Max number of navigation buttons visible at once
    private net.minecraft.client.gui.components.Button navScrollUpButton; // The scroll up button for navigation
    private net.minecraft.client.gui.components.Button navScrollDownButton; // The scroll down button for navigation

    // Page constants
    private static final int OVERVIEW_TASKS_PAGE = 0;
    private static final int HEALTH_AURA_PAGE = 1;
    private static final int INTELLIGENCE_AURA_PAGE = 2;
    private static final int SOCIAL_AURA_PAGE = 3;
    private static final int ORGANIZATION_PRODUCTIVITY_AURA_PAGE = 4;
    private static final int CREATIVE_HOBBY_AURA_PAGE = 5;
    private static final int SKILL_TREES_PAGE = 6;
    private static final int SETTINGS_PAGE = 7;


    // Inside your GuiPage.java (or where your enum is defined)

    public enum GuiPage {
        OVERVIEW_TASKS_PAGE, // No specific aura data for this page

        // Define your aura pages with their specific data and implementations
        HEALTH_AURA_PAGE {
            @Override
            public int getAuraLevel() { return 75; } // Example: Return specific aura level for this page
            @Override
            public Component getDisplayName() { return Component.literal("Health"); }
            @Override
            public int getCategoryColor() { return 0xFF0000; } // Red
            @Override
            public ItemStack getIconStack() { return new ItemStack(net.minecraft.world.item.Items.APPLE); } // Example item icon
            @Override
            public String getCategoryName() { return "Health"; }
            @Override
            public String getCategoryIcon() { return "❤️"; }

            @Override
            public List<Component> getBenefits() { // <--- RETURN TYPE IS NOW List<Component>
                return Arrays.asList(
                        Component.literal("♥ Permanent Regeneration I"),
                        Component.literal("❤︎❤︎ +2 Max Health"),
                        Component.literal("🪶 Reduced Fall Damage (Feather Falling I)"),
                        Component.literal("🔥 Permanent Fire Protection I"),
                        Component.literal("🍔 Food 0% Hunger Chance")
                );
            }
            @Override
            public List<Component> getNextTierBenefits() { // <--- RETURN TYPE IS NOW List<Component>
                return Arrays.asList(
                        Component.literal("⚡ Permanent Haste I"),
                        Component.literal("❤︎❤︎❤︎❤︎ +4 Max Health"),
                        Component.literal("💧 Significantly Reduced Environmental Damage")
                );
            }
            @Override
            public List<Component> getAssociatedTasks() { // <--- RETURN TYPE IS NOW List<Component>
                return Arrays.asList(
                        Component.literal("Task: Workout [M] [███--]"),
                        Component.literal("Task: Floss [S] [████-]")
                );
            }
        },

        INTELLIGENCE_AURA_PAGE {
            @Override
            public int getAuraLevel() { return 50; }
            @Override
            public Component getDisplayName() { return Component.literal("Intelligence"); }
            @Override
            public int getCategoryColor() { return 0x0000FF; }
            @Override
            public ItemStack getIconStack() { return new ItemStack(net.minecraft.world.item.Items.BOOK); }

            @Override
            public List<Component> getBenefits() { // <--- Also List<Component>
                return Arrays.asList(
                        Component.literal("🧠 Enhanced Learning Speed"),
                        Component.literal("💡 Increased XP Gain (15%)"),
                        Component.literal("🔍 Improved Enchantments (Tier I)")
                );
            }
            @Override
            public List<Component> getNextTierBenefits() { // <--- Also List<Component>
                return Arrays.asList(
                        Component.literal("📚 Wisdom of Ages (Double XP Gain)"),
                        Component.literal("🌟 Divine Enchantments (Tier II)")
                );
            }
            @Override
            public List<Component> getAssociatedTasks() { // <--- Also List<Component>
                return Arrays.asList(
                        Component.literal("Task: Read a Book [M] [███--]"),
                        Component.literal("Task: Solve a Puzzle [S] [████-]")
                );
            }
        },

        SOCIAL_AURA_PAGE {
            @Override
            public int getAuraLevel() { return 60; }
            @Override
            public Component getDisplayName() { return Component.literal("Social"); }
            @Override
            public int getCategoryColor() { return 0xFF00FF; }
            @Override
            public ItemStack getIconStack() { return new ItemStack(net.minecraft.world.item.Items.EMERALD); }
            @Override
            public String getCategoryName() { return "Social"; }
            @Override
            public String getCategoryIcon() { return "👥"; }

            @Override
            public List<Component> getBenefits() {
                return Arrays.asList(
                        Component.literal("👥 Charismatic Presence"),
                        Component.literal("🤝 Improved Trading Prices (5%)"),
                        Component.literal("🗣️ Villager Reputation Boost")
                );
            }
            @Override
            public List<Component> getNextTierBenefits() {
                return Arrays.asList(
                        Component.literal("👑 Leadership Aura (Passive Mob Luring)"),
                        Component.literal("💰 Even Better Trading Prices (10%)")
                );
            }
            @Override
            public List<Component> getAssociatedTasks() {
                return Arrays.asList(
                        Component.literal("Task: Trade with Villager [M] [███--]"),
                        Component.literal("Task: Play with Friend [S] [████-]")
                );
            }
        },

        ORGANIZATION_PRODUCTIVITY_AURA_PAGE {
            @Override
            public int getAuraLevel() { return 80; }
            @Override
            public Component getDisplayName() { return Component.literal("Organization & Productivity"); }
            @Override
            public int getCategoryColor() { return 0x00FF00; }
            @Override
            public ItemStack getIconStack() { return new ItemStack(net.minecraft.world.item.Items.CRAFTING_TABLE); }
            @Override
            public String getCategoryName() { return "Organization & Productivity"; }
            @Override
            public String getCategoryIcon() { return "📦"; }

            @Override
            public List<Component> getBenefits() {
                return Arrays.asList(
                        Component.literal("✏️ Efficient Crafting"),
                        Component.literal("📦 Inventory Auto-Sort"),
                        Component.literal("⏱️ Reduced Block Break Time (5%)")
                );
            }
            @Override
            public List<Component> getNextTierBenefits() {
                return Arrays.asList(
                        Component.literal("⚙️ Automated Smelting (Small Chance)"),
                        Component.literal("💎 Double Ore Drop Chance (Very Small)")
                );
            }
            @Override
            public List<Component> getAssociatedTasks() {
                return Arrays.asList(
                        Component.literal("Task: Organize Chests [M] [███--]"),
                        Component.literal("Task: Complete Daily Goal [S] [████-]")
                );
            }
        },

        CREATIVE_HOBBY_AURA_PAGE {
            @Override
            public int getAuraLevel() { return 40; }
            @Override
            public Component getDisplayName() { return Component.literal("Creative & Hobby"); }
            @Override
            public int getCategoryColor() { return 0x00FFFF; }
            @Override
            public ItemStack getIconStack() { return new ItemStack(net.minecraft.world.item.Items.PAINTING); }
            @Override
            public String getCategoryName() { return "Creative & Hobby"; }
            @Override
            public String getCategoryIcon() { return "🎨"; }

            @Override
            public List<Component> getBenefits() {
                return Arrays.asList(
                        Component.literal("🎵 Inspired Building"),
                        Component.literal("🌈 New Dye Recipes Unlocked"),
                        Component.literal("🎨 Increased Painting Drop Chance")
                );
            }
            @Override
            public List<Component> getNextTierBenefits() {
                return Arrays.asList(
                        Component.literal("✨ Creative Flight (Limited Time)"),
                        Component.literal("🎭 Unique Block Textures Unlocked")
                );
            }
            @Override
            public List<Component> getAssociatedTasks() {
                return Arrays.asList(
                        Component.literal("Task: Build Something New [M] [███--]"),
                        Component.literal("Task: Mine for Gems [S] [████-]")
                );
            }
        },

        SKILL_TREES_PAGE,
        SETTINGS_PAGE,
        TIERS;

        // --- Abstract Methods (or default implementations) ---
        // All enum constants *must* implement these if they are abstract.
        // If you define them as non-abstract with a default, only override for specific pages.

        public int getAuraLevel() { return 0; } // Default: 0 aura level
        public Component getDisplayName() { return Component.literal("Default Page"); }
        public int getCategoryColor() { return 0xFFFFFF; } // Default: White
        public ItemStack getIconStack() { return ItemStack.EMPTY; } // Default: Empty item stack
        public String getCategoryName() { return "Default"; } // Default category name
        public String getCategoryIcon() { return ""; } // Default category icon

        // These methods were from previous discussion
        public List<Component> getBenefits() { return Arrays.asList(Component.literal("Generic Benefit 1"), Component.literal("Generic Benefit 2")); }
        public List<Component> getNextTierBenefits() { return Arrays.asList(Component.literal("Next Tier Generic Benefit 1"), Component.literal("Next Tier Generic Benefit 2")); }
        public List<Component> getAssociatedTasks() { return Arrays.asList(Component.literal("Generic Task 1"), Component.literal("Generic Task 2")); }

        public boolean isAuraPage() {
            return this == HEALTH_AURA_PAGE ||
                    this == INTELLIGENCE_AURA_PAGE ||
                    this == SOCIAL_AURA_PAGE ||
                    this == ORGANIZATION_PRODUCTIVITY_AURA_PAGE ||
                    this == CREATIVE_HOBBY_AURA_PAGE;
        }
    }

    // Current page being displayed
    private GuiPage currentPage = GuiPage.OVERVIEW_TASKS_PAGE;

    private EditBox taskInputField;
    private Button addTaskButton;
    private List<Task> tasks = new ArrayList<>(); // Use our Task data class
    private List<Checkbox> taskCheckboxes = new ArrayList<>(); // To manage checkboxes

    // Navigation buttons for left panel
    // Note: navigationButtons is already declared at the top of the class

    // Top header components
    private Button confessCheatButton;

    // Add aura display
    private net.amaha.habitmod.data.AuraManager auraManager;

    private int xSize = 400; // Reduced from 512 to better fit content
    private int ySize = 220; // Increased height to better accommodate content and legend
    private int guiLeft;
    private int guiTop;

    private static final int CHECKBOX_WIDTH = 20; // Width of the checkbox square itself
    private static final int CHECKBOX_TASK_NAME_PADDING = 5; // Space between checkbox and task name
    private static final int MAX_TASK_NAME_DISPLAY_WIDTH = 100; // Max pixel width for task name
    private static final int TASK_NAME_ICONS_PADDING = 15; // Space between task name and first icon

    // Scrolling variables for tasks
    private int scrollOffset = 0; // Current scroll position
    private int maxVisibleTasks = 6; // Maximum number of tasks visible at once
    private Button scrollUpButton;
    private Button scrollDownButton;

    // Scrolling variables for navigation panel
    // Note: navScrollOffset, maxVisibleNavButtons, navScrollUpButton, and navScrollDownButton are already declared at the top of the class

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

    /**
     * Changes the current page and reinitializes the GUI
     * @param page The page to switch to
     */
    private void setPage(GuiPage page) {
        // Store the current page before changing it
        GuiPage oldPage = this.currentPage;

        // Only change the page if it's different from the current one
        if (page != oldPage) {
            this.currentPage = page;
            this.auraCategoryScrollOffset = 0; // Reset scroll position

            // Schedule the GUI reinitialization for the next tick to avoid concurrent modification
            Minecraft.getInstance().tell(() -> {
                this.clearWidgets();
                this.init(); // Reinitialize the GUI with the new page
            });
        }
    }

    // Add these fields if you don't have them to control your task buttons
    private List<Button> taskEditButtons = new ArrayList<>();
    private List<Button> taskDeleteButtons = new ArrayList<>();
    // And for the current editing state:
    private Button taskSaveButton;
    private Button taskCancelButton;


    public ChronicleScreen() {
        super(Component.translatable("gui.habitmod.chronicle.title"));
        // Load tasks from TaskManager
        tasks = new ArrayList<>(TaskManager.getTasks());
    }

    @Override
    protected void init() {
        super.init();
        this.clearWidgets();

        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;

        // --- Confess Cheat Button (Bottom left, inline with navigation buttons) ---
        confessCheatButton = Button.builder(Component.literal("Confess Cheat"), (button) -> {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                player.sendSystemMessage(Component.literal("You confessed to cheating! Consequences will follow..."));
                net.amaha.habitmod.data.AuraManager.removeAura(50);
            }
        }).pos(guiLeft + 10, guiTop + ySize - 30).size(90, 20).build();
        this.addRenderableWidget(confessCheatButton);

        // --- Init Navigation Button Lists ---
        if (allNavButtons == null) {
            allNavButtons = new ArrayList<>();
        } else {
            allNavButtons.clear();
        }

        if (navigationButtons == null) {
            navigationButtons = new ArrayList<>();
        } else {
            navigationButtons.clear();
        }

        // --- Button Config ---
        int buttonWidth = 75;
        int buttonHeight = 20;
        int buttonSpacing = 5;
        int navPanelStartX = guiLeft + 10;
        int navButtonX = navPanelStartX + (100 - buttonWidth) / 2;

        // --- Create Navigation Buttons ---
        allNavButtons.add(Button.builder(Component.literal("Overview"), (button) -> {
            setPage(GuiPage.OVERVIEW_TASKS_PAGE);
        }).pos(navButtonX, 0).size(buttonWidth, buttonHeight).build());

        for (GuiPage page : GuiPage.values()) {
            if (page.isAuraPage()) {
                allNavButtons.add(Button.builder(page.getDisplayName(), (button) -> {
                    setPage(page);
                }).pos(navButtonX, 0).size(buttonWidth, buttonHeight).build());
            } else if (page == GuiPage.SKILL_TREES_PAGE) {
                allNavButtons.add(Button.builder(Component.literal("Skill Trees"), (button) -> {
                    setPage(GuiPage.SKILL_TREES_PAGE);
                }).pos(navButtonX, 0).size(buttonWidth, buttonHeight).build());
            } else if (page == GuiPage.SETTINGS_PAGE) {
                allNavButtons.add(Button.builder(Component.literal("Settings"), (button) -> {
                    setPage(GuiPage.SETTINGS_PAGE);
                }).pos(navButtonX, 0).size(buttonWidth, buttonHeight).build());
            }
        }

        // --- Scroll Buttons ---
        int scrollButtonWidth = 20;
        int scrollButtonHeight = 20;
        int navPanelTopY = guiTop + 40;
        int navPanelBottomY = guiTop + ySize - 10;

        navScrollUpButton = Button.builder(Component.literal("▲"), (button) -> {
            navScrollOffset = Math.max(0, navScrollOffset - 1);
            updateNavigationPanel();
        }).pos(navPanelStartX, navPanelTopY + 2).size(scrollButtonWidth, scrollButtonHeight).build();
        this.addRenderableWidget(navScrollUpButton);

        navScrollDownButton = Button.builder(Component.literal("▼"), (button) -> {
            int maxOffset = Math.max(0, allNavButtons.size() - maxVisibleNavButtons);
            navScrollOffset = Math.min(maxOffset, navScrollOffset + 1);
            updateNavigationPanel();
        }).pos(navPanelStartX, navPanelBottomY - scrollButtonHeight - 2).size(scrollButtonWidth, scrollButtonHeight).build();
        this.addRenderableWidget(navScrollDownButton);

        // --- Populate Initial Visible Buttons ---
        updateNavigationPanel();

        // --- Page-Specific Initialization ---
        switch (currentPage) {
            case OVERVIEW_TASKS_PAGE -> initTasksPage();
            case HEALTH_AURA_PAGE, INTELLIGENCE_AURA_PAGE, SOCIAL_AURA_PAGE,
                 ORGANIZATION_PRODUCTIVITY_AURA_PAGE, CREATIVE_HOBBY_AURA_PAGE -> initAuraCategoryPage();
            case SKILL_TREES_PAGE, SETTINGS_PAGE -> initTiersPage(); // placeholder
        }
    }


    /**
     * Initializes the tasks page with task management widgets
     */
    private void initTasksPage() {
        // Define content area bounds
        int contentAreaStartY = guiTop + 125; // Moved down to match the new task list position
        int contentAreaHeight = ySize - 135; // Adjusted to account for the new position
        int contentAreaEndY = contentAreaStartY + contentAreaHeight;

        // Calculate maximum visible tasks to fit within the content area
        maxVisibleTasks = 5; // Fixed to show exactly 5 tasks as requested

        // --- Add Filter and Sort Buttons above the task list ---
        Button filterButton = Button.builder(Component.literal("Filter"), (button) -> {
            // TODO: Implement filter functionality
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                player.sendSystemMessage(Component.literal("Filter options will be shown here"));
            }
        }).pos(guiLeft + 120, guiTop + 40).size(60, 20).build();
        this.addRenderableWidget(filterButton);

        Button sortButton = Button.builder(Component.literal("Sort"), (button) -> {
            // TODO: Implement sort functionality
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                player.sendSystemMessage(Component.literal("Sort options will be shown here"));
            }
        }).pos(guiLeft + 220, guiTop + 40).size(60, 20).build();
        this.addRenderableWidget(sortButton);

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
        // If the current page is NOT an aura page, reset the scroll offset
        if (!currentPage.isAuraPage()) {
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
        // Commenting out for now, this will be available for admin use
        // Only add buttons if they would be visible
        // if (buttonY >= contentAreaStartY && buttonY <= contentAreaStartY + contentAreaHeight - 25) {
            // For now, we'll just add a button to increase the aura level for testing
            // Button increaseAuraButton = Button.builder(Component.literal("Increase Aura"), (button) -> {
               //  net.amaha.habitmod.data.AuraManager.addAura(10);
                // Player player = Minecraft.getInstance().player;
                // if (player != null) {
                    // player.sendSystemMessage(Component.literal("Aura increased by 10. Current Aura: " +
                        // net.amaha.habitmod.data.AuraManager.getAuraLevel()));
                // }
            // }).pos(guiLeft + xSize/2 - 45, buttonY).size(90, 20).build();
            // this.addRenderableWidget(increaseAuraButton);
         // }

        // buttonY = guiTop + 140 - (auraCategoryScrollOffset * 30);

        // Only add buttons if they would be visible
        // Commenting out for now, this will be available for admin use
        // if (buttonY >= contentAreaStartY && buttonY <= contentAreaStartY + contentAreaHeight - 25) {
            // Add a button to filter tasks by the current category
            // Button filterTasksButton = Button.builder(Component.literal("Show Related Tasks"), (button) -> {
                // This would filter tasks by category in a real implementation
                // Player player = Minecraft.getInstance().player;
                // if (player != null) {
                    // player.sendSystemMessage(Component.literal("Showing tasks for this category"));
                // }
            // }).pos(guiLeft + xSize/2 - 45, buttonY).size(90, 20).build();
            // this.addRenderableWidget(filterTasksButton);
        // }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {

        // --- 1. Handle Navigation Panel Scrolling ---
        // Check if the mouse is over the navigation panel area
        // (You have this condition below, let's put it first for clarity)
        // NOTE: This area overlaps with the aura page content area for x!
        // Consider adding a specific `navPanelWidth` variable to define the area.
        // Assuming nav panel X: guiLeft + 10 to guiLeft + 110 (width 100)
        // Assuming nav panel Y: guiTop + 40 to guiTop + ySize - 10 (or whatever it's defined as)
        int navPanelStartX = guiLeft + 10;
        int navPanelEndX = guiLeft + 110; // Left panel is 100px wide from 10px offset
        int navPanelStartY = guiTop + 40; // As defined in init()
        int navPanelEndY = guiTop + ySize - 10; // Or whatever is your nav panel end Y

        if (mouseX >= navPanelStartX && mouseX <= navPanelEndX &&
                mouseY >= navPanelStartY && mouseY <= navPanelEndY) {

            // Calculate max scroll offset dynamically based on all navigation buttons
            // Ensure 'allNavButtons' is a field that stores ALL nav buttons created in init()
            int maxNavScrollOffset = Math.max(0, allNavButtons.size() - maxVisibleNavButtons);

            if (delta > 0 && navScrollOffset > 0) { // Scroll up
                navScrollOffset--;
                updateNavigationPanel(); // Update only the nav panel's visibility/positions
                return true;
            } else if (delta < 0 && navScrollOffset < maxNavScrollOffset) { // Scroll down
                navScrollOffset++;
                updateNavigationPanel(); // Update only the nav panel's visibility/positions
                return true;
            }
        }


        // --- 2. Handle Task Page Scrolling ---
        // If we're on the tasks page AND mouse is over the task list area
        if (currentPage == GuiPage.OVERVIEW_TASKS_PAGE) {
            // Redefine taskListY to match its new, higher position (e.g., guiTop + 75)
            // Ensure this matches the value set in renderTasksPage and where task widgets are added.
            int taskListStartX = guiLeft + 120;
            int taskListEndX = guiLeft + xSize - 35; // Matches contentAreaEndX for consistency
            int taskListStartY = guiTop + 75; // IMPORTANT: Update this to your actual current Y
            int taskListEndY = taskListStartY + (maxVisibleTasks * 24); // Assuming 24px per task row

            if (mouseX >= taskListStartX && mouseX <= taskListEndX &&
                    mouseY >= taskListStartY && mouseY <= taskListEndY) {

                if (delta > 0 && scrollOffset > 0) { // Scroll up
                    scrollOffset--;
                    rebuildTaskCheckboxes(); // This method should handle visibility/position updates
                    return true;
                } else if (delta < 0 && scrollOffset < Math.max(0, tasks.size() - maxVisibleTasks)) { // Scroll down
                    scrollOffset++;
                    rebuildTaskCheckboxes(); // This method should handle visibility/position updates
                    return true;
                }
            }
        }

        // --- 3. Handle Aura Category Page Scrolling ---
        // Use the isAuraPage() helper from the previous discussion
        if (currentPage.isAuraPage()) { // Use the enum helper method for clarity
            // Optionally, restrict scrolling to the content area.
            // If you want global scrolling for aura pages, remove this mouse check.
            int contentAreaStartX = guiLeft + 120;
            int contentAreaEndX = guiLeft + xSize - 35; // Matches the grey box width
            int contentAreaStartY = guiTop + 50; // Top of the grey content box
            int contentAreaEndY = guiTop + ySize - 10; // Or guiTop + 50 + contentAreaHeight (from render method)

            // Only handle scrolling if mouse is over the content area
            if (mouseX >= contentAreaStartX && mouseX <= contentAreaEndX &&
                    mouseY >= contentAreaStartY && mouseY <= contentAreaEndY) {

                // Use your accurate content height calculation
                // Make sure calculateAuraCategoryContentHeight is defined and uses 'font'
                int totalContentHeight = calculateAuraCategoryContentHeight(currentPage, this.font, contentAreaEndX - contentAreaStartX - 10); // -10 for inner padding

                // Define contentAreaHeight consistently (from your render method)
                int definedContentAreaHeight = ySize - 60; // guiTop + 50 to guiTop + ySize - 10 = (ySize-10) - 50 = ySize - 60

                // Max scroll offset calculation. Use font.lineHeight for scroll unit.
                int maxAuraScrollOffset = Math.max(0, (totalContentHeight - definedContentAreaHeight) / font.lineHeight);

                if (delta > 0 && auraCategoryScrollOffset > 0) { // Scroll up
                    auraCategoryScrollOffset--;
                    // NO clearWidgets(); init(); here. Just trigger a re-render.
                    // Minecraft's screen system will call render() on the next tick.
                    return true;
                } else if (delta < 0 && auraCategoryScrollOffset < maxAuraScrollOffset) { // Scroll down
                    auraCategoryScrollOffset++;
                    // NO clearWidgets(); init(); here. Just trigger a re-render.
                    return true;
                }
            }
        }

        // If no custom scrolling was handled, let the super class handle it (e.g., for global screen scrolling)
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    // Inside your ChronicleScreen class (e.g., after init() or other helper methods)

    private void updateNavigationPanel() {
        // 1. Remove currently visible navigation buttons from Minecraft's widget list
        // This prevents duplicates if you're rebuilding the list.
        // Make sure 'navigationButtons' only contains the *currently visible* buttons.
        navigationButtons.forEach(this::removeWidget);
        navigationButtons.clear(); // Clear our internal list of visible buttons

        // Re-calculate max visible buttons if needed (e.g., if screen size changes dynamically)
        int buttonHeight = 20;
        int buttonSpacing = 5;
        int navPanelStartY = guiTop + 40; // This should match the Y you used to calculate navPanelStartY in mouseScrolled
        int navPanelAreaHeight = ySize - 10 - navPanelStartY; // Total height of the navigation area

        // This needs to be slightly adjusted if you have scroll buttons within this area that take up space
        // Let's assume navScrollUpButton and navScrollDownButton exist and are positioned outside the scrollable buttons list area.
        // If your scroll buttons are *within* the navPanelAreaHeight, adjust navButtonsAreaHeight to exclude them.
        int navButtonsDisplayHeight = navPanelAreaHeight - (buttonHeight + buttonSpacing) * 2; // Subtract space for scroll up/down buttons

        // Set to fixed value of 5 as requested
        maxVisibleNavButtons = 5;


        // 2. Ensure navScrollOffset is within valid bounds
        // This prevents scrolling past the end or before the beginning of the list.
        int maxScrollOffset = Math.max(0, allNavButtons.size() - maxVisibleNavButtons);
        navScrollOffset = Math.max(0, Math.min(navScrollOffset, maxScrollOffset));


        // 3. Add only the currently visible buttons to the screen
        int startIndex = navScrollOffset;
        int endIndex = Math.min(startIndex + maxVisibleNavButtons, allNavButtons.size());

        for (int i = startIndex; i < endIndex; i++) {
            net.minecraft.client.gui.components.Button button = allNavButtons.get(i);
            int buttonY = navPanelStartY + 25 + ((i - startIndex) * (buttonHeight + buttonSpacing)); // +25 for padding below header/scroll button
            button.setX(guiLeft + 35); // X position for the button
            button.setY(buttonY);     // Y position for the button
            button.visible = true;    // Make sure it's visible
            navigationButtons.add(button); // Add to our list of *currently visible* buttons
            this.addRenderableWidget(button); // Add to Minecraft's rendering list
        }

        // 4. Update the visibility and active state of scroll buttons
        // Make sure navScrollUpButton and navScrollDownButton are initialized in your init() method
        if (navScrollUpButton != null) {
            navScrollUpButton.active = navScrollOffset > 0;
            navScrollUpButton.visible = maxScrollOffset > 0; // Only visible if scrolling is possible
        }
        if (navScrollDownButton != null) {
            navScrollDownButton.active = navScrollOffset < maxScrollOffset;
            navScrollDownButton.visible = maxScrollOffset > 0; // Only visible if scrolling is possible
        }

        // 5. Highlight the current page button
        // Iterate through *all* navigation buttons to find the one corresponding to currentPage
        for (net.minecraft.client.gui.components.Button button : allNavButtons) {
            // Use a safer approach to compare buttons with pages
            // First, get the button text
            String buttonText = button.getMessage().getString();

            // Check if this button corresponds to the current page
            boolean isCurrentPageButton = false;

            // Check for Overview page
            if (currentPage == GuiPage.OVERVIEW_TASKS_PAGE && buttonText.equals("Overview")) {
                isCurrentPageButton = true;
            }
            // Check for Skill Trees page
            else if (currentPage == GuiPage.SKILL_TREES_PAGE && buttonText.equals("Skill Trees")) {
                isCurrentPageButton = true;
            }
            // Check for Settings page
            else if (currentPage == GuiPage.SETTINGS_PAGE && buttonText.equals("Settings")) {
                isCurrentPageButton = true;
            }
            // Check for aura pages by comparing display names
            else if (currentPage.isAuraPage() && buttonText.equals(currentPage.getDisplayName().getString())) {
                isCurrentPageButton = true;
            }

            // Set button state based on whether it's the current page
            button.active = !isCurrentPageButton; // Deactivate current page button, activate others
        }
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
        // Assuming 'x' here is the starting x-coordinate for the *additional icons*
        // after the checkbox and task name, NOT the start of the entire task row.

        // If x is still the start of the row, then adjust offsets:
        // int currentX = x + 150; // Start icons after the checkbox and task name
        // (This depends on where your checkbox and task name end)

        // Let's assume you've calculated an 'iconStartX' that's already past your task name.
        int currentX = x; // For example, if x is where the icons start after the task name

        // Render effort level - Place it first among the icons
        String effortLevel = "M"; // Default to medium
        String taskNameLower = task.getName().toLowerCase(); // Use this for checks
        if (taskNameLower.contains("quick") || taskNameLower.contains("small") || taskNameLower.contains("brief")) {
            effortLevel = "S"; // Small
        } else if (taskNameLower.contains("big") || taskNameLower.contains("large") || taskNameLower.contains("major")) {
            effortLevel = "L"; // Large
        }
        graphics.drawString(this.font, "[" + effortLevel + "]", currentX, y, 0x808080, false);
        currentX += this.font.width("[" + effortLevel + "]") + 5; // Move x for next icon

        // Render category icon - Now placed after effort
        String categoryIcon = "♥"; // Default to health
        int categoryColor = 0xFF0000; // Red for health
        if (taskNameLower.contains("read") || taskNameLower.contains("study") || taskNameLower.contains("learn")) {
            categoryIcon = "🧠"; // Intelligence
            categoryColor = 0x0000FF; // Blue
        } else if (taskNameLower.contains("meet") || taskNameLower.contains("call") || taskNameLower.contains("social")) {
            categoryIcon = "👥"; // Social
            categoryColor = 0xFF00FF; // Magenta
        } else if (taskNameLower.contains("clean") || taskNameLower.contains("organize") || taskNameLower.contains("desk")) {
            categoryIcon = "⚙️"; // Organization
            categoryColor = 0x00FF00; // Green
        } else if (taskNameLower.contains("music") || taskNameLower.contains("art") || taskNameLower.contains("hobby")) {
            categoryIcon = "🎵"; // Creative
            categoryColor = 0x00FFFF; // Cyan
        }
        graphics.drawString(this.font, categoryIcon, currentX, y, categoryColor, false);
        currentX += this.font.width(categoryIcon) + 5; // Move x for next icon

        // Render freshness indicator
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
        graphics.drawString(this.font, freshnessBar, currentX, y, freshnessColor, false);
        currentX += this.font.width(freshnessBar) + 5; // Move x for next icon

        // Draw "Done" button (visual only)
        String doneText = task.isCompleted() ? "[Done]" : "[Done]"; // Use "[Done]" for both states, color changes
        int doneColor = task.isCompleted() ? 0x00FF00 : 0x808080; // Green if done, gray if not
        graphics.drawString(this.font, doneText, currentX, y, doneColor, false);
    }

    private void rebuildTaskCheckboxes() {
        // Only rebuild if we're on the overview/tasks page
        if (currentPage != GuiPage.OVERVIEW_TASKS_PAGE) return; // Use GuiPage enum directly

        // --- 1. Clear Existing Widgets ---
        // Remove all current task-related widgets from the screen and their lists
        taskCheckboxes.forEach(this::removeWidget);
        taskCheckboxes.clear();
        taskEditButtons.forEach(this::removeWidget);
        taskEditButtons.clear();
        taskDeleteButtons.forEach(this::removeWidget);
        taskDeleteButtons.clear();

        // Remove task edit field and its associated buttons if they exist
        if (taskEditField != null) {
            this.removeWidget(taskEditField);
            taskEditField = null; // Clear reference
        }
        if (taskSaveButton != null) {
            this.removeWidget(taskSaveButton);
            taskSaveButton = null;
        }
        if (taskCancelButton != null) {
            this.removeWidget(taskCancelButton);
            taskCancelButton = null;
        }


        // --- 2. Calculate Layout and Scroll State ---
        // IMPORTANT: Align this Y position with your renderTasksPage's taskListY
        int taskListStartX = guiLeft + 120; // Matches taskListX from renderTasksPage
        int taskListStartY = guiTop + 75;  // <--- **FIXED Y-OFFSET HERE**
        int taskRowHeight = 24; // Height of each task row (checkbox + padding)

        // Calculate visible tasks range
        int startIndex = scrollOffset;
        int endIndex = Math.min(startIndex + maxVisibleTasks, tasks.size());

        // Update scroll buttons state
        boolean needsScrolling = tasks.size() > maxVisibleTasks;
        scrollUpButton.visible = needsScrolling;
        scrollDownButton.visible = needsScrolling;
        scrollUpButton.active = scrollOffset > 0;
        scrollDownButton.active = scrollOffset < Math.max(0, tasks.size() - maxVisibleTasks);


        // --- 3. Render Visible Tasks or Edit Field ---
        // Only show tasks in the visible range
        for (int i = startIndex; i < endIndex; i++) {
            final int taskIndex = i; // Need final for lambda
            Task task = tasks.get(taskIndex);

            // Calculate the Y position for the current task row
            int currentTaskRenderY = taskListStartY + ((i - startIndex) * taskRowHeight);

            // If we're editing THIS specific task, show the edit field instead of the checkbox/buttons
            if (editingTaskIndex == taskIndex) {
                // Create and add the EditBox for the current task
                taskEditField = new EditBox(this.font,
                        taskListStartX + 5, // A little padding from the left edge of list
                        currentTaskRenderY + 2, // Centered vertically
                        taskListStartX + (xSize - 130) - (25 + 25 + 10) - (taskListStartX + 5), // Width, accounting for buttons + padding
                        20,
                        Component.empty());
                taskEditField.setMaxLength(50); // Limit task name length
                taskEditField.setValue(task.getName());
                this.addRenderableWidget(taskEditField);
                taskEditField.setFocused(true); // Give focus to the edit field

                // Add Save Button
                taskSaveButton = Button.builder(Component.literal("✓"), (button) -> {
                    String newName = taskEditField.getValue().trim();
                    if (!newName.isEmpty()) {
                        Task oldTask = tasks.get(editingTaskIndex);
                        tasks.set(editingTaskIndex, new Task(newName, oldTask.isCompleted())); // Update task in list
                        net.amaha.habitmod.data.TaskManager.setTasks(tasks); // Save changes to TaskManager
                    }
                    editingTaskIndex = -1; // Exit edit mode
                    rebuildTaskCheckboxes(); // Rebuild to show checkboxes again
                }).pos(taskListStartX + (xSize - 130) - 50, currentTaskRenderY + 2).size(20, 20).build();
                this.addRenderableWidget(taskSaveButton);
                taskEditButtons.add(taskSaveButton); // Add to our list for cleaning

                // Add Cancel Button
                taskCancelButton = Button.builder(Component.literal("✖"), (button) -> {
                    editingTaskIndex = -1; // Exit edit mode without saving
                    rebuildTaskCheckboxes(); // Rebuild to show checkboxes again
                }).pos(taskListStartX + (xSize - 130) - 25, currentTaskRenderY + 2).size(20, 20).build();
                this.addRenderableWidget(taskCancelButton);
                taskDeleteButtons.add(taskCancelButton); // Add to our list for cleaning

            } else {
                // --- Normal Checkbox and Buttons for non-editing tasks ---

                // Checkbox takes (x, y, width, height, message, checked)
                // The message here is just for internal widget use;
                // The actual task name drawing will be in renderTasksPage for truncation control.
                Checkbox checkbox = new Checkbox(
                        taskListStartX + 5, // X position for the checkbox itself
                        currentTaskRenderY + 2, // Y position for the checkbox itself, +2 for vertical centering
                        20, // Standard checkbox width (the visual square part)
                        20, // Standard checkbox height
                        Component.empty(), // No text in the checkbox widget itself, we draw it separately
                        task.isCompleted()
                ) {
                    @Override
                    public void onClick(double pMouseX, double pMouseY) {
                        super.onClick(pMouseX, pMouseY);
                        boolean newCheckedState = this.selected();

                        if (newCheckedState && !task.isCompleted()) {
                            task.setCompleted(true);
                            net.amaha.habitmod.data.TaskManager.setTasks(tasks); // Save state

                            int auraGain = 20; // Example aura gain
                            net.amaha.habitmod.data.AuraManager.addAura(auraGain);

                            Player player = Minecraft.getInstance().player;
                            if (player != null) {
                                player.sendSystemMessage(Component.literal("You gained " + auraGain + " Aura points!"));
                                player.sendSystemMessage(Component.literal("Current Aura: " + net.amaha.habitmod.data.AuraManager.getAuraLevel()));
                            }

                            // Specific logic for "Workout" task
                            if (task.getName().equalsIgnoreCase("Workout")) {
                                if (player != null) {
                                    PacketHandler.sendToServer(new ApplyEffectPacket(MobEffects.JUMP, 60, 4, false, false));
                                    player.sendSystemMessage(Component.literal("You feel invigorated from your workout!"));
                                }
                            }
                        } else if (!newCheckedState && task.isCompleted()) {
                            // Allow unchecking, but for this concept, no aura point revocation
                            task.setCompleted(false);
                            net.amaha.habitmod.data.TaskManager.setTasks(tasks);
                        }
                        // No need to rebuild entirely here unless unchecking needs visual changes.
                        // If you have icons tied to completion, you might need to force a re-render.
                    }
                };
                taskCheckboxes.add(checkbox);
                this.addRenderableWidget(checkbox); // Add the checkbox to the screen

                // Edit and delete buttons are commented out for now, this will be implemented in the future
                /*
                // Add Rename (Edit) Button
                Button renameButton = Button.builder(Component.literal("✎"), (button) -> {
                    editingTaskIndex = taskIndex; // Set the index of the task to edit
                    rebuildTaskCheckboxes(); // Rebuild the GUI to show the edit field
                }).pos(taskListStartX + (xSize - 130) - 50, currentTaskRenderY + 2).size(20, 20).build();
                taskEditButtons.add(renameButton);
                this.addRenderableWidget(renameButton);

                // Add Delete Button
                Button deleteButton = Button.builder(Component.literal("✖"), (button) -> {
                    tasks.remove(taskIndex); // Remove from your data list
                    net.amaha.habitmod.data.TaskManager.setTasks(tasks); // Save changes

                    // Adjust scroll offset if deleted item would cause gap or go out of bounds
                    if (scrollOffset > 0 && tasks.size() <= maxVisibleTasks) { // If all tasks now fit
                        scrollOffset = 0; // Reset scroll
                    } else if (scrollOffset > 0 && scrollOffset >= tasks.size() - maxVisibleTasks && tasks.size() > 0) {
                        // If deleted from end and scroll was at max, adjust max
                        scrollOffset = Math.max(0, tasks.size() - maxVisibleTasks);
                    }
                    rebuildTaskCheckboxes(); // Rebuild the list after deletion
                }).pos(taskListStartX + (xSize - 130) - 25, currentTaskRenderY + 2).size(20, 20).build();
                taskDeleteButtons.add(deleteButton);
                this.addRenderableWidget(deleteButton);
                */
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

        if (currentPage == GuiPage.OVERVIEW_TASKS_PAGE) { // Direct comparison is correct
            renderTasksPage(graphics, mouseX, mouseY, partialTick);
        } else if (currentPage.isAuraPage()) { // Use the helper method
            renderAuraCategoryPage(graphics, mouseX, mouseY, partialTick);
        }
    }

    /**
     * Renders content specific to the tasks page
     */
    private void renderTasksPage(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // Draw central content area title (This position is good)
        String tasksTitle = "Task Management";
        int titleWidth = this.font.width(tasksTitle);
        graphics.drawString(this.font, tasksTitle, guiLeft + xSize / 2 - titleWidth / 2, guiTop + 15, 0x404040, false);

        // Draw filter and sort controls (MOVED UP)
        String filterLabel = "Filter: All";
        // Placed below "Task Management" title, with some padding
        graphics.drawString(this.font, filterLabel, guiLeft + 120, guiTop + 40, 0x808080, false); // New Y position: guiTop + 40

        String sortLabel = "Sort: Freshness";
        graphics.drawString(this.font, sortLabel, guiLeft + 220, guiTop + 40, 0x808080, false); // New Y position: guiTop + 40

        // Draw task list header
        int taskListX = guiLeft + 120; // This X position remains consistent for the main content block
        // NEW taskListY: Just below the legend (guiTop + 60) + 15px padding = guiTop + 75
        int taskListY = guiTop + 75;
        int taskListWidth = xSize - 130; // Remains the same

        // Draw task list background
        graphics.fill(taskListX, taskListY, taskListX + taskListWidth, taskListY + (maxVisibleTasks * 24), 0x22000000);

        // Calculate the FIXED starting X for the icons. (This logic remains correct)
        int fixedIconStartX = taskListX + CHECKBOX_WIDTH + CHECKBOX_TASK_NAME_PADDING + MAX_TASK_NAME_DISPLAY_WIDTH + TASK_NAME_ICONS_PADDING;

        // Render task items loop (remains the same, as taskY is relative to taskListY)
        int startIndex = scrollOffset;
        int endIndex = Math.min(startIndex + maxVisibleTasks, tasks.size());

        for (int i = startIndex; i < endIndex; i++) {
            Task task = tasks.get(i);
            int taskY = taskListY + ((i - startIndex) * 24) + 2; // Y position for the row

            // --- Render Truncated Task Name ---
            String taskName = task.getName();
            // Define the width available for the task name text, considering checkbox and icon space
            int availableTextWidth = (taskListX + taskListWidth) - (taskListX + CHECKBOX_WIDTH + CHECKBOX_TASK_NAME_PADDING + TASK_NAME_ICONS_PADDING) - 10; // -10 for right padding
            String displayedTaskName = this.font.plainSubstrByWidth(taskName, availableTextWidth);

            // Append "..." if the name was truncated
            if (!displayedTaskName.equals(taskName)) {
                // Adjust width to fit "..."
                displayedTaskName = this.font.plainSubstrByWidth(taskName, availableTextWidth - this.font.width("...")) + "...";
            }
            // Draw the task name.
            // Position: taskListX + CHECKBOX_WIDTH + CHECKBOX_TASK_NAME_PADDING
            graphics.drawString(this.font, displayedTaskName, taskListX + CHECKBOX_WIDTH + CHECKBOX_TASK_NAME_PADDING, taskY, 0xFFFFFF, false);

            // Render task icons (make sure fixedIconStartX is correctly calculated)
            renderTaskIcons(graphics, task, fixedIconStartX, taskY);
        }

        // Render scroll indicators if needed (positions are relative to taskListY, so they move with it)
        if (tasks.size() > maxVisibleTasks) {
            // ... (your existing scroll bar and task count rendering code) ...
            // The scroll bar's trackY is set to taskListY, so it will move up with the list.
            // The task count text's Y is relative to taskListY, so it will also move up.
        }

        // Draw category icons legend (This position remains unchanged in the top-right)
        int legendX = guiLeft + xSize - 150;
        int legendY = guiTop + 10;
        graphics.fill(legendX, legendY, legendX + 140, legendY + 50, 0x22000000);

        // Draw legend background
        graphics.fill(legendX, legendY, legendX + 140, legendY + 50, 0x22000000);

        // Legend title
        graphics.drawString(this.font, "Category Legend:", legendX + 5, legendY + 5, 0x404040, false);

        // Health icon - First row
        graphics.drawString(this.font, "♥", legendX + 10, legendY + 20, 0xFF0000, false);
        graphics.drawString(this.font, "Health", legendX + 20, legendY + 20, 0x808080, false);

        // Intelligence icon
        graphics.drawString(this.font, "🧠", legendX + 70, legendY + 20, 0x0000FF, false);
        graphics.drawString(this.font, "Intel", legendX + 80, legendY + 20, 0x808080, false);

        // Social icon - Second row
        graphics.drawString(this.font, "👥", legendX + 10, legendY + 30, 0xFF00FF, false);
        graphics.drawString(this.font, "Social", legendX + 20, legendY + 30, 0x808080, false);

        // Organization icon
        graphics.drawString(this.font, "✏️", legendX + 70, legendY + 30, 0x00FF00, false);
        graphics.drawString(this.font, "Org", legendX + 80, legendY + 30, 0x808080, false);

        // Creative icon - Third row
        graphics.drawString(this.font, "🎵", legendX + 10, legendY + 40, 0x00FFFF, false);
        graphics.drawString(this.font, "Creative", legendX + 20, legendY + 40, 0x808080, false);
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
        if (currentPage == null) return;

        Minecraft mc = Minecraft.getInstance();
        Font font = mc.font;

        int auraLevel = currentPage.getAuraLevel();
        String categoryName = currentPage.getDisplayName().getString();
        int categoryColor = currentPage.getCategoryColor();
        ItemStack categoryIcon = currentPage.getIconStack();

        // Title text
        String auraText = categoryName + " Aura: " + auraLevel;
        int titleWidth = font.width(auraText);
        graphics.drawString(font, Component.literal(auraText),
                (this.width / 2) - (titleWidth / 2),
                28,
                categoryColor,
                false
        );

        // Progress bar bounds
        int barX = this.width / 2 - 102;
        int barY = 42;
        int barWidth = 204;
        int barHeight = 10;

        // Progress bar background
        graphics.fill(barX, barY, barX + barWidth, barY + barHeight, 0xFFAAAAAA);

        // Progress bar fill
        float fillRatio = Math.min(1.0f, auraLevel / 200.0f); // Assuming 200 max
        int filled = (int)(barWidth * fillRatio);
        graphics.fill(barX, barY, barX + filled, barY + barHeight, categoryColor | 0xFF000000);

        // Category icon
        graphics.renderItem(categoryIcon, barX - 20, barY - 3);

        // Scrollable content bounds
        int contentAreaStartX = this.width / 2 - 102;
        int contentAreaEndX = this.width / 2 + 102;
        int contentAreaWidth = contentAreaEndX - contentAreaStartX;
        int contentAreaStartY = 60;
        int contentAreaEndY = this.height - 40;

        // Enable scissor for scroll clipping
        enableScissor(contentAreaStartX, contentAreaStartY, contentAreaEndX, contentAreaEndY);

        int currentRenderY = contentAreaStartY - (auraCategoryScrollOffset * font.lineHeight);

        // --- SECTION: Current Tier ---
        Component tierStatusComponent = Component.literal(categoryName + " Aura Status: Tier 51-100 (Awakened)"); // <--- CHANGE HERE
        currentRenderY += renderWrappedText(graphics, tierStatusComponent, contentAreaStartX + 5, currentRenderY, contentAreaWidth - 10, 0x404040); // <--- CHANGE HERE
        currentRenderY += 5;
        graphics.fill(contentAreaStartX + 5, currentRenderY, contentAreaEndX - 5, currentRenderY + 1, 0x33000000);
        currentRenderY += 10;

        // --- SECTION: Benefits ---
        graphics.drawString(font, Component.literal("Benefits:"), contentAreaStartX + 5, currentRenderY, 0x404040, false); // <--- CHANGE HERE
        currentRenderY += 15;
        // Iterate over Component objects
        for (Component benefit : currentPage.getBenefits()) { // <--- CHANGE HERE: String to Component
            renderWrappedText(graphics, benefit, contentAreaStartX + 5, currentRenderY, contentAreaWidth - 10, categoryColor);
            currentRenderY += getWrappedTextHeight(graphics, benefit, contentAreaWidth - 10);
        }
        currentRenderY += 5;
        graphics.fill(contentAreaStartX + 5, currentRenderY, contentAreaEndX - 5, currentRenderY + 1, 0x33000000);
        currentRenderY += 10;

        // --- SECTION: Next Tier Benefits ---
        // Convert String to Component before passing
        Component nextTierTextComponent = Component.literal("Next Unlock at 101 " + categoryName + " Aura:"); // <--- CHANGE HERE
        renderWrappedText(graphics, nextTierTextComponent, contentAreaStartX + 5, currentRenderY, contentAreaWidth - 10, 0x404040); // <--- CHANGE HERE
        currentRenderY += getWrappedTextHeight(graphics, nextTierTextComponent, contentAreaWidth - 10); // <--- CHANGE HERE
        // Iterate over Component objects
        for (Component benefit : currentPage.getNextTierBenefits()) { // <--- CHANGE HERE: String to Component
            renderWrappedText(graphics, benefit, contentAreaStartX + 5, currentRenderY, contentAreaWidth - 10, 0x808080);
            currentRenderY += getWrappedTextHeight(graphics, benefit, contentAreaWidth - 10);
        }
        currentRenderY += 5;
        graphics.fill(contentAreaStartX + 5, currentRenderY, contentAreaEndX - 5, currentRenderY + 1, 0x33000000);
        currentRenderY += 10;

        // --- SECTION: Associated Tasks ---
        // Convert String to Component before passing
        Component associatedTasksTitle = Component.literal("Associated Tasks:"); // <--- CHANGE HERE
        renderWrappedText(graphics, associatedTasksTitle, contentAreaStartX + 5, currentRenderY, contentAreaWidth - 10, 0x404040); // <--- CHANGE HERE
        currentRenderY += getWrappedTextHeight(graphics, associatedTasksTitle, contentAreaWidth - 10); // <--- CHANGE HERE
        // Iterate over Component objects
        for (Component task : currentPage.getAssociatedTasks()) { // <--- CHANGE HERE: String to Component
            renderWrappedText(graphics, task, contentAreaStartX + 5, currentRenderY, contentAreaWidth - 10, 0x808080);
            currentRenderY += getWrappedTextHeight(graphics, task, contentAreaWidth - 10);
        }

        // Disable scissor
        disableScissor();

        // --- Scrollbar Rendering (Optional) ---
        int contentHeight = calculateAuraCategoryContentHeight();
        if (contentHeight > (contentAreaEndY - contentAreaStartY)) {
            renderScrollbar(graphics, contentAreaEndX + 2, contentAreaStartY, contentAreaEndY, contentHeight);
        }
    }
// Inside ChronicleScreen.java

    // This method is a placeholder to be removed - we'll use the implementation below

    // Ensure you have this helper method that calculates wrapped text height
    /**
     * Calculates the height a string would consume if wrapped within maxLineWidth.
     */
    private int getWrappedTextHeight(Font fontRenderer, String text, int maxWidth) {
        // Cast to FormattedText for compatibility
        @SuppressWarnings("unchecked")
        List<FormattedText> wrappedLines = (List<FormattedText>)(Object)fontRenderer.split(Component.literal(text), maxWidth);
        return wrappedLines.size() * fontRenderer.lineHeight;
    }

    /**
     * Helper method to get wrapped text height from GuiGraphics context
     */
// Inside your ChronicleScreen.java

    /**
     * Calculates the height consumed by a Component when wrapped within a given width.
     * This method now correctly accepts a Component.
     */
    private int renderWrappedText(GuiGraphics graphics, Component textComponent, int x, int y, int maxLineWidth, int color) {
        int startY = y;

        // font.split returns List<FormattedCharSequence>
        List<FormattedCharSequence> wrappedLines = this.font.split(textComponent, maxLineWidth);

        for (FormattedCharSequence line : wrappedLines) { // Iterate over FormattedCharSequence directly
            // graphics.drawString accepts FormattedCharSequence (since it implements FormattedText)
            graphics.drawString(this.font, line, x, y, color, false);
            y += this.font.lineHeight;
        }
        return y - startY; // Return total height consumed
    }

    // Your overloaded getWrappedTextHeight that calls the main one (optional, but convenient)
    // This should also accept Component and delegate correctly.
    private int getWrappedTextHeight(net.minecraft.client.gui.Font fontRenderer, net.minecraft.network.chat.Component textComponent, int maxWidth) {
        if (fontRenderer == null) return 0;
        List<net.minecraft.util.FormattedCharSequence> wrappedLines = fontRenderer.split(textComponent, maxWidth);
        return wrappedLines.size() * fontRenderer.lineHeight;
    }

    // Your overloaded getWrappedTextHeight that calls the main one (optional, but convenient)
// This version should also accept Component if you use it for Components.
// If you only use the (Font, Component, int) version, you might not need this.
// Assuming this is just a convenience wrapper for internal calls:
    private int getWrappedTextHeight(GuiGraphics graphics, net.minecraft.network.chat.Component textComponent, int maxWidth) {
        return getWrappedTextHeight(this.font, textComponent, maxWidth);
    }

    // This duplicate method is removed

    /**
     * Enables scissor test for rendering within a specific area
     */
    private void enableScissor(int x, int y, int width, int height) {
        // In newer Minecraft versions, scissor is handled by GuiGraphics
        // This is a placeholder method that would be implemented with RenderSystem.enableScissor
        // in a real implementation
    }

    /**
     * Disables scissor test
     */
    private void disableScissor() {
        // In newer Minecraft versions, scissor is handled by GuiGraphics
        // This is a placeholder method that would be implemented with RenderSystem.disableScissor
        // in a real implementation
    }

    /**
     * Renders a scrollbar
     */
    private void renderScrollbar(GuiGraphics graphics, int x, int y, int height, int contentHeight) {
        // Simple scrollbar rendering
        int scrollbarHeight = Math.max(20, height * height / contentHeight);
        int scrollbarY = y + (int)((height - scrollbarHeight) * (auraCategoryScrollOffset / (float)(contentHeight - height)));

        // Draw scrollbar background
        graphics.fill(x, y, x + 6, y + height, 0x33000000);

        // Draw scrollbar handle
        graphics.fill(x, scrollbarY, x + 6, scrollbarY + scrollbarHeight, 0x66FFFFFF);
    }

    /**
     * Calculates the total height of content for the current aura category page
     */
    private int calculateAuraCategoryContentHeight() {
        // Call the parameterized version with the current page and font
        return calculateAuraCategoryContentHeight(currentPage, this.font, xSize - 150);
    }
    /**
     * Calculates the total theoretical height of all content on the Aura Category page.
     * This is crucial for accurate scroll bar calculation.
     */
    private int calculateAuraCategoryContentHeight(GuiPage currentPage, Font font, int contentWidth) {
        int totalHeight = 0; // Renamed to totalHeight for clarity
        // Get the category icon from the current page
        String categoryIcon = currentPage.getCategoryIcon();

        // 1. Current Tier Status
        // This text might be dynamic, but for height calculation, use its representative length
        totalHeight += getWrappedTextHeight(font, currentPage.name() + " Aura Status: Tier 51-100 (Awakened)", contentWidth);
        totalHeight += 5; // Padding after section

        // Separator
        totalHeight += 1;
        totalHeight += 10; // Padding after separator

        // 2. Active Benefits Title
        totalHeight += getWrappedTextHeight(font, "Benefits:", contentWidth);
        totalHeight += font.lineHeight; // Add height for the title's line height (assuming one line)
        // You added 15 earlier, so ensure consistency, let's use the line height + a fixed padding
        totalHeight += 5; // Padding after title (adjust this if you had a bigger padding before)

        // 3. Active Benefits (Dynamically fetch from enum)
        List<net.minecraft.network.chat.Component> activeBenefits = currentPage.getBenefits();
        for (net.minecraft.network.chat.Component benefit : activeBenefits) { // Iterating over Component
            totalHeight += getWrappedTextHeight(font, benefit, contentWidth);
        }

        // Separator
        totalHeight += 5;
        totalHeight += 1;
        totalHeight += 10;

        // 4. Next Tier Benefits Title
        totalHeight += getWrappedTextHeight(font, "Next Unlock at 101 " + currentPage.name() + " Aura:", contentWidth); // Use page's name for dynamic text
        totalHeight += font.lineHeight; // Adjust this if you had 15 before for a fixed line height + padding
        totalHeight += 5; // Padding after title

        // 5. Next Tier Benefits (Dynamically fetch from enum)
        List<net.minecraft.network.chat.Component> nextTierBenefits = currentPage.getNextTierBenefits();
        for (net.minecraft.network.chat.Component benefit : nextTierBenefits) { // Iterating over Component
            totalHeight += getWrappedTextHeight(font, benefit, contentWidth);
        }

        // Separator
        totalHeight += 5;
        totalHeight += 1;
        totalHeight += 10;

        // 6. Associated Tasks Title
        totalHeight += getWrappedTextHeight(font, "Associated Tasks:", contentWidth);
        totalHeight += font.lineHeight; // Adjust this if you had 15 before
        totalHeight += 5; // Padding after title

        // 7. Associated Tasks (Dynamically fetch from enum)
        List<net.minecraft.network.chat.Component> associatedTasks = currentPage.getAssociatedTasks(); // Iterating over Component
        for (net.minecraft.network.chat.Component task : associatedTasks) {
            totalHeight += getWrappedTextHeight(font, task, contentWidth);
        }

        // Add any final padding at the very bottom of the content area
        totalHeight += 10;

        return totalHeight;
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
