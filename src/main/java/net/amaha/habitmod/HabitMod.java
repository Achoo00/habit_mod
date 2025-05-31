// src/main/java/net/amaha/habitmod/HabitMod.java
package net.amaha.habitmod;

import com.mojang.blaze3d.platform.InputConstants;
import net.amaha.habitmod.gui.ChronicleScreen;
import net.amaha.habitmod.network.PacketHandler;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.glfw.GLFW;

@Mod(HabitMod.MOD_ID)
public class HabitMod {
    public static final String MOD_ID = "habitmod"; // Your mod ID

    public static KeyMapping OPEN_GUI_KEY;

    public HabitMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the client setup event for keybindings
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::registerKeyMappings);
        modEventBus.addListener(this::commonSetup); // <--- NEW: Register common setup
        MinecraftForge.EVENT_BUS.addListener(HabitMod::onClientTick);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // <--- NEW: Register networking here in common setup
        event.enqueueWork(PacketHandler::register);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        // This runs on the client thread
        event.enqueueWork(() -> {
            // No specific initial setup needed here, keybinding will open GUI
        });
    }

    private void registerKeyMappings(RegisterKeyMappingsEvent event) {
        OPEN_GUI_KEY = new KeyMapping("key.habitmod.open_gui", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_O, "key.category.habitmod.gui");
        event.register(OPEN_GUI_KEY);
    }

    // You'll also need an event handler to open the GUI when the key is pressed.
    // This is usually done with @SubscribeEvent, but for simplicity, we'll put it here.
    // However, events usually go in separate classes for cleaner code.
    // For a minimal test, we'll make a static method and subscribe it.
    public static void onClientTick(net.minecraftforge.event.TickEvent.ClientTickEvent event) {
        if (event.phase == net.minecraftforge.event.TickEvent.Phase.END) {
            if (OPEN_GUI_KEY.consumeClick()) { // Check if the key was pressed
                Minecraft.getInstance().setScreen(new ChronicleScreen());
            }
        }
    }
    // You need to register this method to the MinecraftForge.EVENT_BUS
    // In HabitMod constructor: MinecraftForge.EVENT_BUS.addListener(HabitMod::onClientTick);
    // You need to add `import net.minecraftforge.common.MinecraftForge;` to HabitMod
    // And also ensure Minecraft.getInstance().player is not null before applying effects
    // However, if you are just testing the GUI for display, you might just call setScreen
    // directly in your runClient. For actual in-game testing, the keybinding is better.
}