package xilef11.mc.runesofwizardry_classics.runes.entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
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
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import scala.actors.threadpool.Arrays;
import xilef11.mc.runesofwizardry_classics.ModLogger;
import xilef11.mc.runesofwizardry_classics.Refs;
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
	private int ticksSinceGolem=0;
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
			initialPos = findArea(world,getPos().down());
			if(initialPos==null){
				player.addChatMessage(new TextComponentTranslation(Refs.Lang.RUNE+".mountain.noarea"));
				this.onPatternBrokenByPlayer(player);
			}
			currentHeight = new int[initialPos.size()];
			Arrays.fill(currentHeight, 0);
			if(negated){
				gotGolem=true;
				entity.setDrawBeam(true);
			}
			
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
			ticksSinceGolem++;
			if(ticksSinceGolem<TICKRATE*2)return;
			if(ticksSinceGolem%TICKRATE==0){
				Iterator<BlockPos> it = initialPos.iterator();
				//for all columns
				//FIXME skips every 2nd slice
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
				if(ticksSinceGolem>=TICKRATE*height)this.onPatternBroken();
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
		ticksSinceGolem = compound.getInteger("GolemTicks");
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
		compound.setInteger("GolemTicks", ticksSinceGolem);
	}
	private static final int MAX_SEARCH_RANGE=32;
	private static LinkedHashSet<BlockPos> findArea(World world, BlockPos initial){
		search:for(int offset=0;offset<=MAX_SEARCH_RANGE;offset++){
			for(EnumFacing face:EnumFacing.HORIZONTALS){
				BlockPos current = initial.offset(face,offset);
				if(isMarker(world, current)){
					initial=current;
					break search;
				}
			}
			if(offset==MAX_SEARCH_RANGE){
				ModLogger.logWarn("Could not find marked area");
				return null;
			}
		}
		LinkedHashSet<BlockPos> result = new LinkedHashSet<BlockPos>();
		EdgeResult edge = new EdgeResult(initial.getX(),initial.getZ());
		edge = findEdge(world,initial,edge);
		//FIXME what prints here is not the full edge
		ModLogger.logInfo("Found "+edge);
		int y = initial.getY();
		//add edge to result
		for(Integer x:edge.positions.keySet()){
			for(Integer z: edge.positions.get(x)){
				result.add(new BlockPos(x,y,z));
			}
		}
		//find the area
		for(int cx=edge.smallX;cx<=edge.bigX;cx++){
			for(int cz = edge.smallZ;cz<=edge.bigZ;cz++){
				if(isInside(cx,cz,edge))result.add(new BlockPos(cx,y,cz));
			}
		}
		//FIXME this is *only* the edge (most of the time)
		ModLogger.logInfo("Found "+result.size()+" blocks in area");
		return result;
	}
	private static EdgeResult findEdge(World world, BlockPos currentBlock, EdgeResult edge){
		//add the current block to the edge
		int currentX=currentBlock.getX(), currentZ=currentBlock.getZ();
		//set the extreme positions
		if(currentX<edge.smallX)edge.smallX=currentX;
		if(currentZ<edge.smallZ)edge.smallZ=currentZ;
		if(currentX>edge.bigX)edge.smallX=currentX;
		if(currentX>edge.bigZ)edge.bigZ=currentZ;
		Set<Integer> zpos = edge.positions.get(currentX);
		if(zpos==null){
			zpos=new HashSet<Integer>();
			edge.positions.put(currentX, zpos);
		}
		zpos.add(currentZ);
		//find surrounding blocks
		for(EnumFacing f:EnumFacing.HORIZONTALS){
			BlockPos next = currentBlock.offset(f);
			if(isMarker(world, next)){
				Set<Integer> zz = edge.positions.get(next.getX());
				if(zz==null || !zz.contains(next.getZ())){
					findEdge(world, next, edge);
				}
			}
		}
		return edge;
	}
	
	private static boolean isMarker(World world, BlockPos pos){
		return world.getBlockState(pos).getBlock()==Blocks.CLAY;
	}
	
	private static boolean isInside(int x, int z, EdgeResult edge){
		int smaller=0,larger=0;
		Set<Integer> zz = edge.positions.get(x);
		if(zz==null)return false;
		for(Integer cz:zz){
			if(cz<z)smaller++;
			if(cz>z)larger++;
		}
		//for the point to be inside the polygon, the number of points on the edge on both sides (left/right) must be odd http://alienryderflex.com/polygon/
		return smaller%2!=0 && larger%2!=0;
	}
	
	private static class EdgeResult{
		public EdgeResult(int initialX,int initialZ){
			smallX=bigX=initialX;
			smallZ=bigZ=initialZ;
			positions = new HashMap<Integer, Set<Integer>>();
		}
		public int smallX,smallZ,bigX,bigZ;
		public Map<Integer, Set<Integer>> positions;
		public String toString(){
			StringBuilder sb = new StringBuilder();
			sb.append("Edge: ");
			sb.append("x: ").append(smallX).append(" to ").append(bigX);
			sb.append(" z: ").append(smallZ).append(" to ").append(bigZ);
			sb.append(" total blocks: ").append(positions.values().size());
			return sb.toString();
		}
	}
}
