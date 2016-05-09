package xilef11.mc.runesofwizardry_classics.runes.entity;

import java.util.Set;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import xilef11.mc.runesofwizardry_classics.utils.Utils;

import com.zpig333.runesofwizardry.api.IRune;
import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

public class RuneEntityWisdom extends RuneEntity {
	private static final byte RCDelay = 5*20;//delay in ticks between r-click and taking xp
	private String user;//username of activating player
	private byte ticksSinceRC=RCDelay+1;//ticks since last right-clicked (always <= delay)
	private int xpAbsorbed=0;
	public RuneEntityWisdom(ItemStack[][] actualPattern, EnumFacing facing,
			Set<BlockPos> dusts, TileEntityDustActive entity, IRune creator) {
		super(actualPattern, facing, dusts, entity, creator);
	}

	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,
			ItemStack[] sacrifice, boolean negated) {
		World world = player.worldObj;
		if(!world.isRemote){
			if(negated || Utils.takeXP(player, 6)){
				user = player.getName();
				ticksSinceRC = 0;
			}else{
				this.onPatternBrokenByPlayer(player);
			}
		}

	}

	@Override
	public void update() {
		//absorb player XP
		World world = entity.getWorld();
		if(!world.isRemote) {
			if (ticksSinceRC>RCDelay) {
				EntityPlayer player = world.getClosestPlayer(getPos().getX(), getPos().getY(), getPos().getZ(), 2, false);
				if(player!=null){
					if(player.experience>0){
						//TODO sounds maybe?
						player.addExperience(-1);
						xpAbsorbed++;
					}else if(player.experienceLevel>0){
						player.removeExperienceLevel(1);
						player.addExperience(player.xpBarCap()-1);
						xpAbsorbed++;
					}
				}
			}else{
				ticksSinceRC++;
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#handleRightClick(net.minecraft.world.World, net.minecraft.util.BlockPos, net.minecraft.block.state.IBlockState, net.minecraft.entity.player.EntityPlayer, net.minecraft.util.EnumFacing, float, float, float)
	 */
	@Override
	public boolean handleRightClick(World worldIn, BlockPos pos,
			IBlockState state, EntityPlayer playerIn, EnumFacing side,
			float hitX, float hitY, float hitZ) {
		if(!worldIn.isRemote && playerIn.getName().equals(user)){
			//pause the rune
			ticksSinceRC=0;
			//give all xp
			playerIn.addExperience(xpAbsorbed);
			xpAbsorbed=0;
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#handleLeftClick(net.minecraft.world.World, net.minecraft.util.BlockPos, net.minecraft.entity.player.EntityPlayer, net.minecraft.util.Vec3)
	 */
	@Override
	public boolean handleLeftClick(World worldIn, BlockPos pos,
			EntityPlayer playerIn, Vec3d hit) {
		return playerIn.getName()!=user;//prevent non-owners from breaking it
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#handleEntityCollision(net.minecraft.world.World, net.minecraft.util.BlockPos, net.minecraft.block.state.IBlockState, net.minecraft.entity.Entity)
	 */
	@Override
	public boolean handleEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		//absorb xp orbs
		if(!worldIn.isRemote && ticksSinceRC>RCDelay && entityIn instanceof EntityXPOrb){
			EntityXPOrb orb = (EntityXPOrb)entityIn;
			xpAbsorbed += orb.getXpValue();
			orb.setDead();
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#onPatternBroken()
	 */
	@Override
	public void onPatternBroken() {
		//drop the stored XP as orbs
		World world = entity.getWorld();
		if(!world.isRemote){
			while(xpAbsorbed>0){
				int xp = EntityXPOrb.getXPSplit(xpAbsorbed);
				xpAbsorbed-=xp;
				world.spawnEntityInWorld(new EntityXPOrb(world, getPos().getX()+0.5, getPos().getY()+0.5, getPos().getZ()+0.5, xp));
			}
		}
		super.onPatternBroken();
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		ticksSinceRC = compound.getByte("ticksSinceRC");
		user = compound.getString("Activator");
		xpAbsorbed=compound.getInteger("xpHeld");
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setByte("ticksSinceRC", ticksSinceRC);
		compound.setString("Activator", user);
		compound.setInteger("xpHeld", xpAbsorbed);
	}
	
}
