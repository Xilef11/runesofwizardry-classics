package xilef11.mc.runesofwizardry_classics.runes.entity;

import java.util.List;
import java.util.Set;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.utils.Utils;

import com.zpig333.runesofwizardry.api.IRune;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

public class RuneEntitySpriteFire extends FueledRuneEntity {

	public RuneEntitySpriteFire(ItemStack[][] actualPattern, EnumFacing facing,
			Set<BlockPos> dusts, TileEntityDustActive entity, IRune creator) {
		super(actualPattern, facing, dusts, entity, creator);
	}
	private String activatingPlayer;
	@Override
	protected int initialTicks() {
		return Refs.TICKS_PER_DAY*3;
	}

	/* (non-Javadoc)
	 * @see xilef11.mc.runesofwizardry_classics.runes.entity.FueledRuneEntity#onRuneActivatedbyPlayer(net.minecraft.entity.player.EntityPlayer, net.minecraft.item.ItemStack[], boolean)
	 */
	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,ItemStack[] sacrifice, boolean negated) {
		if(!player.worldObj.isRemote){
			if(!(negated || Utils.takeXP(player, 22))){
				this.onPatternBrokenByPlayer(player);
				return;
			}
			super.onRuneActivatedbyPlayer(player, sacrifice, negated);
			activatingPlayer=player.getName();
			entity.setupStar(0xFF0000, 0xFF0000);
			entity.setDrawStar(true);
			//TODO spawn the sprite entity
		}
	}
	private static final int RAD=3;
	/* (non-Javadoc)
	 * @see xilef11.mc.runesofwizardry_classics.runes.entity.FueledRuneEntity#update()
	 */
	@Override
	public void update() {
		World world = entity.getWorld();
			if(!this.hasRedstoneSignal()){
				super.update();
				EntityPlayer player = world.getPlayerEntityByName(activatingPlayer);
				if(player!=null){
					entity.setDrawStar(true);
					//make the star spin around the player's head
					double dx = MathHelper.cos(entity.ticksExisted()/3f)/2d - 0.5;
					double dz = MathHelper.sin(entity.ticksExisted()/3f)/2d - 0.5; 
					Vec3d pos = new Vec3d(player.posX+dx,player.posY+1.5,player.posZ+dz).subtract(new Vec3d(getPos()));
					entity.stardata.offset=pos;
					entity.markDirty();
					//burn hostiles
					if(!world.isRemote){
						BlockPos ppos = player.getPosition();
						List<EntityLiving> ents = world.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(ppos.add(-RAD, -RAD, -RAD), ppos.add(RAD,RAD,RAD)));
						for(EntityLiving e:ents){
							if(player.equals(e.getAttackTarget())||(e.equals(player.getLastAttacker())&&player.ticksExisted-player.getLastAttackerTime()<5*Refs.TPS)){
								if(!e.isBurning())e.setFire(2+world.rand.nextInt(5));
							}
						}
						//burn some terrain once in awhile?
						if (entity.ticksExisted() % 10*Refs.TPS == 0 && Math.random() < 0.2){
							boolean burnt=false;
							for(int x=-RAD;x<=RAD&&!burnt;x++){
								for(int y=-RAD;y<=RAD&&!burnt;y++){
									for(int z=-RAD;z<=RAD&&!burnt;z++){
										BlockPos bp = player.getPosition().add(x, y, z);
										if(world.isAirBlock(bp)&&!world.isAirBlock(bp.down())){
											world.setBlockState(bp, Blocks.FIRE.getDefaultState());
											burnt=true;
										}
									}
								}
							}
						}
					}
				}else{
					entity.setDrawStar(false);
				}
			}else{
				entity.setDrawStar(false);
			}
	}

	/* (non-Javadoc)
	 * @see xilef11.mc.runesofwizardry_classics.runes.entity.FueledRuneEntity#onPatternBroken()
	 */
	@Override
	public void onPatternBroken() {
		super.onPatternBroken();
	}

	/* (non-Javadoc)
	 * @see xilef11.mc.runesofwizardry_classics.runes.entity.FueledRuneEntity#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		activatingPlayer=compound.getString("player");
	}

	/* (non-Javadoc)
	 * @see xilef11.mc.runesofwizardry_classics.runes.entity.FueledRuneEntity#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setString("player", activatingPlayer);
	}
	

}
