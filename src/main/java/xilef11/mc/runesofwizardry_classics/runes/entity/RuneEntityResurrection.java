package xilef11.mc.runesofwizardry_classics.runes.entity;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItem;
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
import xilef11.mc.runesofwizardry_classics.runes.RuneResurrection;

import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

public class RuneEntityResurrection extends RuneEntity {
	public RuneEntityResurrection(ItemStack[][] actualPattern,
			EnumFacing facing, Set<BlockPos> dusts,
			TileEntityDustActive entity, RuneResurrection creator) {
		super(actualPattern, facing, dusts, entity, creator);
	}
	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,
			ItemStack[] sacrifice, boolean negated) {
		if(!player.worldObj.isRemote){
			//nothing I think
		}
	}
	private static final int RAD=2;
	@Override
	public void update() {
		World world = entity.getWorld();
		if(!world.isRemote){
			List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(getPos().add(-RAD,0,-RAD), getPos().add(RAD,1,RAD)));
			List<ItemStack> stacks = new LinkedList<ItemStack>();
			int numItems= 0;
			for(EntityItem ei:items){
				ItemStack s = ei.getEntityItem();
				stacks.add(s);
				numItems+=s.stackSize;
			}
			if(numItems>1){
				//try to get the entity
				String id = ((RuneResurrection)creator).entityIDFromDrops(stacks);
				//spawn the entity
				if(id!=null){
					for(EntityItem ei:items){
						ei.setDead();
						((WorldServer)world).spawnParticle(EnumParticleTypes.SMOKE_NORMAL, false, ei.posX, ei.posY, ei.posZ, 1, 0d, 0.5d, 0d, 0d);
					}
					Entity e = EntityList.createEntityByName(id, world);
					e.setPositionAndUpdate(getPos().getX()+0.5, getPos().getY()+0.5, getPos().getZ()+0.5);
					//XXX could add a delay before spawning the entity
					world.spawnEntityInWorld(e);
					//world.playSoundAtEntity(e, "mob.chicken.plop", 0.5F, 0.8F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
					world.playSound(e.posX, e.posY, e.posZ, SoundEvents.ENTITY_CHICKEN_EGG, SoundCategory.BLOCKS, 0.5F, 0.8F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F, false);
					this.onPatternBroken();
				}
			}
		}

	}

}
