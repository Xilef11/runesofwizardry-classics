package xilef11.mc.runesofwizardry_classics.runes;

import java.io.IOException;

import org.apache.logging.log4j.Level;

import xilef11.mc.runesofwizardry_classics.Config;
import xilef11.mc.runesofwizardry_classics.ModLogger;
import net.minecraft.item.ItemStack;

import com.zpig333.runesofwizardry.runes.inscription.RuneChargeInscription;

public class RuneInscriptionEnchant extends RuneChargeInscription {
	private ItemStack[][] pattern;
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IRune#getPattern()
	 */
	@Override
	public ItemStack[][] getPattern() {
		if(Config.cacheRuneInfo){
			if(pattern==null){
				try {
					pattern=setupPattern();
				} catch (IOException e) {
					ModLogger.logException(Level.FATAL, e, "Could not set up pattern");
				}
			}
			return pattern;
		}else{
			try {
				return setupPattern();
			} catch (IOException e) {
				ModLogger.logException(Level.FATAL, e, "Could not set up pattern");
				return new ItemStack[4][4];
			}
		}
	}
	protected ItemStack[][] setupPattern() throws IOException{
		return null;
	}
	
}
