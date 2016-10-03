package xilef11.mc.runesofwizardry_classics.runes;
import java.io.IOException;
import java.util.Set;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.runes.entity.RuneEntityProtection;

import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.core.rune.PatternUtils;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;
public class RuneProtection extends VariableRune {
	@Override
	protected ItemStack[][] setupPattern() throws IOException {
		return PatternUtils.importFromJson(Refs.PATTERN_PATH+"runeProtection.json");
	}
	@Override
	public String getID() {
		return "runeProtection";
	}
	@Override
	protected Vec3i setupEntityPos() {
		return new Vec3i(1,1,0);
	}
	@Override
	protected ItemStack[][] setupSacrifice() {
		//TODO fix this now
		ItemStack villagerEgg = new ItemStack(Items.SPAWN_EGG,1,120);//Looks like we're using meta, tag is 1.9
//		NBTTagCompound id = new NBTTagCompound();
//		id.setString("id", "Villager");
//		villagerEgg.getTagCompound().setTag("EntityTag", id);
		return new ItemStack[][]{
				{villagerEgg}
				};
		//take 15 XP
	}
	@Override
	public String getName() {
		return Refs.Lang.RUNE+".protection";
	}
	@Override
	public RuneEntity createRune(ItemStack[][] actualPattern, EnumFacing front,
			Set<BlockPos> dusts, TileEntityDustActive entity) {
		return new RuneEntityProtection(actualPattern, front, dusts, entity, this);
	}
}
