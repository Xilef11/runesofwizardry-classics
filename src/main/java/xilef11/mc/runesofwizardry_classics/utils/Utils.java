/**
 *
 */
package xilef11.mc.runesofwizardry_classics.utils;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
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
		world.spawnEntity(item);
	}
	/**
	 * Attempts to remove experience levels from the player.
	 * @param player the player from which to take XP
	 * @param levels the number of XP levels to take
	 * @return true if the levels have been removed, false if the player didn't have enough
	 */
	public static boolean takeXP(EntityPlayer player, int levels){
		if(player.capabilities.isCreativeMode)return true;
		boolean enough =player.experienceLevel>=levels;
		if(enough){
			player.addExperienceLevel(-levels);
		}
		return enough;
	}
	/** stores coordinates for an array or whatever**/
	public static class Coords{
		public final int row,col;
		public Coords(int r, int c) {
			this.row = r;
			this.col = c;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return row+col;//not the best, but should be sufficient
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if(obj==this)return true;
			if(!(obj instanceof Coords))return false;
			Coords o = (Coords)obj;
			return this.row==o.row && this.col==o.col;
		}
	}
}
