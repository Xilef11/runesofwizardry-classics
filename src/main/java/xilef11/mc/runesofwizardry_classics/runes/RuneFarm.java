package xilef11.mc.runesofwizardry_classics.runes;
import java.io.IOException;
import java.util.Set;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.runes.entity.RuneEntityFarm;

import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.core.rune.PatternUtils;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;
public class RuneFarm extends VariableRune {
	@Override
	protected ItemStack[][] setupPattern() throws IOException {
		return PatternUtils.importFromJson(Refs.PATTERN_PATH+"runeFarm.json");
	}
	@Override
	protected Vec3i setupEntityPos() {
		return new Vec3i(1,1,0);
	}
	@Override
	public String getID() {
		return "runeFarm";
	}
	@Override
	protected ItemStack[][] setupSacrifice() {
		return new ItemStack[][]{
				{new ItemStack(Items.IRON_INGOT,8)}
		};
	}
	@Override
	public String getName() {
		return Refs.Lang.RUNE+".farm";
	}
	@Override
	public RuneEntity createRune(ItemStack[][] actualPattern, EnumFacing front,
			Set<BlockPos> dusts, TileEntityDustActive entity) {
		return new RuneEntityFarm(actualPattern, front, dusts, entity, this);
	}
}
