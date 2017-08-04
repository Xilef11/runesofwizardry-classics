package xilef11.mc.runesofwizardry_classics.inscriptions;

import java.io.IOException;

import com.zpig333.runesofwizardry.api.Inscription;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import xilef11.mc.runesofwizardry_classics.Config;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.RunesofWizardry_Classics;

public abstract class ClassicInscription extends Inscription{
	private ItemStack[][] pattern;
	private ItemStack[] sacrifice;
	
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
	 * @see com.zpig333.runesofwizardry.api.Inscription#getChargeItems()
	 */
	@Override
	public ItemStack[] getChargeItems() {
		if(Config.cacheRuneInfo){
			if(sacrifice==null){
				sacrifice=setupChargeItems();
			}
			return sacrifice;
		}else{
			return setupChargeItems();
		}
	}
	
	protected abstract ItemStack[] setupChargeItems();
	
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.Inscription#getName()
	 */
	@Override
	public String getName() {
		return Refs.MODID+".inscription."+getID();
	}
	public abstract String getID();
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.Inscription#canBeActivatedByPlayer(net.minecraft.entity.player.EntityPlayer, net.minecraft.world.World, net.minecraft.util.math.BlockPos)
	 */
	@Override
	public boolean canBeActivatedByPlayer(EntityPlayer player, World world,	BlockPos activationPos) {
		if(world.isRemote)return false;//Server-side only
		String perm = Config.getPermissionsForInscription(this);
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
		if(!allowed)player.sendMessage(new TextComponentTranslation(Refs.MODID+".inscription.nopermission.message",new TextComponentTranslation(getName())));
		return allowed;
	}
	
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.Inscription#getExtraChargeInfo()
	 */
	@Override
	public String getExtraChargeInfo() {
		return hasExtraSacrifice()? getName()+".extrasac" : null;
	}

	protected boolean hasExtraSacrifice(){
		return false;
	}
	
}
