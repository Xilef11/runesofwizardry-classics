package xilef11.mc.runesofwizardry_classics.inscriptions;

import java.io.IOException;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xilef11.mc.runesofwizardry_classics.ModLogger;
import xilef11.mc.runesofwizardry_classics.Refs;

import com.zpig333.runesofwizardry.api.DustRegistry;
import com.zpig333.runesofwizardry.core.rune.PatternUtils;

public class InscriptionBounce extends ClassicInscription {

	public InscriptionBounce(){
		//MinecraftForge.EVENT_BUS.register(this);
	}
	//looks like this is not fired in creative mode :(
//	@SubscribeEvent
//	public void onFall(LivingFallEvent event){
//		EntityLivingBase ent = event.getEntityLiving();
//		if(ent instanceof EntityPlayer){
//			EntityPlayer player = (EntityPlayer)  ent;
//			ItemStack insc = DustRegistry.getWornInscription(player);
//			if(insc!=null && DustRegistry.getInscriptionFromStack(insc)==this){
//				player.motionY=-player.motionY*0.76D;
//				//player.setVelocity(player.motionX, -player.motionY*0.76, player.motionZ);
//				player.velocityChanged=true;
//				event.setDistance(event.getDistance()/2);
//				ModLogger.logInfo("bounced. X:"+player.motionX+" y:"+player.motionY+" z:"+player.motionZ);
//				event.setCanceled(true);
//			}
//		}
//	}
	
	@Override
	protected ItemStack[][] setupPattern() throws IOException {
		return PatternUtils.importFromJson(new ResourceLocation(Refs.MODID,"patterns/inscriptions/InscriptionBounce.json"));
	}

	@Override
	protected ItemStack[] setupChargeItems() {
		return new ItemStack[]{new ItemStack(Items.FEATHER,8),new ItemStack(Items.LEATHER_BOOTS)};
	}

	@Override
	public String getID() {
		return "bounce";
	}

	@Override
	public int getMaxDurability() {
		return 1001;
		//not sure, 24 per bounce?
	}
	@Override
	public void onWornTick(World world, EntityPlayer player, ItemStack stack) {
		// TODO Auto-generated method stub

	}

}
