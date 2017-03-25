/**
 *
 */
package xilef11.mc.runesofwizardry_classics.runes.entity;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.utils.Utils;

import com.zpig333.runesofwizardry.api.IRune;
import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.core.rune.RunesUtil;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;
/** This RuneEntity serves as a placeholder so runes may be registered even if they're not implemented
 * @author Xilef11
 *
 */
public class RuneEntityUnimplemented extends RuneEntity {
	private final String runeName;
	public static final String message=Refs.Lang.RUNE+".unimplemented.message";
	/**
	 * @param actualPattern
	 * @param facing
	 * @param dusts
	 * @param entity
	 */
	public RuneEntityUnimplemented(ItemStack[][] actualPattern, EnumFacing facing,Set<BlockPos> dusts, TileEntityDustActive entity,IRune rune) {
		super(actualPattern, facing, dusts, entity,rune);
		this.runeName=rune.getName();
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#onRuneActivatedbyPlayer(net.minecraft.entity.player.EntityPlayer, net.minecraft.item.ItemStack[])
	 */
	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,ItemStack[] sacrifice,boolean negated) {
		player.sendMessage(new TextComponentTranslation(message, new TextComponentTranslation(runeName)));
		RunesUtil.deactivateRune(this);
		//return the sacrifice
		for(ItemStack i:sacrifice){
			Utils.spawnItemCentered(player.world, getPos(), i);
		}
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#update()
	 */
	@Override
	public void update() {
		//NOP
	}
}
