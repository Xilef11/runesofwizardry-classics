package xilef11.mc.runesofwizardry_classics.runes.entity;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import xilef11.mc.runesofwizardry_classics.Refs;

import com.zpig333.runesofwizardry.api.IRune;
import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive.BeamType;
/*Behaviour:
 * Lasts 1 day
 * Every mob kill adds 1/8 day
 * Every item kill (*stacksize) adds 1/2 sec
 * No matter where mobs are killed, (double) XP is dropped at the 4 "central" corners
 * Does not hurt players (summoning player only)
 * Kill zone is the smallest rectangle that contains the whole rune
 * - center + 4 on the long side (9 blocks)
 * - center + 3 on the short side (7 blocks)
 * don't forget the +1 in SE direction
 * - Height is 2(3) blocks
 */
public class RuneEntitySarlacc extends RuneEntity {
	private BlockPos nwd,seu;//north-west-down south-east-up bounds for the damage
	private int ticksremaining=0;
	private String activator="";
	public RuneEntitySarlacc(ItemStack[][] actualPattern, EnumFacing facing,
			Set<BlockPos> dusts, TileEntityDustActive entity, IRune creator) {
		super(actualPattern, facing, dusts, entity, creator);
		//calculate NW and SE positions
		if(facing==EnumFacing.NORTH || facing ==EnumFacing.SOUTH){
			//short side is North-south axis
			nwd = getPos().north(3).west(4).up();
			seu = getPos().south(4).east(5).up(3);
		}else{
			//short side is E-W axis
			nwd = getPos().north(4).west(3).up();
			seu = getPos().south(5).east(4).up(3);
		}
	}
	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,
			ItemStack[] sacrifice, boolean negated) {
		//not much
		if(!player.world.isRemote){
			ticksremaining=Refs.TICKS_PER_DAY;
			activator=player.getName();
			entity.setupStar(0xFFFFFF, 0xFFFFFF);
			entity.setupBeam(0x3333FF, BeamType.SPIRAL, new Vec3d(0,1,0));
		}
	}
	@Override
	public void update() {
		World world = entity.getWorld();
		if(!world.isRemote){
			//grab all entities in the kill zone
			List<Entity> ents = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(nwd, seu));
			for(Entity e:ents){
				if(e instanceof EntityItem){
					//increase the lifetime
					ticksremaining+=((EntityItem)e).getEntityItem().stackSize*(Refs.TPS/2);
					e.setDead();
				}else if(e instanceof EntityLiving){
					//EntityLiving should be mobs but not players (or armor stands wtf), might cause issues with modded mobs if they extend EntityLivingBase directly
					EntityLiving ent = (EntityLiving)e;
					if(ent.getHealth()>0){//don't get stuck if the entity is already dying
						int xp = ReflectionHelper.getPrivateValue(EntityLiving.class, ent, "experienceValue","field_70728_aV");
						//int value = (int) xpValueGet.invokeExact((EntityLiving)ent);
						xp*=2;//drop double XP
						//kill the entity (2M half-hearts of damage => 1M hearts)
						ent.attackEntityFrom(DamageSource.magic, 2000000);
						if(ent.getHealth()<=0){//avoid spawning too much XP if a mob has a silly amount of health and needs multiple ticks to die
							while(xp>0){
								int toDrop = EntityXPOrb.getXPSplit(xp);
								xp-=toDrop;
								//get the position of a random corner (+1 in any 2 directions + random offset)
								double zPos = getPos().getZ() + ((Math.random() > 0.5D) ? 1 : -1) + Math.random() * 0.4D - 0.2D;
								double xPos = getPos().getX() + ((Math.random() > 0.5D) ? 1 : -1) + Math.random() * 0.4D - 0.2D;
								EntityXPOrb orb = new EntityXPOrb(world, xPos+0.5, getPos().getY()+0.2, zPos+0.5, toDrop);
								orb.motionX=orb.motionY=orb.motionZ=0;//no initial speed
								world.spawnEntityInWorld(orb);
							}
							//add to the rune's lifetime
							ticksremaining+=Refs.TICKS_PER_DAY/8;
						}
					}
				}else if(e instanceof EntityPlayer){
					EntityPlayer p = (EntityPlayer)e;
					if(p.getName().equals(activator)) {//slow damage to the owner
						if (e.ticksExisted%Refs.TPS==0 && e.ticksExisted!=0) {
							p.attackEntityFrom(DamageSource.magic, 2);
						}
					}else{//fast damage to other players (but not as fast as mobs)
						p.attackEntityFrom(DamageSource.magic, 1);
					}
				}
			}
			//decrease lifetime
			ticksremaining--;
			if(ticksremaining<=0){
				this.onPatternBroken();
			}
		}
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		ticksremaining=compound.getInteger("ticksLeft");
		activator=compound.getString("activator");
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("ticksLeft", ticksremaining);
		compound.setString("activator", activator);
	}
	//makes accessing XP value through reflection faster
/*	private static final MethodHandle xpValueGet;
	static {
	    Field field;
		try {
			field = EntityLiving.class.getDeclaredField("field_70728_aV");
		} catch (NoSuchFieldException e) {
			ModLogger.logWarn("Couldn't get experienceValue field with srg name");
			try {
				field = EntityLiving.class.getDeclaredField("experienceValue");
			} catch (NoSuchFieldException e1) {
				ModLogger.logException(Level.ERROR, e, "Couldn't get experienceValue Field with deobf name");
				throw new IllegalStateException(e);
			}
		} catch (SecurityException e) {
			ModLogger.logException(Level.ERROR, e, "");
			throw new IllegalStateException(e);
		}
		field.setAccessible(true);
		try {
			xpValueGet = MethodHandles.publicLookup().unreflectGetter(field);
		} catch (IllegalAccessException e) {
			ModLogger.logException(Level.FATAL, e, "");
			throw new IllegalStateException(e);
		}
	}*/
}
