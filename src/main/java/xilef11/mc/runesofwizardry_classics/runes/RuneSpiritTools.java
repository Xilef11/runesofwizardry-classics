
package xilef11.mc.runesofwizardry_classics.runes;

import java.io.IOException;
import java.util.Set;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.items.EnumDustTypes;
import xilef11.mc.runesofwizardry_classics.runes.entity.RuneEntityUnimplemented;

import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

public class RuneSpiritTools extends ClassicRune {

	@Override
	protected ItemStack[][] setupPattern() throws IOException {
		ItemStack l = EnumDustTypes.LAPIS.getStack(1);
		return new ItemStack[][]{
				{l,l,l,l},
				{l,l,l,l},
				{l,l,l,l},
				{l,l,l,l}
		};
	}
	@Override
	public String getID() {
		return "runeSpiritTools";
	}
	@Override
	protected Vec3i setupEntityPos() {
		return new Vec3i(0,0,0);
	}

	@Override
	protected ItemStack[][] setupSacrifice() {
		return new ItemStack[][]{
				{new ItemStack(Items.GOLDEN_PICKAXE),new ItemStack(Blocks.TNT,4)},//SAC take 18 XP (both times
				{new ItemStack(Items.GOLDEN_SWORD),new ItemStack(Blocks.GLOWSTONE)}
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
		return Refs.Lang.RUNE+".spirittools";
	}

	@Override
	public RuneEntity createRune(ItemStack[][] actualPattern, EnumFacing front,
			Set<BlockPos> dusts, TileEntityDustActive entity) {
		return new RuneEntityUnimplemented(actualPattern, front, dusts, entity, this);
	}

}

    