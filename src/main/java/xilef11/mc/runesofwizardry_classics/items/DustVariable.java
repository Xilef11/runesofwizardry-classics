/**
 * 
 */
package xilef11.mc.runesofwizardry_classics.items;

import net.minecraft.item.ItemStack;import com.zpig333.runesofwizardry.item.dust.DustPlaceholder;

/** This placeholder dust matches only dusts from this addon.
 * @author Xilef11
 *
 */
public class DustVariable extends DustPlaceholder {
	public static DustVariable instance;
	/**
	 */
	public DustVariable() {
		super("variable", 0xb92db1, true);
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IDust#dustsMatch(net.minecraft.item.ItemStack, net.minecraft.item.ItemStack)
	 */
	@Override
	public boolean dustsMatch(ItemStack thisDust, ItemStack other) {
		return other.getItem() == ClassicDusts.instance;
	}
	
}
