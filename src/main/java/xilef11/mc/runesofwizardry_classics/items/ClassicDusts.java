/**
 * 
 */
package xilef11.mc.runesofwizardry_classics.items;

import net.minecraft.item.ItemStack;

import com.zpig333.runesofwizardry.api.IDust;

/**
 * @author Xilef11
 *
 */
public class ClassicDusts extends IDust {
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IDust#getDustName()
	 */
	@Override
	public String getDustName() {
		return "classic";
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IDust#getInfusionItems(net.minecraft.item.ItemStack)
	 */
	@Override
	public ItemStack[] getInfusionItems(ItemStack arg0) {
		//custom crafting recipes
		return null;
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IDust#getPrimaryColor(net.minecraft.item.ItemStack)
	 */
	@Override
	public int getPrimaryColor(ItemStack stack) {
		//we can assume it is this item...
		return EnumDustTypes.getByMeta(stack.getMetadata()).primaryColor;
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IDust#getSecondaryColor(net.minecraft.item.ItemStack)
	 */
	@Override
	public int getSecondaryColor(ItemStack stack) {
		return EnumDustTypes.getByMeta(stack.getMetadata()).secondaryColor;
	}
	
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IDust#getPlacedColor(net.minecraft.item.ItemStack)
	 */
	@Override
	public int getPlacedColor(ItemStack stack) {
		return EnumDustTypes.getByMeta(stack.getMetadata()).floorColor;
	}

	private static int[] metaVals=null;
	@Override
	public int[] getMetaValues() {
		if(metaVals==null){
			EnumDustTypes vals[] = EnumDustTypes.values();
			metaVals = new int[vals.length];
			for(int i=0;i<vals.length;i++){
				metaVals[i]=vals[i].meta();
			}
		}
		return metaVals;
	}
	
}
