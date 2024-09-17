package world.maryt.out_of_combat;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = OutOfCombat.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue DEBUG = BUILDER.comment("Enable this for debugging purpose").define("DEBUG", false);

    private static final ForgeConfigSpec.LongValue NO_ATTACKED_TIME_THRESHOLD = BUILDER.comment("Out-of-combat timer only counts after Not-being-attacked timer counts more than this value.")
            .defineInRange("noAttackedTimeThreshold",
                    Long.MAX_VALUE,
                    0,
                    Long.MAX_VALUE);
    private static final ForgeConfigSpec.LongValue NO_ATTACKING_TIME_THRESHOLD = BUILDER.comment("Out-of-combat timer only counts after No-attacking timer counts more than this value.")
            .defineInRange("noAttackingTimeThreshold",
                    Long.MAX_VALUE,
                    0,
                    Long.MAX_VALUE);
    private static final ForgeConfigSpec.LongValue OUT_OF_COMBAT_THRESHOLD = BUILDER.comment("When Out-of-combat timer counts exceeds this value, the player is considered out of combat.")
            .defineInRange("outOfCombatTimeThreshold",
                    Long.MAX_VALUE,
                    0,
                    Long.MAX_VALUE);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean debug;
    public static long noAttackingTimeThreshold;
    public static long noAttackedTimeThreshold;
    public static long outOfCombatTimeThreshold;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        debug = DEBUG.get();
        noAttackedTimeThreshold = NO_ATTACKED_TIME_THRESHOLD.get();
        noAttackingTimeThreshold = NO_ATTACKING_TIME_THRESHOLD.get();
        outOfCombatTimeThreshold = OUT_OF_COMBAT_THRESHOLD.get();
    }
}
