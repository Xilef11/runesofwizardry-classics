/**
 * 
 */
package xilef11.mc.runesofwizardry_classics.runes.entity;

import java.util.Set;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.utils.Utils;

import com.zpig333.runesofwizardry.api.IRune;
import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

/**
 * @author Xilef11
 *
 */
public class RuneEntitySpawnerCollection extends RuneEntity {

	public RuneEntitySpawnerCollection(ItemStack[][] actualPattern,
			EnumFacing facing, Set<BlockPos> dusts, TileEntityDustActive entity,IRune creator) {
		super(actualPattern, facing, dusts, entity,creator);
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#onRuneActivatedbyPlayer(net.minecraft.entity.player.EntityPlayer, net.minecraft.item.ItemStack[])
	 */
	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,ItemStack[] sacrifice,boolean negated) {
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
		}

	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#update()
	 */
	@Override
	public void update() {
		World world = entity.getWorld();
		if(!world.isRemote && entity.ticksExisted()==Refs.TPS*5){
			BlockPos spawnerPos = getPos().offset(face);
			IBlockState spawnState = world.getBlockState(spawnerPos);
			if(!(spawnState.getBlock()==Blocks.MOB_SPAWNER)){
				this.onPatternBroken();//break the pattern
			}else{
				world.setBlockToAir(spawnerPos);
				Utils.spawnItemCentered(world, spawnerPos, new ItemStack(Blocks.MOB_SPAWNER));
				world.playSoundEffect(getPos().getX(), getPos().getY(), getPos().getZ(), "mob.chicken.plop", 0.5F, 0.8F + (world.rand.nextFloat() - world.rand.nextFloat()));
			}
			this.onPatternBroken();
		}
	}

}
