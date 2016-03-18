package xilef11.mc.runesofwizardry_classics.runes.entity;

import java.util.List;
import java.util.Set;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import com.zpig333.runesofwizardry.api.IRune;
import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

public class RuneEntityBouncing extends RuneEntity {

	public RuneEntityBouncing(ItemStack[][] actualPattern, EnumFacing facing,
			Set<BlockPos> dusts, TileEntityDustActive entity,IRune creator) {
		super(actualPattern, facing, dusts, entity,creator);
	}

	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,
			ItemStack[] sacrifice, boolean negated) {
		// TODO FX

	}

	@Override
	public void update() {
		World world = entity.getWorld();
		if(!world.isRemote){
			//get all living on the rune
//			List<EntityLiving> onRune = world.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(getPos(), getPos().add(1,1,1)));
//			for(EntityLiving ent:onRune){
//				if(ent.motionY>0&&ent.isAirBorne){//if entity is going up (there is probably a better way to see if jumping)
//					ent.getJumpHelper().setJumping();
//					ent.getJumpHelper().doJump();
//					ent.addVelocity(0, 1.27D, 0);
//					ent.velocityChanged=true;
//				}
//			}
			//FIXME not cancelling fall damage
			//remove fall damage for all entities within the action radius
			int radius=6;
			List<EntityLiving> negateFall = world.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(getPos().add(-radius, 0, -radius), getPos().add(radius,radius,radius)));
			for(EntityLiving ent:negateFall){
				ent.fallDistance=0;
			}
		}
	}

}
