package xilef11.mc.runesofwizardry_classics.runes.entity;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import xilef11.mc.runesofwizardry_classics.items.EnumDustTypes;
import xilef11.mc.runesofwizardry_classics.runes.RuneLumber;
import xilef11.mc.runesofwizardry_classics.utils.Utils.Coords;

import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;
import com.zpig333.runesofwizardry.util.Utils;
public class RuneEntityLumber extends RuneEntity {
	public RuneEntityLumber(ItemStack[][] actualPattern, EnumFacing facing,
			Set<BlockPos> dusts, TileEntityDustActive entity, RuneLumber creator) {
		super(actualPattern, facing, dusts, entity, creator);
	}
	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,
			ItemStack[] sacrifice, boolean negated) {
		World world = player.worldObj;
		if(!world.isRemote){
			Coords c = ((RuneLumber)creator).getVariableDusts().iterator().next();
			dust = EnumDustTypes.getByMeta((placedPattern[c.row][c.col].getMetadata()));
			switch(dust){
			case PLANT:rad=6;doubleC=0.12;stickC=0.2;maxDouble=2;maxStick=2;dustChance=0.02;maxDust=1;
			break;
			case GUNPOWDER:rad=9;doubleC=0.2;stickC=0.35;maxDouble=2;maxStick=3;dustChance=0.065;maxDust=1;
			break;
			case LAPIS:rad=12;doubleC=0.35;stickC=0.5;maxDouble=3;maxStick=3;dustChance=0.1;maxDust=2;
			break;
			case BLAZE:rad=16;doubleC=0.5;stickC=0.65;maxDouble=3;maxStick=4;dustChance=0.187;maxDust=2;
			break;
			default:this.onPatternBroken();return;
			}
			IBlockState state = world.getBlockState(getPos().offset(face));
			Block block = state.getBlock();
			ItemStack stack = new ItemStack(block, 1, block.getMetaFromState(state));
			if(!Utils.stacksEqualWildcardSize(new ItemStack(Blocks.LOG),stack , true)){
				this.onPatternBroken();return;
			}
		}
	}
	private EnumDustTypes dust=EnumDustTypes.PLANT;
	private int rad=0,maxDouble=0,maxStick=0,maxDust=0;
	private double doubleC=0,stickC=0,dustChance=0;
	@Override
	public void update() {
		World world = entity.getWorld();
		if(!world.isRemote){
			BlockPos pos = getPos();
			int x=pos.getX(),y=pos.getY(),z=pos.getZ();
			if (Math.random() < 0.08){//spawn particles
				for (int dx = -rad; dx <= rad; dx++){
					for (int dy = -rad / 2; dy <= rad / 2; dy++){
						for (int dz = -rad; dz <= rad; dz++){
							if (isWood(world,pos.add(dx, dy, dz))){
								spawnExplosionParticle(world, x + dx, y + dy, z + dz);
							}
						}
					}
				}
			}
			if (entity.ticksExisted() > 100){//chop trees
				for (int dx = -rad; dx <= rad; dx++){
					for (int dy = -rad / 2; dy <= rad / 2; dy++){
						for (int dz = -rad; dz <= rad; dz++){
							BlockPos current = pos.add(dx,dy,dz);
							if (isWood(world,current)){
								chopTree(world,current,current);
							}
						}
					}
				}
				this.onPatternBroken();
			}
		}
	}
	private static final int RANGE=16;
	private void chopTree(World world, BlockPos current,BlockPos start){
		int dx = Math.abs(current.getX()-start.getX()),
			dy = Math.abs(current.getY()-start.getY()),
			dz = Math.abs(current.getZ()-start.getZ());
		if(dx>RANGE||dy>RANGE||dz>RANGE )return;//don't chop if the distance from the start block is more than 16 blocks
		IBlockState state = world.getBlockState(current);
		Block block = state.getBlock();
		Random rand = new Random();
		if(Math.random()<doubleC){//double logs
			for(int a=rand.nextInt(maxDouble);a>0;a--){
				block.dropBlockAsItem(world, current, state,1);
			}
		}
		if(Math.random()<stickC){//sticks
			for(int a=rand.nextInt(maxStick);a>0;a--){
				xilef11.mc.runesofwizardry_classics.utils.Utils.spawnItemCentered(world, current, new ItemStack(Items.STICK));
			}
		}
		//break the block
		block.dropBlockAsItem(world, current, state,1);
		world.setBlockToAir(current);
		for (int i = -2; i <= 2; i++){//checks blocks around and recursively chop the tree
            for (int j = 0; j <= 1; j++){
                for (int k = -2; k <= 2; k++){
                    if (i == 0 || k == 0){//don't check diagonals of current block (trees don't grow diagonals (?))
                    	BlockPos pos = current.add(i,j,k);
                        if (isWood(world,pos)){
                            chopTree(world, pos,start);
                        } else if(isLeaves(world,pos)){
                            chopLeaves(world, pos, start);
                        }
                    }
                }
            }
		}
	}
	private void chopLeaves(World world, BlockPos current, BlockPos start){
		int dx = Math.abs(current.getX()-start.getX()),
				dy = Math.abs(current.getY()-start.getY()),
				dz = Math.abs(current.getZ()-start.getZ());
		if(dx>RANGE||dy>RANGE||dz>RANGE )return;//don't chop if the distance from the start block is more than 16 blocks
		Random rand = new Random();
		if(Math.random()<dustChance){//drop dust
			for(int a=rand.nextInt(maxDust);a>=0;a--){
				xilef11.mc.runesofwizardry_classics.utils.Utils.spawnItemCentered(world, current, EnumDustTypes.PLANT.getStack(1));
			}
		}
		//remove next 3 lines to not drop saplings
		IBlockState state = world.getBlockState(current);
		Block block = state.getBlock();
		block.dropBlockAsItem(world, current, state, 1);
		//break block
		world.setBlockToAir(current);
		//recursively break connected leaves
		for (int i = -2; i <= 2; i++){
            for (int j = 0; j <= 1; j++){
                for (int k = -2; k <= 2; k++){
                    if (i == 0 || k == 0){//don't check diagonals of current block (trees don't grow diagonals (?))
                	BlockPos pos = current.add(i,j,k);
//                        if (isWood(world,pos))
//                        {
//                            chopTree(world, pos,start);
//                        } else
                    	if(isLeaves(world,pos)){
                           chopLeaves(world, pos, start);
                        }
                    }
                }
            }
        }
	}
	private static boolean isLeaves(World world, BlockPos pos){
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		ItemStack stack = new ItemStack(block, 1, block.getMetaFromState(state));
		if(stack.getItem()==null)return false;
		return Utils.stacksEqualWildcardSize(new ItemStack(Blocks.LEAVES),stack , true);
	}
	private static boolean isWood(World world, BlockPos pos){
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		ItemStack stack = new ItemStack(block, 1, block.getMetaFromState(state));
		if(stack.getItem()==null)return false;
		return Utils.stacksEqualWildcardSize(new ItemStack(Blocks.LOG),stack , true);
	}
	private static void spawnExplosionParticle(World world, double x, double y, double z){
		double width = 1.3d;
		double height = 1.3d;
		if(world instanceof WorldServer){
			WorldServer ws = (WorldServer)world;
			ws.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, x + width/2, y + height/2, z + width/2,ws.rand.nextInt(5), 0d, 0d, 0d, 0d,5);
		}else{
			world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, x + width/2, y + height/2, z + width/2, 0, 0, 0, 5);
		}
	}
}
