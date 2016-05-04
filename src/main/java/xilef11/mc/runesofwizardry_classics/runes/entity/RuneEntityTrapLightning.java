package xilef11.mc.runesofwizardry_classics.runes.entity;

import java.util.List;
import java.util.Set;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.items.EnumDustTypes;
import xilef11.mc.runesofwizardry_classics.runes.RuneTrapLightning;
import xilef11.mc.runesofwizardry_classics.utils.Utils.Coords;

import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

public class RuneEntityTrapLightning extends RuneEntity {

	public RuneEntityTrapLightning(ItemStack[][] actualPattern,
			EnumFacing facing, Set<BlockPos> dusts,
			TileEntityDustActive entity, RuneTrapLightning creator) {
		super(actualPattern, facing, dusts, entity, creator);
	}
	int meta=0;
	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,
			ItemStack[] sacrifice, boolean negated) {
		if(!player.worldObj.isRemote){
			Coords c = ((RuneTrapLightning)creator).getVariableDusts().iterator().next();
			meta=placedPattern[c.row][c.col].getMetadata();
		}
	}

	@Override
	public void update() {
		World world = entity.getWorld();
		if(entity.ticksExisted()==5*Refs.TPS)this.renderActive=false;
		if(!world.isRemote&&entity.ticksExisted()>5*20){
			int radius=0;
			switch(EnumDustTypes.getByMeta(meta)){
			case GUNPOWDER:radius=4;
			break;
			case LAPIS: radius=6;
			break;
			case BLAZE:radius=8;
			break;
			default: this.onPatternBroken();return;
			}
			List<EntityLivingBase> ents =world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(getPos().add(-radius,-radius,-radius), getPos().add(radius,radius,radius))); 
			if(!ents.isEmpty()){
				for(EntityLivingBase e:ents){
					world.addWeatherEffect(new EntityLightningBolt(world, e.posX, e.posY, e.posZ));
				}
				world.addWeatherEffect(new EntityLightningBolt(world, getPos().getX(), getPos().getY(), getPos().getZ()));
				this.onPatternBroken();
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#handleEntityCollision(net.minecraft.world.World, net.minecraft.util.BlockPos, net.minecraft.block.state.IBlockState, net.minecraft.entity.Entity)
	 */
	@Override
	public boolean handleEntityCollision(World worldIn, BlockPos pos,IBlockState state, Entity entityIn) {
		if(worldIn.isRemote && entityIn instanceof EntityItem &&entity.ticksExisted()>5*20){
			worldIn.addWeatherEffect(new EntityLightningBolt(worldIn, getPos().getX(), getPos().getY(), getPos().getZ()));
			this.onPatternBroken();
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		meta = compound.getInteger("dustMeta");
		super.readFromNBT(compound);
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		compound.setInteger("dustMeta", meta);
		super.writeToNBT(compound);
	}
	
	
}
