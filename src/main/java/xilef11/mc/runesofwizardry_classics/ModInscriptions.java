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
	public static ClassicInscription insLeap,
					   insLeap2,
					   insReturn,
					   insVoid,
					   insBounce,
					   insBlink,
					   insBlink2,
					   insForesight;
	public static List<ClassicInscription> getInscriptions(){
		return insc;
	}
	public static void registerInscriptions(){
		for(ClassicInscription i:insc){
			DustRegistry.registerInscription(i,i.getID());
		}
	}
	public static void initInscriptions(){
		insc = new LinkedList<>();
		insLeap = new InscriptionLeap();
		insLeap2= new InscriptionLeapII();
		insReturn= new InscriptionReturn();
		insVoid= new InscriptionVoid();
		insBounce= new InscriptionBounce();
		insBlink= new InscriptionBlink();
		insBlink2= new InscriptionBlinkII();
		insForesight= new InscriptionForesight();
		
		if(!Config.disableWIP)insc.add(insLeap);
		if(!Config.disableWIP)insc.add(insLeap2);
		if(!Config.disableWIP)insc.add(insReturn);
		if(!Config.disableWIP)insc.add(insVoid);
		if(!Config.disableWIP)insc.add(insBounce);
		if(!Config.disableWIP)insc.add(insBlink);
		if(!Config.disableWIP)insc.add(insBlink2);
		if(!Config.disableWIP)insc.add(insForesight);
	}
}
