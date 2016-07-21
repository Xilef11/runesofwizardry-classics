package xilef11.mc.runesofwizardry_classics.runes.entity;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import scala.actors.threadpool.Arrays;
import xilef11.mc.runesofwizardry_classics.ModLogger;
import xilef11.mc.runesofwizardry_classics.items.EnumDustTypes;
import xilef11.mc.runesofwizardry_classics.runes.RuneMountain;
import xilef11.mc.runesofwizardry_classics.utils.Utils;
import xilef11.mc.runesofwizardry_classics.utils.Utils.Coords;

import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.core.WizardryRegistry;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive.BeamType;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustPlaced;
/* Behaviour:
 * (DELiftTerrain)
 * lifts an area delimited by clay blocks (which must be connected to the rune)
 * can't test original, has a tendency to crash the game
 * 
 * dust type defines height:
 * 		plant: 12
 * 		gunpowder: 16
 * 		lapis: 22
 * 		blaze: 32
 * 
 */
public class RuneEntityMountain extends RuneEntity {
	
	public RuneEntityMountain(ItemStack[][] actualPattern, EnumFacing facing,
			Set<BlockPos> dusts, TileEntityDustActive entity, RuneMountain creator) {
		super(actualPattern, facing, dusts, entity, creator);
	}
	public static final int TICKRATE = 32;
	private boolean gotGolem=false;
	private int height=0;
	private LinkedHashSet<BlockPos> initialPos;
	private int[] currentHeight;
	
	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,ItemStack[] sacrifice, boolean negated) {
		World world=player.worldObj;
		if(!world.isRemote){
			if(!negated && !Utils.takeXP(player, 10)){
				this.onPatternBrokenByPlayer(player);
				return;
			}
			Coords c = ((RuneMountain)creator).getVariableDusts().iterator().next();
			switch(EnumDustTypes.getByMeta(this.placedPattern[c.row][c.col].getMetadata())){
			case PLANT:height=12;
				break;
			case GUNPOWDER:height=16;
				break;
			case LAPIS:height=22;
				break;
			case BLAZE:height=32;
				break;
			default:ModLogger.logError("Wrong Dust Type in RuneEntityMountain#onRuneActivatedByPlayer");
				break;
			}
			entity.setupStar(0xFFFF00, 0xFFFFFF);
			entity.setupBeam(0, BeamType.SPIRAL);
			entity.setDrawStar(true);
			initialPos = findArea(world,getPos());
			currentHeight = new int[initialPos.size()];
			Arrays.fill(currentHeight, 0);
		}

	}
	
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#handleEntityCollision(net.minecraft.world.World, net.minecraft.util.math.BlockPos, net.minecraft.block.state.IBlockState, net.minecraft.entity.Entity)
	 */
	@Override
	public boolean handleEntityCollision(World worldIn, BlockPos pos,IBlockState state, Entity entityIn) {
		if(!worldIn.isRemote&&!gotGolem){
			if(entityIn instanceof EntityIronGolem){
				gotGolem=true;
				((EntityIronGolem) entityIn).setDead();
				entity.setDrawBeam(true);
			}
		}
		return super.handleEntityCollision(worldIn, pos, state, entityIn);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		World world = entity.getWorld();
		if(!world.isRemote && gotGolem){
			if(entity.ticksExisted()<TICKRATE*2)return;
			if(entity.ticksExisted()%TICKRATE==0){
				Iterator<BlockPos> it = initialPos.iterator();
				//for all columns
		columns: for(int i=0;i<currentHeight.length&&it.hasNext();i++){
					int ch = currentHeight[i];
					BlockPos base = it.next();
					int bottom = base.getY()-ch;
					BlockPos nextUp = base.add(0, ch+1, 0);
					//special case for the dusts, we want to lift them and not stop.
					if(world.getBlockState(nextUp).getBlock()==WizardryRegistry.dust_placed){
						BlockPos up2=nextUp.up();
						//update the posSet of the rune if we will move the dust
						if(world.isAirBlock(up2)){
							TileEntity te = world.getTileEntity(nextUp);
							if(te instanceof TileEntityDustPlaced){
								TileEntityDustPlaced ted = (TileEntityDustPlaced)te;
								RuneEntity ent = ted.getRune();
								if(ent!=null){
									//replace the dust position in the rune with 1 block higher (because it will get moved)
									Iterator<BlockPos> dp = ent.dustPositions.iterator();
									while(dp.hasNext()){
										if(dp.next().equals(nextUp)){
											dp.remove();
											break;
										}
									}
									ent.dustPositions.add(up2);
								}
							}
						}
						nextUp=up2;
					}
					if(world.isAirBlock(nextUp)){
						for(int y=ch;y>bottom-base.getY();y--){
							BlockPos current = base.add(0,y,0);
							IBlockState toPush = world.getBlockState(current);
							BlockPos up = current.up();
							//IBlockState stateUp = world.getBlockState(up);
							if(toPush.getBlock()==Blocks.BEDROCK)continue columns;
							world.setBlockState(up, toPush);
							TileEntity te = world.getTileEntity(current);
							if(te!=null){
								NBTTagCompound tagCompound = te.writeToNBT(new NBTTagCompound());
								TileEntity newTE = world.getTileEntity(up);
								if(newTE!=null)newTE.readFromNBT(tagCompound);
							}
							world.setBlockToAir(current);
							//might need to do notifyBlockUpdate
						}
						currentHeight[i]++;
					}
					
				}
				if(entity.ticksExisted()>=TICKRATE*height)this.onPatternBroken();
			}
		}

	}
	
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		gotGolem = compound.getBoolean("GOLEM");
		height = compound.getInteger("HEIGHT");
		NBTTagList positions = (NBTTagList) compound.getTag("POSITIONS");
		initialPos = new LinkedHashSet<BlockPos>();
		for(int i=0;i<positions.tagCount();i++){
			NBTTagCompound coords = positions.getCompoundTagAt(i);
			int[] c = coords.getIntArray("Coords");
			initialPos.add(new BlockPos(c[0],c[1],c[2]));
		}
		currentHeight = compound.getIntArray("CURRENT_HEIGHT");
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setBoolean("GOLEM", gotGolem);
		compound.setInteger("HEIGHT", height);
		NBTTagList positions = new NBTTagList();
		for(BlockPos p:initialPos){
			NBTTagCompound coords = new NBTTagCompound();
			coords.setIntArray("Coords", new int[]{p.getX(),p.getY(),p.getZ()});
			positions.appendTag(coords);
		}
		compound.setTag("POSITIONS", positions);
		compound.setIntArray("CURRENT_HEIGHT", currentHeight);
	}

	private static LinkedHashSet<BlockPos> findArea(World world, BlockPos initial){
		LinkedHashSet<BlockPos> result = new LinkedHashSet<BlockPos>();
		//TODO find the area
		return result;
	}
}
