package xilef11.mc.runesofwizardry_classics.inscriptions;

import java.io.IOException;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import org.lwjgl.input.Keyboard;

import xilef11.mc.runesofwizardry_classics.Config;
import xilef11.mc.runesofwizardry_classics.Refs;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.core.rune.PatternUtils;
import com.zpig333.runesofwizardry.util.RayTracer;

public class InscriptionReturn extends ClassicInscription {
	@Override
	protected ItemStack[][] setupPattern() throws IOException {
		return PatternUtils.importFromJson(new ResourceLocation(Refs.MODID,"patterns/inscriptions/inscriptionreturn.json"));
	}

	@Override
	protected ItemStack[] setupChargeItems() {
		return new ItemStack[]{new ItemStack(Blocks.QUARTZ_BLOCK),new ItemStack(Items.ENDER_PEARL,2)};
	}

	@Override
	public String getID() {
		return "return";
	}

	@Override
	public int getMaxDurability() {
		return 1001;
		//take 20 per tp
	}
	private static final int DAMAGE=20;
	@Override
	public void onWornTick(World world, EntityPlayer player, ItemStack stack) {
		if(player.isSneaking() && (stack.getItemDamage()+DAMAGE)<(getMaxDurability())){
			long time = world.getTotalWorldTime();
			long stackTime = getTime(stack);
			if(time>stackTime/*+(5*Refs.TPS)*/){
				double speed = Math.sqrt(player.motionX*player.motionX+player.motionY*player.motionY+player.motionZ*player.motionZ);
				if(speed<0.08 && player.onGround){
					RayTraceResult res = RayTracer.retrace(player);
					if(player.getPosition().down().equals(res.getBlockPos())){
						BlockPos to = getLocation(stack);
						if(to!=null){
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
							if(!player.capabilities.isCreativeMode)stack.setItemDamage(stack.getItemDamage()+DAMAGE);
						}
					}
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.Inscription#handleRightClick(net.minecraft.item.ItemStack, net.minecraft.world.World, net.minecraft.entity.player.EntityPlayer, net.minecraft.util.EnumHand)
	 */
	@Override
	public ActionResult<ItemStack> handleRightClick(ItemStack itemStackIn,World worldIn, EntityPlayer playerIn, EnumHand hand) {
		setLocation(itemStackIn, playerIn.getPosition());
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
	}
	
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.Inscription#addInformation(net.minecraft.item.ItemStack, net.minecraft.entity.player.EntityPlayer, java.util.List, boolean)
	 */
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn,List<String> tooltip, boolean advanced) {
		boolean sneak = Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
		if(sneak){
			tooltip.add(RunesOfWizardry.proxy.translate(getName()+".instructions"));
		}
		if(advanced||Config.returnCoords){
			BlockPos pos = getLocation(stack);
			if(pos!=null){
				tooltip.add(RunesOfWizardry.proxy.translate("runesofwizardry_classics.lang.coords", pos.getX(),pos.getY(),pos.getZ()));
			}
		}
	}

	private void setLocation(ItemStack stack, BlockPos pos){
		NBTTagCompound tag = stack.getOrCreateSubCompound(Refs.MODID);
		tag.setIntArray("returnPos", new int[]{pos.getX(),pos.getY(),pos.getZ()});
	}
	@Nullable
	private BlockPos getLocation(ItemStack stack){
		NBTTagCompound tag = stack.getSubCompound(Refs.MODID);
		if(tag!=null){
			int[] c = tag.getIntArray("returnPos");
			return new BlockPos(c[0],c[1],c[2]);
		}
		return null;
	}
	private void setTime(ItemStack stack, long time){
		NBTTagCompound tag = stack.getOrCreateSubCompound(Refs.MODID);
		tag.setLong("useTime", time);
	}
	private long getTime(ItemStack stack){
		NBTTagCompound tag = stack.getOrCreateSubCompound(Refs.MODID);
		return tag.getLong("useTime");
	}
}
