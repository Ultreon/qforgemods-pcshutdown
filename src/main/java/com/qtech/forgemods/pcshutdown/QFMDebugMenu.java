package com.qtech.forgemods.pcshutdown;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.qsoftware.modlib.api.annotations.FieldsAreNonnullByDefault;
import com.qtech.forgemods.core.QFMCore;
import com.qtech.forgemods.core.QFMCorePlugin;
import com.qtech.forgemods.core.QFMVersion;
import com.qtech.forgemods.core.common.ModuleManager;
import com.qtech.forgemods.core.internal.QfmArgs;
import com.qtech.forgemods.core.plugins.AbstractPluginManager;
import com.qtech.forgemods.core.plugins.QFMCorePluginManager;
import lombok.Getter;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

/**
 * The main mod class.
 * Here's everything started.
 *
 * @author Qboi123
 * @see #QFMDebugMenu()
 * @see Mod
 * @see Mod.EventBusSubscriber
 * @see QFMCore
 */
@SuppressWarnings("unused")
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@FieldsAreNonnullByDefault
@Mod(QFMDebugMenu.modId)
@Mod.EventBusSubscriber(modid = QFMDebugMenu.modId, bus = Mod.EventBusSubscriber.Bus.MOD)
public class QFMDebugMenu implements QFMCorePlugin {
    /**
     * QForgeMod's Logger
     */
    public static final Logger LOGGER = LogManager.getLogger("QFM:Generic");

    // Mod Data
    @Getter public static final String modId = "qfm_pcshutdown";
    @Getter public static final String modName = "Qboi's Forge Mods: PC Shutdown";
    @Getter public static final String nbtName = "QFMCore";
    @Getter public static final String modVersion;
    @Getter public static final QFMVersion version;
    @Getter private static final QfmArgs modArgs;

    public static final DebugMenuModule DEBUG_MENU_MODULE = new DebugMenuModule();
    public static final boolean MOD_TEST_PHASE = false;

    static {
        if (new File("/mnt/chromeos").exists()) {
            throw new UnsupportedOperationException("Tried to run QForgeMod on Chrome OS (Linux subsystem), this is unsupported.");
        }

        // Create gson instance.
        Gson gson = new Gson();

        // Get stream.
        InputStream qfmArgsStream = QFMDebugMenu.class.getResourceAsStream("/META-INF/qfm_args.json");
        Objects.requireNonNull(qfmArgsStream, "Couldn't get QFM Args file.");

        // Get data.
        InputStreamReader isr = new InputStreamReader(qfmArgsStream);
        JsonObject o = gson.fromJson(isr, JsonObject.class);

        modArgs = new QfmArgs(o);
        modVersion = modArgs.getVersion().getName();
        version = modArgs.getVersion().toVersionObject();
    }

    // Getters
    @Getter private final IEventBus modEventBus;

    /**
     * The QForgeUtils constructor for mod-loading.
     *
     * @since 2.0.1386-b1
     * @see Mod
     * @see QFMDebugMenu
     */
    public QFMDebugMenu() {
        ModuleManager.getInstance().register(DEBUG_MENU_MODULE);
        QFMCorePluginManager.get().registerPlugin(this);

        // Final fields.
        this.modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    }

    /**
     * Get the resource location based on QForgeMod's id.
     *
     * @param path the resource path.
     * @return a resource location.
     */
    public static ResourceLocation rl(String path) {
        return new ResourceLocation(modId, path);
    }

    /**
     * Internal method.
     *
     * @return boolean.
     */
    @OnlyIn(Dist.CLIENT)
    private static boolean isDevState0() {
        return Minecraft.getInstance().getVersion().equals("MOD_DEV");
    }

    /**
     * Check if QForgeMod is currently a development build.
     *
     * @return the QFM dev-state..
     */
    public static boolean isModDev() {
        try {
            return isDevState0();
        } catch (NoSuchMethodError error) {
            return false;
        }
    }

    /**
     * Check if QForgeMod is currently a development build.
     *
     * @param def the default value if failed to detect development mode.
     * @return the QFM dev-state..
     */
    public static boolean isModDev(boolean def) {
        try {
            return isDevState0();
        } catch (NoSuchMethodError error) {
            return def;
        }
    }

    /**
     * Check test phase.
     *
     * @return true if QForgeMod is in test phase, false otherwise.
     */
    public static boolean isTestPhase() {
        return isModDev() || MOD_TEST_PHASE;
    }

    /**
     * @return true if QForgeMod is a dev test version, false otherwise.
     */
    public static boolean isDevtest() {
        return modArgs.getFlags().isDevTest();
    }

    @Override
    public int getCoreMinBuild() {
        return 1363;
    }

    @Override
    public int getCoreMaxBuild() {
        return 1363;
    }

    @Nullable
    @Override
    public AbstractPluginManager<QFMCorePlugin> getPluginManager() {
        return null;
    }
}