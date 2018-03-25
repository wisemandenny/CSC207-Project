package restaurant;

import java.io.IOException;
import java.util.logging.*;

public class RestaurantLogger {
    private static Logger logger;

    private  RestaurantLogger() throws IOException{
        Handler fh;
        Formatter plainText;
        logger = Logger.getLogger(RestaurantLogger.class.getName());
        fh = new FileHandler("log.txt", true);
        plainText =  new SimpleFormatter();
        fh.setFormatter(plainText);
        logger.addHandler(fh);
    }

    private static Logger getLogger(){
        if(logger == null){
            try {
                new RestaurantLogger();
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
}
