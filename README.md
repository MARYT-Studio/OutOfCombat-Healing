## Out of Combat

![](https://i.imgur.com/ffUX75U.png)

## Overview

Out of Combat mod.

As what its name represents, it brings a simple but easily configurable Out-of-Combat mechanic into Minecraft. 

Simple means its logic and mechanic are both simple:

(`T1, T2, T3` are independent variables, representing number of Minecraft game ticks)

1. Detect if player has not attacked for a given time `T1` / been damaged for a given time `T2`.
2. If player has neither attacked for `T1` nor been damaged for `T2`, start to count the out-of-combat time.

   If player attacks / is damaged, clear the corresponding timer and the out-of-combat timer.
3. If out-of-combat time exceeded `T3`, player is out of combat.

3 NBT tags updated by events serve as timers, to fulfill the logic above. 

Besides, an additional countdown timer will pause the ticking of the out-of-combat timer, but not clear it.
It can be used in other combat mechanics.

For example, assuming player can get "Invulnerable"-like abilities in your modpack.
when those abilities are activated, they may still in combat, but will not be damaged.
If you would like to do so, you may let this countdown timer countdown for the time equals to the abilities' durations. 
So that the out-of-combat timer will wait for the abilities to deactivate, and continues after that.

## Documents for Usage

### Config File

```
debug {
    # Enable this for debugging purpose
    B:Debug=true
}

general {
    # Out-of-combat timer only counts after Not-being-attacked timer counts more than this value.
    I:noAttackedTimeThreshold=214738467

    # Out-of-combat timer only counts after No-attacking timer counts more than this value.
    D:noAttackingTimeThreshold=214738467

    # When Out-of-combat timer counts exceeds this value, the player is considered out of combat.
    I:outOfCombatTimeThreshold=214738467
}
```

It is well documented in its comments.

Default values are large, means this mod are not effective until you modify this config file.

### NBT Names and Structure

These 4 NBT tags are saved in player's persisted data: `ForgeData/PlayerPersisted`, with the structure below.

```
ForgeData: {
    PlayerPersisted: {
        out_of_combat: {
            noAttackingTime: 0L         // No Attacking Timer
            noAttackedTime: 0L          // Not Being Damaged Timer
            stopOutOfCombatTimer: 0L    // Countdown. Before This is count to 0, Out-of-Combat timer will not tick.
            outOfCombatTime: 0L         // Out of Combat Timer
        }
    }
}
```

That means, you can easily manipulate the value of each of them
with your mods or CraftTweaker/KubeJS scripts or any other scripts like them.

**No need for complex APIs or special integrations/compatibilities.**

___________

## Future Plans

Support other Minecraft versions.
