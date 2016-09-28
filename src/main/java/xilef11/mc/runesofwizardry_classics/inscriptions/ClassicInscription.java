package xilef11.mc.runesofwizardry_classics.inscriptions;

import java.io.IOException;

import net.minecraft.item.ItemStack;

import org.apache.logging.log4j.Level;

import xilef11.mc.runesofwizardry_classics.Config;
import xilef11.mc.runesofwizardry_classics.ModLogger;

import com.zpig333.runesofwizardry.api.Inscription;

public abstract class ClassicInscription extends Inscription{
	private ItemStack[][] pattern;
	private ItemStack[] sacrifice;
	
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
	protected abstract ItemStack[][] setupPattern() throws IOException;
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.Inscription#getChargeItems()
	 */
	@Override
	public ItemStack[] getChargeItems() {
		if(Config.cacheRuneInfo){
			if(sacrifice==null){
				sacrifice=setupChargeItems();
			}
			return sacrifice;
		}else{
			return setupChargeItems();
		}
	}
	
	protected abstract ItemStack[] setupChargeItems();
	
}
