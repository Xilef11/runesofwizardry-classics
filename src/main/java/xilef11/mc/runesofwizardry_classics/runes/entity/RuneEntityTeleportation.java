package xilef11.mc.runesofwizardry_classics.runes.entity;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import xilef11.mc.runesofwizardry_classics.ModLogger;
import xilef11.mc.runesofwizardry_classics.managers.TeleportationData;
import xilef11.mc.runesofwizardry_classics.utils.Utils;

import com.zpig333.runesofwizardry.api.IRune;
import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;
public class RuneEntityTeleportation extends RuneEntity {
	public RuneEntityTeleportation(ItemStack[][] actualPattern,
			EnumFacing facing, Set<BlockPos> dusts,
			TileEntityDustActive entity, IRune creator) {
		super(actualPattern, facing, dusts, entity, creator);
	}
	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,ItemStack[] sacrifice, boolean negated) {
		World world = player.worldObj;
		if(!world.isRemote){
			//take the XP
			if(!(negated||Utils.takeXP(player, 5))){
				this.onPatternBrokenByPlayer(player);
				return;
			}
			//get the block below the blaze square
			IBlockState state = world.getBlockState(getPos().down());
			//register the teleport location
			TeleportationData locations = TeleportationData.get(world);
			locations.registerLocation(state, getPos());
		}
	}
	@Override
	public void update() {
		World world = entity.getWorld();
		if(!world.isRemote){
			//grab players on the rune
			List<EntityPlayer> players = world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(getPos(), getPos().add(1,1,1)));
			for(EntityPlayer p:players){
				//teleport them after delay
				//maybe we can use timeuntilportal for the delay
				//ModLogger.logInfo("Time until portal: "+p.timeUntilPortal);
				if(p.timeUntilPortal<=0){
					p.timeUntilPortal=105;
				}else if(p.timeUntilPortal<=5){
					//get possible destinations
					TeleportationData teles = TeleportationData.get(world);
					IBlockState net = world.getBlockState(getPos().down());
					Set<BlockPos> destinations = teles.getDestinations(net);
					if(destinations==null){
						ModLogger.logError("Tried to teleport while no destinations exist! "+net);
						return;
					}
					//select a random destination
					int index = new Random().nextInt(destinations.size());
					Iterator<BlockPos> it = destinations.iterator();
					for(int i=0;i<index;i++){//forward the iterator to the selected random index
						it.next();
					}
					BlockPos dest = it.next();
					if(!dest.equals(getPos())) {
						//could remove the check (would kill the player on arrival if not enough health)
						if (p.getHealth()>6) {
							p.setPositionAndUpdate(dest.getX()+0.5, dest.getY(), dest.getZ()+0.5);
							p.timeUntilPortal=0;
							p.fallDistance=0;
							//deal 3 hearts of damage
							p.attackEntityFrom(DamageSource.magic, 6);
							//sound + particles for fun
							world.playSound(getPos().getX(), getPos().getY(), getPos().getZ(), SoundEvents.ENTITY_ENDERMEN_TELEPORT,SoundCategory.PLAYERS, 1.0F, 1.0F,false);
							world.playSound(dest.getX(), dest.getY(), dest.getZ(), SoundEvents.ENTITY_ENDERMEN_TELEPORT,SoundCategory.PLAYERS, 1.0F, 1.0F,false);
							if(world instanceof WorldServer){
								WorldServer ws = (WorldServer)world;
								ws.spawnParticle(EnumParticleTypes.SPELL_WITCH, getPos().getX(), getPos().getY(), getPos().getZ(), 10, 0.5, 0.5, 0.5, 0);
								//ws.spawnParticle(EnumParticleTypes.PORTAL, getPos().getX(), getPos().getY(), getPos().getZ(), 0.5F, 0.5, 0.5);
								//ws.spawnParticle(EnumParticleTypes.PORTAL, dest.getX(), dest.getY(), dest.getZ(), 0.5F, 0.5, 0.5);
								ws.spawnParticle(EnumParticleTypes.PORTAL, dest.getX(), dest.getY(), dest.getZ(), 10, 0.5, 0.5, 0.5, 10);
							}
						}
					}else{
						p.timeUntilPortal=4;//to avoid waiting again if unlucky
					}
				}else if(p.timeUntilPortal>5){
					p.timeUntilPortal--;
				}
			}
		}
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#onPatternBroken()
	 */
	@Override
	public void onPatternBroken() {
		World world = entity.getWorld();
		if(!world.isRemote){
			super.onPatternBroken();
			TeleportationData locations = TeleportationData.get(world);
			IBlockState state = world.getBlockState(getPos().down());
			locations.removeLocation(state, getPos());
		}
		super.onPatternBroken();
	}
}
