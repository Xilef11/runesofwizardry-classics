package xilef11.mc.runesofwizardry_classics.items;

import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import xilef11.mc.runesofwizardry_classics.Refs;

import com.zpig333.runesofwizardry.RunesOfWizardry;

public class ItemSpiritSword extends ItemSword{
	private String name = "spiritSword";
	private static ItemSpiritSword instance=null;
	private ItemSpiritSword(ToolMaterial material) {
		super(material);
		GameRegistry.register(this, new ResourceLocation(Refs.MODID,getName()));
		this.setCreativeTab(RunesOfWizardry.wizardry_tab);
		setUnlocalizedName(Refs.MODID+"_"+getName());
		this.setMaxDamage(131);
	}
	public String getName(){
		return name;
	}
	private ItemSpiritSword(){
		this(ToolMaterial.DIAMOND);
	}
	public ItemSpiritSword instance(){
		if(instance==null)instance=new ItemSpiritSword();
		return instance;
	}
	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#getRarity(net.minecraft.item.ItemStack)
	 */
	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.EPIC;
	}
	//we want knockback 10 and smite 5 on this. original did it in onUpdate
	public static ItemStack createStack(){
		ItemStack stack = new ItemStack(instance);
		stack.addEnchantment(Enchantments.KNOCKBACK, 10);
		stack.addEnchantment(Enchantments.SMITE, 5);
		return stack;
	}
	/* (non-Javadoc)
	 * @see net.minecraft.item.ItemSword#getDamageVsEntity()
	 */
	@Override
	public float getDamageVsEntity() {
		//diamond is 3.0
		return 4.0F;
	}
	/* (non-Javadoc)
	 * @see net.minecraft.item.ItemSword#hitEntity(net.minecraft.item.ItemStack, net.minecraft.entity.EntityLivingBase, net.minecraft.entity.EntityLivingBase)
	 */
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target,EntityLivingBase attacker) {
		if(attacker instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer) attacker;
			Random rand = new Random();
			double r = rand.nextDouble();

			int level = player.experienceLevel+5;
			double tol = (double)level/25D;

			if(r < tol){
				int amt = rand.nextDouble() < 0.5D? 2:1;
				EntityItem ei = player.dropItem(EnumDustTypes.GUNPOWDER.getStack(amt), false);
				ei.setPosition(target.posX, target.posY, target.posZ);
				ei.setPickupDelay(0);
			}
		}
		return false;
	}




}
