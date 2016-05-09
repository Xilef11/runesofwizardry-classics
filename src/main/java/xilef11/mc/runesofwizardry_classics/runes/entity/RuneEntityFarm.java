package xilef11.mc.runesofwizardry_classics.runes.entity;

import java.util.Random;import java.util.Set;import net.minecraft.block.Block;import net.minecraft.entity.player.EntityPlayer;import net.minecraft.init.Blocks;import net.minecraft.item.ItemStack;import net.minecraft.util.EnumFacing;import net.minecraft.util.math.BlockPos;import net.minecraft.world.World;import xilef11.mc.runesofwizardry_classics.items.EnumDustTypes;import xilef11.mc.runesofwizardry_classics.runes.RuneFarm;import xilef11.mc.runesofwizardry_classics.utils.Utils.Coords;import com.zpig333.runesofwizardry.api.RuneEntity;import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;
/*
 * Behaviour:
 * makes a nxn patch of farmland with water in the middle and crops planted
 * plant is 3x3, newly planted crops
 * gunpowder is 5x5 , slightly more grown crops
 * lapis is 7x7 , 50% grown crops
 * blaze is 9x9 , some fully grown crops, some ~50%
 * 
 * Only replaces dirt, sand and grass blocks with farmland, replaces anything (BUG: including bedrock) with water
 */
public class RuneEntityFarm extends RuneEntity {

	public RuneEntityFarm(ItemStack[][] actualPattern, EnumFacing facing,
			Set<BlockPos> dusts, TileEntityDustActive entity, RuneFarm creator) {
		super(actualPattern, facing, dusts, entity, creator);
	}
	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,
			ItemStack[] sacrifice, boolean negated) {
		World world = player.worldObj;
		if(!world.isRemote){
			Coords c = ((RuneFarm)creator).getVariableDusts().iterator().next();
			int rad, cropBase, cropRand;
			switch(EnumDustTypes.getByMeta(placedPattern[c.row][c.col].getMetadata())){
			case PLANT:rad=1;cropBase=0;cropRand=2;
				break;
			case GUNPOWDER:rad=2;cropBase=1;cropRand=3;
				break;
			case LAPIS:rad=3;cropBase=3;cropRand=3;
				break;
			case BLAZE:rad=4;cropBase=4;cropRand=5;
				break;
			default:this.onPatternBroken();return;
			}
			//crops (XXX replace with config stuff...)
			Random rand = new Random();
			Block crop=Blocks.WHEAT;
			switch(rand.nextInt(3)){
			case 0: crop = Blocks.WHEAT;break;
			case 1: crop = Blocks.CARROTS;break;
			case 2: crop = Blocks.POTATOES;break;
			}
			this.onPatternBroken();//deactivate the rune before replacing the blocks to avoid errors
			//replace with farmland and crops
			for(int i=-rad;i<=rad;i++){
				for(int j=-rad;j<=rad;j++){
					if(i!=0||j!=0){
						BlockPos current = getPos().down().east(i).south(j);
						Block block = world.getBlockState(current).getBlock();
						//XXX this should be replaced by something in the config to avoid hardcoded stuff
						if(block==Blocks.GRASS||block==Blocks.DIRT||block==Blocks.SAND||block==Blocks.FARMLAND){
							world.setBlockState(current, Blocks.FARMLAND.getDefaultState());
							int meta = cropBase + rand.nextInt(cropRand);
							if(meta>7)meta=7;//XXX replace with something like AGE.max value
							world.setBlockState(current.up(), crop.getStateFromMeta(meta));
						}
					}
				}
			}
			if(world.getBlockState(getPos().down()).getBlock()!=Blocks.BEDROCK)world.setBlockState(getPos().down(), Blocks.WATER.getDefaultState());
			world.setBlockToAir(getPos());
		}
	}

	@Override
	public void update() {

	}

}
