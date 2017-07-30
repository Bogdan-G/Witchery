package com.emoniph.witchery.util;

import com.emoniph.witchery.util.Config;
import cpw.mods.fml.common.FMLCommonHandler;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log {

   static final Log INSTANCE = new Log();
   final Logger logger = LogManager.getLogger(getModPrefix() + FMLCommonHandler.instance().getEffectiveSide());


   public static Log instance() {
      return INSTANCE;
   }

   static String getModPrefix() {
      return "witchery: ";
   }

   public void warning(String msg) {
      this.logger.log(Level.WARN, getModPrefix() + msg);
   }

   public void warning(Throwable exception, String msg) {
      this.logger.log(Level.WARN, getModPrefix() + msg);
      exception.printStackTrace();
   }

   public void debug(String msg) {
      if(Config.instance().isDebugging()) {
         this.logger.log(Level.INFO, getModPrefix() + msg);
      }

   }

   public void traceRite(String msg) {
      if(Config.instance().traceRites()) {
         this.logger.log(Level.INFO, getModPrefix() + msg);
      }

   }

}
