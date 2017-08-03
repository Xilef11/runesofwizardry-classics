package xilef11.mc.runesofwizardry_classics.utils;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetMetadata;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

/**
 * Helper functions for loot tables. borrowed from Way2MuchNoise's JustEnoughResources
 * https://github.com/way2muchnoise/JustEnoughResources/blob/master/src/main/java/jeresources/util/LootHelper.java#L19-L37
 *
 */
public class LootUtils {
	
    public static List<LootPool> getPools(LootTable table)
    {
        return ReflectionHelper.getPrivateValue(LootTable.class, table, "pools", "field_186466_c");
    }

    public static List<LootEntry> getEntries(LootPool pool)
    {
        return ReflectionHelper.getPrivateValue(LootPool.class, pool, "lootEntries", "field_186453_a");
    }

    public static Item getItem(LootEntryItem lootEntry)
    {
        return ReflectionHelper.getPrivateValue(LootEntryItem.class, lootEntry, "item", "field_186368_a");
    }
    public static LootFunction[] getFunctions(LootEntryItem lootEntry)
    {
        return ReflectionHelper.getPrivateValue(LootEntryItem.class, lootEntry, "functions", "field_186369_b");
    }
    /**
     * Converts a LootTable to a list of possible drops, only looks for Item and metadata.
     * @param table the loot table to get items from
     * @return a LinkedList of the stacks in the loot table
     */
    public static List<ItemStack> tableToItemStacks(LootTable table){
    	List<ItemStack> stacks = new LinkedList<>();
    	for(LootPool p:getPools(table)){
    		for(LootEntry entry:getEntries(p)){
    			if(entry instanceof LootEntryItem){
    				LootEntryItem ei = (LootEntryItem)entry;
    				Item item = getItem(ei);
    				LootFunction[] functs = getFunctions(ei);
    				boolean metaSet = false;
    				for(LootFunction func:functs){
    					if(func instanceof SetMetadata){
    						metaSet=true;
    						RandomValueRange range = (RandomValueRange)ReflectionHelper.getPrivateValue(SetMetadata.class, (SetMetadata)func, "metaRange","field_186573_b");
    						int meta = MathHelper.floor(range.getMin());
    						stacks.add(new ItemStack(item,1,meta));
    					}
    				}
    				if(!metaSet)stacks.add(new ItemStack(item));
    			}
    			/* won't bother with this case for now
    			else if(entry instanceof LootEntryTable){
    				//restart with that table
    				ResourceLocation location = (ResourceLocation) ReflectionHelper.getPrivateValue(LootEntryTable.class, (LootEntryTable)entry, "table","field_186371_a");
    			}
    			*/
    		}
    	}
    	return stacks;
    }
    
}
