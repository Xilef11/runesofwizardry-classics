/**
 * 
 */
package xilef11.mc.runesofwizardry_classics.utils;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
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
	/**
	 * Attempts to remove experience levels from the player.
	 * @param player the player from wich to take XP
	 * @param levels the number of XP levels to take
	 * @return true if the levels have been removed, false if the player didn't have enough
	 */
	public static boolean takeXP(EntityPlayer player, int levels){
		boolean enough =player.experienceLevel>=levels;
		if(enough){
			player.removeExperienceLevel(10);
		}
		return enough;
	}
}
