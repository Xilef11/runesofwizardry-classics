package xilef11.mc.runesofwizardry_classics.runes;

import java.io.IOException;
import java.util.Set;

import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.runes.entity.RuneEntityDawn;

import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.core.rune.PatternUtils;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

public class RuneDawn extends ClassicRune {

	@Override
	protected ItemStack[][] setupPattern() throws IOException {
		return PatternUtils.importFromJson(Refs.PATTERN_PATH+"runeDawn.json");
	}
	@Override
	public String getID() {
		return "runeDawn";
	}
	@Override
	protected Vec3i setupEntityPos() {
		return new Vec3i(1,1,0);//dosen't matter for this one, but center is 1,1
	}

	@Override
	protected ItemStack[][] setupSacrifice() {
		return new ItemStack[][]{
				{new ItemStack(Items.REDSTONE,4),new ItemStack(Items.dye,1,EnumDyeColor.BLUE.getMetadata())}
				};
	}

	@Override
	public String getName() {
		return Refs.Lang.RUNE+".dawn";
	}

	@Override
	public RuneEntity createRune(ItemStack[][] actualPattern, EnumFacing front,
			Set<BlockPos> dusts, TileEntityDustActive entity) {
		return new RuneEntityDawn(actualPattern, front, dusts, entity, this);
	}

}
