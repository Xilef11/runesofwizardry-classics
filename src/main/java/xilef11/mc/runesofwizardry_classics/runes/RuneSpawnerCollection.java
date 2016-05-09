
package xilef11.mc.runesofwizardry_classics.runes;

import java.io.IOException;
import java.util.Set;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.runes.entity.RuneEntitySpawnerCollection;

import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.core.rune.PatternUtils;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

public class RuneSpawnerCollection extends ClassicRune {

	@Override
	protected ItemStack[][] setupPattern() throws IOException {
		return PatternUtils.importFromJson(Refs.PATTERN_PATH+"runeSpawnerCollection.json");
	}

	@Override
	protected Vec3i setupEntityPos() {
		return new Vec3i(1,2,0);//just below the spawner
	}

	@Override
	protected ItemStack[][] setupSacrifice() {
		return new ItemStack[][]{
				{new ItemStack(Items.GOLD_INGOT,6)}//SAC take 10 XP
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
		return Refs.Lang.RUNE+".spawnercollection";
	}

	@Override
	public RuneEntity createRune(ItemStack[][] actualPattern, EnumFacing front,
			Set<BlockPos> dusts, TileEntityDustActive entity) {
		return new RuneEntitySpawnerCollection(actualPattern, front, dusts, entity,this);
	}
	@Override
	public String getID() {
		return "runeSpawnerCollection";
	}
}

    