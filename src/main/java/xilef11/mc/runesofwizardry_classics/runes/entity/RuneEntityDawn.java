package xilef11.mc.runesofwizardry_classics.runes.entity;
import java.util.Set;

import com.zpig333.runesofwizardry.api.IRune;
import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive.BeamType;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
public class RuneEntityDawn extends RuneEntity {
	private boolean activatedAtDay=false;
	public RuneEntityDawn(ItemStack[][] actualPattern, EnumFacing facing,
			Set<BlockPos> dusts, TileEntityDustActive entity, IRune creator) {
		super(actualPattern, facing, dusts, entity, creator);
	}
	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,ItemStack[] sacrifice, boolean negated) {
		World world = player.world;
		if(!world.isRemote){
			activatedAtDay=world.isDaytime();
			if(activatedAtDay){
				entity.setupStar(0xFFFFFF,0xFFFFFF);
				entity.setDrawStar(true);
			}
		}
		this.renderActive=true;
	}
	@Override
	public void update() {
		World world = entity.getWorld();
		if(!world.isRemote){
			if(!world.isDaytime()){//always false on the client
				if(entity.beamdata==null){
					entity.setupBeam(0xFFFFCC, BeamType.SPIRAL);
					entity.setDrawBeam(true);
					entity.setDrawStar(false);
					IBlockState state = world.getBlockState(getPos());
					world.notifyBlockUpdate(getPos(), state, state, 3);
				}
				world.setWorldTime(world.getWorldTime()+25);
				activatedAtDay=false;//we got to night time
			}else{//if we got to day time
				if(!activatedAtDay)this.onPatternBroken();
			}
		}else{
			//night: 0.25 to 0.75
			//RunesofWizardry_Classics.log().info(world.getCelestialAngle(0));
			if(world.getCelestialAngle(0)>0.248 && world.getCelestialAngle(0)<0.752)world.setWorldTime(world.getWorldTime()+25);
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
