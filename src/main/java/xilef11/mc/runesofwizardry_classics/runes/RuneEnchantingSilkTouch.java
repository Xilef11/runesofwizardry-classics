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
import xilef11.mc.runesofwizardry_classics.runes.entity.RuneEntityEnchantingSilktouch;

import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.core.rune.PatternUtils;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;
public class RuneEnchantingSilkTouch extends ClassicRune {
	@Override
	protected ItemStack[][] setupPattern() throws IOException {
		return PatternUtils.importFromJson(Refs.PATTERN_PATH+"runeEnchantingSilkTouch.json");
	}
	@Override
	public String getID() {
		return "runeEnchantingSilkTouch";
	}
	@Override
	protected Vec3i setupEntityPos() {
		return new Vec3i(1,1,0);
	}
	@Override
	protected ItemStack[][] setupSacrifice() {
		return new ItemStack[][]{
				//FIXME this one doesn't accept the items with wildcard meta
				{new ItemStack(Blocks.GOLD_BLOCK),new ItemStack(Items.DIAMOND_PICKAXE,1,OreDictionary.WILDCARD_VALUE)},
				{new ItemStack(Blocks.GOLD_BLOCK),new ItemStack(Items.DIAMOND_SHOVEL,1,OreDictionary.WILDCARD_VALUE)}
				//take 10 XP
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
		return Refs.Lang.RUNE+".enchantingsilktouch";
	}
	@Override
	public RuneEntity createRune(ItemStack[][] actualPattern, EnumFacing front,
			Set<BlockPos> dusts, TileEntityDustActive entity) {
		return new RuneEntityEnchantingSilktouch(actualPattern, front, dusts, entity, this);
	}
}
