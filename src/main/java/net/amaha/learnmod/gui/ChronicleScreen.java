// src/main/java/net/amaha/learnmod/gui/ChronicleScreen.java
package net.amaha.learnmod.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.amaha.learnmod.LearnMod; // Import your minimal main mod class
import net.amaha.learnmod.data.Task; // Import the Task data class
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

@OnlyIn(Dist.CLIENT)
public class ChronicleScreen extends Screen {
    // A simple placeholder texture. For a real mod, you'd create your own.
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(LearnMod.MOD_ID, "textures/gui/chronicle_background.png");

    private EditBox taskInputField;
    private Button addTaskButton;
    private List<Task> tasks = new ArrayList<>(); // Use our Task data class
    private List<Checkbox> taskCheckboxes = new ArrayList<>(); // To manage checkboxes

    private int xSize = 256;
    private int ySize = 200; // Make GUI slightly taller to fit more tasks
    private int guiLeft;
    private int guiTop;

    public ChronicleScreen() {
        super(Component.translatable("gui.learnmod.chronicle.title"));
        // Initialize with some default tasks
        tasks.add(new Task("Workout", false));
        tasks.add(new Task("Read a book", false));
        tasks.add(new Task("Clean room", false));
        tasks.add(new Task("Study Java", false));
    }

    @Override
    protected void init() {
        super.init();
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;

        // --- Task Input and Add Button ---
        taskInputField = new EditBox(this.font, guiLeft + 10, guiTop + 10, 150, 20, Component.empty());
        taskInputField.setMaxLength(50);
        this.addRenderableWidget(taskInputField);

        addTaskButton = Button.builder(Component.literal("Add Task"), (button) -> {
            String taskName = taskInputField.getValue();
            if (!taskName.isEmpty()) {
                tasks.add(new Task(taskName, false));
                taskInputField.setValue(""); // Clear input field
                rebuildTaskCheckboxes(); // Rebuild the checkboxes after adding a task
            }
        }).pos(guiLeft + 170, guiTop + 10).size(70, 20).build();
        this.addRenderableWidget(addTaskButton);

        // --- Initialize and Rebuild Task Checkboxes ---
        rebuildTaskCheckboxes();
    }

    // Helper method to rebuild the list of checkboxes
    private void rebuildTaskCheckboxes() {
        // Clear existing checkboxes from the screen
        taskCheckboxes.forEach(this::removeWidget);
        taskCheckboxes.clear();

        int yOffset = guiTop + 40; // Starting Y position for task list
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            // Checkbox takes (x, y, width, height, message, checked)
            Checkbox checkbox = new Checkbox(
                    guiLeft + 10, yOffset,
                    xSize - 20, 20, // Full width of the content area
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

                        // --- Specific logic for "Workout" task ---
                        if (task.getName().equalsIgnoreCase("Workout")) {
                            Player player = Minecraft.getInstance().player; // Correct way to get the player on client
                            if (player != null) {
                                // Apply speed boost (10% faster for 30 seconds)
                                // Amplifier 0 is +10%, Amplifier 1 is +20%, etc.
                                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 600, 0, false, false));
                                player.sendSystemMessage(Component.literal("You feel invigorated from your workout!"));
                            }
                        }
                    } else if (!newCheckedState && task.isCompleted()) {
                        // Optional: if you want to allow unchecking and revoking effects
                        task.setCompleted(false); // Mark task as uncompleted
                        // No effect revocation for simplicity in this basic concept
                    }
                }
            };
            taskCheckboxes.add(checkbox);
            this.addRenderableWidget(checkbox); // Add the checkbox to the screen
            yOffset += 24; // Spacing for next task (checkbox height + padding)
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
        graphics.drawString(this.font, this.title, guiLeft + xSize / 2 - font.width(this.title) / 2, guiTop + 5, 0x404040, false);
    }

    @Override
    public boolean isPauseScreen() {
        return false; // Allows game to continue while GUI is open
    }

    @Override
    public void onClose() {
        // This is called when the GUI is closed (e.g., by pressing ESC)
        // In a real mod, you'd save your 'tasks' list here to persistent storage
        super.onClose();
    }
}