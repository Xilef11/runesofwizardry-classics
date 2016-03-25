package xilef11.mc.runesofwizardry_classics.runes.entity;

import java.util.Set;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import xilef11.mc.runesofwizardry_classics.items.EnumDustTypes;
import xilef11.mc.runesofwizardry_classics.runes.RuneDetonation;
import xilef11.mc.runesofwizardry_classics.utils.Utils.Coords;

import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

public class RuneEntityDetonation extends RuneEntity {

	public RuneEntityDetonation(ItemStack[][] actualPattern, EnumFacing facing,
			Set<BlockPos> dusts, TileEntityDustActive entity, RuneDetonation creator) {
		super(actualPattern, facing, dusts, entity, creator);
	}
	int fuselength=0,power=0;
	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,
			ItemStack[] sacrifice, boolean negated) {
		if(!player.worldObj.isRemote){
			Coords c = ((RuneDetonation)creator).getFuse().iterator().next();
			switch(EnumDustTypes.getByMeta(placedPattern[c.row][c.col].getMetadata())){
			case PLANT:fuselength=-1;
				break;
			case GUNPOWDER:fuselength=5*20;
				break;
			case LAPIS:fuselength=10*20;
				break;
			case BLAZE:fuselength=15*20;
				break;
			default: fuselength=0;
				break;
			}
			c = ((RuneDetonation)creator).getCenter().iterator().next();
			switch(EnumDustTypes.getByMeta(placedPattern[c.row][c.col].getMetadata())){
			case PLANT:power=100;
				break;
			case GUNPOWDER:power=200;
				break;
			case LAPIS:power=300;
				break;
			case BLAZE:power=400;
				break;
			default: power=0;
				break;
			}
		}
	}

	@Override
	public void update() {
		if(!entity.getWorld().isRemote){
			if(fuselength>=0){
				if(entity.ticksExisted()>=fuselength)boom();
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#handleEntityCollision(net.minecraft.world.World, net.minecraft.util.BlockPos, net.minecraft.block.state.IBlockState, net.minecraft.entity.Entity)
	 */
	@Override
	public boolean handleEntityCollision(World worldIn, BlockPos pos,IBlockState state, Entity entityIn) {
		if(!worldIn.isRemote){
			if(fuselength<0&&entity.ticksExisted()>3*20)boom();
		}
		return true;
	}

	private void boom(){
		this.onPatternBroken();
		EntityXPOrb dummy = new EntityXPOrb(entity.getWorld(), getPos().getX()+0.5, getPos().getY()+0.5, getPos().getZ()+0.5, 0);
		
		entity.getWorld().createExplosion(dummy, getPos().getX()+0.5, getPos().getY()+0.5, getPos().getZ()+0.5, (float)(power*power)/10000 + 2F, true);
		//Might need to create the explosion manually else it may vary between clients
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		fuselength=compound.getInteger("fuse");
		power = compound.getInteger("boomPower");
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("fuse", fuselength);
		compound.setInteger("boomPower", power);
	}
	
}
