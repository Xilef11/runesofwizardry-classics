package xilef11.mc.runesofwizardry_classics.inscriptions;

import java.io.IOException;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.utils.Utils;

import com.zpig333.runesofwizardry.api.DustRegistry;
import com.zpig333.runesofwizardry.core.rune.PatternUtils;

public class InscriptionLeapII extends InscriptionLeap {
	public InscriptionLeapII(){
		super(1.85,4);
		MinecraftForge.EVENT_BUS.register(this);
	}
	@SubscribeEvent
	public void onJump(LivingJumpEvent event){
		EntityLivingBase ent = event.getEntityLiving();
		if(ent instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer)ent;
			ItemStack worn = DustRegistry.getWornInscription(player);
			if(!player.isSneaking()&&DustRegistry.getInscriptionFromStack(worn)==this){
				//player.jumpMovementFactor=1.5F;
				player.addVelocity(0, 0.025, 0);
				player.velocityChanged=true;
			}
		}
	}
	
	@Override
	protected ItemStack[][] setupPattern() throws IOException {
		return PatternUtils.importFromJson(new ResourceLocation(Refs.MODID,"patterns/inscriptions/inscriptionleapii.json"));
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
		if(!player.world.isRemote){
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

}
