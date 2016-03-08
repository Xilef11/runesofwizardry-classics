/**
 * 
 */
package xilef11.mc.runesofwizardry_classics;

import xilef11.mc.runesofwizardry_classics.runes.RuneTorch;

import com.zpig333.runesofwizardry.api.DustRegistry;

/** Registers all the runes in the mod
 * @author Xilef11
 *
 */
public class ModRunes {
	public static void registerRunes(){
		DustRegistry.registerRune(new RuneTorch());
	}
}
