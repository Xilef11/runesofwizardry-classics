package xilef11.mc.runesofwizardry_classics.runes;
import java.io.IOException;
import java.util.Set;

import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.core.rune.PatternUtils;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.oredict.OreDictionary;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.runes.entity.RuneEntityEnchantingFortune;
public class RuneEnchantingFortune extends ClassicRune {
	@Override
	protected ItemStack[][] setupPattern() throws IOException {
		return PatternUtils.importFromJson(Refs.PATTERN_PATH+"runeenchantingfortune.json");
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
		//FIXME somehow this accepted a shovel
		return new ItemStack[][]{
				{new ItemStack(Blocks.DIAMOND_ORE),new ItemStack(Blocks.LAPIS_ORE),new ItemStack(Blocks.REDSTONE_ORE),new ItemStack(Items.DIAMOND_PICKAXE,1,OreDictionary.WILDCARD_VALUE)},
				{new ItemStack(Blocks.DIAMOND_ORE),new ItemStack(Blocks.LAPIS_ORE),new ItemStack(Blocks.REDSTONE_ORE),new ItemStack(Items.DIAMOND_SWORD,1,OreDictionary.WILDCARD_VALUE)}
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
