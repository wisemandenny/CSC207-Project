package src.main;

import java.io.IOException;
import java.util.logging.*;

public class MyLogger {
    //Used this website for reference http://hanoo.org/index.php?article=how-to-generate-logs-in-java
    static Logger logger;
    //file handler
    public Handler fh;
    Formatter plainText;

    private  MyLogger() throws IOException{
        logger = Logger.getLogger(MyLogger.class.getName());
        fh = new FileHandler("log.txt", true);
        plainText =  new SimpleFormatter();
        fh.setFormatter(plainText);
        logger.addHandler(fh);
    }

    private static Logger getLogger(){
        if(logger == null){
            try {
                new MyLogger();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return logger;
    }
    public static void log(Level level, String msg){
        getLogger().log(level, msg);
        System.out.println(msg);
    }

    //this code is for writing logs to a text file.
    /**
         Logger logger = Logger.getLogger("MyLog");
         FileHandler fh;

         try {

         // This block configure the logger with handler and formatter
         fh = new FileHandler("C:/temp/test/MyLogFile.log");
         logger.addHandler(fh);
         SimpleFormatter formatter = new SimpleFormatter();
         fh.setFormatter(formatter);

         // the following statement is used to log any messages
         logger.info("My first log");

         } catch (SecurityException e) {
         e.printStackTrace();
         } catch (IOException e) {
         e.printStackTrace();
         }

         logger.info("Hi How r u?");
     */

    //this code is what will be called from classes to log an event.
    /**
     MyLogger.log(Level.INFO, MyLogger.class.getName());
     */
}
