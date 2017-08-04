package xilef11.mc.runesofwizardry_classics.runes;

import java.io.IOException;

import com.zpig333.runesofwizardry.core.rune.PatternUtils;
import com.zpig333.runesofwizardry.runes.inscription.RuneChargeInscription;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import xilef11.mc.runesofwizardry_classics.Config;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.RunesofWizardry_Classics;

public class RuneInscriptionEnchant extends RuneChargeInscription {
	private ItemStack[][] pattern;
	
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.runes.inscription.RuneChargeInscription#getName()
	 */
	@Override
	public String getName() {
		return Refs.MODID+".rune.classicinscriptionenchant";
	}
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
					RunesofWizardry_Classics.log().fatal("Could not set up pattern",e);
				}
			}
			return pattern;
		}else{
			try {
				return setupPattern();
			} catch (IOException e) {
				RunesofWizardry_Classics.log().fatal("Could not set up pattern",e);
				return new ItemStack[4][4];
			}
		}
	}
	protected ItemStack[][] setupPattern() throws IOException{
		return PatternUtils.importFromJson(new ResourceLocation(Refs.PATTERN_PATH+"runeclassicchargeinscription.json"));
	}
	
}
