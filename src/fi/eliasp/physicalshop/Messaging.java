package fi.eliasp.physicalshop;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import org.bukkit.command.CommandSender;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.text.MessageFormat;
public class Messaging
{
    public static String BUY;
    public static String BUY_RATE;
    public static String CANT_BUILD;
    public static String CANT_BUILD_SERVER;
    public static String CANT_DESTROY;
    public static String CANT_PLACE_CHEST;
    public static String CANT_USE;
    public static String CANT_USE_CHEST;
    public static String CHEST_INVENTORY_FULL;
    public static String DEPOSIT;
    public static String EXISTING_CHEST;
    public static String NO_BUY;
    public static String NO_SELL;
    public static String NOT_ENOUGH_PLAYER_ITEMS;
    public static String NOT_ENOUGH_PLAYER_MONEY;
    public static String NOT_ENOUGH_SHOP_ITEMS;
    public static String NOT_ENOUGH_SHOP_MONEY;
    public static String PLAYER_INVENTORY_FULL;
    public static String SELL;
    public static String SELL_RATE;
    private static CommandSender sender1;
    public static String STATUS;
    public static String WITHDRAW;
    
    private static Logger logger = Logger.getLogger("Minecraft");

    public static void save(CommandSender sender)
    {
        sender1 = sender;
    }

    public static void send(String string)
    {
        if (sender1 != null)
        {
            sender1.sendMessage(string);
        }
    }

    public static void send(String string, Object[] args)
    {
        if (sender1 != null)
        {
            send(String.format(string, args));
        }
    }
    /**
     * Parameterized logger
     *
     * @param level
     * @param msg   the message
     * @param arg   the arguments
     */
    public static void log(Level level, String msg)
    {
        logger.log(level, "[PhysicalShop]" + msg);
    }
    public static void log(String msg)
    {
        log(Level.INFO, msg);
    }
    public static void dump(Object o){
        log(string_dump(o));
    }
    public static String string_dump(Object obj) {
       StringBuffer buffer = new StringBuffer();
    Field[] fields = obj.getClass().getDeclaredFields();
    for (Field f : fields) {
      if (!Modifier.isStatic(f.getModifiers())) {
                try {
                    f.setAccessible(true);
                    Object value = f.get(obj);
                    buffer.append(f.getType().getName());
                    buffer.append(" ");
                    buffer.append(f.getName());
                    buffer.append("=");
                    buffer.append("" + value.toString());
                    buffer.append("\n");
                } catch (IllegalArgumentException ex) {
                    buffer.append(ex);
                } catch (IllegalAccessException ex) {
                    buffer.append(ex);
                }
      }
    }
    return buffer.toString();
}


}