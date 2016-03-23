package xilef11.mc.runesofwizardry_classics.runes.entity;

import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

import com.zpig333.runesofwizardry.api.IRune;
import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

public class RuneEntityLeapingFrog extends RuneEntity {

	public RuneEntityLeapingFrog(ItemStack[][] actualPattern,
			EnumFacing facing, Set<BlockPos> dusts,
			TileEntityDustActive entity, IRune creator) {
		super(actualPattern, facing, dusts, entity, creator);
	}

	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,
			ItemStack[] sacrifice, boolean negated) {
		//start FX?
	}

	@Override
	public void update() {
		World world = entity.getWorld();
		if(!world.isRemote){
			if(entity.ticksExisted()%10==0){
				BlockPos current = getPos().offset(face, 1+blocksplaced);
				//Block down = world.getBlockState(current.down()).getBlock();
				if(world.isAirBlock(current)&&world.isAnyLiquid(new AxisAlignedBB(current.down(), current.down()))){
					world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, current.getX(), current.getY(), current.getZ(), 0.5, 0, 0.5);
					world.setBlockState(current, Blocks.waterlily.getDefaultState());
					world.playAuxSFX(2005, current, 0);
					blocksplaced++;
				}else{
					this.onPatternBroken();//we're done
				}
			}
		}

	}
	private int blocksplaced=0;
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		blocksplaced = compound.getInteger("runeFrog.blocksplaced");
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("runeFrog.blocksplaced",blocksplaced);
	}
	

}
