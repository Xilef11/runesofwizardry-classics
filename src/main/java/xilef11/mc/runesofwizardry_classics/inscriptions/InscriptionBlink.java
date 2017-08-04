package xilef11.mc.runesofwizardry_classics.inscriptions;

import java.io.IOException;

import com.zpig333.runesofwizardry.core.rune.PatternUtils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.utils.Utils;

public class InscriptionBlink extends ClassicInscription {

	@Override
	protected ItemStack[][] setupPattern() throws IOException {
		return PatternUtils.importFromJson(new ResourceLocation(Refs.MODID,"patterns/inscriptions/inscriptionblink.json"));
	}

	@Override
	protected ItemStack[] setupChargeItems() {
		return new ItemStack[]{
				new ItemStack(Items.ENDER_PEARL,8),
				new ItemStack(Blocks.QUARTZ_BLOCK),
				new ItemStack(Items.BLAZE_ROD,2)
		};//10 XP
	}

	@Override
	public String getID() {
		return "blink";
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
				return Utils.takeXP(player, 10);
			}
		}
		return true;
	}

	@Override
	public int getMaxDurability() {
		return 1001;
		//take 20 per teleport
	}
	private static final int DELAY=20;
	private static final int DAMAGE=20;
	@Override
	public void onWornTick(World world, EntityPlayer player, ItemStack stack) {
		if(player.isSneaking()&&player.isSwingInProgress&&player.getHeldItemMainhand()==null){
			int newDamage =stack.getItemDamage()+DAMAGE; 
			if(newDamage<getMaxDurability()){
				if(world.getTotalWorldTime()>(getTime(stack)+DELAY)){
					//get the look direction
					Vec3d look = player.getLookVec();
					look = look.addVector(Math.random()*2-1,0, Math.random()*2-1);
					double dist = Math.random()*9+9D;
					BlockPos to = player.getPosition().add(dist*look.x, dist*look.y, dist*look.z);
					//sound + particles for fun
					world.playSound(null,player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ(), SoundEvents.ENTITY_ENDERMEN_TELEPORT,SoundCategory.PLAYERS, 1.0F, 1.0F);
					world.playSound(null,to.getX(), to.getY(), to.getZ(), SoundEvents.ENTITY_ENDERMEN_TELEPORT,SoundCategory.PLAYERS, 1.0F, 1.0F);
					if(world instanceof WorldServer){
						WorldServer ws = (WorldServer)world;
						ws.spawnParticle(EnumParticleTypes.SPELL_WITCH, player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ(), 10, 0.5, 0.5, 0.5, 0);
						//ws.spawnParticle(EnumParticleTypes.PORTAL, getPos().getX(), getPos().getY(), getPos().getZ(), 0.5F, 0.5, 0.5);
						//ws.spawnParticle(EnumParticleTypes.PORTAL, dest.getX(), dest.getY(), dest.getZ(), 0.5F, 0.5, 0.5);
						ws.spawnParticle(EnumParticleTypes.PORTAL, to.getX(), to.getY(), to.getZ(), 10, 0.5, 0.5, 0.5, 10);
					}
					player.setPositionAndUpdate(to.getX()+0.5, to.getY(), to.getZ()+0.5);
					player.fallDistance=0;
					setTime(stack, world.getTotalWorldTime());
					if(!player.capabilities.isCreativeMode)stack.setItemDamage(newDamage);
				}
			}
		}
	}
	protected static void setTime(ItemStack stack, long time){
		stack.getOrCreateSubCompound(Refs.MODID).setLong("lastTime", time);
	}
	protected static long getTime(ItemStack stack){
		return stack.getOrCreateSubCompound(Refs.MODID).getLong("lastTime");
	}

}
