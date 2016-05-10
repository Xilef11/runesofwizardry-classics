package xilef11.mc.runesofwizardry_classics.runes;
import java.io.IOException;
import java.util.Set;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.runes.entity.RuneEntityLevelEarth;

import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.core.rune.PatternUtils;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;
public class RuneLevelEarth extends VariableRune {
	@Override
	protected ItemStack[][] setupPattern() throws IOException {
		return PatternUtils.importFromJson(Refs.PATTERN_PATH+"runeLevelEarth.json");
	}
	@Override
	public String getID() {
		return "runeLevelEarth";
	}
	@Override
	protected Vec3i setupEntityPos() {
		return new Vec3i(2,2,0);
	}
	@Override
	protected ItemStack[][] setupSacrifice() {
		return new ItemStack[][]{
				{new ItemStack(Blocks.IRON_ORE,5)}//SAC take XP according to dust
				};
	}
	/* (non-Javadoc)
	 * @see xilef11.mc.runesofwizardry_classics.runes.ClassicRune#hasExtraSacrifice()
	 */
	@Override
	protected boolean hasExtraSacrifice() {
		return true;
	}
	@Override
	public String getName() {
		return Refs.Lang.RUNE+".levelearth";
	}
	@Override
	public RuneEntity createRune(ItemStack[][] actualPattern, EnumFacing front,
			Set<BlockPos> dusts, TileEntityDustActive entity) {
		return new RuneEntityLevelEarth(actualPattern, front, dusts, entity, this);
	}
}
