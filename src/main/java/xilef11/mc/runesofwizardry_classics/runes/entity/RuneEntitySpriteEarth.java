package xilef11.mc.runesofwizardry_classics.runes.entity;

import java.util.HashSet;
import java.util.Set;

import com.zpig333.runesofwizardry.api.IRune;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.utils.Utils;
/*
 * Really hard to guess what this does from code, and it crashes on activation...
 * managed to test on an older version:
 * 8 blocks float around the player, and surround it when crouching
 * when floating, blocks have no collision
 */
public class RuneEntitySpriteEarth extends FueledRuneEntity {

	public RuneEntitySpriteEarth(ItemStack[][] actualPattern,EnumFacing facing, Set<BlockPos> dusts,TileEntityDustActive entity, IRune creator) {
		super(actualPattern, facing, dusts, entity, creator);
	}

	@Override
	protected int initialTicks() {
		return 3*Refs.TICKS_PER_DAY;
	}
	private String activatingPlayer;
	/* (non-Javadoc)
	 * @see xilef11.mc.runesofwizardry_classics.runes.entity.FueledRuneEntity#onRuneActivatedbyPlayer(net.minecraft.entity.player.EntityPlayer, net.minecraft.item.ItemStack[], boolean)
	 */
	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,ItemStack[] sacrifice, boolean negated) {
		super.onRuneActivatedbyPlayer(player, sacrifice, negated);
		if(!player.world.isRemote){
			if((negated||Utils.takeXP(player, 20))){
				activatingPlayer = player.getName();
				entity.setupStar(0x00FF00, 0x00FF00);
			}else{
				this.onPatternBrokenByPlayer(player);
			}
		}
	}
	
	private boolean wasProtecting=false;
	private Set<BlockPos> box;
	/* (non-Javadoc)
	 * @see xilef11.mc.runesofwizardry_classics.runes.entity.FueledRuneEntity#update()
	 */
	@Override
	public void update() {
		super.update();
		World world = entity.getWorld();
		EntityPlayer player = world.getPlayerEntityByName(activatingPlayer);
		if(player!=null){
			entity.setDrawStar(true);
			//make the star spin
			float period = 60;
            float dist = 2f;
            float ticks = (entity.ticksExisted()) % period;
            float ticksOff = ((entity.ticksExisted()) % period)*3;
            float sin = MathHelper.sin((ticks / period) * (float)Math.PI * 2F);
            float sinY = MathHelper.sin((ticksOff/ period) * (float)Math.PI * 2F);
            float cos = MathHelper.cos((ticks / period) * (float)Math.PI * 2F);
            float dx = cos * dist;
            float dz = sin * dist;
            float dy = sinY * 0.5F + 0.5F;
            Vec3d pos = new Vec3d(player.posX+dx, player.posY + dy, player.posZ + dz).subtract(new Vec3d(getPos()));
            entity.stardata.offset=pos;
            //protection
            if(!world.isRemote){
            	double speed = Math.sqrt(player.motionX*player.motionX+player.motionY*player.motionY+player.motionZ*player.motionZ);
            	boolean protect = player.isSneaking() && speed<0.08 && player.onGround;
            	if(protect && !wasProtecting){
            		//set blocks
            		BlockPos pl = player.getPosition();
            		box = new HashSet<>();
            		for(EnumFacing f: EnumFacing.HORIZONTALS){
            			BlockPos current = pl.offset(f);
            			if(world.isAirBlock(current)){
            				world.setBlockState(current, Blocks.GLASS.getDefaultState());
            				box.add(current);
            			}
            			current=current.up();//might need to be down
            			if(world.isAirBlock(current)){
            				world.setBlockState(current, Blocks.GLASS.getDefaultState());
            				box.add(current);
            			}
            		}
            		wasProtecting=true;
            	}else if(!protect && wasProtecting){
            		//remove blocks
            		wasProtecting=false;
    				for(BlockPos p:box){
    					if(world.getBlockState(p).getBlock()==Blocks.GLASS)world.setBlockToAir(p);
    				}
    				box.clear();
            	}
            }
		}else{
			entity.setDrawStar(false);
			if(!world.isRemote&&wasProtecting){
				wasProtecting=false;
				for(BlockPos p:box){
					if(world.getBlockState(p).getBlock()==Blocks.GLASS)world.setBlockToAir(p);
				}
				box.clear();
			}
		}
	}

	/* (non-Javadoc)
	 * @see xilef11.mc.runesofwizardry_classics.runes.entity.FueledRuneEntity#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		activatingPlayer=compound.getString("player");
		wasProtecting = compound.getBoolean("protecting");
		NBTTagList pos = (NBTTagList) compound.getTag("boxPositions");
		box = new HashSet<>();
		for(int i=0;i<pos.tagCount();i++){
			int[] c = pos.getIntArrayAt(i);
			box.add(new BlockPos(c[0],c[1],c[2]));
		}
	}

	/* (non-Javadoc)
	 * @see xilef11.mc.runesofwizardry_classics.runes.entity.FueledRuneEntity#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setString("player", activatingPlayer);
		compound.setBoolean("protecting", wasProtecting);
		NBTTagList pos = new NBTTagList();
		if(box!=null){
			for(BlockPos p:box){
				pos.appendTag(new NBTTagIntArray(new int[]{p.getX(),p.getY(),p.getZ()}));
			}
		}
		compound.setTag("boxPositions", pos);
	}

	
}
