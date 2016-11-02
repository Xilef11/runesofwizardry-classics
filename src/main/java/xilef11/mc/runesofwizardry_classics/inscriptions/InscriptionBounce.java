package xilef11.mc.runesofwizardry_classics.inscriptions;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import xilef11.mc.runesofwizardry_classics.Refs;

import com.zpig333.runesofwizardry.core.rune.PatternUtils;

public class InscriptionBounce extends ClassicInscription {
	
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
		if(!player.onGround){
			//update speed and fall status
			if(getLastSpeedY(stack)>player.motionY){
				setFalling(stack, true, (float)player.motionY);
			}
		}else if(wasFalling(stack) && getLastSpeedY(stack) < -0.75f){
			//we were falling fast enough to bounce
			if(!player.isSneaking()){//cancel if sneak
				player.fallDistance /=2;
				player.motionY = -getLastSpeedY(stack)*0.76D;
				if(!player.capabilities.isCreativeMode)stack.setItemDamage(stack.getItemDamage()+8);
			}
			//reset falling status and speed
			setFalling(stack,false,0);
		}
	}

	private float getLastSpeedY(ItemStack item){
		NBTTagCompound tag = item.getSubCompound(Refs.MODID, true);
		return tag.getFloat("ySpeed");
	}
	private boolean wasFalling(ItemStack item){
		NBTTagCompound tag = item.getSubCompound(Refs.MODID, true);
		return tag.getBoolean("falling");
	}
	private void setFalling(ItemStack item,boolean falling,float yspeed){
		NBTTagCompound tag = item.getSubCompound(Refs.MODID, true);
		tag.setBoolean("falling", falling);
		tag.setFloat("ySpeed", yspeed);
	}
}
