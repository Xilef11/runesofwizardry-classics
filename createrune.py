import sys
rune_Path = "./src/main/java/xilef11/mc/runesofwizardry_classics/runes/Rune"
lang_file = "src/main/resources/assets/runesofwizardry_classics/lang/en_US.lang"
runes_file = "src/main/java/xilef11/mc/runesofwizardry_classics/ModRunes.java"
shortName = sys.argv[1]
locName = sys.argv[2]

clas = open(rune_Path+shortName+".java","w")
clas.write('''
package xilef11.mc.runesofwizardry_classics.runes;

import java.io.IOException;
import java.util.Set;

import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.runes.entity.RuneEntityUnimplemented;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;

import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.core.rune.PatternUtils;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

public class Rune'''+shortName+''' extends ClassicRune {

	@Override
	protected ItemStack[][] setupPattern() throws IOException {
		return PatternUtils.importFromJson(Refs.PATTERN_PATH+"rune'''+shortName+'''.json");
	}

	@Override
	protected Vec3i setupEntityPos() {
		return new Vec3i(0,0,0);
	}

	@Override
	protected ItemStack[][] setupSacrifice() {
		return new ItemStack[][]{
				{}
				};
	}

	@Override
	public String getName() {
		return Refs.Lang.RUNE+".'''+shortName.lower()+'''";
	}

	@Override
	public RuneEntity createRune(ItemStack[][] actualPattern, EnumFacing front,
			Set<BlockPos> dusts, TileEntityDustActive entity) {
		return new RuneEntityUnimplemented(actualPattern, front, dusts, entity, this);
	}

}

    ''')
clas.close()

lang = open(lang_file,"a")
lang.write('runesofwizardry_classics.rune.'+shortName.lower()+'='+locName+'\n')
lang.close()
#Note: This will always append to the complete end of the file
runes = open(runes_file,"a")
runes.write('		DustRegistry.registerRune(new Rune'+shortName+'());\n')
runes.close()
