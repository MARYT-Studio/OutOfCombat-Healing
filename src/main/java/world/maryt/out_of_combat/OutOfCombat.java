package world.maryt.out_of_combat;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import world.maryt.out_of_combat.handler.OutOfCombatHandler;

import java.io.File;

@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION)
public class OutOfCombat {

    public static final Logger LOGGER = LogManager.getLogger(Tags.MOD_NAME);

    public static long noAttackingTimeThreshold = Long.MAX_VALUE;
    public static long noAttackedTimeThreshold = Long.MAX_VALUE;
    public static long outOfCombatTimeThreshold = Long.MAX_VALUE;

    public static boolean DEBUG = false;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent preEvent) {
        Configuration config = new Configuration(new File(Loader.instance().getConfigDir(), Tags.MOD_ID + ".cfg"));
        try {
            config.load();
            // Here should be Long.MAX_VALUE, but it will be read in a double form "9.xxE18" and throw an exception.
            // Int.MAX will also make this mod have no effect as default. Use it till a better way is found.
            {
                Property property = config.get(Configuration.CATEGORY_GENERAL, "noAttackingTimeThreshold", Integer.MAX_VALUE);
                property.setComment("Out-of-combat timer only counts after No-attacking timer counts more than this value.");
                OutOfCombat.noAttackingTimeThreshold = property.getLong();
                property.setShowInGui(true);
            }
            {
                Property property = config.get(Configuration.CATEGORY_GENERAL, "noAttackedTimeThreshold", Integer.MAX_VALUE);
                property.setComment("Out-of-combat timer only counts after Not-being-attacked timer counts more than this value.");
                OutOfCombat.noAttackedTimeThreshold = property.getLong();
                property.setShowInGui(true);
            }
            {
                Property property = config.get(Configuration.CATEGORY_GENERAL, "outOfCombatTimeThreshold", Integer.MAX_VALUE);
                property.setComment("When Out-of-combat timer counts exceeds this value, the player is considered out of combat.");
                OutOfCombat.outOfCombatTimeThreshold = property.getLong();
                property.setShowInGui(true);
            }
            {
                Property property = config.get("Debug", "Debug", false);
                property.setComment("Enable this for debugging purpose");
                OutOfCombat.DEBUG = property.getBoolean();
                property.setShowInGui(false);
            }

            LOGGER.info("No Mob Friendly Fire - configuration loaded.");
        } finally {
            config.save();
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new OutOfCombatHandler());
    }
}
