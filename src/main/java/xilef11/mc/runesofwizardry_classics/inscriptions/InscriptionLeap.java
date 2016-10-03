package xilef11.mc.runesofwizardry_classics.inscriptions;

import java.io.IOException;

import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.utils.Utils;

import com.zpig333.runesofwizardry.core.rune.PatternUtils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class InscriptionLeap extends ClassicInscription {

	@Override
	protected ItemStack[][] setupPattern() throws IOException {
		return PatternUtils.importFromJson(new ResourceLocation(Refs.MODID,"patterns/inscriptions/InscriptionLeap.json"));
	}

	@Override
	protected ItemStack[] setupChargeItems() {
		return new ItemStack[]{new ItemStack(Items.FEATHER,4),new ItemStack(Items.FIREWORKS)};//4 xp
	}
	
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.Inscription#onInscriptionCharged(net.minecraft.entity.player.EntityPlayer, net.minecraft.item.ItemStack[], boolean)
	 */
	@Override
	public boolean onInscriptionCharged(EntityPlayer player,ItemStack[] sacrifice, boolean negated) {
		if(!player.worldObj.isRemote){
			if(!negated){
				return Utils.takeXP(player, 4);
			}
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see xilef11.mc.runesofwizardry_classics.inscriptions.ClassicInscription#hasExtraSacrifice()
	 */
	@Override
	protected boolean hasExtraSacrifice() {
		return true;
	}

	@Override
	public String getID() {
		return "leap";
	}

	@Override
	public int getMaxDurability() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void onWornTick(World world, EntityPlayer player, ItemStack stack) {
		// TODO Auto-generated method stub

	}

}
