package xilef11.mc.runesofwizardry_classics.runes.entity;

import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.items.ItemSpiritSword;
import xilef11.mc.runesofwizardry_classics.utils.Utils;

import com.zpig333.runesofwizardry.api.IRune;
import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive.BeamType;

public class RuneEntitySpiritTools extends RuneEntity {

	public RuneEntitySpiritTools(ItemStack[][] actualPattern,
			EnumFacing facing, Set<BlockPos> dusts,
			TileEntityDustActive entity, IRune creator) {
		super(actualPattern, facing, dusts, entity, creator);
	}
	private static enum EnumType {SWORD,PICK};
	private EnumType type;
	private int countdown;
	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player, ItemStack[] sacrifice,boolean negated) {
		if(player.worldObj.isRemote)return;
		if(negated || Utils.takeXP(player, 18)){
			for(ItemStack i:sacrifice){
				if(i!=null){
					if(i.getItem()==Items.GOLDEN_SWORD)type=EnumType.SWORD;
					else if(i.getItem()==Items.GOLDEN_PICKAXE)type=EnumType.PICK;
				}
			}
			if(type==null)type=EnumType.SWORD;
			countdown=5*Refs.TPS;
			entity.setupBeam(0x0000FF, BeamType.SPIRAL);
			entity.setupStar(0xFFFFFF, 0x0000FF);
			entity.setDrawBeam(true);
			entity.setDrawStar(false);
		}else{
			this.onPatternBrokenByPlayer(player);
		}

	}

	@Override
	public void update() {
		if(!entity.getWorld().isRemote){
			countdown--;
			if(countdown==0){
				switch(type){
				case SWORD:
					Utils.spawnItemCentered(entity.getWorld(), getPos(), ItemSpiritSword.createStack());
					break;
				default:
				}
				this.onPatternBroken();
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.countdown=compound.getInteger("countdown");
		this.type=EnumType.valueOf(compound.getString("ToolType"));
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setString("ToolType", type.name());
		compound.setInteger("countdown", countdown);
	}

}
