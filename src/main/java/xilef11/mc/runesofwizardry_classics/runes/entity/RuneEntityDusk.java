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
public class RuneEntityDusk extends RuneEntity {
	private boolean activatedAtNight=false;
	public RuneEntityDusk(ItemStack[][] actualPattern, EnumFacing facing,
			Set<BlockPos> dusts, TileEntityDustActive entity, IRune creator) {
		super(actualPattern, facing, dusts, entity, creator);
	}
	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,ItemStack[] sacrifice, boolean negated) {
		World world = player.world;
		if(!world.isRemote){
			activatedAtNight=!world.isDaytime();
			if(activatedAtNight){
				entity.setupStar(0xFFFFFF,0xFFFFFF);
				entity.setDrawStar(true);
			}
		}
		//maybe setup FX
	}
	@Override
	public void update() {
		World world = entity.getWorld();
		if(!world.isRemote){
			if(world.isDaytime()){//always true client-side
				if(entity.beamdata==null){
					entity.setupBeam(0x000000, BeamType.SPIRAL);
					entity.setDrawBeam(true);
					entity.setDrawStar(false);
					IBlockState state = world.getBlockState(getPos());
					world.notifyBlockUpdate(getPos(), state, state, 3);
				}
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
