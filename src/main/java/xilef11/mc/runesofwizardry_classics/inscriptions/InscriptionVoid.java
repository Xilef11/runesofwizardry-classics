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
import com.zpig333.runesofwizardry.core.rune.PatternUtils;

public class InscriptionVoid extends ClassicInscription {
	public InscriptionVoid() {
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void onItemPickup(EntityItemPickupEvent event){
		EntityPlayer player = event.getEntityPlayer();
		if(player==null)return;
		ItemStack insc = DustRegistry.getWornInscription(player);
		if(!insc.isEmpty() && DustRegistry.getInscriptionFromStack(insc)==this){
			int damage = insc.getItemDamage();
			int damageLeft = getMaxDurability()-damage;
			if(damageLeft>0){
				IVoidStorageCapability store = player.getCapability(VoidStorageCapability.VOID_STORAGE_CAPABILITY, null);
				if(store!=null){
					ItemStack stack = event.getItem().getEntityItem();
					ItemStack split = stack.splitStack(damageLeft);
					store.addStackToVoid(split);
					if(!player.capabilities.isCreativeMode)insc.setItemDamage(damage+split.getCount());
				}
			}
		}
	}
	
	@Override
	protected ItemStack[][] setupPattern() throws IOException {
		return PatternUtils.importFromJson(new ResourceLocation(Refs.MODID,"patterns/inscriptions/inscriptionvoid.json"));
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
		if(!player.world.isRemote){
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
