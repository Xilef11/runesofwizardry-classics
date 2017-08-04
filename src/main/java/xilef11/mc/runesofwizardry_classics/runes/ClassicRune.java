/**
 *
 */
package xilef11.mc.runesofwizardry_classics.runes;
import java.io.IOException;

import com.zpig333.runesofwizardry.api.IRune;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import xilef11.mc.runesofwizardry_classics.Config;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.RunesofWizardry_Classics;
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
		if(Config.cacheRuneInfo){
			if(pattern==null){
				try {
					pattern=setupPattern();
				} catch (IOException e) {
					RunesofWizardry_Classics.log().fatal("Could not set up pattern",e);
				}
			}
			return pattern;
		}else{
			try {
				return setupPattern();
			} catch (IOException e) {
				RunesofWizardry_Classics.log().fatal("Could not set up pattern",e);
				return new ItemStack[4][4];
			}
		}
	}
	protected abstract ItemStack[][] setupPattern() throws IOException;
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IRune#getEntityPosition()
	 */
	@Override
	public Vec3i getEntityPosition() {
		if(Config.cacheRuneInfo){
			if(entityPos==null){
				entityPos = setupEntityPos();
			}
			return entityPos;
		}else{
			return setupEntityPos();
		}
	}
	protected abstract Vec3i setupEntityPos();
	public abstract String getID();
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IRune#getSacrifice()
	 */
	@Override
	public ItemStack[][] getSacrifice() {
		if(Config.cacheRuneInfo){
			if(sacrifice==null){
				sacrifice=setupSacrifice();
			}
			return sacrifice;
		}else{
			return setupSacrifice();
		}
	}
	//this has slightly more overhead in case of a completely null sacrifice, but reduces it otherwise
	protected abstract ItemStack[][] setupSacrifice();
	//return true to enable the extra sacrifice info
	protected boolean hasExtraSacrifice(){
		return false;
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IRune#getName()
	 */
	@Override
	public String getExtraSacrificeInfo() {
		return hasExtraSacrifice()? getName()+".extrasac" : null;
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IRune#canBeActivatedByPlayer(net.minecraft.entity.player.EntityPlayer, net.minecraft.world.World, net.minecraft.util.BlockPos)
	 */
	@Override
	public boolean canBeActivatedByPlayer(EntityPlayer player, World world,	BlockPos activationPos) {
		if(world.isRemote)return false;//Server-side only
		String perm = Config.getPermissionsForRune(this);
		boolean allowed=false;
		if(perm!=null){
			//if(perm.equals(Config.PERMISSIONS_NONE))allowed=false;
			if(perm.equals(Config.PERMISSIONS_ALL))allowed=true;
			if(perm.equals(Config.PERMISSIONS_OP)){
				String[] ops = player.getServer().getPlayerList().getOppedPlayerNames();
				for(String name:ops){
					if(name.equals(player.getName()))allowed=true;
				}
				//TODO check if cheats enabled if single player
			}
		}
		if(!allowed)player.sendMessage(new TextComponentTranslation(Refs.Lang.RUNE+".nopermission.message",new TextComponentTranslation(getName())));
		return allowed;
	}
}
