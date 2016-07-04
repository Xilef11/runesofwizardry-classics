package xilef11.mc.runesofwizardry_classics.runes.entity;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.items.EnumDustTypes;
import xilef11.mc.runesofwizardry_classics.runes.RuneProtection;
import xilef11.mc.runesofwizardry_classics.utils.Utils;
import xilef11.mc.runesofwizardry_classics.utils.Utils.Coords;

import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;
public class RuneEntityProtection extends FueledRuneEntity {
	public RuneEntityProtection(ItemStack[][] actualPattern, EnumFacing facing,
			Set<BlockPos> dusts, TileEntityDustActive entity, RuneProtection creator) {
		super(actualPattern, facing, dusts, entity, creator);
	}
	@Override
	protected int initialTicks() {
		return Refs.TICKS_PER_DAY;
	}
	/* (non-Javadoc)
	 * @see xilef11.mc.runesofwizardry_classics.runes.entity.FueledRuneEntity#onRuneActivatedbyPlayer(net.minecraft.entity.player.EntityPlayer, net.minecraft.item.ItemStack[], boolean)
	 */
	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,
			ItemStack[] sacrifice, boolean negated) {
		super.onRuneActivatedbyPlayer(player, sacrifice, negated);
		World world = player.worldObj;
		if(!world.isRemote){
			if(!(negated || Utils.takeXP(player, 15))){
				this.onPatternBrokenByPlayer(player);
				return;
			}
			Coords c = ((RuneProtection)creator).getVariableDusts().iterator().next();
			switch(EnumDustTypes.getByMeta(placedPattern[c.row][c.col].getMetadata())){
			case PLANT:radius=1;
				break;
			case GUNPOWDER:radius=2;
				break;
			case LAPIS:radius=3;
				break;
			case BLAZE:radius=4;
				break;
			default:this.onPatternBroken();return;
			}
			radius*=8;
			//FX
			entity.setupStar(0xFFFFFF, (128<<4)|62);
		}
	}
	private int radius=0;
	/* (non-Javadoc)
	 * @see xilef11.mc.runesofwizardry_classics.runes.entity.FueledRuneEntity#update()
	 */
	@Override
	public void update() {
		super.update();
		World world = entity.getWorld();
		if(!world.isRemote){
			List<EntityLiving> ents = world.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(getPos().add(-radius,-radius,-radius), getPos().add(radius,radius,radius)));
			for(EntityLiving e:ents){
				//XXX actually, not all mods' hostile creatures implement this :(
				if(e instanceof IMob){//hopefully this identifies potentially "hostile" creatures
					BlockPos thisPos = getPos();
					float dist = MathHelper.sqrt_double(e.getDistanceSqToCenter(thisPos));
					if(dist<radius){
						//get the distance (x and z) from the center
						double dx = e.posX-(thisPos.getX()+0.5);
						double dz = e.posZ-(thisPos.getZ()+0.5);
						//get the angle (RAD) with the center of our rune
						double mobAngle	= Math.atan2(dz, dx);
						//get how close the mob is to the center, proportionally to the radius
						float propDist = ((radius / dist) * 0.5F);
						//push it back
						e.motionX+=MathHelper.cos((float) mobAngle)*propDist;
						e.motionZ+=MathHelper.sin((float) mobAngle)*propDist;
					}
				}
			}
		}
	}
	/* (non-Javadoc)
	 * @see xilef11.mc.runesofwizardry_classics.runes.entity.FueledRuneEntity#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		radius=compound.getInteger("radius");
	}
	/* (non-Javadoc)
	 * @see xilef11.mc.runesofwizardry_classics.runes.entity.FueledRuneEntity#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("radius", radius);
	}
}
