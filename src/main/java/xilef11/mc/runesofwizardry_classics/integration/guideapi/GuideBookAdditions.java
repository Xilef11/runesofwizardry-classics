package xilef11.mc.runesofwizardry_classics.integration.guideapi;

import com.zpig333.runesofwizardry.integration.guideapi.WizardryGuide;
import com.zpig333.runesofwizardry.integration.guideapi.category.CategoryDusts;
import com.zpig333.runesofwizardry.integration.guideapi.category.CategoryRunes;

import amerifrance.guideapi.api.GuideAPI;
import amerifrance.guideapi.api.impl.Book;
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.api.util.BookHelper;
import amerifrance.guideapi.page.PageItemStack;
import net.minecraft.item.ItemStack;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.items.DustVariable;
import xilef11.mc.runesofwizardry_classics.items.ItemSpiritPickaxe;
import xilef11.mc.runesofwizardry_classics.items.ItemSpiritSword;

public class GuideBookAdditions {
	/**Add a few things to the entries created automatically by the base mod **/
	public static void handlePost(){
		Book book = GuideAPI.getBooks().get(WizardryGuide.LOCATION);
		//placeholder
		CategoryAbstract dusts = BookHelper.getCategoryFromName(book, WizardryGuide.CAT_LOC+CategoryDusts.NAME);
		EntryAbstract placeholders = BookHelper.getEntryFromName(dusts, CategoryDusts.ENTRY_KEY+"placeholders");
		placeholders.addPage(new PageItemStack(Refs.MODID+".guide.variabledust",new ItemStack(DustVariable.instance)));
		
		//spirit tools
		CategoryAbstract runes = BookHelper.getCategoryFromName(book,WizardryGuide.CAT_LOC+CategoryRunes.NAME); 
		EntryAbstract spiritTools = BookHelper.getEntryFromName(runes,Refs.Lang.RUNE+".spirittools");
		if(spiritTools!=null){
			spiritTools.addPage(new PageItemStack(Refs.MODID+".guide.spiritpick", ItemSpiritPickaxe.instance()));
			spiritTools.addPage(new PageItemStack(Refs.MODID+".guide.spiritsword", ItemSpiritSword.instance()));
		}
	}
}
