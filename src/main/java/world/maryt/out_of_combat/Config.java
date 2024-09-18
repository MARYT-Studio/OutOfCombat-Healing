package world.maryt.out_of_combat;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = OutOfCombat.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec.BooleanValue DEBUG;

    public static final ForgeConfigSpec.LongValue NO_ATTACKED_TIME_THRESHOLD;
    public static final ForgeConfigSpec.LongValue NO_ATTACKING_TIME_THRESHOLD;
    public static final ForgeConfigSpec.LongValue OUT_OF_COMBAT_THRESHOLD;

    static {
        BUILDER.comment("General settings").push("DEBUG");
        DEBUG = BUILDER.comment("Enable this for debugging purpose").define("DEBUG", false);
        BUILDER.pop();

        BUILDER.comment("General settings").push("General Settings");
        NO_ATTACKED_TIME_THRESHOLD = BUILDER.comment("Out-of-combat timer only counts after Not-being-attacked timer counts more than this value.")
                .defineInRange("noAttackedTimeThreshold",
                        Long.MAX_VALUE,
                        0,
                        Long.MAX_VALUE);
        NO_ATTACKING_TIME_THRESHOLD = BUILDER.comment("Out-of-combat timer only counts after No-attacking timer counts more than this value.")
                .defineInRange("noAttackingTimeThreshold",
                        Long.MAX_VALUE,
                        0,
                        Long.MAX_VALUE);
        OUT_OF_COMBAT_THRESHOLD = BUILDER.comment("When Out-of-combat timer counts exceeds this value, the player is considered out of combat.")
                .defineInRange("outOfCombatTimeThreshold",
                        Long.MAX_VALUE,
                        0,
                        Long.MAX_VALUE);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
