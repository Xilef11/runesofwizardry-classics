/**
 *
 */
package xilef11.mc.runesofwizardry_classics.runes;
import java.util.Set;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import static net.minecraft.item.ItemStack.EMPTY;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.items.DustVariable;
import xilef11.mc.runesofwizardry_classics.runes.entity.RuneEntityTorch;

import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;
/**
 * @author Xilef11
 *
 */
public class RuneTorch extends VariableRune {
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IRune#getName()
	 */
	@Override
	public String getName() {
		return Refs.Lang.RUNE+".torch";
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IRune#createRune(net.minecraft.item.ItemStack[][], net.minecraft.util.EnumFacing, java.util.Set, com.zpig333.runesofwizardry.tileentity.TileEntityDustActive)
	 */
	@Override
	public RuneEntity createRune(ItemStack[][] actualPattern, EnumFacing front,
			Set<BlockPos> dusts, TileEntityDustActive entity) {
		return new RuneEntityTorch(actualPattern, front, dusts, entity,this);
	}
	@Override
	protected ItemStack[][] setupPattern() {
		ItemStack variable = new ItemStack(DustVariable.instance);
		return new ItemStack[][]{
				{EMPTY,EMPTY,EMPTY,EMPTY},
				{EMPTY,variable,variable,EMPTY},
				{EMPTY,variable,variable,EMPTY},
				{EMPTY,EMPTY,EMPTY,EMPTY}
				};
	}
	//sacrifice is nothing or a piece of flint
	@Override
	protected ItemStack[][] setupSacrifice() {
		return new ItemStack[][]{
				null,
				{new ItemStack(Items.FLINT)}
		};
	}
	@Override
	protected Vec3i setupEntityPos() {
		return new Vec3i(0,0,0);
	}
	@Override
	public String getID() {
		return "runeTorch";
	}
}
