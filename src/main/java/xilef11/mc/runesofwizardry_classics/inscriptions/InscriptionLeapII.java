package xilef11.mc.runesofwizardry_classics.inscriptions;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.utils.Utils;

import com.zpig333.runesofwizardry.api.DustRegistry;
import com.zpig333.runesofwizardry.core.rune.PatternUtils;

public class InscriptionLeapII extends ClassicInscription {

	@Override
	protected ItemStack[][] setupPattern() throws IOException {
		return PatternUtils.importFromJson(new ResourceLocation(Refs.MODID,"patterns/inscriptions/InscriptionLeapII.json"));
	}

	@Override
	protected ItemStack[] setupChargeItems() {
		ItemStack leap1 = DustRegistry.getStackForInscription(Refs.MODID+":leap");
		ItemStack slimeEgg = new ItemStack(Items.SPAWN_EGG);
		NBTTagCompound id = new NBTTagCompound();
		id.setString("id", "Slime");
		NBTTagCompound main = new NBTTagCompound();
		main.setTag("EntityTag", id);
		slimeEgg.setTagCompound(main);
		return new ItemStack[]{leap1,slimeEgg};
		//+7 xp
	}
	
	/* (non-Javadoc)
	 * @see xilef11.mc.runesofwizardry_classics.inscriptions.ClassicInscription#hasExtraSacrifice()
	 */
	@Override
	protected boolean hasExtraSacrifice() {
		return true;
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.Inscription#onInscriptionCharged(net.minecraft.entity.player.EntityPlayer, net.minecraft.item.ItemStack[], boolean)
	 */
	@Override
	public boolean onInscriptionCharged(EntityPlayer player,ItemStack[] sacrifice, boolean negated) {
		if(!player.worldObj.isRemote){
			if(!negated){
				return Utils.takeXP(player, 7);
			}
		}
		return true;
	}

	@Override
	public String getID() {
		return "leap2";
	}

	@Override
	public int getMaxDurability() {
		return 1001;
		//take 10 per launch
	}

	@Override
	public void onWornTick(World world, EntityPlayer player, ItemStack stack) {
		// TODO Auto-generated method stub

	}

}
