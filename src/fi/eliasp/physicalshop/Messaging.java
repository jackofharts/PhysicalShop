package fi.eliasp.physicalshop;

import org.bukkit.command.CommandSender;

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
    private static CommandSender sender;
    public static String STATUS;
    public static String WITHDRAW;

    public static void save(CommandSender sender)
    {
        sender = sender;
    }

    public static void send(String string)
    {
        if (sender != null)
        {
            sender.sendMessage(string);
        }
    }

    public static void send(String string, Object[] args)
    {
        if (sender != null)
        {
            send(String.format(string, args));
        }
    }
}