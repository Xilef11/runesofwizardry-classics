package xilef11.mc.runesofwizardry_classics.runes.entity;

import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xilef11.mc.runesofwizardry_classics.Refs;

import com.zpig333.runesofwizardry.api.IRune;
import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.core.WizardryRegistry;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

public class RuneEntityTrapCage extends RuneEntity {
	private String user="";
	public RuneEntityTrapCage(ItemStack[][] actualPattern, EnumFacing facing,
			Set<BlockPos> dusts, TileEntityDustActive entity, IRune creator) {
		super(actualPattern, facing, dusts, entity, creator);
	}

	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,
			ItemStack[] sacrifice, boolean negated) {
		//nothing here
		if(!player.worldObj.isRemote)user=player.getName();
	}
	private static final int RADIUS=3;
	@Override
	public void update() {
		World world = entity.getWorld();
		if(!world.isRemote){
			if(entity.ticksExisted()==5*Refs.TPS)this.renderActive=false;
			List<EntityLivingBase> ents = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(getPos().add(-RADIUS,-1,-RADIUS), getPos().add(RADIUS,1,RADIUS)));
			if(!ents.isEmpty()){
				boolean broken=false;
				for(EntityLivingBase e:ents){
					if(e instanceof EntityPlayer){
						if(((EntityPlayer)e).getName().equals(user))continue;
					}
					if(!broken){
						this.onPatternBroken();
						broken=true;
					}
					BlockPos pos = e.getPosition();
					for(int x=-1;x<=1;x++){
						for(int z=-1;z<=1;z++){
							for(int y=-1;y<=1;y++){
								BlockPos p = pos.add(x, y, z);
								Block b = world.getBlockState(p).getBlock();
								if((b.isReplaceable(world, p)||b==WizardryRegistry.dust_placed)&&!(x==0&&z==0)){
									world.setBlockState(p, Blocks.iron_bars.getDefaultState());
								}
							}
						}
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
		user=compound.getString("username");
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setString("username", user);
	}
	
}
