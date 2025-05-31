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
    private static final int TASKS_PAGE = 0;
    private static final int TIERS_PAGE = 1;

    // Current page being displayed
    private int currentPage = TASKS_PAGE;

    private EditBox taskInputField;
    private Button addTaskButton;
    private List<Task> tasks = new ArrayList<>(); // Use our Task data class
    private List<Checkbox> taskCheckboxes = new ArrayList<>(); // To manage checkboxes

    // Navigation buttons
    private Button nextPageButton;
    private Button prevPageButton;

    // Add aura display
    private net.amaha.habitmod.data.AuraManager auraManager;

    private int xSize = 256;
    private int ySize = 200; // Make GUI slightly taller to fit more tasks
    private int guiLeft;
    private int guiTop;

    // Scrolling variables
    private int scrollOffset = 0; // Current scroll position
    private int maxVisibleTasks = 6; // Maximum number of tasks visible at once
    private Button scrollUpButton;
    private Button scrollDownButton;

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

        // --- Add Navigation Buttons (visible on both pages) ---
        nextPageButton = Button.builder(Component.literal("Next Page >>"), (button) -> {
            currentPage = TIERS_PAGE;
            clearWidgets();
            init(); // Reinitialize the GUI with the new page
        }).pos(guiLeft + xSize - 90, guiTop + 10).size(80, 20).build();

        prevPageButton = Button.builder(Component.literal("<< Prev Page"), (button) -> {
            currentPage = TASKS_PAGE;
            clearWidgets();
            init(); // Reinitialize the GUI with the new page
        }).pos(guiLeft + 10, guiTop + 10).size(80, 20).build();

        // Initialize the appropriate page
        if (currentPage == TASKS_PAGE) {
            initTasksPage();
        } else {
            initTiersPage();
        }
    }

    /**
     * Initializes the tasks page with task management widgets
     */
    private void initTasksPage() {
        // Add navigation button (only next page on tasks page)
        this.addRenderableWidget(nextPageButton);

        // --- Task Input and Add Button ---
        taskInputField = new EditBox(this.font, guiLeft + 10, guiTop + 40, 150, 20, Component.empty());
        taskInputField.setMaxLength(50);
        this.addRenderableWidget(taskInputField);

        addTaskButton = Button.builder(Component.literal("Add Task"), (button) -> {
            String taskName = taskInputField.getValue();
            if (!taskName.isEmpty()) {
                Task newTask = new Task(taskName, false);
                tasks.add(newTask);
                TaskManager.addTask(newTask); // Add to TaskManager
                taskInputField.setValue(""); // Clear input field
                rebuildTaskCheckboxes(); // Rebuild the checkboxes after adding a task
            }
        }).pos(guiLeft + 170, guiTop + 40).size(70, 20).build();
        this.addRenderableWidget(addTaskButton);

        // --- Add Scroll Buttons ---
        scrollUpButton = Button.builder(Component.literal("▲"), (button) -> {
            if (scrollOffset > 0) {
                scrollOffset--;
                rebuildTaskCheckboxes();
            }
        }).pos(guiLeft + xSize - 30, guiTop + 70).size(20, 20).build();
        this.addRenderableWidget(scrollUpButton);

        scrollDownButton = Button.builder(Component.literal("▼"), (button) -> {
            if (scrollOffset < Math.max(0, tasks.size() - maxVisibleTasks)) {
                scrollOffset++;
                rebuildTaskCheckboxes();
            }
        }).pos(guiLeft + xSize - 30, guiTop + 70 + (maxVisibleTasks * 24)).size(20, 20).build();
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
        // Add navigation button (only prev page on tiers page)
        this.addRenderableWidget(prevPageButton);

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

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        // Only handle scrolling if we're on the tasks page
        if (currentPage != TASKS_PAGE) {
            return super.mouseScrolled(mouseX, mouseY, delta);
        }

        // Only handle scrolling if mouse is over the task list area
        int taskListStartX = guiLeft + 10;
        int taskListEndX = guiLeft + xSize - 10;
        int taskListStartY = guiTop + 70; // Adjusted for navigation button
        int taskListEndY = guiTop + 70 + (maxVisibleTasks * 24);

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
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    // Helper method to rebuild the list of checkboxes
    private void rebuildTaskCheckboxes() {
        // Only rebuild if we're on the tasks page
        if (currentPage != TASKS_PAGE) return;

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

        int yOffset = guiTop + 70; // Starting Y position for task list (adjusted for navigation button)

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
            Checkbox checkbox = new Checkbox(
                    guiLeft + 10, yOffset,
                    xSize - 100, 20, // Reduced width to make room for buttons
                    Component.literal(task.getName()),
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
                taskEditField = new EditBox(this.font, guiLeft + 10, finalYOffset, xSize - 100, 20, Component.empty());
                taskEditField.setMaxLength(50);
                taskEditField.setValue(task.getName());
                this.addRenderableWidget(taskEditField);

                // Rebuild to hide the checkbox and show the edit field
                rebuildTaskCheckboxes();
            }).pos(guiLeft + xSize - 80, yOffset).size(30, 20).build();
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
            }).pos(guiLeft + xSize - 45, yOffset).size(30, 20).build();
            this.addRenderableWidget(deleteButton);

            yOffset += 24; // Spacing for next task (checkbox height + padding)
        }

        // If we're in edit mode, show the edit field and hide the checkbox
        if (editingTaskIndex >= 0 && editingTaskIndex < tasks.size()) {
            // Only create the edit field if it doesn't exist
            if (taskEditField == null) {
                int editYOffset = guiTop + 70 + ((editingTaskIndex - scrollOffset) * 24); // Adjusted for navigation button

                // Create edit field
                taskEditField = new EditBox(this.font, guiLeft + 10, editYOffset, xSize - 100, 20, Component.empty());
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
                }).pos(guiLeft + xSize - 80, editYOffset).size(30, 20).build();
                this.addRenderableWidget(saveButton);

                // Add cancel button
                Button cancelButton = Button.builder(Component.literal("✖"), (button) -> {
                    // Exit edit mode without saving
                    editingTaskIndex = -1;

                    // Rebuild the checkboxes
                    rebuildTaskCheckboxes();
                }).pos(guiLeft + xSize - 45, editYOffset).size(30, 20).build();
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

        // Render screen title
        String pageTitle = this.title.getString();
        if (currentPage == TIERS_PAGE) {
            pageTitle += " - Aura Tiers";
        } else {
            pageTitle += " - Tasks";
        }
        graphics.drawString(this.font, pageTitle, guiLeft + xSize / 2 - font.width(pageTitle) / 2, guiTop + 5, 0x404040, false);

        // Render page-specific content
        if (currentPage == TASKS_PAGE) {
            renderTasksPage(graphics, mouseX, mouseY, partialTick);
        } else {
            renderTiersPage(graphics, mouseX, mouseY, partialTick);
        }

        // Render aura information (shown on both pages)
        int auraLevel = net.amaha.habitmod.data.AuraManager.getAuraLevel();
        int tier = net.amaha.habitmod.data.AuraManager.getCurrentTier();
        String tierName = getTierName(tier);

        // Display aura level
        String auraText = "Aura Level: " + auraLevel;
        graphics.drawString(this.font, auraText, guiLeft + 10, guiTop + ySize - 30, 0x00FF00, false);

        // Display tier
        String tierText = "Current Tier: " + tier + " - " + tierName;
        graphics.drawString(this.font, tierText, guiLeft + 10, guiTop + ySize - 20, getTierColor(tier), false);
    }

    /**
     * Renders content specific to the tasks page
     */
    private void renderTasksPage(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
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
            int trackY = guiTop + 70;

            // Draw track background
            graphics.fill(trackX, trackY, trackX + 5, trackY + trackHeight, 0x33000000);

            // Draw scroll thumb
            int thumbHeight = Math.max(20, (int)(percentVisible * trackHeight));
            int thumbY = trackY + (int)((trackHeight - thumbHeight) * percentScrolled);
            graphics.fill(trackX, thumbY, trackX + 5, thumbY + thumbHeight, 0x66FFFFFF);

            // Show task count
            String taskCountText = scrollOffset + 1 + "-" + Math.min(scrollOffset + maxVisibleTasks, tasks.size()) + " of " + tasks.size();
            graphics.drawString(this.font, taskCountText, guiLeft + 10, guiTop + 70 + (maxVisibleTasks * 24) + 5, 0x808080, false);
        }
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
