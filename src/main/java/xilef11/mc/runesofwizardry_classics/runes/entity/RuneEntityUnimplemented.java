/**
 * 
 */
package xilef11.mc.runesofwizardry_classics.runes.entity;

import java.util.Set;

import xilef11.mc.runesofwizardry_classics.Refs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;

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
	private final String id;
	public static final String message=Refs.Lang.RUNE+".unimplemented.message";
	/**
	 * @param actualPattern
	 * @param facing
	 * @param dusts
	 * @param entity
	 */
	public RuneEntityUnimplemented(ItemStack[][] actualPattern, EnumFacing facing,Set<BlockPos> dusts, TileEntityDustActive entity,IRune rune) {
		super(actualPattern, facing, dusts, entity);
		this.runeName=rune.getName();
		String id = runeName.substring(runeName.lastIndexOf('.')+1);
		this.id = "rune"+id.substring(0, 1).toUpperCase()+id.substring(1);
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#getRuneID()
	 */
	@Override
	public String getRuneID() {
		return id;
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#onRuneActivatedbyPlayer(net.minecraft.entity.player.EntityPlayer, net.minecraft.item.ItemStack[])
	 */
	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,ItemStack[] sacrifice) {
		player.addChatMessage(new ChatComponentTranslation(message, StatCollector.translateToLocal(runeName)));
		RunesUtil.deactivateRune(this);
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#update()
	 */
	@Override
	public void update() {
		//NOP
	}

}
