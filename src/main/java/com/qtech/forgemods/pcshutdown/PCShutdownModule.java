package com.qtech.forgemods.pcshutdown;

import com.qsoftware.modlib.api.annotations.FieldsAreNonnullByDefault;
import com.qtech.forgemods.core.Modules;
import com.qtech.forgemods.core.QFMCore;
import com.qtech.forgemods.core.client.gui.modules.ModuleCompatibility;
import com.qtech.forgemods.core.common.Module;
import com.qtech.forgemods.core.common.ModuleManager;
import com.qtech.forgemods.core.common.ModuleSecurity;
import com.qtech.forgemods.core.modules.actionmenu.AbstractActionMenu;
import com.qtech.forgemods.core.modules.actionmenu.MainActionMenu;
import com.qtech.forgemods.core.modules.actionmenu.MenuHandler;
import com.qtech.forgemods.core.modules.pcShutdown.ComputerMenu;
import com.qtech.forgemods.core.modules.pcShutdown.ConfirmShutdownScreen;
import com.qtech.forgemods.core.util.ComputerUtils;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import javax.annotation.ParametersAreNonnullByDefault;

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PCShutdownModule extends Module {
    private static final AbstractActionMenu computerMenu = new ComputerMenu();

    public PCShutdownModule() {
        MainActionMenu.registerHandler(new MenuHandler(new StringTextComponent("Computer"), computerMenu, this::enableMenu));
    }

    private boolean enableMenu() {
        return ModuleManager.getInstance().isEnabled(Modules.PC_SHUTDOWN) || ModuleManager.getInstance().isEnabled(Modules.PC_CRASH);
    }

    @Override
    public void onEnable() {
        if (QFMCore.isClientSide()) {
            MinecraftForge.EVENT_BUS.register(this);
        }
    }

    @Override
    public void onDisable() {
        if (QFMCore.isClientSide()) {
            MinecraftForge.EVENT_BUS.unregister(this);
        }
    }

    @Override
    public ModuleSecurity getSecurity() {
        return ModuleSecurity.RISC;
    }

    @Override
    public boolean canDisable() {
        return true;
    }

    @Override
    public @NotNull String getName() {
        return "pc_shutdown";
    }

    @Override
    public @NotNull ModuleCompatibility getCompatibility() {
        if (QFMCore.isClientSide()) {
            return ModuleCompatibility.FULL;
        } else if (QFMCore.isServerSide()) {
            return ModuleCompatibility.PARTIAL;
        } else {
            return ModuleCompatibility.NONE;
        }
    }

    @Override
    public boolean isDefaultEnabled() {
        return false;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        long win = mc.getMainWindow().getHandle();

        boolean k1 = InputMappings.isKeyDown(win, GLFW.GLFW_KEY_LEFT_CONTROL);
        boolean k2 = InputMappings.isKeyDown(win, GLFW.GLFW_KEY_RIGHT_CONTROL);
        boolean k3 = InputMappings.isKeyDown(win, GLFW.GLFW_KEY_LEFT_ALT);
        boolean k4 = InputMappings.isKeyDown(win, GLFW.GLFW_KEY_RIGHT_ALT);
        boolean k5 = InputMappings.isKeyDown(win, GLFW.GLFW_KEY_INSERT);

        if ((k1 || k2) && (k3 || k4) && k5 && !(mc.currentScreen instanceof ConfirmShutdownScreen)) {
            ComputerUtils.shutdown();
        }
    }
}
