package xilef11.mc.runesofwizardry_classics.runes;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
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

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.core.rune.PatternUtils;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

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
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import scala.util.Random;
import xilef11.mc.runesofwizardry_classics.Config;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.RunesofWizardry_Classics;
import xilef11.mc.runesofwizardry_classics.runes.entity.RuneEntityResurrection;
import xilef11.mc.runesofwizardry_classics.utils.LootUtils;
public class RuneResurrection extends ClassicRune {
	public static Map<String,Set<ResourceLocation>> dropToEntity=null;
	public RuneResurrection() {
		super();
		MinecraftForge.EVENT_BUS.register(this);
	}
	@Override
	protected ItemStack[][] setupPattern() throws IOException {
		return PatternUtils.importFromJson(Refs.PATTERN_PATH+"runeresurrection.json");
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
	public void initDropsTable(World world){
		if(!world.isRemote){
			if(Config.resurrectionMode.equals("JSON")){
				File map = new File("config/"+Refs.MODID+"_ResurrectionMap.json");
				if(map.exists()){
					Reader read;
					try {
						read = new FileReader(map);
					} catch (FileNotFoundException e) {
						RunesofWizardry_Classics.log().warn("couldn't find drops initialisation file",e);
						initDropsTable_entity(world);
						return;
					}
					Gson gson = new Gson();
					@SuppressWarnings("serial")
					Type type = new TypeToken<Map<String,Set<String>>>(){}.getType();
					dropToEntity = gson.fromJson(read,type);
				}else{
					initDropsTable_entity(world);
					Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
					try {
						Writer out = new BufferedWriter(new FileWriter(new File("config/"+Refs.MODID+"_ResurrectionMap.json"))); 
						gson.toJson(dropToEntity, out);
						out.close();
					} catch (JsonIOException | IOException e) {
						RunesofWizardry_Classics.log().error("Couldn't write JSON map",e);
					}
				}
			}else{
				initDropsTable_entity(world);
			}
		}
	}
	public void initDropsTable_entity(World world){
		if(world.isRemote)return;//server side only
		RunesofWizardry_Classics.log().info("Creating drop table for world: "+world.provider.getDimensionType());
		if(dropToEntity!=null){
			RunesofWizardry_Classics.log().info("drop table already exists");
			//return;
		}else{
			dropToEntity = new HashMap<>();
		}
		for(ResourceLocation entName:EntityList.getEntityNameList()){
			Entity e=null;
			try{
				e = EntityList.createEntityByIDFromName(entName, world);
			}catch(NoClassDefFoundError err){
				RunesofWizardry_Classics.log().error("An entity has caused a client-side class to load on the server: "+entName+" ; Please report to that mod's author.",err);
				continue;
			}
			if(e instanceof EntityLiving){//if its a mob
				EntityLiving ent = (EntityLiving)e;
				List<ItemStack> list = Config.resurrectionMode.equals("Kill")? getEntityLoot_Hacky(ent) : getEntityLoot_Table(ent) ;
				for(ItemStack stack:list){
					Item i = stack.getItem();
					if(i==null){
						RunesofWizardry_Classics.log().error("Error - NULL Item (in a non-null ItemStack) - while finding drops of entity: "+entName);
						continue;
					}
					String key = i.getRegistryName().toString()+"@"+stack.getMetadata();
					Set<ResourceLocation> ids = dropToEntity.get(key);
					if(ids==null){
						ids=new HashSet<>();
						dropToEntity.put(key, ids);
					}
					ids.add(entName);
				}
				ent.setDead();
			}
		}
	}
	
	private Method getLT = ObfuscationReflectionHelper.findMethod(EntityLiving.class, "func_184647_J", ResourceLocation.class);
	private List<ItemStack> getEntityLoot_Table(EntityLiving el){
		//ResourceLocation location = (ResourceLocation)ReflectionHelper.getPrivateValue(EntityLiving.class, el, "deathLootTable","field_184659_bA");
		ResourceLocation location = (ResourceLocation)ObfuscationReflectionHelper.getPrivateValue(EntityLiving.class, el, "field_184659_bA");
		if(location==null){
			//Method getLT = ObfuscationReflectionHelper.findMethod(EntityLiving.class, "func_184647_J", ResourceLocation.class);
			//Method getLT = ReflectionHelper.findMethod(EntityLiving.class,"getLootTable","func_184647_J");
			try {
				location = (ResourceLocation)getLT.invoke(el);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				RunesofWizardry_Classics.log().error("Exception when trying to get LootTable from entity: "+el.getName(),e);
				return getEntityLoot_Hacky(el);
			}
		}
		if(location==null){
			RunesofWizardry_Classics.log().warn(el.getName()+" does not have a LootTable. falling back to kill method");
			return getEntityLoot_Hacky(el);
		}
		LootTableManager manager = el.world.getLootTableManager();
		LootTable table = manager.getLootTableFromLocation(location);
		return LootUtils.tableToItemStacks(table);
	}
	private Method getdrops = ObfuscationReflectionHelper.findMethod(EntityLivingBase.class,"func_184610_a",void.class,boolean.class,int.class,DamageSource.class);
	private List<ItemStack> getEntityLoot_Hacky(EntityLiving ent){
		List<ItemStack> result = new LinkedList<>();
		ent.captureDrops=true;
		//Method getdrops = ReflectionHelper.findMethod(EntityLivingBase.class, "dropLoot","func_184610_a",boolean.class,int.class,DamageSource.class);
		//Method getdrops = ObfuscationReflectionHelper.findMethod(EntityLivingBase.class,"func_184610_a",void.class,boolean.class,int.class,DamageSource.class);
		try {
			getdrops.invoke(ent,true, 10,DamageSource.GENERIC);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			RunesofWizardry_Classics.log().error("Exception when trying to get drops from entity: "+ent,e1);
			return result;
		}
		for(EntityItem item:ent.capturedDrops){
			if(item==null){
				RunesofWizardry_Classics.log().error("Error - NULL entityItem- while finding drops of entity: "+ent.getName());
				continue;
			}
			ItemStack stack = item.getItem();
			if(stack.isEmpty()){
				RunesofWizardry_Classics.log().error("Error - Empty ItemStack (in a valid EntityItem) - while finding drops of entity: "+ent.getName());
				continue;
			}
			result.add(stack);
		}
		return result;
	}
	/**
	 * Returns the ID of a random entity that might drop all items passed in (ignores NBT)
	 * @param drops the stacks for which to get a random entity
	 * @param aWorld any Loaded World (the one that contains the rune would do)
	 * @return a random entity that may drop the items passed, or null if there is none.
	 */
	public ResourceLocation entityIDFromDrops(Collection<ItemStack> drops,World aWorld){
		List<ResourceLocation> possible = null;
		if(dropToEntity==null)initDropsTable(aWorld);
		for(ItemStack s:drops){
			String key = s.getItem().getRegistryName().toString()+"@"+s.getMetadata();
			Set<ResourceLocation> entities = dropToEntity.get(key);
			if(entities==null)return null;
			if(possible==null){
				possible = new LinkedList<>();
				possible.addAll(entities);
			}else{
				Iterator<ResourceLocation> it = possible.iterator();
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
