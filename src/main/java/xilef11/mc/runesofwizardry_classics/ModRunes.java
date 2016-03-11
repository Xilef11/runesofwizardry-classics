/**
 * 
 */
package xilef11.mc.runesofwizardry_classics;

import xilef11.mc.runesofwizardry_classics.runes.*;

import com.zpig333.runesofwizardry.api.DustRegistry;


/** Registers all the runes in the mod
 * @author Xilef11
 *
 */
public class ModRunes {
	public static void registerRunes(){
		DustRegistry.registerRune(new RuneTorch());
		DustRegistry.registerRune(new RuneRabbitHole());
		DustRegistry.registerRune(new RuneHealing());
		DustRegistry.registerRune(new RuneLumber());
		DustRegistry.registerRune(new RuneFire());
		DustRegistry.registerRune(new RuneDepths());
		DustRegistry.registerRune(new RuneHeights());
		DustRegistry.registerRune(new RuneFarm());
		DustRegistry.registerRune(new RuneLeapingFrog());
		DustRegistry.registerRune(new RuneDawn());
		//auto-inserted
		DustRegistry.registerRune(new RuneDusk());
		DustRegistry.registerRune(new RuneTrapFire());
		DustRegistry.registerRune(new RuneTrapLightning());
		DustRegistry.registerRune(new RuneTrapPoison());
		DustRegistry.registerRune(new RuneDetonation());
		DustRegistry.registerRune(new RuneEntrapment());
		DustRegistry.registerRune(new RuneLockedTime());
		DustRegistry.registerRune(new RuneVoid());
		DustRegistry.registerRune(new RuneBarrier());
		DustRegistry.registerRune(new RuneWisdom());
		DustRegistry.registerRune(new RuneSpeed());
		DustRegistry.registerRune(new RuneCompression());
		DustRegistry.registerRune(new RuneHellstorm());
		DustRegistry.registerRune(new RuneRebirth());
		DustRegistry.registerRune(new RuneResurrection());
		DustRegistry.registerRune(new RunePowerDistribution());
		DustRegistry.registerRune(new RuneSpawnerCollection());
		DustRegistry.registerRune(new RuneSpawnerReassignment());
		DustRegistry.registerRune(new RuneTeleportation());
		DustRegistry.registerRune(new RuneTransport());
		DustRegistry.registerRune(new RuneSpriteFire());
		DustRegistry.registerRune(new RuneSpriteEarth());
		DustRegistry.registerRune(new RuneBouncing());
		DustRegistry.registerRune(new RuneMusic());
		DustRegistry.registerRune(new RuneSpiritTools());
		DustRegistry.registerRune(new RuneEnchantingFireBow());
		DustRegistry.registerRune(new RuneEnchantingSilkTouch());
		DustRegistry.registerRune(new RuneEnchantingFortune());
		DustRegistry.registerRune(new RuneProtection());
		DustRegistry.registerRune(new RuneLevelEarth());
		DustRegistry.registerRune(new RuneMountain());
		DustRegistry.registerRune(new RuneSarlacc());
	}
}