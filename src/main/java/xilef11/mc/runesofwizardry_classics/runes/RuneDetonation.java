
package xilef11.mc.runesofwizardry_classics.runes;

import java.io.IOException;
import java.util.Set;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.runes.entity.RuneEntityUnimplemented;

import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.core.rune.PatternUtils;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

public class RuneDetonation extends VariableRune {

	@Override
	protected ItemStack[][] setupPattern() throws IOException {
		return PatternUtils.importFromJson(Refs.PATTERN_PATH+"runeDetonation.json");
	}
	@Override
	public String getID() {
		return "runeDetonation";
	}
	@Override
	protected Vec3i setupEntityPos() {
		return new Vec3i(0,1,0);
	}

	@Override
	protected ItemStack[][] setupSacrifice() {
		return new ItemStack[][]{
				{new ItemStack(Items.gunpowder,2)}
				};
	}

	@Override
	public String getName() {
		return Refs.Lang.RUNE+".detonation";
	}

	@Override
	public RuneEntity createRune(ItemStack[][] actualPattern, EnumFacing front,
			Set<BlockPos> dusts, TileEntityDustActive entity) {
		return new RuneEntityUnimplemented(actualPattern, front, dusts, entity, this);
	}
	/* (non-Javadoc)
	 * @see xilef11.mc.runesofwizardry_classics.runes.VariableRune#variablesOK(net.minecraft.item.ItemStack[][])
	 */
	@Override
	protected boolean variablesOK(ItemStack[][] foundPattern) {
		//TODO in this one, the center can be different from the "fuse"
		return super.variablesOK(foundPattern);
	}

}

    