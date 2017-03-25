package xilef11.mc.runesofwizardry_classics.runes.entity;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xilef11.mc.runesofwizardry_classics.ModLogger;
import xilef11.mc.runesofwizardry_classics.Refs;

import com.zpig333.runesofwizardry.api.IRune;
import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustPlaced;
public class RuneEntityPowerDistribution extends FueledRuneEntity {
	public RuneEntityPowerDistribution(ItemStack[][] actualPattern,
			EnumFacing facing, Set<BlockPos> dusts,
			TileEntityDustActive entity, IRune creator) {
		super(actualPattern, facing, dusts, entity, creator);
	}
	@Override
	protected int initialTicks() {
		return Refs.TPS;//not really used, but must be >0
	}
	/* (non-Javadoc)
	 * @see xilef11.mc.runesofwizardry_classics.runes.entity.FueledRuneEntity#onFuelRunOut()
	 */
	@Override
	protected void onFuelRunOut() {
		//not really used
		this.ticksLeft=0;//make sure it never goes under 0
	}
	/* (non-Javadoc)
	 * @see xilef11.mc.runesofwizardry_classics.runes.entity.FueledRuneEntity#onRuneActivatedbyPlayer(net.minecraft.entity.player.EntityPlayer, net.minecraft.item.ItemStack[], boolean)
	 */
	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,
			ItemStack[] sacrifice, boolean negated) {
		//super.onRuneActivatedbyPlayer(player, sacrifice, negated); don't register to other runes
		World world = player.world;
		if(!world.isRemote){
			entity.setupStar(0xFFFFFF, 0xFFFFFF);
			findNearbyRunes();
			entity.setDrawStar(!poweredRunes.isEmpty());
			initialised=true;
		}
	}
	//the runes that are being powered by this one
	private List<FueledRuneEntity> poweredRunes=new LinkedList<FueledRuneEntity>();
	private static final int TICKRATE=10;
	private static final int STABLE_FUEL_CYCLES=120;//120 should be about 1 minute
	private int stableFuel=-1;
	/* (non-Javadoc)
	 * @see xilef11.mc.runesofwizardry_classics.runes.entity.FueledRuneEntity#update()
	 */
	@Override
	public void update() {
		if(!initialised)init();
		//super.update(); - don't consume fuel
		//if(!entity.getWorld().isRemote){ - do work on both sides for the star to be drawn correctly
			if(stableFuel<0)stableFuel = TICKRATE*poweredRunes.size()*STABLE_FUEL_CYCLES;
			if(entity.ticksExisted()%TICKRATE==0){
				//add fuel to the runes
				for(FueledRuneEntity fe:poweredRunes){
					if(this.ticksLeft>=TICKRATE){
						fe.addFuel(TICKRATE);
						ticksLeft-=TICKRATE;
					}
				}
				double percent = stableFuel>0? (double)ticksLeft/(double)stableFuel : 1;
				int gb = (int)(255*percent);
				if(gb>255)gb=255;
				int color = 0xFF0000|(gb<<8)|(gb);
				//if(entity.ticksExisted()%10==0)ModLogger.logInfo(percent+" "+gb+" "+Integer.toHexString(color));
				if(entity.stardata!=null){
					entity.stardata.outercolor=color;
					entity.stardata.innercolor=color;
				}
			}
		//}
	}
	/**Horizontal radius of the powering**/
	public static final int RANGE=32;
	/**Vertical range **/
	public static final int VRANGE=8;
	private void findNearbyRunes(){
		World world = entity.getWorld();
		if(!world.isRemote){
			poweredRunes = new LinkedList<FueledRuneEntity>();
			Set<BlockPos> checkedPositions = new HashSet<BlockPos>();
			BlockPos thisPos = getPos();
			//don't check this rune
			checkedPositions.addAll(this.dustPositions);
			for(int dx=-RANGE;dx<=RANGE;dx++){
				for(int dz=-RANGE;dz<=RANGE;dz++){
					for(int dy=-VRANGE;dy<=VRANGE;dy++){
						BlockPos current = thisPos.add(dx,dy,dz);
						//don't check if we already found that rune
						if(checkedPositions.contains(current))continue;
						TileEntity t = world.getTileEntity(current);
						//don't do anything with non dust blocks
						if(!(t instanceof TileEntityDustPlaced))continue;
						TileEntityDustPlaced ted = (TileEntityDustPlaced)t;
						//get the rune
						RuneEntity rune = ted.getRune();
						if(rune!=null)checkedPositions.addAll(rune.dustPositions);
						//skip non-fuelable runes
						if(!(rune instanceof FueledRuneEntity))continue;
						//don't affect runes of power distribution
						if(rune instanceof RuneEntityPowerDistribution)continue;
						//add the rune to our list
						//poweredRunes.add((FueledRuneEntity)rune);
						((FueledRuneEntity)rune).registerTo(this);
					}
				}
			}
		}
	}
	/**Adds a rune to be powered by this**/
	public void register(FueledRuneEntity toPower){
		poweredRunes.add(toPower);
		if(toPower.entity.stardata!=null)toPower.entity.stardata.scale*=1.04F;
		stableFuel = TICKRATE*poweredRunes.size()*STABLE_FUEL_CYCLES;
		entity.setDrawStar(true);
		IBlockState state = entity.getWorld().getBlockState(getPos());
		entity.getWorld().notifyBlockUpdate(getPos(), state, state, 3);
	}
	/**removes a rune to be powered by this**/
	public void unregister(FueledRuneEntity toPower){
		poweredRunes.remove(toPower);
		if(toPower.entity.stardata!=null)toPower.entity.stardata.scale/=1.04F;
		stableFuel = TICKRATE*poweredRunes.size()*STABLE_FUEL_CYCLES;
		if(poweredRunes.isEmpty())entity.setDrawStar(false);
		IBlockState state = entity.getWorld().getBlockState(getPos());
		entity.getWorld().notifyBlockUpdate(getPos(), state, state, 3);
	}
	private Set<BlockPos> toInit=null;
	private boolean initialised=false;
	private void init(){
		if(initialised || toInit==null ||entity==null ||entity.getWorld()==null)return;
		for(BlockPos p:toInit){
			TileEntity t = entity.getWorld().getTileEntity(p);
			if(t instanceof TileEntityDustPlaced){
				TileEntityDustPlaced te = (TileEntityDustPlaced)t;
				RuneEntity rune = te.getRune();
				if((rune instanceof FueledRuneEntity) && !(rune instanceof RuneEntityPowerDistribution)){
					((FueledRuneEntity)rune).registerTo(this);;
				}else{
					ModLogger.logError("rune at "+p+" was not appropriate for power distribution");
				}
			}else{
				ModLogger.logError("TileEntity at "+p+" was not placed dust for power distribution");
			}
		}
		initialised=true;
	}
	/* (non-Javadoc)
	 * @see xilef11.mc.runesofwizardry_classics.runes.entity.FueledRuneEntity#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		NBTTagList runes = (NBTTagList) compound.getTag("PoweredRunes");
		toInit = new HashSet<BlockPos>();
		for(int i=0;i<runes.tagCount();i++){
			int[] c = runes.getIntArrayAt(i);
			BlockPos pos = new BlockPos(c[0],c[1],c[2]);
			toInit.add(pos);//we will need to initialise later on an update tick because world may be null on first load
		}
		initialised=false;
	}
	/* (non-Javadoc)
	 * @see xilef11.mc.runesofwizardry_classics.runes.entity.FueledRuneEntity#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		NBTTagList runes = new NBTTagList();
		for(FueledRuneEntity ent:poweredRunes){
			BlockPos pos = ent.getPos();
			NBTTagIntArray coords = new NBTTagIntArray(new int[]{pos.getX(),pos.getY(),pos.getZ()});
			runes.appendTag(coords);
		}
		compound.setTag("PoweredRunes", runes);
	}
	/* (non-Javadoc)
	 * @see xilef11.mc.runesofwizardry_classics.runes.entity.FueledRuneEntity#registerTo(xilef11.mc.runesofwizardry_classics.runes.entity.RuneEntityPowerDistribution)
	 */
	@Override
	public void registerTo(RuneEntityPowerDistribution power) {
		// NOTHING
		ModLogger.logWarn("Something tried to power a rune of power distribution...");
	}
	@Override
	public void unregisterFrom(RuneEntityPowerDistribution power) {
		// NOTHING
		ModLogger.logWarn("Something tried to unregister a rune of power distribution...");
	}
	/* (non-Javadoc)
	 * @see xilef11.mc.runesofwizardry_classics.runes.entity.FueledRuneEntity#onPatternBroken()
	 */
	@Override
	public void onPatternBroken() {
		super.onPatternBroken();
		for(FueledRuneEntity f:poweredRunes){
			f.unregisterFrom(this);
		}
	}
}
