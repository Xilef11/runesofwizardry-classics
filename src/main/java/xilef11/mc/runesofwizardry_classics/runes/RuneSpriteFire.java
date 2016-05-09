
package xilef11.mc.runesofwizardry_classics.runes;

import java.io.IOException;
import java.util.Set;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.runes.entity.RuneEntityUnimplemented;

import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.core.rune.PatternUtils;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

public class RuneSpriteFire extends ClassicRune {

	@Override
	protected ItemStack[][] setupPattern() throws IOException {
		return PatternUtils.importFromJson(Refs.PATTERN_PATH+"runeSpriteFire.json");
	}

	@Override
	protected Vec3i setupEntityPos() {
		return new Vec3i(1,1,0);
	}
	@Override
	public String getID() {
		return "runeSpriteFire";
	}
	@Override
	protected ItemStack[][] setupSacrifice() {
		return new ItemStack[][]{
				{new ItemStack(Items.GHAST_TEAR),new ItemStack(Items.fire_charge,2)}//SAC take 22 XP
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
		return Refs.Lang.RUNE+".spritefire";
	}

	@Override
	public RuneEntity createRune(ItemStack[][] actualPattern, EnumFacing front,
			Set<BlockPos> dusts, TileEntityDustActive entity) {
		return new RuneEntityUnimplemented(actualPattern, front, dusts, entity, this);
	}

}

    