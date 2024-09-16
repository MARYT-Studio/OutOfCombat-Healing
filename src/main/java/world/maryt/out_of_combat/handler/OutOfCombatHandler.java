package world.maryt.out_of_combat.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static world.maryt.out_of_combat.OutOfCombat.*;
import static world.maryt.out_of_combat.Tags.MOD_ID;

public class OutOfCombatHandler {

    private static NBTTagCompound getOutOfCombatData(EntityPlayer player) {
        NBTTagCompound forgeData = player.getEntityData();
        if (!(forgeData.hasKey("PlayerPersisted"))) {
            forgeData.setTag("PlayerPersisted", new NBTTagCompound());
        }
        NBTTagCompound persistedData = forgeData.getCompoundTag("PlayerPersisted");
        if (!(persistedData.hasKey(MOD_ID))) {
            persistedData.setTag(MOD_ID, new NBTTagCompound());
        }
        return persistedData.getCompoundTag(MOD_ID);
    }

    // Clear when player is attacking
    // Event counts only if it is finally fired without being cancelled.
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onPlayerAttacking(LivingAttackEvent event) {
        if (event.getSource().getTrueSource() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
            NBTTagCompound outOfCombatData = getOutOfCombatData(player);
            outOfCombatData.setLong("noAttackingTime", 0L);
            if (DEBUG) LOGGER.debug("noAttackingTime cleared.");
            outOfCombatData.setLong("outOfCombatTime", 0L);
            if (DEBUG) LOGGER.debug("outOfCombatTime cleared.");
        }
    }


    // Clear when player is being attacked
    // Event counts only if it is finally fired without being cancelled.
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onPlayerAttacked(LivingDamageEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            NBTTagCompound outOfCombatData = getOutOfCombatData(player);
            outOfCombatData.setLong("noAttackedTime", 0L);
            if (DEBUG) LOGGER.debug("noAttackedTime cleared.");
            outOfCombatData.setLong("outOfCombatTime", 0L);
            if (DEBUG) LOGGER.debug("outOfCombatTime cleared.");
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (event.side.isServer()) {
                NBTTagCompound outOfCombatData = getOutOfCombatData(event.player);

                long newNoAttackingTime = outOfCombatData.getLong("noAttackingTime") + 1;
                long newNoAttackedTime = outOfCombatData.getLong("noAttackedTime") + 1;

                outOfCombatData.setLong("noAttackingTime", newNoAttackingTime);
                outOfCombatData.setLong("noAttackedTime", newNoAttackedTime);

                // Stop Out-of-combat Timer.
                // If this timer has not been initialized, it will be initialized with a 0 value.
                // If this timer has got a zero value, this line effectively do nothing.
                // If this timer has a nonzero value, it will function as a countdown timer.
                outOfCombatData.setLong("stopOutOfCombatTimer", Long.max(0L, outOfCombatData.getLong("stopOutOfCombatTimer") - 1L));
                if (DEBUG) LOGGER.debug("stopOutOfCombatTimer: {}, It should be set to: {}",
                        outOfCombatData.getLong("stopOutOfCombatTimer"),
                        Long.max(0L, outOfCombatData.getLong("stopOutOfCombatTimer") - 1L));

                if (outOfCombatData.getLong("noAttackingTime") >= noAttackingTimeThreshold &&
                        outOfCombatData.getLong("noAttackedTime") >= noAttackedTimeThreshold &&
                        outOfCombatData.getLong("stopOutOfCombatTimer") <= 0L
                ) {
                    long newOutOfCombatTime = outOfCombatData.getLong("outOfCombatTime") + 1L;
                    outOfCombatData.setLong("outOfCombatTime", newOutOfCombatTime);
                    if (DEBUG && outOfCombatData.getLong("outOfCombatTime") >= outOfCombatTimeThreshold)
                        LOGGER.info("Player is out of combat for {} ticks.",
                                outOfCombatData.getLong("outOfCombatTime") - outOfCombatTimeThreshold);
                } else {
                    if (DEBUG) {
                        LOGGER.debug("Out-of-combat timer's ticking is stopped due to: noAttackingTime = {}, noAttackedTime = {}, stopOutOfCombatTimer = {}.",
                                outOfCombatData.getLong("noAttackingTime"),
                                outOfCombatData.getLong("noAttackedTime"),
                                outOfCombatData.getLong("stopOutOfCombatTimer")
                        );
                    }
                }

                // Debug info here
                if (DEBUG) LOGGER.debug("noAttackingTime: {}/{}, noAttackedTime: {}/{}, outOfCombatTime: {}/{} (Current/Threshold).",
                        outOfCombatData.getLong("noAttackingTime"), noAttackingTimeThreshold,
                        outOfCombatData.getLong("noAttackedTime"), noAttackedTimeThreshold,
                        outOfCombatData.getLong("outOfCombatTime"), outOfCombatTimeThreshold
                );
            }
        }
    }
}
