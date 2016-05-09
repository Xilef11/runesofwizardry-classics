package xilef11.mc.runesofwizardry_classics.runes.entity;

import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xilef11.mc.runesofwizardry_classics.items.EnumDustTypes;
import xilef11.mc.runesofwizardry_classics.runes.RuneDepths;
import xilef11.mc.runesofwizardry_classics.utils.Utils.Coords;

import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.core.WizardryRegistry;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;
import com.zpig333.runesofwizardry.util.Utils;

public class RuneEntityDepths extends RuneEntity {

	public RuneEntityDepths(ItemStack[][] actualPattern, EnumFacing facing,
			Set<BlockPos> dusts, TileEntityDustActive entity, RuneDepths creator) {
		super(actualPattern, facing, dusts, entity, creator);
	}

	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,
			ItemStack[] sacrifice, boolean negated) {
		World world = player.worldObj;
		if(!world.isRemote){
			Coords c = ((RuneDepths)creator).getVariableDusts().iterator().next();
			EnumDustTypes type = EnumDustTypes.getByMeta(placedPattern[c.row][c.col].getMetadata());
			ItemStack wanted=null;
			int size=1;
			//sacrifice + size is dependant on dust used
			switch(type){
			case PLANT:size=8;wanted = new ItemStack(Blocks.LOG,2);
				break;
			case GUNPOWDER:size=16;wanted = new ItemStack(Blocks.LOG,2);
				break;
			case LAPIS:size=20;wanted = new ItemStack(Items.COAL,2);
				break;
			case BLAZE:size=48;wanted = new ItemStack(Items.COAL,2);
				break;
			default:size=0;
				break;
			}
			if(!(negated || Utils.stacksEqualWildcardSize(wanted, sacrifice[0], creator.allowOredictSacrifice()))){
				this.onPatternBroken();//or return the sacrifice and add a message
				return;
			}
			BlockPos beginPos = getPos().offset(face).down();
			if(!world.isAirBlock(beginPos)){
				this.onPatternBroken();
			}
			for(int dy=0;dy<size;dy++){
				BlockPos current = beginPos.down(dy);
				IBlockState state = world.getBlockState(current);
				Block toBreak = state.getBlock();
				toBreak.onBlockDestroyedByPlayer(world, current, state);
				toBreak.dropBlockAsItem(world, current, state, 0);
				world.setBlockToAir(current);
			}
			BlockPos last = beginPos.down(1);
			//world.spawnEntityInWorld(new EntityLightningBolt(world, last.getX(), last.getY()+1, last.getZ()));
			//lightning eats sacrifice negator, so we'll spawn a new one
			world.addWeatherEffect(new EntityLightningBolt(world, last.getX(), last.getY()+1, last.getZ(),true));//XXX might need a +.5
			if(negated)xilef11.mc.runesofwizardry_classics.utils.Utils.spawnItemCentered(world, getPos(), new ItemStack(WizardryRegistry.sacrifice_negator));
		}
	}

	@Override
	public void update() {
		BlockPos topLeft = getPos().offset(this.face,2).offset(face.rotateYCCW(),1).down();
		BlockPos bottomR = getPos().offset(face.rotateY(),2).offset(face.getOpposite()).up();
		List<EntityLiving> entities = entity.getWorld().getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(topLeft, bottomR));
		for(EntityLiving e:entities){
			e.extinguish();
		}
		if(entity.ticksExisted()>40)this.onPatternBroken();
	}

}
