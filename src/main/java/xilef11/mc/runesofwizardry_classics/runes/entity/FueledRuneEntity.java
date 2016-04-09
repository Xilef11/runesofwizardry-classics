/**
 * 
 */
package xilef11.mc.runesofwizardry_classics.runes.entity;

import java.util.Set;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

import com.zpig333.runesofwizardry.api.IRune;
import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

/** This class is for runes that use fuel
 * @author Xilef11
 *
 */
public abstract class FueledRuneEntity extends RuneEntity {
	protected int ticksLeft=0;
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
		if(!player.worldObj.isRemote){
			this.ticksLeft=initialTicks();
			//TODO register this rune to nearby runes of power distribution
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
		//TODO unregister from nearby Runes of power distribution
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

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#handleEntityCollision(net.minecraft.world.World, net.minecraft.util.BlockPos, net.minecraft.block.state.IBlockState, net.minecraft.entity.Entity)
	 */
	@Override
	public boolean handleEntityCollision(World worldIn, BlockPos pos,
			IBlockState state, Entity entityIn) {
		if(entityIn instanceof EntityItem){
			ItemStack stack = ((EntityItem)entityIn).getEntityItem();
			//find the furnace burn time. if not hardcoded in vanilla furnace, it will check Forge's registered fuelHandlers
			int burnTime = TileEntityFurnace.getItemBurnTime(stack);
			if(burnTime!=0){
				this.addFuel(burnTime*stack.stackSize);
				worldIn.playSoundAtEntity(entityIn, "random.fizz", 1, 1);
				worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, entityIn.posX, entityIn.posY, entityIn.posZ, 0, 0, 0);
				entityIn.setDead();
			}
		}
		return true;
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
