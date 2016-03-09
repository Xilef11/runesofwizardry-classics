/**
 * 
 */
package xilef11.mc.runesofwizardry_classics.runes;

import java.io.IOException;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3i;

import org.apache.logging.log4j.Level;

import xilef11.mc.runesofwizardry_classics.ModLogger;

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
			try {
				pattern=setupPattern();
			} catch (IOException e) {
				ModLogger.logException(Level.FATAL, e, "Could not set up pattern");
			}
		}
		return pattern;
	}

	protected abstract ItemStack[][] setupPattern() throws IOException;
	

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
	//this has slightly more overhead in case of a completely null sacrifice, but reduces it otherwise
	protected abstract ItemStack[][] setupSacrifice();


}
