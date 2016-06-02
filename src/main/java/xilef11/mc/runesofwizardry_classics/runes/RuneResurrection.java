package xilef11.mc.runesofwizardry_classics.runes;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import org.apache.logging.log4j.Level;

import scala.util.Random;
import xilef11.mc.runesofwizardry_classics.Config;
import xilef11.mc.runesofwizardry_classics.ModLogger;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.runes.entity.RuneEntityResurrection;
import xilef11.mc.runesofwizardry_classics.utils.LootUtils;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.core.rune.PatternUtils;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;
public class RuneResurrection extends ClassicRune {
	public static Map<String,Set<String>> dropToEntity=null;
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
				{new ItemStack(Blocks.SOUL_SAND,4)}//SAC 2x mob drops...
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
	//FIXME we get a NPE now, guess we have to create the map at a different time
	@SubscribeEvent
	public void initDropsTable(WorldEvent.Load event) throws FileNotFoundException{
		if(!event.getWorld().isRemote){
			if(Config.resurrectionMode.equals("JSON")){
				File map = new File("config/"+Refs.MODID+"_ResurrectionMap.json");
				if(map.exists()){
					Reader read = new FileReader(map);
					Gson gson = new Gson();
					@SuppressWarnings("serial")
					Type type = new TypeToken<Map<String,Set<String>>>(){}.getType();
					dropToEntity = gson.fromJson(read,type);
				}else{
					initDropsTable_entity(event.getWorld());
					Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
					try {
						gson.toJson(dropToEntity, new FileWriter(new File("config/"+Refs.MODID+"_ResurrectionMap.json")));
					} catch (JsonIOException e) {
						ModLogger.logException(Level.ERROR, e, "Couldn't write JSON map");
					} catch (IOException e) {
						ModLogger.logException(Level.ERROR, e, "Couldn't write JSON map");
					}
				}
			}else{
				initDropsTable_entity(event.getWorld());
			}
		}
	}
	public void initDropsTable_entity(World world){
		if(world.isRemote)return;//server side only
		ModLogger.logInfo("Creating drop table for world: "+world.provider.getDimensionType());
		if(dropToEntity!=null){
			ModLogger.logInfo("drop table already exists");
			//return;
		}else{
			dropToEntity = new HashMap<String,Set<String>>();
		}
		for(String entName:EntityList.getEntityNameList()){
			Entity e=null;
			try{
				e = EntityList.createEntityByName(entName, world);
			}catch(NoClassDefFoundError err){
				ModLogger.logError("Class for entity does not exist on the server: "+entName);
				continue;
			}
			if(e instanceof EntityLiving){//if its a mob
				EntityLiving ent = (EntityLiving)e;
				List<ItemStack> list = Config.resurrectionMode.equals("Kill")? getEntityLoot_Hacky(ent) : getEntityLoot_Table(ent) ;
				for(ItemStack stack:list){
					Item i = stack.getItem();
					if(i==null){
						ModLogger.logError("Error - NULL Item (in a non-null ItemStack) - while finding drops of entity: "+entName);
						continue;
					}
					String key = i.getRegistryName().toString()+stack.getMetadata();
					Set<String> ids = dropToEntity.get(key);
					if(ids==null){
						ids=new HashSet<String>();
						dropToEntity.put(key, ids);
					}
					ids.add(entName);
				}
				ent.setDead();
			}
		}
	}
	
	private List<ItemStack> getEntityLoot_Table(EntityLiving el){
		ResourceLocation location = (ResourceLocation)ReflectionHelper.getPrivateValue(EntityLiving.class, el, "deathLootTable","field_184659_bA");
		LootTableManager manager = el.worldObj.getLootTableManager();
		LootTable table = manager.getLootTableFromLocation(location);
		return LootUtils.tableToItemStacks(table);
	}
	private List<ItemStack> getEntityLoot_Hacky(EntityLiving ent){
		List<ItemStack> result = new LinkedList<ItemStack>();
		ent.captureDrops=true;
		Method getdrops = ReflectionHelper.findMethod(EntityLivingBase.class, (EntityLivingBase)ent, new String[]{"dropLoot","func_184610_a"},boolean.class,int.class,DamageSource.class);
		try {
			getdrops.invoke(ent,true, 10,DamageSource.generic);
		} catch (IllegalAccessException e1) {
			ModLogger.logException(Level.ERROR, e1, "Exception when trying to get drops from entity: "+ent);
			return result;
		} catch (IllegalArgumentException e1) {
			ModLogger.logException(Level.ERROR, e1, "Exception when trying to get drops from entity: "+ent);
			return result;
		} catch (InvocationTargetException e1) {
			ModLogger.logException(Level.ERROR, e1, "Exception when trying to get drops from entity: "+ent);
			return result;
		}
		for(EntityItem item:ent.capturedDrops){
			if(item==null){
				ModLogger.logError("Error - NULL entityItem- while finding drops of entity: "+ent.getName());
				continue;
			}
			ItemStack stack = item.getEntityItem();
			if(stack==null){
				ModLogger.logError("Error - NULL ItemStack (in a valid EntityItem) - while finding drops of entity: "+ent.getName());
				continue;
			}
			result.add(stack);
		}
		return result;
	}
	/**
	 * Returns the ID of a random entity that might drop all items passed in (ignores NBT)
	 * @param drops the stacks for which to get a random entity
	 * @return a random entity that may drop the items passed, or null if there is none.
	 */
	public String entityIDFromDrops(Collection<ItemStack> drops){
		List<String> possible = null;
		if(dropToEntity==null)return "";
		for(ItemStack s:drops){
			String key = s.getItem().getRegistryName().toString()+s.getMetadata();
			Set<String> entities = dropToEntity.get(key);
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
