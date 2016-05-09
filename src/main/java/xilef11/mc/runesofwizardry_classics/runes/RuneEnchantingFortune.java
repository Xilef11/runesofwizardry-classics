
package xilef11.mc.runesofwizardry_classics.runes;

import java.io.IOException;
import java.util.Set;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.oredict.OreDictionary;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.runes.entity.RuneEntityEnchantingFortune;

import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.core.rune.PatternUtils;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

public class RuneEnchantingFortune extends ClassicRune {

	@Override
	protected ItemStack[][] setupPattern() throws IOException {
		return PatternUtils.importFromJson(Refs.PATTERN_PATH+"runeEnchantingFortune.json");
	}
	@Override
	public String getID() {
		return "runeEnchantingFortune";
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
		//take 15 XP
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
		return Refs.Lang.RUNE+".enchantingfortune";
	}

	@Override
	public RuneEntity createRune(ItemStack[][] actualPattern, EnumFacing front,
			Set<BlockPos> dusts, TileEntityDustActive entity) {
		return new RuneEntityEnchantingFortune(actualPattern, front, dusts, entity, this);
	}

}

    