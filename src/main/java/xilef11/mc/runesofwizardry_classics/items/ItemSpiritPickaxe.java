package xilef11.mc.runesofwizardry_classics.items;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import org.apache.logging.log4j.Level;

import xilef11.mc.runesofwizardry_classics.ModLogger;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.RunesofWizardry_Classics;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.util.RayTracer;

public class ItemSpiritPickaxe extends ItemPickaxe {

	private static ItemSpiritPickaxe instance;
	private String name = "spirit_pickaxe";

	protected ItemSpiritPickaxe(ToolMaterial material) {
		super(material);
		GameRegistry.register(this, new ResourceLocation(Refs.MODID,getName()));
		this.setCreativeTab(RunesOfWizardry.wizardry_tab);
		setUnlocalizedName(Refs.MODID+"_"+getName());
		this.setMaxDamage(250);
		this.efficiencyOnProperMaterial=16F;
	}
	public String getName(){
		return name;
	}

	public static ItemSpiritPickaxe instance(){
		if(instance==null){
			instance=new ItemSpiritPickaxe(ToolMaterial.DIAMOND);
			RunesofWizardry_Classics.proxy.RegisterItemModel(instance, 0, Refs.TEXTURE_PATH+instance.getName(), "inventory");
			//model
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

	/**
	 * How long it takes to use or consume an item
	 */
	public int getMaxItemUseDuration(ItemStack stack) {
		return 72000;
	}

	/**
	 * returns the action that specifies what animation to play when the items
	 * is being used
	 */
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is
	 * pressed. Args: itemStack, world, entityPlayer
	 */
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world,	EntityPlayer player,EnumHand hand) {
		//player.setItemInUse(stack,this.getMaxItemUseDuration(stack));
		player.setActiveHand(hand);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}

	/**@author billythegoat101 with tweaks by Xilef11**/
	@Override
	public void onPlayerStoppedUsing(ItemStack item, World world,EntityLivingBase user,int timeLeft) {
		int use = this.getMaxItemUseDuration(item) - timeLeft;

		if(!(user instanceof EntityPlayer))return;
		EntityPlayer player=(EntityPlayer)user;

		RayTraceResult click = RayTracer.retrace(player);

		if (click == null) {
			return;
		}
		BlockPos hitBlock = click.getBlockPos();

		int x, y, z;
		x = hitBlock.getX();
		y = hitBlock.getY();
		z = hitBlock.getZ();

		Random rand = new Random();
		int level = player.experienceLevel + 1;
		level *= level;
		double tol = (double) level / 900D;

		if (use > 25) {
			boolean creative = player.capabilities.isCreativeMode;
			boolean playedSound = false;
			int rad = 1;
			for (int i = -rad; i <= rad; i++) {
				for (int j = -rad; j <= rad; j++) {
					for (int k = -rad; k <= rad; k++) {
						if (item.getItemDamage() >= item.getMaxDamage())
							continue;
						//						int bid = world.getBlockId(x + i, y + j, z + k);
						//						int meta = world.getBlockMetadata(x + i, y + j, z + k);
						//						Block block = Block.blocksList[bid];
						BlockPos currentPos = hitBlock.add(i,j,k);
						IBlockState state = world.getBlockState(currentPos);
						Block block = state.getBlock();
						try {

							if (block !=Blocks.AIR) // block is not null (air)
							{
								//XXX maybe a check for "is the pick effective on this" instead of just ROCK
								if (state.getMaterial() == Material.ROCK
										&& block != Blocks.BEDROCK) // if block
									// is made
									// of rock
								{
									if (!playedSound) {
										SoundType sound = block.getSoundType();
										world.playSound(
												player,
												hitBlock,
												sound.getStepSound(),
												SoundCategory.BLOCKS,
												(sound.getVolume() + 1.0F) / 6.0F,
												sound.getPitch() * 0.99F);
										playedSound = true;
									}
									if (rand.nextDouble() < tol) {
										EntityItem ei = player
												.dropItem(EnumDustTypes.LAPIS.getStack(1), false);
										ei.setPosition(x + 0.5 + i,
												y + 0.5 + j, z + 0.5 + k);
										ei.motionX = ei.motionY = ei.motionZ;
									}
									boolean sucess = block.removedByPlayer(state,
											world, currentPos,player,true);

									if (sucess) {
										block.onBlockDestroyedByPlayer(world, currentPos,state);
									}
									/*
									world.setBlockAndMetadataWithNotify(i + x, j + y, k
											+ z, 0,0,3); // update world
									block.onBlockDestroyedByPlayer(world,
											i + x, j + y, k + z, world
													.getBlockMetadata(i + x, j
															+ y, k + z)); // destroy
																			// block
									block.dropBlockAsItem(world, i + x, j + y,
											k + z, bid, 0); // drop block
									 */
									if (!creative){
										item.damageItem(1, player);
										block.onBlockDestroyedByPlayer(world, currentPos, state);
										block.dropBlockAsItem(world, currentPos, state, 0);
									}
									world.setBlockToAir(currentPos);
								}
							}
						} catch (Exception e) {
							ModLogger.logException(Level.WARN,e,"Error breaking block "
									+ block.getUnlocalizedName()+" at "+currentPos);
						}// fracking mods
					}
				}
			}
		}
	}

}
