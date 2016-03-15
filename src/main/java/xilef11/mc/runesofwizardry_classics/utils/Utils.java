/**
 * 
 */
package xilef11.mc.runesofwizardry_classics.utils;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

/** Misc. utility methods
 * @author Xilef11
 *
 */
public class Utils {
	private Utils(){}
	
	public static void spawnItemCentered(World world, BlockPos pos, ItemStack stack){
		EntityItem item = new EntityItem(world, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, stack);
		item.setDefaultPickupDelay();
		world.spawnEntityInWorld(item);
	}
}
