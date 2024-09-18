package world.maryt.out_of_combat;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(OutOfCombat.MODID)
public class OutOfCombat {
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();

    public static final String MODID = "out_of_combat";

    public static boolean DEBUG;
    public static long noAttackingTimeThreshold;
    public static long noAttackedTimeThreshold;
    public static long outOfCombatTimeThreshold;

    public OutOfCombat() {
        MinecraftForge.EVENT_BUS.register(this);

        // Configs
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        DEBUG = Config.DEBUG.get();
        noAttackingTimeThreshold = Config.NO_ATTACKING_TIME_THRESHOLD.get();
        noAttackedTimeThreshold = Config.NO_ATTACKED_TIME_THRESHOLD.get();
        outOfCombatTimeThreshold = Config.OUT_OF_COMBAT_THRESHOLD.get();
    }
}
