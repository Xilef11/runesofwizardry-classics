package xilef11.mc.runesofwizardry_classics.runes.entity;

import java.util.List;
import java.util.Set;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.items.EnumDustTypes;
import xilef11.mc.runesofwizardry_classics.runes.RuneTrapPoison;
import xilef11.mc.runesofwizardry_classics.utils.Utils.Coords;

import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

public class RuneEntityTrapPoison extends RuneEntity {

	public RuneEntityTrapPoison(ItemStack[][] actualPattern, EnumFacing facing,
			Set<BlockPos> dusts, TileEntityDustActive entity, RuneTrapPoison creator) {
		super(actualPattern, facing, dusts, entity, creator);
	}

	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,
			ItemStack[] sacrifice, boolean negated) {
		// Nothing to do here

	}

	@Override
	public void update() {
		if(entity.ticksExisted()==5*Refs.TPS)this.renderActive=false;
		// not here
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#handleEntityCollision(net.minecraft.world.World, net.minecraft.util.BlockPos, net.minecraft.block.state.IBlockState, net.minecraft.entity.Entity)
	 */
	@Override
	public boolean handleEntityCollision(World worldIn, BlockPos pos,
			IBlockState state, Entity entityIn) {
		if(/*entityIn instanceof EntityLiving &&*/ !worldIn.isRemote &&entity.ticksExisted()>5*20){//could leave it to any entity
			Coords c = ((RuneTrapPoison)creator).getVariableDusts().iterator().next();
			int rad=0,poisonbase=0,poisonrand=0;
			switch(EnumDustTypes.getByMeta(placedPattern[c.row][c.col].getMetadata())){
			case GUNPOWDER:rad=3;poisonbase=5;poisonrand=2;
				break;
			case LAPIS:rad=4;poisonbase=7;poisonrand=4;
				break;
			case BLAZE:rad=6;poisonbase=10;poisonrand=8;
				break;
			default: this.onPatternBroken();return true;//leave everything at 0
			}
			//poison all entities in radius
			List<EntityLivingBase> ents = worldIn.getEntitiesWithinAABB(EntityLivingBase.class,new AxisAlignedBB(getPos().add(-rad, -rad, -rad), getPos().add(rad,rad,rad)));
			for(EntityLivingBase e:ents){
				e.addPotionEffect(new PotionEffect(MobEffects.POISON, (poisonbase + ((int)Math.floor(Math.random() * (double)poisonrand))) * 20, 2));
			}
			this.onPatternBroken();
		}
		return true;
	}
	

}
