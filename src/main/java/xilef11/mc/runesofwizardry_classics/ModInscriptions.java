package xilef11.mc.runesofwizardry_classics;

import java.util.LinkedList;
import java.util.List;

import xilef11.mc.runesofwizardry_classics.inscriptions.ClassicInscription;
import xilef11.mc.runesofwizardry_classics.inscriptions.InscriptionBlink;
import xilef11.mc.runesofwizardry_classics.inscriptions.InscriptionBlinkII;
import xilef11.mc.runesofwizardry_classics.inscriptions.InscriptionBounce;
import xilef11.mc.runesofwizardry_classics.inscriptions.InscriptionForesight;
import xilef11.mc.runesofwizardry_classics.inscriptions.InscriptionLeap;
import xilef11.mc.runesofwizardry_classics.inscriptions.InscriptionLeapII;
import xilef11.mc.runesofwizardry_classics.inscriptions.InscriptionReturn;
import xilef11.mc.runesofwizardry_classics.inscriptions.InscriptionVoid;

import com.zpig333.runesofwizardry.api.DustRegistry;

public class ModInscriptions {
	private static List<ClassicInscription> insc;
	public static List<ClassicInscription> getInscriptions(){
		return insc;
	}
	public static void registerInscriptions(){
		for(ClassicInscription i:insc){
			DustRegistry.registerInscription(i,i.getID());
		}
	}
	public static void initInscriptions(){
		insc = new LinkedList<ClassicInscription>();
		if(!Config.disableWIP)insc.add(new InscriptionLeap());
		if(!Config.disableWIP)insc.add(new InscriptionLeapII());
		if(!Config.disableWIP)insc.add(new InscriptionReturn());
		if(!Config.disableWIP)insc.add(new InscriptionVoid());
		if(!Config.disableWIP)insc.add(new InscriptionBounce());
		if(!Config.disableWIP)insc.add(new InscriptionBlink());
		if(!Config.disableWIP)insc.add(new InscriptionBlinkII());
		if(!Config.disableWIP)insc.add(new InscriptionForesight());
	}
}
