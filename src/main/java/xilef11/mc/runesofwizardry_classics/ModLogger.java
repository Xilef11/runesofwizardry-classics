package xilef11.mc.runesofwizardry_classics;

import net.minecraftforge.fml.common.FMLLog;

import org.apache.logging.log4j.Level;

public class ModLogger {

	public static void log(Level level, Object obj){
		FMLLog.log(Refs.MODID, level, String.valueOf(obj));
	}

	public static void logFatal(Object obj){
		log(Level.FATAL, obj);
	}

	public static void logError(Object obj){
		log(Level.ERROR, obj);
	}

	public static void logDebug(Object obj){
		log(Level.DEBUG, obj);
	}

	public static void logInfo(Object obj){
		log(Level.INFO, obj);
	}

	public static void logTrace(Object obj){
		log(Level.TRACE, obj);
	}

	public static void logException(Level level, Throwable ex, Object message){
		FMLLog.log(Refs.MODID, level, ex, String.valueOf(message));
	}
}
