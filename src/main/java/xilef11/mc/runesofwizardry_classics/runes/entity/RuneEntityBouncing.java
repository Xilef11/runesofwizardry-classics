package xilef11.mc.runesofwizardry_classics.runes.entity;
import java.util.List;
import java.util.Set;

import com.zpig333.runesofwizardry.api.IRune;
import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
public class RuneEntityBouncing extends RuneEntity {
	public RuneEntityBouncing(ItemStack[][] actualPattern, EnumFacing facing,
			Set<BlockPos> dusts, TileEntityDustActive entity,IRune creator) {
		super(actualPattern, facing, dusts, entity,creator);
	}
	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,
			ItemStack[] sacrifice, boolean negated) {
	}
	@Override
	public void update() {
		World world = entity.getWorld();
		if(!world.isRemote){
			//remove fall damage for all entities within the action radius
			int radius=6;
			List<EntityLivingBase> negateFall = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(getPos().add(-radius, -1, -radius), getPos().add(radius,radius,radius)));
			for(EntityLivingBase ent:negateFall){
				ent.fallDistance=0;
			}
		}
	}
}
