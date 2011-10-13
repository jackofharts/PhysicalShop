package fi.eliasp.physicalshop;

import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PhysicalShop extends JavaPlugin
{
    private static Config configuration;
    private static ShopMaterial currency;
    private static Permissions permissions;
    private final PhysicalShopBlockListener blockListener = new PhysicalShopBlockListener();
    private final PhysicalShopEntityListener entityListener = new PhysicalShopEntityListener();
    private final PhysicalShopPlayerListener playerListener = new PhysicalShopPlayerListener();

    public static Config getConf()
    {
        return configuration;
    }

    public static ShopMaterial getCurrency()
    {
        return currency;
    }

    public static Permissions getPermissions()
    {
        return permissions;
    }

    public void onDisable()
    {
    }

    public void onEnable()
    {
        configuration = new Config(getConfig());
        currency = configuration.getCurrency();
        permissions = new Permissions(this);

        Messaging.STATUS = "The shop contains %1$d %2$s and %3$d %4$s";
        Messaging.CANT_DESTROY = "You are not allowed to destroy this shop";
        Messaging.CANT_USE = "You are not allowed to use shops";
        Messaging.CANT_BUILD = "You are not allowed to build shops";
        Messaging.CANT_BUILD_SERVER = "You are not allowed to build server shops";
        Messaging.CANT_USE_CHEST = "You are not allowed to use this shop chest";
        Messaging.CANT_PLACE_CHEST = "You are not allowed to place this chest";
        Messaging.DEPOSIT = "You deposited %1$d %2$s and %3$d %4$s";
        Messaging.WITHDRAW = "You withdrew %1$d %2$s and %3$d %4$s";
        Messaging.NO_BUY = "You can't buy at this shop";
        Messaging.BUY = "You bought %1$d %2$s for %3$d %4$s";
        Messaging.BUY_RATE = "You can buy %1$d %2$s for %3$d %4$s";
        Messaging.SELL_RATE = "You can sell %1$d %2$s for %3$d %4$s";
        Messaging.NO_SELL = "You can't sell at this shop";
        Messaging.SELL = "You sold %1$d %2$s for %3$d %4$s";
        Messaging.NOT_ENOUGH_PLAYER_MONEY = "You don't have enough %1$s";
        Messaging.NOT_ENOUGH_PLAYER_ITEMS = "You don't have enough %1$s";
        Messaging.NOT_ENOUGH_SHOP_MONEY = "The shop doesn't have enough %1$s";
        Messaging.NOT_ENOUGH_SHOP_ITEMS = "The shop doesn't have enough %1$s";
        Messaging.CHEST_INVENTORY_FULL = "The shop is full";
        Messaging.PLAYER_INVENTORY_FULL = "Your inventory is full";
        Messaging.EXISTING_CHEST = "You can't build a shop over an existing chest";

        PluginManager pm = getServer().getPluginManager();

        pm.registerEvent(Event.Type.BLOCK_BREAK, blockListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_PLACE, blockListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.ENTITY_EXPLODE, entityListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.SIGN_CHANGE, blockListener, Event.Priority.Normal, this);

        getServer().getLogger().info(String.format("[%s] version %s enabled", new Object[]
                {
                    getDescription().getName(), getDescription().getVersion()
                }));
    }

    public void onLoad()
    {
    }
}