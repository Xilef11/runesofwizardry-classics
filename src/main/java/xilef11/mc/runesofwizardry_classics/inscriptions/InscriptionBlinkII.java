package xilef11.mc.runesofwizardry_classics.inscriptions;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.utils.Utils;

import com.zpig333.runesofwizardry.api.DustRegistry;
import com.zpig333.runesofwizardry.core.rune.PatternUtils;
import com.zpig333.runesofwizardry.util.RayTracer;

public class InscriptionBlinkII extends ClassicInscription {
	//also has a particle effect at the teleport destination
	@Override
	protected ItemStack[][] setupPattern() throws IOException {
		return PatternUtils.importFromJson(new ResourceLocation(Refs.MODID,"patterns/inscriptions/InscriptionBlinkII.json"));
	}

	@Override
	protected ItemStack[] setupChargeItems() {
		ItemStack blink1 = DustRegistry.getStackForInscription(Refs.MODID+":blink");
		return new ItemStack[]{
				blink1,
				new ItemStack(Items.ENDER_PEARL,8),
				new ItemStack(Blocks.OBSIDIAN,4),
				new ItemStack(Blocks.END_STONE,8)
		};//10 xp
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
				return Utils.takeXP(player, 10);
			}
		}
		return true;
	}
	@Override
	public String getID() {
		return "blink2";
	}

	@Override
	public int getMaxDurability() {
		return 1001;
		//take 10 per teleport
	}
	private static final int DELAY=20;
	private static final int DAMAGE=10;
	@Override
	public void onWornTick(World world, EntityPlayer player, ItemStack stack) {
		if(player.isSneaking()){
			int newDamage =stack.getItemDamage()+DAMAGE; 
			if(newDamage<getMaxDurability()){
				if(world.getTotalWorldTime()>(getTime(stack)+DELAY)){
					//crappy workaround for the client-side wierdness
					RayTraceResult res = world.isRemote? Minecraft.getMinecraft().objectMouseOver : RayTracer.retrace(player,16);
					BlockPos to = res.getBlockPos();
					if(player.getDistanceSqToCenter(to)>16*16)return;
					if(world.isRemote){
						//FIXME to is the player position (client side) if using world.raytrace
						world.spawnParticle(EnumParticleTypes.SPELL, true, to.getX()+0.5, to.getY(), to.getZ()+0.5, 0d, 0d, 0d, 0);
					}
					if(player.isSwingInProgress&&player.getHeldItemMainhand()==null){
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
						player.setPositionAndUpdate(to.getX(), to.getY()+1, to.getZ());
						player.fallDistance=0;
						setTime(stack, world.getTotalWorldTime());
						if(!player.capabilities.isCreativeMode)stack.setItemDamage(newDamage);
					}
				}
			}
		}
	}
	protected static void setTime(ItemStack stack, long time){
		stack.getSubCompound(Refs.MODID, true).setLong("lastTime", time);
	}
	protected static long getTime(ItemStack stack){
		return stack.getSubCompound(Refs.MODID, true).getLong("lastTime");
	}

}
