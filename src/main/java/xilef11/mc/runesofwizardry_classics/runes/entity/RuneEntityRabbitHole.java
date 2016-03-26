package xilef11.mc.runesofwizardry_classics.runes.entity;

import java.util.List;
import java.util.Set;

import net.minecraft.block.BlockFalling;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;
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
		World world = player.worldObj;
		if(!world.isRemote){
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
//			BlockPos nw = getPos().add(-rad, height+cover, -rad);
//			BlockPos se = getPos().add(rad, cover, rad);
			torch = getPos().down(height+cover);
			for(int x=-rad;x<=rad;x++){
				for(int z=-rad;z<=rad;z++){
					for(int y=-height;y<=-1;y++){
						BlockPos current = getPos().add(x, (y-cover), z);
						if(y==-1){
							//top slice
							if(world.getBlockState(current.up()).getBlock() instanceof BlockFalling){
								world.setBlockState(current.up(), Blocks.cobblestone.getDefaultState());
							}
						}else if(y==-height){//bottom slice
							//not sure if this is the right instanceof
							if(world.getBlockState(current.down()).getBlock() instanceof BlockFluidBase){
								world.setBlockState(current.down(), Blocks.cobblestone.getDefaultState());
							}
						}
						//don't break obsidian or bedrock
						if(world.getBlockState(current).getBlock().getBlockHardness(world, current)<Blocks.obsidian.getBlockHardness(world, current)){
							world.setBlockToAir(current);
						}
					}
				}
			}
			//place the torch
			world.setBlockState(torch, Blocks.torch.getDefaultState());
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
		//FIXME this part is broken
		if(torch==null && !setTorchPos())return;
		World world = entity.getWorld();
			if(!world.isRemote){
				delay--;
				if(delay<=0){
				List<EntityPlayer> up = world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(getPos(), getPos().add(1,2,1)));
				for(EntityPlayer p:up){
					if(p.isSneaking()){
						p.setPosition(torch.getX()+0.5, torch.getY()+1.5, torch.getZ()+0.5);
						p.fallDistance=0;
						delay=45;
					}
				}
				if(up.isEmpty()){
					up = world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(torch, torch.add(1,2,1)));
					for(EntityPlayer p:up){
						if(p.isSneaking()){
							p.setPosition(getPos().getX()+0.5, getPos().getY()+1.5, getPos().getZ()+0.5);
							p.fallDistance=0;
							delay=45;
						}
					}
				}
			}else{
				delay--;
			}
		}
	}

}
