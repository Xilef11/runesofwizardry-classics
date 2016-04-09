package xilef11.mc.runesofwizardry_classics.runes.entity;

import java.util.Set;

import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import xilef11.mc.runesofwizardry_classics.Refs;

import com.zpig333.runesofwizardry.api.IRune;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

public class RuneEntityHellstorm extends FueledRuneEntity {

	public RuneEntityHellstorm(ItemStack[][] actualPattern, EnumFacing facing,
			Set<BlockPos> dusts, TileEntityDustActive entity, IRune creator) {
		super(actualPattern, facing, dusts, entity, creator);
	}

	@Override
	protected int initialTicks() {
		return Refs.TICKS_PER_DAY/2;
	}
	//original radius was 100 blocks, with 20 arrows every 5 ticks
	private static final int RAD=32,AMOUNT=6,TICKRATE=7;
	/* (non-Javadoc)
	 * @see xilef11.mc.runesofwizardry_classics.runes.entity.FueledRuneEntity#update()
	 */
	@Override
	public void update() {
		super.update();
		World world = entity.getWorld();
		if(!world.isRemote && entity.ticksExisted()%TICKRATE==0){
			BlockPos pos = getPos();
			for(int i=0;i<AMOUNT;i++){
				double x = pos.getX()+Math.random() * RAD * 2 - RAD,
					   z = pos.getZ()+Math.random() * RAD * 2 - RAD;
				BlockPos spawn = new BlockPos(x,pos.getY()+94,z);
				while(!world.isAirBlock(spawn)&&spawn.getY()>pos.getY()+1)spawn=spawn.down();
				//Original spawned arrows at a fixed height of 158
				EntityArrow arrow = new EntityArrow(world, x, spawn.getY(), z);
				arrow.motionX=0;
				arrow.motionZ=0;
				arrow.motionY=-2D;
				arrow.setFire(30);
				arrow.canBePickedUp=0;//why is this an int...
				//arrow.setThrowableHeading(0, -1, 0, 2F, 0);
				world.spawnEntityInWorld(arrow);
			}
		}
	}
	
}
