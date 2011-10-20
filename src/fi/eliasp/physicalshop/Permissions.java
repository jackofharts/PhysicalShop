package fi.eliasp.physicalshop;

import com.nijiko.permissions.PermissionHandler;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
public class Permissions
{
    private PermissionHandler permissions = null;
    private final String pluginName;

    Permissions(PhysicalShop plugin)
    {
        pluginName = plugin.getDescription().getName();

        Plugin test = plugin.getServer().getPluginManager().getPlugin("Permissions");

        if (test != null)
        {
            permissions = ((com.nijikokun.bukkit.Permissions.Permissions) test).getHandler();
        }
    }

    public boolean hasAdmin(Player p)
    {
        if (permissions != null)
        {
            return permissions.has(p, pluginName + ".admin");
        }
        return p.isOp();
    }

    public boolean hasBuild(Player p)
    {
        if (permissions != null)
        {
            return permissions.has(p, pluginName + ".build");
        }
        return true;
    }

    public boolean hasUse(Player p)
    {
        if (permissions != null)
        {
            return permissions.has(p, pluginName + ".use");
        }
        return true;
    }
}