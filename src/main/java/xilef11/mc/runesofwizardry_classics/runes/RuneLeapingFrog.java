package xilef11.mc.runesofwizardry_classics.runes;

import java.io.IOException;
import java.util.Set;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.runes.entity.RuneEntityLeapingFrog;

import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.core.rune.PatternUtils;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

public class RuneLeapingFrog extends ClassicRune {

	@Override
	protected ItemStack[][] setupPattern() throws IOException {
		return PatternUtils.importFromJson(Refs.PATTERN_PATH+"runeLeapingFrog.json");
	}
	@Override
	public String getID() {
		return "runeLeapingFrog";
	}
	@Override
	protected Vec3i setupEntityPos() {
		return new Vec3i(1,0,0);
	}

	@Override
	protected ItemStack[][] setupSacrifice() {
		return new ItemStack[][]{
				{new ItemStack(Blocks.LEAVES,4)}
		};
	}

	@Override
	public String getName() {
		return Refs.Lang.RUNE+".leapingfrog";
	}

	@Override
	public RuneEntity createRune(ItemStack[][] actualPattern, EnumFacing front,
			Set<BlockPos> dusts, TileEntityDustActive entity) {
		return new RuneEntityLeapingFrog(actualPattern, front, dusts, entity, this);
	}

}
