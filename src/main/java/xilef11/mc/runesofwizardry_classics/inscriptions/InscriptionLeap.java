package xilef11.mc.runesofwizardry_classics.inscriptions;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.utils.Utils;

import com.zpig333.runesofwizardry.core.rune.PatternUtils;

public class InscriptionLeap extends ClassicInscription {

	public InscriptionLeap(){
		this(1.15,10);
	}
	public InscriptionLeap(double power,int damage){
		POWER=power;
		DAMAGE=damage;
	}
	
	@Override
	protected ItemStack[][] setupPattern() throws IOException {
		return PatternUtils.importFromJson(new ResourceLocation(Refs.MODID,"patterns/inscriptions/InscriptionLeap.json"));
	}

	@Override
	protected ItemStack[] setupChargeItems() {
		return new ItemStack[]{new ItemStack(Items.FEATHER,4),new ItemStack(Items.FIREWORKS)};//4 xp
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
	
	/* (non-Javadoc)
	 * @see xilef11.mc.runesofwizardry_classics.inscriptions.ClassicInscription#hasExtraSacrifice()
	 */
	@Override
	protected boolean hasExtraSacrifice() {
		return true;
	}

	@Override
	public String getID() {
		return "leap";
	}

	@Override
	public int getMaxDurability() {
		return 1001;
		//take 10 per launch
	}
	private static final int DELAY=1*Refs.TPS;
	protected final int DAMAGE;
	protected final double POWER;
	@Override
	public void onWornTick(World world, EntityPlayer player, ItemStack stack) {
		if(isFalling(stack)){
			player.fallDistance=0;
			if(player.onGround)setFalling(stack, false);
		}
		if(player.isSneaking() && player.isSwingInProgress && player.getHeldItemMainhand().isEmpty()){
			int newDamage =stack.getItemDamage()+DAMAGE; 
			if(newDamage<getMaxDurability()){
				if(world.getTotalWorldTime()>(getTime(stack)+DELAY)){
					//launch the player
					Vec3d look = player.getLookVec();
					player.addVelocity((-player.motionX+look.xCoord)*POWER, (-player.motionY+look.yCoord)*POWER, (-player.motionZ+look.zCoord)*POWER);
					setTime(stack, world.getTotalWorldTime());
					setFalling(stack, true);
					if(!player.capabilities.isCreativeMode)stack.setItemDamage(newDamage);
				}
			}
		}
	}

	protected static void setFalling(ItemStack stack, boolean value){
		stack.getOrCreateSubCompound(Refs.MODID).setBoolean("falling", value);
	}
	protected static boolean isFalling(ItemStack stack){
		return stack.getOrCreateSubCompound(Refs.MODID).getBoolean("falling");
	}
	protected static void setTime(ItemStack stack, long time){
		stack.getOrCreateSubCompound(Refs.MODID).setLong("lastTime", time);
	}
	protected static long getTime(ItemStack stack){
		return stack.getOrCreateSubCompound(Refs.MODID).getLong("lastTime");
	}
}
