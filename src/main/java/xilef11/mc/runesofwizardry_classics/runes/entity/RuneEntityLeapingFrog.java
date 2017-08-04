package xilef11.mc.runesofwizardry_classics.runes.entity;
import java.util.Set;

import com.zpig333.runesofwizardry.api.IRune;
import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
public class RuneEntityLeapingFrog extends RuneEntity {
	public RuneEntityLeapingFrog(ItemStack[][] actualPattern,
			EnumFacing facing, Set<BlockPos> dusts,
			TileEntityDustActive entity, IRune creator) {
		super(actualPattern, facing, dusts, entity, creator);
	}
	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,
			ItemStack[] sacrifice, boolean negated) {
		//start FX
		entity.setupStar(0xFFFFFF, 0x00FF00, 1, 1);
		entity.setDrawStar(true);
	}
	@Override
	public void update() {
		World world = entity.getWorld();
		if(!world.isRemote){
			if(entity.ticksExisted()%10==0){
				BlockPos current = getPos().offset(face, 1+blocksplaced);
				//Block down = world.getBlockState(current.down()).getBlock();
				if(world.isAirBlock(current)&&world.containsAnyLiquid(new AxisAlignedBB(current.down(), current.down().add(1, 1, 1)))){
					//world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, current.getX(), current.getY(), current.getZ(), 0.5, 0.5, 0.5);
					if(world instanceof WorldServer){
						WorldServer ws = (WorldServer)world;
						ws.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, current.getX()+0.5, current.getY()+0.5, current.getZ()+0.5, ws.rand.nextInt(3)+2, 0,0,0, 2d);
					}
					world.setBlockState(current, Blocks.WATERLILY.getDefaultState());
					world.playBroadcastSound(2005, current, 0);
					world.playSound(null, current, SoundEvents.BLOCK_GRASS_STEP, SoundCategory.BLOCKS, 0.7F, 0.6F);
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
