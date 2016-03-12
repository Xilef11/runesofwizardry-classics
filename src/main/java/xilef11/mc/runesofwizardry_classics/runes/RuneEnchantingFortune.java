
package xilef11.mc.runesofwizardry_classics.runes;

import java.io.IOException;
import java.util.Set;

import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.runes.entity.RuneEntityUnimplemented;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import net.minecraftforge.oredict.OreDictionary;

import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.core.rune.PatternUtils;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

public class RuneEnchantingFortune extends ClassicRune {

	@Override
	protected ItemStack[][] setupPattern() throws IOException {
		return PatternUtils.importFromJson(Refs.PATTERN_PATH+"runeEnchantingFortune.json");
	}

	@Override
	protected Vec3i setupEntityPos() {
		return new Vec3i(1,1,0);
	}

	@Override
	protected ItemStack[][] setupSacrifice() {
		return new ItemStack[][]{
				{new ItemStack(Blocks.diamond_ore),new ItemStack(Blocks.lapis_ore),new ItemStack(Blocks.redstone_ore),new ItemStack(Items.diamond_pickaxe,1,OreDictionary.WILDCARD_VALUE)},
				{new ItemStack(Blocks.diamond_ore),new ItemStack(Blocks.lapis_ore),new ItemStack(Blocks.redstone_ore),new ItemStack(Items.diamond_sword,1,OreDictionary.WILDCARD_VALUE)}
				};
		//SAC take 15 XP
	}

	@Override
	public String getName() {
		return Refs.Lang.RUNE+".enchantingfortune";
	}

	@Override
	public RuneEntity createRune(ItemStack[][] actualPattern, EnumFacing front,
			Set<BlockPos> dusts, TileEntityDustActive entity) {
		return new RuneEntityUnimplemented(actualPattern, front, dusts, entity, this);
	}

}

    