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

public class RuneEntityDusk extends RuneEntity {
	private boolean activatedAtNight=false;
	public RuneEntityDusk(ItemStack[][] actualPattern, EnumFacing facing,
			Set<BlockPos> dusts, TileEntityDustActive entity, IRune creator) {
		super(actualPattern, facing, dusts, entity, creator);
	}

	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,ItemStack[] sacrifice, boolean negated) {
		World world = player.worldObj;
		if(!world.isRemote){
			activatedAtNight=!world.isDaytime();
		}
		//maybe setup FX
	}

	@Override
	public void update() {
		World world = entity.getWorld();
		if(!world.isRemote){
			if(world.isDaytime()){//always true client-side
				world.setWorldTime(world.getWorldTime()+25);
				activatedAtNight=false;//we got to daytime
			}else{//if we got to night time 
				if(!activatedAtNight)this.onPatternBroken();
			}
		}else{
			if(world.getCelestialAngle(0)<0.248 || world.getCelestialAngle(0)>0.752)world.setWorldTime(world.getWorldTime()+25);
		}
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		activatedAtNight=compound.getBoolean("nightActivation");
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		compound.setBoolean("nightActivation", activatedAtNight);
	}
	
}
