/**
 * 
 */
package xilef11.mc.runesofwizardry_classics.runes.entity;

import java.util.Set;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import xilef11.mc.runesofwizardry_classics.utils.Utils;

import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

/**
 * @author Xilef11
 *
 */
public class RuneEntitySpawnerCollection extends RuneEntity {

	public RuneEntitySpawnerCollection(ItemStack[][] actualPattern,
			EnumFacing facing, Set<BlockPos> dusts, TileEntityDustActive entity) {
		super(actualPattern, facing, dusts, entity);
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#getRuneID()
	 */
	@Override
	public String getRuneID() {
		return "runeSpawnerCollection";
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#onRuneActivatedbyPlayer(net.minecraft.entity.player.EntityPlayer, net.minecraft.item.ItemStack[])
	 */
	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,ItemStack[] sacrifice,boolean negated) {
		// TODO rendering fx
		World world = player.worldObj;
		if(!world.isRemote){
			//take xp if not negated
			if(!negated){
				if(player.experienceLevel>=10){
					player.removeExperienceLevel(10);
				}else{
					//kill the rune
					this.onPatternBrokenByPlayer(player);
					return;
				}
			}
			BlockPos spawnerPos = getPos().offset(face);
			IBlockState spawnState = world.getBlockState(spawnerPos);
			if(!(spawnState.getBlock()==Blocks.mob_spawner)){
				this.onPatternBroken();//break the pattern
			}else{
				world.setBlockToAir(spawnerPos);
				Utils.spawnItemCentered(world, spawnerPos, new ItemStack(Blocks.mob_spawner));
			}
			this.onPatternBroken();
		}

	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#update()
	 */
	@Override
	public void update() {

	}

}
