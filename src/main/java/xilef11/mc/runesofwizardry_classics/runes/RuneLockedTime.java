
package xilef11.mc.runesofwizardry_classics.runes;

import java.io.IOException;
import java.util.Set;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.managers.LockedTimeData;
import xilef11.mc.runesofwizardry_classics.runes.entity.RuneEntityLockedTime;

import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.core.rune.PatternUtils;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

public class RuneLockedTime extends ClassicRune {
	public RuneLockedTime() {
		super();
		MinecraftForge.EVENT_BUS.register(this);
	}
	/*this event handler allows us to stop blocks from falling if there is at least one active rune
	 * Basically, we kill the EntityFallingBlock and reset the block.
	 */
	@SubscribeEvent
	public void onEntityCreation(EntityJoinWorldEvent event){
		//XXX might want a config option to enable this
		if(event.getEntity() instanceof EntityFallingBlock){
			if(LockedTimeData.get(event.getWorld()).getNumRunes()>0){
				EntityFallingBlock fb = (EntityFallingBlock)event.getEntity();
				IBlockState state = fb.getBlock();
				fb.setDead();
				event.getWorld().setBlockState(fb.getPosition(), state);
				event.setCanceled(true);
			}
		}
	}
	@Override
	protected ItemStack[][] setupPattern() throws IOException {
		return PatternUtils.importFromJson(Refs.PATTERN_PATH+"runeLockedTime.json");
	}
	@Override
	public String getID() {
		return "runeLockedTime";
	}
	@Override
	protected Vec3i setupEntityPos() {
		return new Vec3i(1,1,0);
	}

	@Override
	protected ItemStack[][] setupSacrifice() {
		return new ItemStack[][]{
				{new ItemStack(Blocks.OBSIDIAN,4),new ItemStack(Items.SLIME_BALL,4),new ItemStack(Items.DYE,1,EnumDyeColor.BLUE.getMetadata())}
				};
	}

	@Override
	public String getName() {
		return Refs.Lang.RUNE+".lockedtime";
	}

	@Override
	public RuneEntity createRune(ItemStack[][] actualPattern, EnumFacing front,
			Set<BlockPos> dusts, TileEntityDustActive entity) {
		return new RuneEntityLockedTime(actualPattern, front, dusts, entity, this);
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IRune#allowOredictSacrifice()
	 */
	@Override
	public boolean allowOredictSacrifice() {
		return false;//lapis is needed, not any blue dye
	}
	/* (non-Javadoc)
	 * @see xilef11.mc.runesofwizardry_classics.runes.ClassicRune#hasExtraSacrifice()
	 */
	@Override
	protected boolean hasExtraSacrifice() {
		return true;
	}
	
}

    