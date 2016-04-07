
package xilef11.mc.runesofwizardry_classics.runes;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import org.apache.logging.log4j.Level;

import scala.util.Random;
import xilef11.mc.runesofwizardry_classics.ModLogger;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.runes.entity.RuneEntityResurrection;

import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.core.rune.PatternUtils;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

public class RuneResurrection extends ClassicRune {
	
	public static Map<String,List<String>> dropToEntity=null;
	public RuneResurrection() {
		super();
		MinecraftForge.EVENT_BUS.register(this);
	}
	@Override
	protected ItemStack[][] setupPattern() throws IOException {
		return PatternUtils.importFromJson(Refs.PATTERN_PATH+"runeResurrection.json");
	}

	@Override
	protected Vec3i setupEntityPos() {
		return new Vec3i(1,1,0);
	}
	@Override
	public String getID() {
		return "runeResurrection";
	}
	@Override
	protected ItemStack[][] setupSacrifice() {
		return new ItemStack[][]{
				{new ItemStack(Blocks.soul_sand,4)}//SAC 2x mob drops...
				};
	}
	

	/* (non-Javadoc)
	 * @see xilef11.mc.runesofwizardry_classics.runes.ClassicRune#hasExtraSacrifice()
	 */
	@Override
	protected boolean hasExtraSacrifice() {
		return true;
	}

	@Override
	public String getName() {
		return Refs.Lang.RUNE+".resurrection";
	}

	@Override
	public RuneEntity createRune(ItemStack[][] actualPattern, EnumFacing front,
			Set<BlockPos> dusts, TileEntityDustActive entity) {
		return new RuneEntityResurrection(actualPattern, front, dusts, entity, this);
	}
	//not guaranteed to work with modded entities though
	@SubscribeEvent
	public void initDropsTable(WorldEvent.Load event){
		//if(dropToEntity!=null)return;
		dropToEntity = new HashMap<String,List<String>>();
		for(String entName:EntityList.getEntityNameList()){
			Entity e = EntityList.createEntityByName(entName, MinecraftServer.getServer().getEntityWorld());
			if(e instanceof EntityLiving){//if its a mob
				EntityLiving ent = (EntityLiving)e;
				e.captureDrops=true;
				Method getdrops = ReflectionHelper.findMethod(EntityLivingBase.class, (EntityLivingBase)ent, new String[]{"dropFewItems","func_70628_a"},boolean.class,int.class);
				try {
					getdrops.invoke(ent,true, 10);
				} catch (IllegalAccessException e1) {
					ModLogger.logException(Level.ERROR, e1, "Exception when trying to get drops from entity: "+ent);
					continue;
				} catch (IllegalArgumentException e1) {
					ModLogger.logException(Level.ERROR, e1, "Exception when trying to get drops from entity: "+ent);
					continue;
				} catch (InvocationTargetException e1) {
					ModLogger.logException(Level.ERROR, e1, "Exception when trying to get drops from entity: "+ent);
					continue;
				}
				for(EntityItem item:ent.capturedDrops){
					ItemStack stack = item.getEntityItem();
					String key = stack.getItem().getRegistryName()+stack.getMetadata();
					List<String> ids = dropToEntity.get(key);
					if(ids==null){
						ids=new LinkedList<String>();
						dropToEntity.put(key, ids);
					}
					ids.add(entName);
				}
				ent.setDead();
			}
		}
	}

	/**
	 * Returns the ID of a random entity that might drop all items passed in (ignores NBT)
	 * @param drops the stacks for which to get a random entity
	 * @return a random entity that may drop the items passed, or null if there is none.
	 */
	public String entityIDFromDrops(Collection<ItemStack> drops){
		List<String> possible = null;
		for(ItemStack s:drops){
			String key = s.getItem().getRegistryName()+s.getMetadata();
			List<String> entities = dropToEntity.get(key);
			if(entities==null)return null;
			if(possible==null){
				possible = new LinkedList<String>();
				possible.addAll(entities);
			}else{
				Iterator<String> it = possible.iterator();
				while(it.hasNext()){
					if(!entities.contains(it.next()))it.remove();
				}
			}
		}
		if(possible.size()==0)return null;
		Random rand = new Random();
		return possible.get(rand.nextInt(possible.size()));
	}

}

    