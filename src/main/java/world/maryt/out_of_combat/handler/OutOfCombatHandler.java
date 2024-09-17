package world.maryt.out_of_combat.handler;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static world.maryt.out_of_combat.Config.*;
import static world.maryt.out_of_combat.OutOfCombat.MODID;
import static world.maryt.out_of_combat.OutOfCombat.LOGGER;

public class OutOfCombatHandler {
    private static CompoundTag getOutOfCombatData(Player player) {
        CompoundTag forgeData = player.getPersistentData();

        if (!(forgeData.contains("PlayerPersisted"))) {
            forgeData.put("PlayerPersisted", new CompoundTag());
        }
        CompoundTag persistedData = forgeData.getCompound("PlayerPersisted");
        if (!(persistedData.contains(MODID))) {
            persistedData.put(MODID, new CompoundTag());
        }
        return persistedData.getCompound(MODID);
    }

    // Clear when player is attacking
    // Event counts only if it is finally fired without being cancelled.
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPlayerAttacking(LivingAttackEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {
            CompoundTag outOfCombatData = getOutOfCombatData(player);
            outOfCombatData.putLong("noAttackingTime", 0L);
            if (debug) LOGGER.debug("noAttackingTime cleared.");
            outOfCombatData.putLong("outOfCombatTime", 0L);
            if (debug) LOGGER.debug("outOfCombatTime cleared.");
        }
    }


    // Clear when player is being attacked
    // Event counts only if it is finally fired without being cancelled.
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPlayerAttacked(LivingDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            CompoundTag outOfCombatData = getOutOfCombatData(player);
            outOfCombatData.putLong("noAttackedTime", 0L);
            if (debug) LOGGER.debug("noAttackedTime cleared.");
            outOfCombatData.putLong("outOfCombatTime", 0L);
            if (debug) LOGGER.debug("outOfCombatTime cleared.");
        }
    }

    // On death, clear all other timers, but save the "stop" countdown timer.
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player) {
            CompoundTag outOfCombatData = getOutOfCombatData(player);
            outOfCombatData.putLong("noAttackingTime", 0L);
            outOfCombatData.putLong("noAttackedTime", 0L);
            outOfCombatData.putLong("outOfCombatTime", 0L);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent.Post event) {
        if (event.player.isAlive() && event.side.isServer()) {

            CompoundTag outOfCombatData = getOutOfCombatData(event.player);

            long newNoAttackingTime = outOfCombatData.getLong("noAttackingTime") + 1;
            long newNoAttackedTime = outOfCombatData.getLong("noAttackedTime") + 1;

            outOfCombatData.putLong("noAttackingTime", newNoAttackingTime);
            outOfCombatData.putLong("noAttackedTime", newNoAttackedTime);

            // Stop Out-of-combat Timer.
            // If this timer has not been initialized, it will be initialized with a 0 value.
            // If this timer has got a zero value, this line effectively do nothing.
            // If this timer has a nonzero value, it will function as a countdown timer.
            outOfCombatData.putLong("stopOutOfCombatTimer", Long.max(0L, outOfCombatData.getLong("stopOutOfCombatTimer") - 1L));
            if (debug) LOGGER.debug("stopOutOfCombatTimer: {}, It should be set to: {}",
                    outOfCombatData.getLong("stopOutOfCombatTimer"),
                    Long.max(0L, outOfCombatData.getLong("stopOutOfCombatTimer") - 1L));

            if (outOfCombatData.getLong("noAttackingTime") >= noAttackingTimeThreshold &&
                    outOfCombatData.getLong("noAttackedTime") >= noAttackedTimeThreshold &&
                    outOfCombatData.getLong("stopOutOfCombatTimer") <= 0L
            ) {
                long newOutOfCombatTime = outOfCombatData.getLong("outOfCombatTime") + 1L;
                outOfCombatData.putLong("outOfCombatTime", newOutOfCombatTime);
                if (debug && outOfCombatData.getLong("outOfCombatTime") >= outOfCombatTimeThreshold)
                    LOGGER.info("Player is out of combat for {} ticks.",
                            outOfCombatData.getLong("outOfCombatTime") - outOfCombatTimeThreshold);
            } else {
                if (debug) {
                    LOGGER.debug("Out-of-combat timer's ticking is stopped due to: noAttackingTime = {}, noAttackedTime = {}, stopOutOfCombatTimer = {}.",
                            outOfCombatData.getLong("noAttackingTime"),
                            outOfCombatData.getLong("noAttackedTime"),
                            outOfCombatData.getLong("stopOutOfCombatTimer")
                    );
                }
            }

            // Debug info here
            if (debug) {
                LOGGER.debug("noAttackingTime: {}/{}, noAttackedTime: {}/{}, outOfCombatTime: {}/{} (Current/Threshold).",
                        outOfCombatData.getLong("noAttackingTime"), noAttackingTimeThreshold,
                        outOfCombatData.getLong("noAttackedTime"), noAttackedTimeThreshold,
                        outOfCombatData.getLong("outOfCombatTime"), outOfCombatTimeThreshold
                );
            }

        }
    }
}