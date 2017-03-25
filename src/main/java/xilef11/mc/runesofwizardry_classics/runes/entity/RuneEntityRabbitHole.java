package xilef11.mc.runesofwizardry_classics.runes.entity;
import java.util.List;
import java.util.Set;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xilef11.mc.runesofwizardry_classics.ModLogger;
import xilef11.mc.runesofwizardry_classics.items.EnumDustTypes;
import xilef11.mc.runesofwizardry_classics.runes.RuneRabbitHole;
import xilef11.mc.runesofwizardry_classics.utils.Utils.Coords;

import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;
public class RuneEntityRabbitHole extends RuneEntity {
	public RuneEntityRabbitHole(ItemStack[][] actualPattern, EnumFacing facing,
			Set<BlockPos> dusts, TileEntityDustActive entity, RuneRabbitHole creator) {
		super(actualPattern, facing, dusts, entity, creator);
	}
	private static int cover=2;//number of blocks between the rune and the hideout
	private BlockPos torch=null;
	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,
			ItemStack[] sacrifice, boolean negated) {
		World world = player.world;
		if(!world.isRemote){
			//FX
			entity.setupStar(0xFFFFFF, 0xFFFFFF);
			entity.setDrawStar(true);
			if(!negated && !player.capabilities.isCreativeMode){
				if(player.getFoodStats().getFoodLevel()>=4){
					player.getFoodStats().addStats(-4, 0);
				}else{
					this.onPatternBrokenByPlayer(player);
					return;
				}
			}
			int rad=0,height=0;
			switch(findDustType()){
			case PLANT: rad=1;height=3;//3x3x3
				break;
			case GUNPOWDER: rad=2;height=3;//5x5x3
				break;
			case LAPIS: rad=3;height=5;//5x5x5 in the original, we'll make it 7x7x5
				break;
			case BLAZE: rad=4;height=6;//9x9x6
				break;
			default:this.onPatternBroken();return;
			}
			torch = getPos().down(height+cover);
			for(int x=-rad;x<=rad;x++){
				for(int z=-rad;z<=rad;z++){
					for(int y=-height;y<=-1;y++){
						BlockPos current = getPos().add(x, (y-cover), z);
						if(y==-1){
							//top slice
							if(world.getBlockState(current.up()).getBlock() instanceof BlockFalling){
								world.setBlockState(current.up(), Blocks.COBBLESTONE.getDefaultState());
							}
						}else if(y==-height){//bottom slice
							//this makes sure we have a platform to land on by filling air + liquid blocks
							if(!world.getBlockState(current.down()).getMaterial().isSolid()){
								world.setBlockState(current.down(), Blocks.COBBLESTONE.getDefaultState());
							}
						}
						//don't break obsidian or bedrock
						IBlockState state = world.getBlockState(current);
						if(state.getBlockHardness(world, current)<Blocks.OBSIDIAN.getDefaultState().getBlockHardness(world, current) && world.getBlockState(current).getBlock()!=Blocks.BEDROCK){
							world.setBlockToAir(current);
						}
					}
				}
			}
			//place the torch
			world.setBlockState(torch, Blocks.TORCH.getDefaultState());
		}
	}
	private EnumDustTypes findDustType() {
		Coords c = ((RuneRabbitHole)creator).getVariableDusts().iterator().next();
		return EnumDustTypes.getByMeta(placedPattern[c.row][c.col].getMetadata());
	}
	private boolean setTorchPos(){
		int height=0;
		switch(findDustType()){
		case PLANT: height=3;//3x3x3
			break;
		case GUNPOWDER: height=3;//5x5x3
			break;
		case LAPIS: height=5;//5x5x5 in the original, we'll make it 7x7x5
			break;
		case BLAZE: height=6;//9x9x6
			break;
		default:this.onPatternBroken();return false;
		}
		torch = getPos().down(height+cover);
		return true;
	}
	private int delay=0;
	@Override
	public void update() {
		if(torch==null && !setTorchPos())return;
		World world = entity.getWorld();
			if(!world.isRemote){
				delay--;
				if(delay<=0){
				List<EntityPlayer> up = world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(getPos(), getPos().add(1,2,1)));
				for(EntityPlayer p:up){
					if(p.isSneaking()){
						//ModLogger.logInfo("Torch: "+torch);
						p.setPositionAndUpdate(torch.getX()+0.5, torch.getY()+0.5, torch.getZ()+0.5);
						p.fallDistance=0;
						delay=15;
						world.playSound(null,p.posX, p.posY, p.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 0.5F, 3.0F);
					}
				}
				if(up.isEmpty()){
					up = world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(torch, torch.add(1,2,1)));
					for(EntityPlayer p:up){
						if(p.isSneaking()){
							ModLogger.logInfo("pos: "+getPos());
							p.setPositionAndUpdate(getPos().getX()+0.5, getPos().getY()+0.5, getPos().getZ()+0.5);
							p.fallDistance=0;
							delay=15;
							world.playSound(null,p.posX, p.posY, p.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 0.5F, 3.0F);
						}
					}
				}
			}else{
				delay--;
			}
		}
	}
}
