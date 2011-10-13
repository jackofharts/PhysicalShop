package fi.eliasp.physicalshop;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.regex.Pattern;

public class Config
{
    private final FileConfiguration configuration;

    public Config(FileConfiguration configuration)
    {
        this.configuration = configuration;
    }

    public ShopMaterial getCurrency()
    {
        Material material = Material.getMaterial(configuration.getInt("currency", Material.GOLD_INGOT.getId()));

        byte durability = (byte) configuration.getInt("currency-data", 0);

        return new ShopMaterial(material, durability);
    }

    public Pattern getMaterialPattern()
    {
        return Pattern.compile(configuration.getString("material-pattern", "\\[(.+)\\]"));
    }

    public Pattern getRatePattern()
    {
        return Pattern.compile(configuration.getString("rate-pattern", "\\D*(\\d+)\\D+(\\d+)\\D*"));
    }

    public String getServerOwner()
    {
        return configuration.getString("server-owner", "[Server]");
    }

    public boolean isAutoFillName()
    {
        return configuration.getBoolean("auto-fill-name", true);
    }

    public boolean isProtectBreak()
    {
        return configuration.getBoolean("protect-break", true);
    }

    public boolean isProtectChestUse()
    {
        return configuration.getBoolean("protect-chest-use", true);
    }

    public boolean isProtectExplode()
    {
        return configuration.getBoolean("protect-explode", true);
    }
}