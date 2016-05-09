
package xilef11.mc.runesofwizardry_classics.runes;

import java.io.IOException;
import java.util.Set;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.runes.entity.RuneEntityBarrier;

import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.core.rune.PatternUtils;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

public class RuneBarrier extends ClassicRune {
	@Override
	public String getID() {
		return "runeBarrier";
	}
	@Override
	protected ItemStack[][] setupPattern() throws IOException {
		return PatternUtils.importFromJson(Refs.PATTERN_PATH+"runeBarrier.json");
	}

	@Override
	protected Vec3i setupEntityPos() {
		return new Vec3i(2,2,0);//not centered, but "behind" the wall to prevent glitches
	}

	@Override
	protected ItemStack[][] setupSacrifice() {
		return new ItemStack[][]{
				{new ItemStack(Blocks.IRON_ORE)}//take 3 xp
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
		return Refs.Lang.RUNE+".barrier";
	}

	@Override
	public RuneEntity createRune(ItemStack[][] actualPattern, EnumFacing front,
			Set<BlockPos> dusts, TileEntityDustActive entity) {
		return new RuneEntityBarrier(actualPattern, front, dusts, entity, this);
	}

}

    