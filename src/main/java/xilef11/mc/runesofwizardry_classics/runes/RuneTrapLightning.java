
package xilef11.mc.runesofwizardry_classics.runes;

import java.io.IOException;
import java.util.Set;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.items.EnumDustTypes;
import xilef11.mc.runesofwizardry_classics.runes.entity.RuneEntityTrapLightning;
import xilef11.mc.runesofwizardry_classics.utils.Utils.Coords;

import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.core.rune.PatternUtils;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

public class RuneTrapLightning extends VariableRune {

	@Override
	protected ItemStack[][] setupPattern() throws IOException {
		return PatternUtils.importFromJson(Refs.PATTERN_PATH+"runeTrapLightning.json");
	}

	@Override
	protected Vec3i setupEntityPos() {
		return new Vec3i(0,0,0);
	}
	@Override
	public String getID() {
		return "runeTrapLightning";
	}
	@Override
	protected ItemStack[][] setupSacrifice() {
		return new ItemStack[][]{
				{new ItemStack(Items.iron_ingot,3)}
				};
	}

	@Override
	public String getName() {
		return Refs.Lang.RUNE+".traplightning";
	}

	@Override
	public RuneEntity createRune(ItemStack[][] actualPattern, EnumFacing front,
			Set<BlockPos> dusts, TileEntityDustActive entity) {
		return new RuneEntityTrapLightning(actualPattern, front, dusts, entity, this);
	}
	/* (non-Javadoc)
	 * @see xilef11.mc.runesofwizardry_classics.runes.VariableRune#variablesOK(net.minecraft.item.ItemStack[][])
	 */
	@Override
	protected boolean variablesOK(ItemStack[][] foundPattern) {
		Coords any = getVariableDusts().iterator().next();
		EnumDustTypes type = EnumDustTypes.getByMeta(foundPattern[any.row][any.col].getMetadata());
		return super.variablesOK(foundPattern) && type!=EnumDustTypes.PLANT;
	}
}

    