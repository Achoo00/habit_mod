package net.amaha.habitmod.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the saving and loading of tasks.
 */
@Mod.EventBusSubscriber
public class TaskManager {
    // NBT tag names
    private static final String TASKS_TAG = "HabitModTasks";
    private static final String TASK_NAME_TAG = "Name";
    private static final String TASK_COMPLETED_TAG = "Completed";
    private static final String TASKS_VERSION_TAG = "TasksVersion";

    // In-memory cache of tasks
    private static List<Task> tasks = new ArrayList<>();

    // Flag to track if tasks have been loaded
    private static boolean tasksLoaded = false;

    // Version flag to force reinitialization of tasks
    private static final int TASKS_VERSION = 1;

    /**
     * Gets the list of tasks.
     * @return The list of tasks.
     */
    public static List<Task> getTasks() {
        return tasks;
    }

    /**
     * Sets the list of tasks.
     * @param newTasks The new list of tasks.
     */
    public static void setTasks(List<Task> newTasks) {
        tasks = new ArrayList<>(newTasks);
    }

    /**
     * Adds a task to the list.
     * @param task The task to add.
     */
    public static void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * Saves the tasks to the player's data.
     * @param player The player to save the tasks for.
     */
    public static void saveTasks(Player player) {
        if (player.level().isClientSide()) {
            return; // Don't save on client side
        }

        ServerPlayer serverPlayer = (ServerPlayer) player;
        CompoundTag persistentData = serverPlayer.getPersistentData();

        // Create a new compound tag for our mod's data
        CompoundTag modData = new CompoundTag();

        // Create a list tag to store all tasks
        ListTag tasksList = new ListTag();

        // Add each task to the list
        for (Task task : tasks) {
            CompoundTag taskTag = new CompoundTag();
            taskTag.putString(TASK_NAME_TAG, task.getName());
            taskTag.putBoolean(TASK_COMPLETED_TAG, task.isCompleted());
            tasksList.add(taskTag);
        }

        // Add the tasks list to our mod data
        modData.put(TASKS_TAG, tasksList);

        // Add the tasks version to our mod data
        modData.putInt(TASKS_VERSION_TAG, TASKS_VERSION);

        // Save our mod data to the player's persistent data
        persistentData.put(TASKS_TAG, modData);
    }

    /**
     * Loads the tasks from the player's data.
     * @param player The player to load the tasks for.
     */
    public static void loadTasks(Player player) {
        if (player.level().isClientSide() || tasksLoaded) {
            return; // Don't load on client side or if already loaded
        }

        tasks.clear();

        CompoundTag persistentData = player.getPersistentData();

        // Check if our mod data exists
        boolean shouldLoadDefaultTasks = true;

        if (persistentData.contains(TASKS_TAG, Tag.TAG_COMPOUND)) {
            CompoundTag modData = persistentData.getCompound(TASKS_TAG);

            // Check if the tasks version matches
            int savedVersion = modData.getInt(TASKS_VERSION_TAG);
            if (savedVersion == TASKS_VERSION) {
                // Check if the tasks list exists
                if (modData.contains(TASKS_TAG, Tag.TAG_LIST)) {
                    ListTag tasksList = modData.getList(TASKS_TAG, Tag.TAG_COMPOUND);

                    // Load each task from the list
                    for (int i = 0; i < tasksList.size(); i++) {
                        CompoundTag taskTag = tasksList.getCompound(i);
                        String name = taskTag.getString(TASK_NAME_TAG);
                        boolean completed = taskTag.getBoolean(TASK_COMPLETED_TAG);

                        tasks.add(new Task(name, completed));
                    }

                    // If we successfully loaded tasks, don't load default tasks
                    if (!tasks.isEmpty()) {
                        shouldLoadDefaultTasks = false;
                    }
                }
            }
            // If version doesn't match, we'll load default tasks
        }

        // If no tasks were loaded or version doesn't match, add default tasks for each category
        if (shouldLoadDefaultTasks) {
            // Health tasks
            tasks.add(new Task("Workout", false));
            tasks.add(new Task("Journaling", false));
            tasks.add(new Task("Scalp massage", false));
            tasks.add(new Task("Rosemary oil", false));
            tasks.add(new Task("Floss", false));
            tasks.add(new Task("Stretch", false));

            // Organization and productivity tasks
            tasks.add(new Task("Plan your day", false));
            tasks.add(new Task("Declutter a space", false));
            tasks.add(new Task("Create or manage to-do list", false));
            tasks.add(new Task("Make bed", false));
            tasks.add(new Task("Clear digital clutter (emails, files)", false));

            // Social tasks
            tasks.add(new Task("Socialize with friends", false));
            tasks.add(new Task("Text a friend you haven't spoken to in a while", false));
            tasks.add(new Task("Socialize at work", false));
            tasks.add(new Task("Extracurricular or activities", false));

            // Intelligence tasks
            tasks.add(new Task("Coding", false));
            tasks.add(new Task("Read", false));
            tasks.add(new Task("Weekly meeting or self reflecting", false));
            tasks.add(new Task("Practice French", false));
        }

        tasksLoaded = true;
    }

    /**
     * Event handler for player login to load tasks.
     * @param event The player logged in event.
     */
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();

        // Load tasks when player logs in
        if (!player.level().isClientSide()) {
            loadTasks(player);
        }
    }

    /**
     * Event handler for player logout to save tasks.
     * @param event The player logged out event.
     */
    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        Player player = event.getEntity();

        // Save tasks when player logs out
        if (!player.level().isClientSide()) {
            saveTasks(player);
        }
    }
}
