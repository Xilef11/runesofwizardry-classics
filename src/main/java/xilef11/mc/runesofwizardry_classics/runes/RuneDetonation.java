package xilef11.mc.runesofwizardry_classics.runes;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.core.rune.PatternUtils;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.runes.entity.RuneEntityDetonation;
import xilef11.mc.runesofwizardry_classics.utils.Utils.Coords;
public class RuneDetonation extends ClassicRune {
	@Override
	protected ItemStack[][] setupPattern() throws IOException {
		return PatternUtils.importFromJson(Refs.PATTERN_PATH+"runedetonation.json");
	}
	@Override
	public String getID() {
		return "runeDetonation";
	}
	@Override
	protected Vec3i setupEntityPos() {
		return new Vec3i(1,1,0);
	}
	@Override
	protected ItemStack[][] setupSacrifice() {
		return new ItemStack[][]{
				{new ItemStack(Items.GUNPOWDER,2)}
				};
	}
	@Override
	public String getName() {
		return Refs.Lang.RUNE+".detonation";
	}
	@Override
	public RuneEntity createRune(ItemStack[][] actualPattern, EnumFacing front,
			Set<BlockPos> dusts, TileEntityDustActive entity) {
		return new RuneEntityDetonation(actualPattern, front, dusts, entity, this);
	}
	private Set<Coords> center=null;
	public Set<Coords> getCenter(){
		if(center==null){
			center = new HashSet<>();
			center.add(new Coords(5,5));
			center.add(new Coords(5,6));
			center.add(new Coords(6,5));
			center.add(new Coords(6,6));
		}
		return center;
	}
	private Set<Coords> fuse=null;
	public Set<Coords> getFuse(){
		if(fuse==null){
			fuse=new HashSet<>();
			fuse.add(new Coords(2,3));
			fuse.add(new Coords(3,3));
			fuse.add(new Coords(3,4));
			fuse.add(new Coords(4,4));
		}
		return fuse;
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IRune#patternMatchesExtraCondition(net.minecraft.item.ItemStack[][], net.minecraft.item.ItemStack[][])
	 */
	@Override
	public boolean patternMatchesExtraCondition(ItemStack[][] foundPattern) {
		return fuseOK(foundPattern)&&centerOK(foundPattern);
	}
	private boolean centerOK(ItemStack[][] foundPattern) {
		ItemStack first = ItemStack.EMPTY;
		for(Coords c:getCenter()){
			if(first.isEmpty())first=foundPattern[c.row][c.col];
			if(!ItemStack.areItemStacksEqual(first, foundPattern[c.row][c.col]))return false;
		}
		return true;
	}
	private boolean fuseOK(ItemStack[][] foundPattern) {
		ItemStack first = ItemStack.EMPTY;
		for(Coords c:getFuse()){
			if(first.isEmpty())first=foundPattern[c.row][c.col];
			if(!ItemStack.areItemStacksEqual(first, foundPattern[c.row][c.col]))return false;
		}
		return true;
	}
	/* (non-Javadoc)
	 * @see xilef11.mc.runesofwizardry_classics.runes.ClassicRune#hasExtraSacrifice()
	 */
	@Override
	protected boolean hasExtraSacrifice() {
		return true;
	}
}
