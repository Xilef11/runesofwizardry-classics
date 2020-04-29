package xilef11.mc.runesofwizardry_classics.items;

import java.util.Random;

import com.google.common.collect.Multimap;
import com.zpig333.runesofwizardry.RunesOfWizardry;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.RunesofWizardry_Classics;

public class ItemSpiritSword extends ItemSword{
	private String name = "spirit_sword";
	private static ItemSpiritSword instance=null;
	private ItemSpiritSword(ToolMaterial material) {
		super(material);
		setRegistryName(new ResourceLocation(Refs.MODID,getName()));
		this.setCreativeTab(RunesOfWizardry.wizardry_tab);
		setUnlocalizedName(Refs.MODID+"_"+getName());
		this.setMaxDamage(131);
	}
	public String getName(){
		return name;
	}
	private ItemSpiritSword(){
		this(ToolMaterial.GOLD);
	}
	public static ItemSpiritSword instance(){
		if(instance==null){
			instance=new ItemSpiritSword();
			RunesofWizardry_Classics.proxy.RegisterItemModel(instance, 0, Refs.TEXTURE_PATH+instance.getName(),"inventory");
		}
		return instance;
	}
	
	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#getRarity(net.minecraft.item.ItemStack)
	 */
	@Override
	public EnumRarity getForgeRarity(ItemStack stack) {
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
	public float getAttackDamage() {
		//diamond is 3.0
		return 3.0F;
	}
	
	/* (non-Javadoc)
	 * @see net.minecraft.item.ItemSword#hitEntity(net.minecraft.item.ItemStack, net.minecraft.entity.EntityLivingBase, net.minecraft.entity.EntityLivingBase)
	 */
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target,EntityLivingBase attacker) {
		if(attacker instanceof EntityPlayer && !attacker.world.isRemote){
			EntityPlayer player = (EntityPlayer) attacker;
			Random rand = new Random();
			double r = rand.nextDouble();

			int level = player.experienceLevel+5;
			double tol = level/25D;

			if(r < tol){
				int amt = rand.nextDouble() < 0.5D? 2:1;
				EntityItem ei = player.dropItem(EnumDustTypes.GUNPOWDER.getStack(amt), false);
				ei.setPosition(target.posX, target.posY, target.posZ);
				ei.setPickupDelay(0);
			}
		}
		return super.hitEntity(stack, target, attacker);
	}
	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#getSubItems(net.minecraft.item.Item, net.minecraft.creativetab.CreativeTabs, java.util.List)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab,NonNullList<ItemStack> subItems) {
		if(this.isInCreativeTab(tab)){
			subItems.add(createStack());
		}
	}
	
	
	@Override
	 public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot){
	        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);

	        if (equipmentSlot == EntityEquipmentSlot.MAINHAND)
	        {
	        	//remove old values
	        	multimap.removeAll(SharedMonsterAttributes.ATTACK_DAMAGE.getName());
	        	multimap.removeAll(SharedMonsterAttributes.ATTACK_SPEED.getName());
	            //set how we want them
	        	multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 7D, 0));
	        	//base is 4
	            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", 2-4, 0));
	        }

	        return multimap;
	    }


}
