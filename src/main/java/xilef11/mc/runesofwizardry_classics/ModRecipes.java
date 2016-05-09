/**
 * 
 */
package xilef11.mc.runesofwizardry_classics;

import net.minecraft.init.Blocks;import net.minecraft.init.Items;import net.minecraft.item.ItemStack;import net.minecraftforge.fml.common.registry.GameRegistry;import net.minecraftforge.oredict.OreDictionary;import net.minecraftforge.oredict.ShapelessOreRecipe;import xilef11.mc.runesofwizardry_classics.items.EnumDustTypes;

/** Registers the recipes
 * @author Xilef11
 *
 */
public class ModRecipes {
	private ModRecipes(){}
	
	public static void registerDustRecipes(){
		ModLogger.logInfo("Registering dust recipes");
		//Plant dust (2x tallGrass, sapling, leaves, seeds + 1x coal)
		ItemStack tallGrass = new ItemStack(Blocks.TALLGRASS,1,OreDictionary.WILDCARD_VALUE),
				  seeds = new ItemStack(Items.WHEAT_SEEDS),
				  coal = new ItemStack(Items.COAL),
				  plantDust = EnumDustTypes.PLANT.getStack(8);
		GameRegistry.addRecipe(new ShapelessOreRecipe(plantDust, coal, "treeSapling","treeSapling"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(plantDust, coal, "treeSapling","treeLeaves"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(plantDust, coal, "treeSapling",seeds));
		GameRegistry.addRecipe(new ShapelessOreRecipe(plantDust, coal, "treeSapling",tallGrass));
		GameRegistry.addRecipe(new ShapelessOreRecipe(plantDust, coal, "treeLeaves","treeLeaves"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(plantDust, coal, "treeLeaves",seeds));
		GameRegistry.addRecipe(new ShapelessOreRecipe(plantDust, coal, "treeLeaves",tallGrass));
		GameRegistry.addShapelessRecipe(plantDust, tallGrass,tallGrass,coal);
		GameRegistry.addShapelessRecipe(plantDust, coal, tallGrass,seeds);
		GameRegistry.addShapelessRecipe(plantDust, coal, seeds,seeds);
		//Gunpowder 2xPlant Runic Dust + 1xGunpowder = 12xGunpowder Runic Dust
		GameRegistry.addShapelessRecipe(EnumDustTypes.GUNPOWDER.getStack(12),
				EnumDustTypes.PLANT.getStack(1),EnumDustTypes.PLANT.getStack(1),new ItemStack(Items.gunpowder));
		//Lapis 3xLapis + 1xCoal = 8xLapis Runic Dust
		GameRegistry.addRecipe(new ShapelessOreRecipe(EnumDustTypes.LAPIS.getStack(8), "gemLapis","gemLapis","gemLapis",coal));
		//blaze 3xLapis Runic Dust + 1xBlaze Powder = 12xBlaze Runic Dust
		GameRegistry.addShapelessRecipe(EnumDustTypes.BLAZE.getStack(12), EnumDustTypes.LAPIS.getStack(1),EnumDustTypes.LAPIS.getStack(1),
				EnumDustTypes.LAPIS.getStack(1),new ItemStack(Items.BLAZE_POWDER));
	}
}
