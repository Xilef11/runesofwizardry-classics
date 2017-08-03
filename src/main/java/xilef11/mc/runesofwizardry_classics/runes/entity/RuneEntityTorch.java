/**
 *
 */
package xilef11.mc.runesofwizardry_classics.runes.entity;
import java.util.Set;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import xilef11.mc.runesofwizardry_classics.ModLogger;
import xilef11.mc.runesofwizardry_classics.Refs;

import com.zpig333.runesofwizardry.api.IRune;
import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive.BeamType;
/**
 * @author Xilef11
 *
 */
public class RuneEntityTorch extends RuneEntity {
	public RuneEntityTorch(ItemStack[][] actualPattern, EnumFacing facing,Set<BlockPos> dusts, TileEntityDustActive entity,IRune creator) {
		super(actualPattern, facing, dusts, entity,creator);
	}
	private boolean beacon=false;
	private int beaconColor=0xFFFFFF;
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#onRuneActivatedbyPlayer(net.minecraft.entity.player.EntityPlayer, net.minecraft.item.ItemStack[])
	 */
	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,ItemStack[] sacrifice,boolean negated) {
		ModLogger.logDebug("Activated torch rune with sacrifice: "+sacrifice);
		if(sacrifice!=null||negated){
			this.beacon=true;
			//TODO create beacon
			entity.setupBeam(0xFFFFFF, BeamType.BEACON);
			entity.beamdata.beamRadius=0.1;
			entity.beamdata.glowRadius=0.2;
			entity.setDrawBeam(true);
		}else{
			entity.setupStar(0xFFFFFF, 0xFFFFFF,1,1,new Vec3d(0,-0.9,0));
			entity.setDrawStar(true);
		}
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#update()
	 */
	@Override
	public void update() {
		World world = entity.getWorld();
		if(beacon){
			if(!world.isRemote && entity.ticksExisted()%Refs.TPS==0){
				if(hasRedstoneSignal())entity.setDrawBeam(false);
				else entity.setDrawBeam(true);
				for(EntityItem i:world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(getPos()))){
					ItemStack stack = i.getItem();
					if(stack.getItem()==Items.DYE){
						if(entity.beamdata!=null)entity.beamdata.color=EnumDyeColor.byDyeDamage(stack.getItemDamage()).getColorValue();
						IBlockState state = world.getBlockState(getPos());
						i.setDead();
						world.notifyBlockUpdate(getPos(), state, state, 3);
					}
					//TODO use oredict eventually
//					for(int id:OreDictionary.getOreIDs(stack)){
//						String n = OreDictionary.getOreName(id);
//						if(n.startsWith("dye")){
//							for(EnumDyeColor c:EnumDyeColor.values()){
//								//FIXME won't work with light colors
//								if(n.endsWith(c.getName())){
//									if(entity.beamdata!=null)entity.beamdata.color=c.getMapColor().colorValue;
//								}
//							}
//						}
//					}
				}
			}
		}else if(!world.isRemote && entity.ticksExisted()==2*Refs.TPS){
			BlockPos pos = getPos();
			entity.clear();
			world.setBlockToAir(pos);
			world.setBlockState(pos, Blocks.TORCH.getDefaultState());
		}
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		beacon = compound.getBoolean("isBeacon");
		beaconColor = compound.getInteger("beaconColor");
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setBoolean("isBeacon", beacon);
		compound.setInteger("beaconColor", beaconColor);
	}
}
