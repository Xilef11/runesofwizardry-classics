package xilef11.mc.runesofwizardry_classics.runes;
import java.io.IOException;
import java.util.Set;

import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.core.rune.PatternUtils;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.runes.entity.RuneEntityUnimplemented;
public class RuneSpawnerReassignment extends ClassicRune {
	@Override
	protected ItemStack[][] setupPattern() throws IOException {
		return PatternUtils.importFromJson(Refs.PATTERN_PATH+"runespawnerreassignment.json");
	}
	@Override
	public String getID() {
		return "runeSpawnerReassignment";
	}
	@Override
	protected Vec3i setupEntityPos() {
		return new Vec3i(1,2,0);//below the spawner
	}
	@Override
	protected ItemStack[][] setupSacrifice() {
		return null;
		//No need for this rune, spawners can be set by using a spawn egg on them directly.
//		return new ItemStack[][]{
//				{new ItemStack(Items.SPAWN_EGG),new ItemStack(Items.ender_pearl,2)}//take 10 xp (might have to do something to ignore egg NBT)
//				};
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
		return Refs.Lang.RUNE+".spawnerreassignment";
	}
	@Override
	public RuneEntity createRune(ItemStack[][] actualPattern, EnumFacing front,
			Set<BlockPos> dusts, TileEntityDustActive entity) {
		return new RuneEntityUnimplemented(actualPattern, front, dusts, entity, this);
	}
}
