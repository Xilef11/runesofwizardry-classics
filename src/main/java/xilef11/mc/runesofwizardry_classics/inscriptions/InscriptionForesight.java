package xilef11.mc.runesofwizardry_classics.inscriptions;

import java.io.IOException;

import com.zpig333.runesofwizardry.api.DustRegistry;
import com.zpig333.runesofwizardry.core.rune.PatternUtils;

import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import xilef11.mc.runesofwizardry_classics.Config;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.utils.Utils;

public class InscriptionForesight extends ClassicInscription {
	//Original doesn't seem to be doing anything...
	@Override
	protected ItemStack[][] setupPattern() throws IOException {
		return PatternUtils.importFromJson(new ResourceLocation(Refs.MODID,"patterns/inscriptions/inscriptionforesight.json"));
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
		if(!player.world.isRemote){
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
		int damage = stack.getItemDamage();
		if(damage<DustRegistry.getInscriptionFromStack(stack).getMaxDurability()){
			if(world.getTotalWorldTime()%(2*Refs.TPS)==0&&!player.capabilities.isCreativeMode){
				stack.setItemDamage(damage+1);
			}
		}else{
			return;
		}
		if(world.isRemote){
			int skysub=world.calculateSkylightSubtracted(1.0f);
			int rad = Config.foresightRadius;
			EntityZombie dummy = new EntityZombie(world);
			for(int i=-rad;i<rad;i++){
				for(int j=-rad;j<rad;j++){
					for(int k=-rad;k<rad;k++){
						BlockPos pos = player.getPosition().add(i, j, k);
						dummy.setPosition(pos.getX(),pos.getY(),pos.getZ());
						boolean test2 = dummy.getCanSpawnHere()&&!world.isAirBlock(pos)&&world.isAirBlock(pos.up());
						if(test2&& Math.random() < 0.2){
							int blockLight = world.getLightFor(EnumSkyBlock.BLOCK, pos.up());
							int skyLight = world.getLightFor(EnumSkyBlock.SKY, pos.up())-skysub;
							//RunesofWizardry_Classics.log().info(blockLight+" "+skyLight);
							if(blockLight<8){
								if(skyLight<8){//can spawn now
									world.spawnParticle(EnumParticleTypes.SPELL, false, pos.getX()+0.5, pos.getY()+1, pos.getZ()+0.5, 0d, 0d, 0d);
								}else{//can spawn at night
									world.spawnParticle(EnumParticleTypes.SPELL_MOB, false, pos.getX()+0.5, pos.getY()+1, pos.getZ()+0.5, 0d, 0d, 0d);
								}
							}
						}
					}
				}
			}
		}

	}

}
