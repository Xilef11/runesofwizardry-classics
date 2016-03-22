package xilef11.mc.runesofwizardry_classics.runes.entity;

import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import com.zpig333.runesofwizardry.api.IRune;
import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

public class RuneEntityDawn extends RuneEntity {
	private boolean activatedAtDay=false;
	
	public RuneEntityDawn(ItemStack[][] actualPattern, EnumFacing facing,
			Set<BlockPos> dusts, TileEntityDustActive entity, IRune creator) {
		super(actualPattern, facing, dusts, entity, creator);
	}

	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,ItemStack[] sacrifice, boolean negated) {
		World world = player.worldObj;
		if(!world.isRemote){
			activatedAtDay=world.isDaytime();
		}
		//maybe setup FX
	}

	@Override
	public void update() {
		World world = entity.getWorld();
		if(!world.isRemote){
			if(!world.isDaytime()){
				//XXX quite "jumpy", maybe a smaller number?
				world.setWorldTime(world.getWorldTime()+25);
				//System.out.println(world.getWorldTime());
				//System.out.println(world.getTotalWorldTime());
				activatedAtDay=false;//we got to night time
			}else{//if we got to day time 
				if(!activatedAtDay)this.onPatternBroken();
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		activatedAtDay=compound.getBoolean("dayActivation");
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		compound.setBoolean("dayActivation", activatedAtDay);
	}

}
