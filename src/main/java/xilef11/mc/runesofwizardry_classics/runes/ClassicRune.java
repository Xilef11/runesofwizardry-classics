/**
 * 
 */
package xilef11.mc.runesofwizardry_classics.runes;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3i;

import com.zpig333.runesofwizardry.api.IRune;

/**
 * @author Xilef11
 *
 */
public abstract class ClassicRune extends IRune {
	private ItemStack[][] pattern=null;
	private ItemStack[][] sacrifice=null;
	private Vec3i entityPos=null;

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IRune#getPattern()
	 */
	@Override
	public ItemStack[][] getPattern() {
		if(pattern==null){
			pattern=setupPattern();
		}
		return pattern;
	}

	protected abstract ItemStack[][] setupPattern();
	

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IRune#getEntityPosition()
	 */
	@Override
	public Vec3i getEntityPosition() {
		if(entityPos==null){
			entityPos = setupEntityPos();
		}
		return entityPos;
	}

	protected abstract Vec3i setupEntityPos();

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IRune#getSacrifice()
	 */
	@Override
	public ItemStack[][] getSacrifice() {
		if(sacrifice==null){
			sacrifice=setupSacrifice();
		}
		return null;
	}

	protected abstract ItemStack[][] setupSacrifice();


}
