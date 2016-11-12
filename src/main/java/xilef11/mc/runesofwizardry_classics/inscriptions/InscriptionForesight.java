package xilef11.mc.runesofwizardry_classics.inscriptions;

import java.io.IOException;

import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import xilef11.mc.runesofwizardry_classics.Config;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.utils.Utils;

import com.zpig333.runesofwizardry.core.rune.PatternUtils;

public class InscriptionForesight extends ClassicInscription {
	//Original doesn't seem to be doing anything...
	@Override
	protected ItemStack[][] setupPattern() throws IOException {
		return PatternUtils.importFromJson(new ResourceLocation(Refs.MODID,"patterns/inscriptions/InscriptionForesight.json"));
	}

	@Override
	protected ItemStack[] setupChargeItems() {
		return new ItemStack[]{new ItemStack(Blocks.LAPIS_BLOCK)};//20 XP
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
				return Utils.takeXP(player, 20);
			}
		}
		return true;
	}
	@Override
	public String getID() {
		return "foresight";
	}

	@Override
	public int getMaxDurability() {
		return 1001;
		//take 1 per 2 seconds
	}

	@Override
	public void onWornTick(World world, EntityPlayer player, ItemStack stack) {
		// TODO Auto-generated method stub
		if(world.isRemote){
			EntityZombie dummy = new EntityZombie(world);
			dummy.setChildSize(true);
			int rad = Config.foresightRadius;
			for(int i=-rad;i<rad;i++){
				for(int j=-rad;j<rad;j++){
					for(int k=-rad;k<rad;k++){
						//FIXME apparently not working
						dummy.setPosition(player.serverPosX+i, player.serverPosY+j, player.serverPosZ+k);
						if(dummy.getCanSpawnHere()/*&& Math.random() < 0.2*/){
							world.spawnParticle(EnumParticleTypes.SPELL_WITCH, false, dummy.posX+0.5, dummy.posY, dummy.posZ+0.5, 0d, 0d, 0d, 0);
						}
					}
				}
			}
		}

	}

}
