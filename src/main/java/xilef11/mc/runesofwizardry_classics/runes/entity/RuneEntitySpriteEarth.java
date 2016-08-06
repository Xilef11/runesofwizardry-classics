package xilef11.mc.runesofwizardry_classics.runes.entity;

import java.util.Set;

import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.utils.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import com.zpig333.runesofwizardry.api.IRune;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;
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
		if(!player.worldObj.isRemote){
			if(!(negated||Utils.takeXP(player, 20))){
				activatingPlayer = player.getName();
			}
		}
	}

	/* (non-Javadoc)
	 * @see xilef11.mc.runesofwizardry_classics.runes.entity.FueledRuneEntity#update()
	 */
	@Override
	public void update() {
		super.update();
		
	}

	/* (non-Javadoc)
	 * @see xilef11.mc.runesofwizardry_classics.runes.entity.FueledRuneEntity#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		activatingPlayer=compound.getString("player");
	}

	/* (non-Javadoc)
	 * @see xilef11.mc.runesofwizardry_classics.runes.entity.FueledRuneEntity#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setString("player", activatingPlayer);
	}

	
}
