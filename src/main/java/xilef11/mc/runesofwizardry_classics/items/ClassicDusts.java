/**
 * 
 */
package xilef11.mc.runesofwizardry_classics.items;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.zpig333.runesofwizardry.api.IDust;

/**
 * @author Xilef11
 *
 */
public class ClassicDusts extends IDust {
	public ClassicDusts(){
		this.setHasSubtypes(true);
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IDust#getDustName()
	 */
	@Override
	public String getDustName() {
		// TODO Auto-generated method stub
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
	private static int[] res=null;
	@Override
	public int[] getMetaValues() {
		if(res==null){
			EnumDustTypes vals[] = EnumDustTypes.values();
			res = new int[vals.length];
			for(int i=0;i<vals.length;i++){
				res[i]=vals[i].meta();
			}
		}
		return res;
	}
	@SideOnly(Side.CLIENT)//duplicating the sideonly in Item
	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab,List<ItemStack> subItems) {
		for(int meta:getMetaValues()){
			subItems.add(new ItemStack(itemIn,1,meta));
		}
	}
	
}
