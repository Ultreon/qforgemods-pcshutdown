package com.qtech.forgemods.pcshutdown;

import com.qtech.forgemods.core.Modules;
import com.qtech.forgemods.core.common.ModuleManager;
import com.qtech.forgemods.core.modules.actionmenu.AbstractActionMenu;
import com.qtech.forgemods.core.modules.actionmenu.IActionMenuItem;
import com.qtech.forgemods.core.modules.debugMenu.DebugMenu;
import com.qtech.forgemods.core.util.ComputerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class ComputerMenu extends AbstractActionMenu {
    public ComputerMenu() {
        addItem(new IActionMenuItem() {
            @Override
            public void onActivate() {
                ComputerUtils.shutdown();
            }

            @Override
            public ITextComponent getText() {
                return new StringTextComponent("Shutdown");
            }

            @Override
            public boolean isEnabled() {
                return ModuleManager.getInstance().isEnabled(Modules.PC_SHUTDOWN);
            }
        });
        addItem(new IActionMenuItem() {
            @Override
            public void onActivate() {
                ComputerUtils.hibernate();
            }

            @Override
            public ITextComponent getText() {
                return new StringTextComponent("Hibernate");
            }

            @Override
            public boolean isEnabled() {
                return ModuleManager.getInstance().isEnabled(Modules.PC_SHUTDOWN) && ComputerUtils.supportsHibernate();
            }
        });
        addItem(new IActionMenuItem() {
            @Override
            public void onActivate() {
                ComputerUtils.reboot();
            }

            @Override
            public ITextComponent getText() {
                return new StringTextComponent("Reboot");
            }

            @Override
            public boolean isEnabled() {
                return ModuleManager.getInstance().isEnabled(Modules.PC_SHUTDOWN) && ComputerUtils.supportsReboot();
            }
        });
        addItem(new IActionMenuItem() {
            @Override
            public void onActivate() {
                ComputerUtils.crash();
            }

            @Override
            public ITextComponent getText() {
                return new StringTextComponent("Crash");
            }

            @Override
            public boolean isEnabled() {
                return ModuleManager.getInstance().isEnabled(Modules.PC_CRASH) && ComputerUtils.supportsCrash();
            }
        });
        addItem(new IActionMenuItem() {
            @Override
            public void onActivate() {
                DebugMenu.DEBUG_PAGE = DebugMenu.PAGE.COMPUTER;
            }

            @Override
            public ITextComponent getText() {
                return new StringTextComponent("Set Debug Page");
            }

            @Override
            public boolean isEnabled() {
                Minecraft mc = Minecraft.getInstance();
                return mc.player != null && mc.world != null && mc.playerController != null;
            }
        });
    }
}
