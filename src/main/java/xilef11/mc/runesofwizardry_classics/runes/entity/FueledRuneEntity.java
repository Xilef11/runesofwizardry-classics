/**
 *
 */
package xilef11.mc.runesofwizardry_classics.runes.entity;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.zpig333.runesofwizardry.api.IRune;
import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustPlaced;
/** This class is for runes that use fuel
 * @author Xilef11
 *
 */
public abstract class FueledRuneEntity extends RuneEntity {
	protected int ticksLeft=0;
	private List<RuneEntityPowerDistribution> poweredBy=new LinkedList<RuneEntityPowerDistribution>();
	public FueledRuneEntity(ItemStack[][] actualPattern, EnumFacing facing,
			Set<BlockPos> dusts, TileEntityDustActive entity, IRune creator) {
		super(actualPattern, facing, dusts, entity, creator);
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#onRuneActivatedbyPlayer(net.minecraft.entity.player.EntityPlayer, net.minecraft.item.ItemStack[], boolean)
	 */
	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,
			ItemStack[] sacrifice, boolean negated) {
		if(!player.world.isRemote){
			this.ticksLeft=initialTicks();
			entity.setupStar(0xFFFFFF, 0xFFFFFF, 1, 1);
			entity.setDrawStar(true);
			//register this rune to nearby runes of power distribution
			findNearbyPowerRunes();
		}
	}
	/** returns the number of ticks this rune lasts without fuel **/
	protected abstract int initialTicks();
	/**called when fuel runs out, breaks the rune by default**/
	protected void onFuelRunOut(){
		this.onPatternBroken();
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#onPatternBroken()
	 */
	@Override
	public void onPatternBroken() {
		super.onPatternBroken();
		//unregister from nearby Runes of power distribution
		for(RuneEntityPowerDistribution p:poweredBy){
			unregisterFrom(p);
		}
	}
	/** Adds fuel to this rune. 1 life tick is added per fuel tick (by default) **/
	protected void addFuel(int fuelTicks){
		this.ticksLeft+=fuelTicks;
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#update()
	 */
	@Override
	public void update() {
		World world = entity.getWorld();
		if(!world.isRemote){
			ticksLeft--;
			if(ticksLeft<=0)this.onFuelRunOut();
		}
	}
	private static final int FUEL_EAT_TICKS=3;
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#handleEntityCollision(net.minecraft.world.World, net.minecraft.util.BlockPos, net.minecraft.block.state.IBlockState, net.minecraft.entity.Entity)
	 */
	@Override
	public boolean handleEntityCollision(World worldIn, BlockPos pos,
			IBlockState state, Entity entityIn) {
		if(entity.ticksExisted()%FUEL_EAT_TICKS==0 && !worldIn.isRemote){
			if(entityIn instanceof EntityItem){
				ItemStack stack = ((EntityItem)entityIn).getEntityItem();
				//find the furnace burn time. if not hardcoded in vanilla furnace, it will check Forge's registered fuelHandlers
				int burnTime = TileEntityFurnace.getItemBurnTime(stack);
				if(burnTime!=0){
					this.addFuel(burnTime*stack.stackSize);
					worldIn.playSound(null,entityIn.posX, entityIn.posY, entityIn.posZ, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1, 1);
					worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, entityIn.posX, entityIn.posY, entityIn.posZ, 0, 0, 0);
					entityIn.setDead();
					IBlockState bstate = worldIn.getBlockState(getPos());
					worldIn.notifyBlockUpdate(getPos(), bstate, bstate, 3);
				}
			}
		}
		return true;
	}
	public void registerTo(RuneEntityPowerDistribution power){
		if(!poweredBy.contains(power)){
			poweredBy.add(power);
			power.register(this);
		}
	}
	public void unregisterFrom(RuneEntityPowerDistribution power){
		poweredBy.remove(power);
		power.unregister(this);
	}
	private void findNearbyPowerRunes(){
		final int range =RuneEntityPowerDistribution.RANGE,
				vrange = RuneEntityPowerDistribution.VRANGE;
		World world = entity.getWorld();
		if(!world.isRemote){
			BlockPos thisPos = getPos();
			Set<BlockPos> checked = new HashSet<BlockPos>();
			checked.addAll(dustPositions);
			for(int dx=-range;dx<=range;dx++){
				for(int dz=-range;dz<=range;dz++){
					for(int dy=-vrange;dy<=vrange;dy++){
						BlockPos current = thisPos.add(dx,dy,dz);
						//don't check if we already found that rune
						if(checked.contains(current))continue;
						TileEntity t = world.getTileEntity(current);
						//don't do anything with non dust blocks
						if(!(t instanceof TileEntityDustPlaced))continue;
						TileEntityDustPlaced ted = (TileEntityDustPlaced)t;
						//get the rune
						RuneEntity rune = ted.getRune();
						if(rune!=null)checked.addAll(rune.dustPositions);
						//only find runes of power distribution
						if(!(rune instanceof RuneEntityPowerDistribution))continue;
						//add the rune to our list
						registerTo((RuneEntityPowerDistribution) rune);
					}
				}
			}
		}
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		ticksLeft = compound.getInteger("FueledRune:ticksLeft");
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("FueledRune:ticksLeft", ticksLeft);
	}
}
