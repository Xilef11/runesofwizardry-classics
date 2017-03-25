package xilef11.mc.runesofwizardry_classics.runes.entity;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
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
public class RuneEntityTransport extends RuneEntity {
	public RuneEntityTransport(ItemStack[][] actualPattern, EnumFacing facing,
			Set<BlockPos> dusts, TileEntityDustActive entity, IRune creator) {
		super(actualPattern, facing, dusts, entity, creator);
	}
	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,
			ItemStack[] sacrifice, boolean negated) {
		if(!player.world.isRemote){
			if(!Utils.takeXP(player, 5)){
				this.onPatternBrokenByPlayer(player);
			}
		}
	}
	@Override
	public void update() {
		World world = entity.getWorld();
		if(!world.isRemote){
			if(entity.ticksExisted()>10*20)this.onPatternBroken();
			//big copy-paste from RuneEntityTeleportation
			//grab players on the rune
			List<EntityPlayer> players = world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(getPos(), getPos().add(1,1,1)));
			for(EntityPlayer p:players){
				//teleport them after delay
				//maybe we can use timeuntilportal for the delay
				//ModLogger.logInfo("Time until portal: "+p.timeUntilPortal);
				if(p.timeUntilPortal<=0){
					p.timeUntilPortal=55;
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
						p.setPositionAndUpdate(dest.getX()+0.5, dest.getY(), dest.getZ()+0.5);
						p.timeUntilPortal=0;
						p.fallDistance=0;
						//sound + particles for fun
						world.playSound(null,getPos().getX(), getPos().getY(), getPos().getZ(), SoundEvents.ENTITY_ENDERMEN_TELEPORT,SoundCategory.PLAYERS, 1.0F, 1.0F);
						world.playSound(null,dest.getX(), dest.getY(), dest.getZ(), SoundEvents.ENTITY_ENDERMEN_TELEPORT,SoundCategory.PLAYERS, 1.0F, 1.0F);
						if(world instanceof WorldServer){
							WorldServer ws = (WorldServer)world;
							ws.spawnParticle(EnumParticleTypes.SPELL_WITCH, getPos().getX(), getPos().getY(), getPos().getZ(), 10, 0d, 0d, 0d, 0);
							//ws.spawnParticle(EnumParticleTypes.PORTAL, getPos().getX(), getPos().getY(), getPos().getZ(), 0.5F, 0.5, 0.5);
							//ws.spawnParticle(EnumParticleTypes.PORTAL, dest.getX(), dest.getY(), dest.getZ(), 0.5F, 0.5, 0.5);
							ws.spawnParticle(EnumParticleTypes.PORTAL, dest.getX(), dest.getY(), dest.getZ(), 10, 0.5, 0.5, 0.5, 10);
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
}
