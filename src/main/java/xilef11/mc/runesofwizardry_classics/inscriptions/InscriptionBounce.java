package xilef11.mc.runesofwizardry_classics.inscriptions;

import java.io.IOException;

import xilef11.mc.runesofwizardry_classics.Refs;

import com.zpig333.runesofwizardry.core.rune.PatternUtils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void onWornTick(World world, EntityPlayer player, ItemStack stack) {
		// TODO Auto-generated method stub

	}

}
