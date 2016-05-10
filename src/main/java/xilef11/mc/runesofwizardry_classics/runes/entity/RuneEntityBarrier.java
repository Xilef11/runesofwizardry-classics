package xilef11.mc.runesofwizardry_classics.runes.entity;
import java.util.Iterator;
import java.util.Set;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xilef11.mc.runesofwizardry_classics.utils.Utils;

import com.zpig333.runesofwizardry.api.IRune;
import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustPlaced;
/*Behaviour:
 * Lifts a 5x6 (hxv) wall *on* the rune
 */
public class RuneEntityBarrier extends RuneEntity {
	private static final int TICKS_PER_BLOCK=15;
	private static final int DEPTH=6;
	private static final int RAD=2;//length of the wall on each side of the rune
	public RuneEntityBarrier(ItemStack[][] actualPattern, EnumFacing facing,
			Set<BlockPos> dusts, TileEntityDustActive entity, IRune creator) {
		super(actualPattern, facing, dusts, entity, creator);
	}
	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,
			ItemStack[] sacrifice, boolean negated) {
		World world = player.worldObj;
		if(!world.isRemote){
			if(!Utils.takeXP(player, 3)){
				this.onPatternBrokenByPlayer(player);
				return;
			}
			//we will need to change the rune's posSet before moving the first block to avoid breaking things
		}
	}
	private int currentDepth[]=new int[RAD*2+1];
	private int[] highestBlock=new int[RAD*2+1];
	@Override
	public void update() {
		World world = entity.getWorld();
		if(!world.isRemote){
			if(entity.ticksExisted()%TICKS_PER_BLOCK==0){
				if(entity.ticksExisted()==TICKS_PER_BLOCK){//first tick
					Iterator<BlockPos> it = dustPositions.iterator();
					while(it.hasNext()){
						BlockPos p = it.next();
						//break all dusts that are "on" the wall
						if(!(p.equals(getPos())||p.equals(getPos().offset(face, 2)))){
							it.remove();//remove it from the rune
							TileEntity t = world.getTileEntity(p);
							if(t instanceof TileEntityDustPlaced){
								TileEntityDustPlaced ted = ((TileEntityDustPlaced)t);
								ted.setRune(null);
								ted.clear();
							}
							world.setBlockToAir(p);//remove the dust
						}
						//world.markBlockForUpdate(p);
						world.notifyBlockUpdate(p, world.getBlockState(p), world.getBlockState(p), 0);
					}
					for(int i=0;i<currentDepth.length;i++){
						currentDepth[i]=DEPTH;
					}
					for(int i=0;i<highestBlock.length;i++){
						highestBlock[i]=0;
					}
				}
				EnumFacing left = face.rotateYCCW();
				//this rune shouldn't ever last long enough to overflow an int
				//lift the wall
				for(int x=-RAD;x<=RAD;x++){//for each column in the wall
					BlockPos highest = getPos().up(highestBlock[x+RAD]-2).offset(face);
					col:for(int y = 0;y<=highestBlock[x+RAD]+currentDepth[x+RAD]-1;y++){//lift the column (see RuneEntityHeights)
						if(y==0){//top block
							if(!world.isAirBlock(highest.up())){
								//FIXME we're "eating" blocks FSR - only if they're *just* above the dust
								//figure out something to keep air blocks in our column
								//or don't, and always crush empty space
								/*if we don't have an air block above our column,
								 * add that block to the pillar and retry
								 */
								highest=highest.up();
								highestBlock[x+RAD]++;
								y--;
								continue col;
							}
						}
						BlockPos current = highest.down(y).offset(left, x);
						IBlockState state = world.getBlockState(current);
						if((current.getY()<getPos().getY() && !state.getMaterial().isSolid())){
							state = Blocks.COBBLESTONE.getDefaultState();
						}
						if(state.getBlock()==Blocks.BEDROCK || world.getBlockState(current.up()).getBlock()==Blocks.BEDROCK){
							break col;//stop moving the column if we get to bedrock
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
					currentDepth[x+RAD]--;
					highestBlock[x+RAD]++;
				}
				boolean done=true;
				for(int i:currentDepth){
					if(i>0)done=false;
				}
				if(done)this.onPatternBroken();
			}
		}
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		highestBlock = compound.getIntArray("highest");
		currentDepth = compound.getIntArray("currentDepth");
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setIntArray("highest", highestBlock);
		compound.setIntArray("currentDepth", currentDepth);
	}
}
