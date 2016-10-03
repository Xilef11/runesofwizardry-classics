package xilef11.mc.runesofwizardry_classics.inscriptions;

import java.io.IOException;

import xilef11.mc.runesofwizardry_classics.Refs;

import com.zpig333.runesofwizardry.core.rune.PatternUtils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class InscriptionForesight extends ClassicInscription {

	@Override
	protected ItemStack[][] setupPattern() throws IOException {
		return PatternUtils.importFromJson(new ResourceLocation(Refs.MODID,"patterns/inscriptions/InscriptionForesight.json"));
	}

	@Override
	protected ItemStack[] setupChargeItems() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getID() {
		return "foresight";
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
