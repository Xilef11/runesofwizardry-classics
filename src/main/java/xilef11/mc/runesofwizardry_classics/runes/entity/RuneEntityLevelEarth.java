package xilef11.mc.runesofwizardry_classics.runes.entity;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xilef11.mc.runesofwizardry_classics.Config;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.items.EnumDustTypes;
import xilef11.mc.runesofwizardry_classics.runes.RuneLevelEarth;
import xilef11.mc.runesofwizardry_classics.utils.Utils;
import xilef11.mc.runesofwizardry_classics.utils.Utils.Coords;

import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.core.WizardryRegistry;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;
public class RuneEntityLevelEarth extends RuneEntity {
	private static final int TICKRATE=Refs.TPS;
	public RuneEntityLevelEarth(ItemStack[][] actualPattern, EnumFacing facing,
			Set<BlockPos> dusts, TileEntityDustActive entity, RuneLevelEarth creator) {
		super(actualPattern, facing, dusts, entity, creator);
	}
	private int radius=0,height=0;
	private IBlockState toFill=Blocks.COBBLESTONE.getDefaultState();
	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,
			ItemStack[] sacrifice, boolean negated) {
		if(!player.worldObj.isRemote){
			//FX
			entity.setupStar(0xFFFFFF, (114<<8)|(53<<4)|(62));
			//set up the rune based on dust used
			int xp=0;
			Coords c = ((RuneLevelEarth)creator).getVariableDusts().iterator().next();
			switch(EnumDustTypes.getByMeta((placedPattern[c.row][c.col].getMetadata()))){
			case PLANT:xp=10;radius=4;height=4;
				break;
			case GUNPOWDER:xp=12;radius=6;height=5;
				break;
			case LAPIS:xp=15;radius=8;height=7;
				break;
			case BLAZE:xp=20;radius=10;height=9;
				break;
			default:this.onPatternBrokenByPlayer(player);return;
			}
			if(!(negated || Utils.takeXP(player, xp))){
				this.onPatternBrokenByPlayer(player);
				return;
			}
			Block under = entity.getWorld().getBlockState(getPos().down()).getBlock();
			if(under==Blocks.COBBLESTONE||under==Blocks.DIRT||under==Blocks.GRASS||under==Blocks.STONE){
				toFill=under.getDefaultState();
			}else{
				toFill=Blocks.COBBLESTONE.getDefaultState();
			}
		}
	}
	@Override
	public void update() {
		World world = entity.getWorld();
		if(!world.isRemote){
			if(entity.ticksExisted()%TICKRATE==0){
				int dist = (int) (entity.ticksExisted()/TICKRATE);
				if(dist>radius)dist=radius;
				for(int x=-dist;x<=dist;x++){
					for(int z=-dist;z<=dist;z++){
						for(int y=height-1;y>=0;y--){
							BlockPos current = getPos().add(x,y,z);
							IBlockState state = world.getBlockState(current);
							Block block = state.getBlock();
							if(block==WizardryRegistry.dust_placed||block==Blocks.AIR||block.hasTileEntity(state)||block==Blocks.BEDROCK||Config.levelEarthBlackList.contains(block.getRegistryName().toString())){
								continue;
							}
							//replace liquid blocks with cobble
							if(state.getMaterial().isLiquid()){
								world.setBlockState(current, toFill);
							}
							if(world.getBlockState(current.up()).getMaterial().isLiquid()){
								world.setBlockState(current.up(), toFill);
							}
							//fill in holes in the floor
							if(y==0) {
								if (world.isAirBlock(current.down())) {
									world.setBlockState(current.down(), toFill);
								}
							}
							world.setBlockToAir(current);
						}
					}
				}
				if(entity.ticksExisted()/TICKRATE>radius+height)this.onPatternBroken();
			}
		}
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		radius=compound.getInteger("radius");
		height=compound.getInteger("height");
		Block b =Block.REGISTRY.getObject(new ResourceLocation(compound.getString("toFill")));
		if(b!=null)toFill = b.getDefaultState();
		else toFill=Blocks.COBBLESTONE.getDefaultState();
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("radius", radius);
		compound.setInteger("height", height);
		compound.setString("toFill",toFill.getBlock().getRegistryName().toString());
	}
}
