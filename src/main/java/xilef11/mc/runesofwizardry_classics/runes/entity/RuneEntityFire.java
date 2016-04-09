package xilef11.mc.runesofwizardry_classics.runes.entity;

import java.util.List;
import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import xilef11.mc.runesofwizardry_classics.Refs;

import com.zpig333.runesofwizardry.api.IRune;
import com.zpig333.runesofwizardry.core.rune.RunesUtil;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

public class RuneEntityFire extends FueledRuneEntity {
	private static final int TICK_RATE=3;
	public RuneEntityFire(ItemStack[][] actualPattern, EnumFacing facing,
			Set<BlockPos> dusts, TileEntityDustActive entity, IRune creator) {
		super(actualPattern, facing, dusts, entity, creator);
	}

	@Override
	protected int initialTicks() {
		return Refs.TICKS_PER_DAY/4;
	}

	/* (non-Javadoc)
	 * @see xilef11.mc.runesofwizardry_classics.runes.entity.FueledRuneEntity#onRuneActivatedbyPlayer(net.minecraft.entity.player.EntityPlayer, net.minecraft.item.ItemStack[], boolean)
	 */
	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,
			ItemStack[] sacrifice, boolean negated) {
		super.onRuneActivatedbyPlayer(player, sacrifice, negated);
		World world =entity.getWorld();
		if(!world.isRemote){
			BlockPos center = getPos().offset(face);
			if(world.isAirBlock(center)&&world.isSideSolid(center.down(), EnumFacing.UP)){
				world.setBlockState(center, Blocks.fire.getDefaultState());
			}else{
				RunesUtil.deactivateRune(this);//or we could kill it
			}
		}
	}
	/* (non-Javadoc)
	 * @see xilef11.mc.runesofwizardry_classics.runes.entity.FueledRuneEntity#update()
	 */
	@Override
	public void update() {
		super.update();
		World world = entity.getWorld();
		if(!world.isRemote){
			BlockPos center = getPos().offset(face);
			//recreate the flame if it went out
			if(world.isAirBlock(center)&&world.isSideSolid(center.down(), EnumFacing.UP)){
				world.setBlockState(center, Blocks.fire.getDefaultState());
			}else if(!(world.getBlockState(center).getBlock()==Blocks.fire)){
				this.onPatternBroken();
			}
			//dies under rain
			if(world.isRaining()&&world.canBlockSeeSky(center)&&world.getBlockState(center).getBlock()==Blocks.fire){
				this.onPatternBroken();
				return;
			}
			/*FIXME it worked for a bit, but now it just burns all items (spawns the result then burns it)
			 * Now it just burns some non-smeltable items
			 */
			//smelt items
			List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(center, center.add(1,1,1)));
			for(EntityItem e: items){
				if(e.isDead){
					continue;
				}
				ReflectionHelper.setPrivateValue(Entity.class, e, true, "isImmuneToFire","field_70178_ae");
				if(entity.ticksExisted()%TICK_RATE==0){//removing this condition seems to destroy less non-smeltable items FSR
					ItemStack stack = e.getEntityItem();
					ItemStack result = ItemStack.copyItemStack(FurnaceRecipes.instance().getSmeltingResult(stack));
					if(result!=null){
						if(stack.stackSize==1){
							result.stackSize*=((Math.random() > 0.85) ? 2:1);
							EntityItem toSpawn = new EntityItem(world, e.posX, e.posY, e.posZ, result);
							ReflectionHelper.setPrivateValue(Entity.class, toSpawn, true, "isImmuneToFire","field_70178_ae");
							world.spawnEntityInWorld(toSpawn);
							shoot(toSpawn);
							toSpawn.extinguish();
						}
						e.setDead();
						world.playSoundAtEntity(e, "random.fizz", 1, 1);
					}else{
						shoot(e);
					}
				}
			}
		}
	}

	private void shoot(EntityItem item) {
		float random = 0.12F;
		item.motionX = (double)((float)item.worldObj.rand.nextGaussian() * random);
		item.motionY = (double)((float)Math.abs(item.worldObj.rand.nextGaussian()) * random + 0.2F);
		item.motionZ = (double)((float)item.worldObj.rand.nextGaussian() * random);
	}

	/* (non-Javadoc)
	 * @see xilef11.mc.runesofwizardry_classics.runes.entity.FueledRuneEntity#onPatternBroken()
	 */
	@Override
	public void onPatternBroken() {
		super.onPatternBroken();
		World world = entity.getWorld();
		if(!world.isRemote){
			BlockPos center = getPos().offset(face);
			if(world.getBlockState(center).getBlock()==Blocks.fire){
				world.setBlockToAir(center);
			}
		}
	}

}
