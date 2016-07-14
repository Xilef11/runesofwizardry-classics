package xilef11.mc.runesofwizardry_classics.items;

import java.util.List;
import java.util.Random;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.RunesofWizardry_Classics;

import com.zpig333.runesofwizardry.RunesOfWizardry;

public class ItemSpiritSword extends ItemSword{
	private String name = "spirit_sword";
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
		//FIXME changing this has no effect
		return 100.0F;
	}
	//TODO might want to make this slightly faster than normal sword
	/* (non-Javadoc)
	 * @see net.minecraft.item.ItemSword#hitEntity(net.minecraft.item.ItemStack, net.minecraft.entity.EntityLivingBase, net.minecraft.entity.EntityLivingBase)
	 */
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target,EntityLivingBase attacker) {
		if(attacker instanceof EntityPlayer && !attacker.worldObj.isRemote){
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
	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#getSubItems(net.minecraft.item.Item, net.minecraft.creativetab.CreativeTabs, java.util.List)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab,List<ItemStack> subItems) {
		subItems.add(createStack());
	}
	



}
