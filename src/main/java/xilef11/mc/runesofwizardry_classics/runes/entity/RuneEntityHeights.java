package xilef11.mc.runesofwizardry_classics.runes.entity;

import java.util.List;
import java.util.Set;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import xilef11.mc.runesofwizardry_classics.Refs;

import com.zpig333.runesofwizardry.api.IRune;
import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;
/*
 * Behaviour:
 * Rises a 16-block column (moves all blocks in the column on each operations)
 * Works even if the top is blocked (also moves all blocks in its path)
 * replaces air + liquids with cobble
 * Trying to move a TE kills the rune (?) (we could semi-easily move TEs)
 * breaking the 16th block above the rune brings the column down
 * Weird behaviour in caves (fills up blocks above its path with cobble)
 * 
 * Modifications: 
 * breaking the highest moved block in the column will also bring it down
 */
public class RuneEntityHeights extends RuneEntity {
	private static final int DEPTH=16;//depth of the column
	private static final int TICKS_BLOCK=20;//number of ticks between each block lifted

	public RuneEntityHeights(ItemStack[][] actualPattern, EnumFacing facing,
			Set<BlockPos> dusts, TileEntityDustActive entity, IRune creator) {
		super(actualPattern, facing, dusts, entity, creator);
	}

	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,
			ItemStack[] sacrifice, boolean negated) {
		// Nothing here
	}
	private int currentDepth=DEPTH;
	private int highestBlock=0;
	private boolean down=false;//is the column going up or down
	@Override
	public void update() {
		World world = entity.getWorld();
		if(!world.isRemote){
			if(entity.ticksExisted()>Refs.TICKS_PER_DAY){
				this.onPatternBroken();
				return;
			}
			if(entity.ticksExisted()%TICKS_BLOCK==0){
				BlockPos lowest = getPos().down(currentDepth+1).offset(face),
						highest = getPos().up(highestBlock-2).offset(face);
				if(!down){//move the column up
					if(currentDepth==0){//if the column is fully up and we broke either the top block or the "ground block"
						BlockPos top = getPos().up(DEPTH-1).offset(face);//"ground" level block
						
						if(world.isAirBlock(highest)||world.isAirBlock(top)){
							down=true;
						}
						return;
					}
					//lift
					//lift entities on the top block
					//not picking up anything -- fixed with the -2 above
					List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(highest, highest.add(1,2,1)));
					for(Entity e: entities){
						e.setPositionAndUpdate(highest.getX()+0.5, highest.getY()+2, highest.getZ()+0.5);
					}
					//raise the column
					for(int y = 0;y<=highestBlock+currentDepth-1;y++){
						if(y==0){//top block
							if(!world.isAirBlock(highest.up())){
								//figure out something to keep air blocks in our column
								//or don't, and always crush empty space
								/*if we don't have an air block above our column, 
								 * add that block to the pillar and retry
								 */
								highest=highest.up();
								highestBlock++;
								y--;
								continue;
							}
						}
						BlockPos current = highest.down(y);
						IBlockState state = world.getBlockState(current);
						if((current.getY()<getPos().getY() && !state.getBlock().getMaterial().isSolid())){
							state = Blocks.cobblestone.getDefaultState();
						}
						if(state.getBlock()==Blocks.bedrock || world.getBlockState(current.up()).getBlock()==Blocks.bedrock){
							break;//stop moving the column if we get to bedrock
						}
						NBTTagCompound temp = new NBTTagCompound();//to save TE data
						TileEntity te = world.getTileEntity(current);//get the TE, if any
						world.setBlockState(current.up(), state);//"move" the block up"
						if(te != null){
							te.writeToNBT(temp);//save TE data
							te = world.getTileEntity(current.up()); 
							if(te!=null){//shouldn't be, but safer this way
								te.readFromNBT(temp);//restore TE data
							}
						}
						world.setBlockToAir(current);
					}
					currentDepth--;
					highestBlock++;
				}else{//move the column down
					if(currentDepth==DEPTH){//if the column is fully down
						this.onPatternBroken();//break the rune
						return;
					}
					//drop
					for(int y = 0;y<=highestBlock+currentDepth;y++){
						if(y==0){//bottom
							if(!world.isAirBlock(lowest.down())){
								//or we could just return and stay blocked until the blockage gets cleared
								//this.onPatternBroken();
								return;
							}
						}
						BlockPos current = lowest.up(y);
						IBlockState state = world.getBlockState(current);
						if(state.getBlock()==Blocks.bedrock || world.getBlockState(current.down()).getBlock()==Blocks.bedrock){
//							this.onPatternBroken();
//							return;//stop moving the column if we get to bedrock
							break;
						}
						NBTTagCompound temp = new NBTTagCompound();//to save TE data
						TileEntity te = world.getTileEntity(current);//get the TE, if any
						world.setBlockState(current.down(), state);//"move" the block down
						if(te != null){
							te.writeToNBT(temp);//save TE data
							te = world.getTileEntity(current.down()); 
							if(te!=null){//shouldn't be, but safer this way
								te.readFromNBT(temp);//restore TE data
							}
						}
						world.setBlockToAir(current);
					}
					currentDepth++;
					highestBlock--;
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		currentDepth=compound.getInteger("currentDepth");
		highestBlock=compound.getInteger("highestBlock");;
		down=compound.getBoolean("goingDown");
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("currentDepth", currentDepth);
		compound.setInteger("highestBlock", highestBlock);
		compound.setBoolean("goingDown", down);
	}

}
