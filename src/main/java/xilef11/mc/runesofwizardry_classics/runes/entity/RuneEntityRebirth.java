/**
 * 
 */
package xilef11.mc.runesofwizardry_classics.runes.entity;

import java.util.Map;
import java.util.Set;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityList.EntityEggInfo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import xilef11.mc.runesofwizardry_classics.utils.Utils;

import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

/**
 * @author Xilef11
 *
 */
public class RuneEntityRebirth extends RuneEntity {

	/**
	 * @param actualPattern
	 * @param facing
	 * @param dusts
	 * @param entity
	 */
	public RuneEntityRebirth(ItemStack[][] actualPattern, EnumFacing facing,
			Set<BlockPos> dusts, TileEntityDustActive entity) {
		super(actualPattern, facing, dusts, entity);
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#getRuneID()
	 */
	@Override
	public String getRuneID() {
		return "runeRebirth";
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#onRuneActivatedbyPlayer(net.minecraft.entity.player.EntityPlayer, net.minecraft.item.ItemStack[], boolean)
	 */
	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,ItemStack[] sacrifice, boolean negated) {
		World world = player.worldObj;
		if(!world.isRemote){
			//take xp if not negated
			if(!negated){
				if(player.experienceLevel>=10){
					player.removeExperienceLevel(10);
				}else{
					//kill the rune
					this.onPatternBrokenByPlayer(player);
					return;
				}
			}
		}
		// TODO start FX
		
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#update()
	 */
	@Override
	public void update() {
		// not much here
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#handleEntityCollision(net.minecraft.world.World, net.minecraft.util.BlockPos, net.minecraft.block.state.IBlockState, net.minecraft.entity.Entity)
	 */
	@Override
	public boolean handleEntityCollision(World worldIn, BlockPos pos,IBlockState state, Entity entityIn) {
		if(!worldIn.isRemote){
			if(entityIn instanceof EntityLivingBase){
				EntityLivingBase ent = (EntityLivingBase)entityIn;
				
				//int id = entityIn.getEntityId();//not the same id as spawn eggs
				//ItemMonsterPlacer
				//try the forge map
				Map<String, EntityEggInfo> eggs = EntityRegistry.getEggs();//this one is empty
				EntityEggInfo egginfo = eggs.get(ent.getName());
				if(egginfo==null){//try the vanilla map
					Map<Integer,EntityEggInfo> egg2 = EntityList.entityEggs;
					for(EntityEggInfo inf: egg2.values()){
						String name = inf.name;
						if(name.equals(ent.getName())){
							egginfo=inf;
							break;
						}
					}
				}
				
				//EntityEggInfo egginfo = eggs.get(id);//null with sheep and zombie?
				if(egginfo!=null){//if the entity has an egg (?)
					@SuppressWarnings("deprecation")
					int spawnID = egginfo.spawnedID;
					//XXX this will be NBT in 1.9
					Utils.spawnItemCentered(worldIn, pos, new ItemStack(Items.spawn_egg,1,spawnID));
					this.onPatternBroken();//kill the rune
					entityIn.setDead();//kill the entity
				}
			}
		}
		return true;
	}
	
}
