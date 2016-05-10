package xilef11.mc.runesofwizardry_classics.runes.entity;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.managers.LockedTimeData;
import xilef11.mc.runesofwizardry_classics.runes.RuneLockedTime;

import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;
public class RuneEntityLockedTime extends FueledRuneEntity {
	private long time=0;
	public RuneEntityLockedTime(ItemStack[][] actualPattern, EnumFacing facing,
			Set<BlockPos> dusts, TileEntityDustActive entity, RuneLockedTime creator) {
		super(actualPattern, facing, dusts, entity, creator);
	}
	/* (non-Javadoc)
	 * @see xilef11.mc.runesofwizardry_classics.runes.entity.FueledRuneEntity#onRuneActivatedbyPlayer(net.minecraft.entity.player.EntityPlayer, net.minecraft.item.ItemStack[], boolean)
	 */
	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,
			ItemStack[] sacrifice, boolean negated) {
		super.onRuneActivatedbyPlayer(player, sacrifice, negated);
		World world = entity.getWorld();
		if(!world.isRemote){
			time = world.getWorldTime();
			LockedTimeData.get(world).addRune();
		}
	}
	/* (non-Javadoc)
	 * @see xilef11.mc.runesofwizardry_classics.runes.entity.FueledRuneEntity#onPatternBroken()
	 */
	@Override
	public void onPatternBroken() {
		super.onPatternBroken();
		LockedTimeData.get(entity.getWorld()).removeRune();
	}
	/* (non-Javadoc)
	 * @see xilef11.mc.runesofwizardry_classics.runes.entity.FueledRuneEntity#update()
	 */
	@Override
	public void update() {
		super.update();
		World world = entity.getWorld();
		if(!world.isRemote){
			//this stops the day/night cycle, but does not do anything for falling blocks + liquids
			//however, the original didn't do anything else (even though it says it does)
			//falling block is handled in RuneLockedTime (event handler)
			//liquids would probably require ASM
			world.setWorldTime(time);
		}
	}
	@Override
	protected int initialTicks() {
		return Refs.TICKS_PER_DAY;
	}
	/* (non-Javadoc)
	 * @see xilef11.mc.runesofwizardry_classics.runes.entity.FueledRuneEntity#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		time = compound.getLong("WorldTime");
	}
	/* (non-Javadoc)
	 * @see xilef11.mc.runesofwizardry_classics.runes.entity.FueledRuneEntity#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setLong("WorldTime", time);
	}
}
