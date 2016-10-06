package xilef11.mc.runesofwizardry_classics.inscriptions;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.managers.IVoidStorageCapability;
import xilef11.mc.runesofwizardry_classics.managers.VoidStorageCapability;
import xilef11.mc.runesofwizardry_classics.utils.Utils;

import com.zpig333.runesofwizardry.api.DustRegistry;
import com.zpig333.runesofwizardry.core.WizardryRegistry;
import com.zpig333.runesofwizardry.core.rune.PatternUtils;
import com.zpig333.runesofwizardry.item.ItemInscription;

public class InscriptionVoid extends ClassicInscription {
	public InscriptionVoid() {
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void onItemPickup(EntityItemPickupEvent event){
		EntityPlayer player = event.getEntityPlayer();
		if(player==null)return;
		ItemStack insc = ((ItemInscription)WizardryRegistry.inscription).getWornInscription(player);
		if(insc!=null && insc.getItem()==WizardryRegistry.inscription){
			if(DustRegistry.getInscriptionFromStack(insc)==this){
				if(insc.getItemDamage()<getMaxDurability()){
					IVoidStorageCapability store = player.getCapability(VoidStorageCapability.VOID_STORAGE_CAPABILITY, null);
					if(store!=null){
						store.addStackToVoid(event.getItem().getEntityItem());
						//TODO durability
					}
				}
			}
		}
	}
	
	@Override
	protected ItemStack[][] setupPattern() throws IOException {
		return PatternUtils.importFromJson(new ResourceLocation(Refs.MODID,"patterns/inscriptions/InscriptionVoid.json"));
	}

	@Override
	protected ItemStack[] setupChargeItems() {
		return new ItemStack[]{new ItemStack(Blocks.OBSIDIAN,4),new ItemStack(Items.ENDER_PEARL,2)};
		// 5 xp
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
				return Utils.takeXP(player, 5);
			}
		}
		return true;
	}

	@Override
	public String getID() {
		return "void";
	}

	@Override
	public int getMaxDurability() {
		return 1001;
		//take 1 per item (not stack)
	}

	@Override
	public void onWornTick(World world, EntityPlayer player, ItemStack stack) {
		//NOP
	}

}
