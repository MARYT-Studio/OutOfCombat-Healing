package world.maryt.out_of_combat;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.slf4j.Logger;
import world.maryt.out_of_combat.handler.OutOfCombatHandler;

@Mod(OutOfCombat.MODID)
public class OutOfCombat {

    public static final String MODID = "out_of_combat";
    public static final Logger LOGGER = LogUtils.getLogger();

    public OutOfCombat() {
        // Event handlers' registrations must be in the constructor of the main class
        MinecraftForge.EVENT_BUS.register(OutOfCombatHandler.class);
        // Configuration building
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}
