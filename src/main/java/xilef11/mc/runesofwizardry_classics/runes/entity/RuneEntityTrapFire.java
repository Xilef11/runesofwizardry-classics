package xilef11.mc.runesofwizardry_classics.runes.entity;

import java.util.List;
import java.util.Set;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.items.EnumDustTypes;
import xilef11.mc.runesofwizardry_classics.runes.RuneTrapFire;
import xilef11.mc.runesofwizardry_classics.utils.Utils.Coords;

import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

public class RuneEntityTrapFire extends RuneEntity {

	public RuneEntityTrapFire(ItemStack[][] actualPattern, EnumFacing facing,
			Set<BlockPos> dusts, TileEntityDustActive entity, RuneTrapFire creator) {
		super(actualPattern, facing, dusts, entity, creator);
	}

	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,
			ItemStack[] sacrifice, boolean negated) {

	}

	@Override
	public void update() {
		if(entity.ticksExisted()==5*Refs.TPS)this.renderActive=false;
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#handleEntityCollision(net.minecraft.world.World, net.minecraft.util.BlockPos, net.minecraft.block.state.IBlockState, net.minecraft.entity.Entity)
	 */
	@Override
	public boolean handleEntityCollision(World worldIn, BlockPos pos,
			IBlockState state, Entity entityIn) {
		if(/*entityIn instanceof EntityLiving &&*/ !worldIn.isRemote &&entity.ticksExisted()>5*20){//could leave it to any entity
			Coords c = ((RuneTrapFire)creator).getVariableDusts().iterator().next();
			int rad=0,damagebase=0,damagerand=0;
			double fire=0;
			switch(EnumDustTypes.getByMeta(placedPattern[c.row][c.col].getMetadata())){
			case GUNPOWDER:rad=3;damagebase=5;damagerand=2;fire=0.05;
				break;
			case LAPIS:rad=4;damagebase=7;damagerand=4;fire=0.12;
				break;
			case BLAZE:rad=6;damagebase=10;damagerand=8;fire=0.4;
				break;
			default: this.onPatternBroken();return true;//leave everything at 0
			}
			//light all entities in radius
			List<EntityLivingBase> ents = worldIn.getEntitiesWithinAABB(EntityLivingBase.class,new AxisAlignedBB(getPos().add(-rad, -rad, -rad), getPos().add(rad,rad,rad)));
			//not getting any entities
			for(EntityLivingBase e:ents){
				e.setFire(damagebase+(int)(Math.random()*damagerand));
			}
			//set the world around on fire
			for (int dx = -rad; dx <= rad; dx++){
				for (int dy = -rad; dy <= rad; dy++){
					for (int dz = -rad; dz <= rad; dz++){
						BlockPos current = getPos().add(dx, dy, dz);
						//if we have an air block over a non-air block
						if(!worldIn.isAirBlock(current.down())&&worldIn.isAirBlock(current)&&(Math.random()<fire)){
							worldIn.setBlockState(current, Blocks.fire.getDefaultState());
						}
					}
				}
			}
			this.onPatternBroken();
		}
		return true;
	}
	

}
