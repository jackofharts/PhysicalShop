package fi.eliasp.physicalshop;

import org.bukkit.CoalType;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.TreeSpecies;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.*;

import java.util.regex.Matcher;

public class ShopMaterial
{
    private final byte durability;
    private final Material material;

    private static byte parseDurability(String string, Material material)
    {
        MaterialData data;
        try
        {
            return Byte.parseByte(string);
        }
        catch (NumberFormatException e)
        {
            String s = string.replace(" ", "_").toUpperCase();
            data = null;
            try
            {
                switch (material)
                {
                    case LOG:
                        data = new Tree(TreeSpecies.valueOf(s));
                        break;
                    case LEAVES:
                        data = new Leaves(TreeSpecies.valueOf(s));
                        break;
                    case STEP:
                        data = new Step(Material.valueOf(s));
                        break;
                    case WOOL:
                        data = new Wool(DyeColor.valueOf(s));
                }
            }
            catch (IllegalArgumentException e2)
            {
            }
            if (data == null)
            {
                return 0;
            }
        }
        return data.getData();
    }

    private static String toHumanReadableString(Object object)
    {
        StringBuilder sb = new StringBuilder();

        for (String word : object.toString().split("_"))
        {
            sb.append(new StringBuilder().append(word.substring(0, 1).toUpperCase()).append(word.substring(1).toLowerCase()).append(" ").toString());
        }

        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    public ShopMaterial(ItemStack itemStack)
    {
        material = itemStack.getType();
        durability = (byte) itemStack.getDurability();
    }

    public ShopMaterial(Material material, byte durability)
    {
        this.material = material;
        this.durability = durability;
    }

    public ShopMaterial(String string) throws InvalidMaterialException
    {
        Matcher m = PhysicalShop.getConf().getMaterialPattern().matcher(string);

        if (!m.find())
        {
            throw new InvalidMaterialException();
        }

        String matchString = m.group(1);
        String[] strings = matchString.split(":");

        if (strings.length == 2)
        {
            this.material = Material.matchMaterial(strings[0]);
            durability = parseDurability(strings[1], this.material);
            return;
        }

        Material mat = null;

        for (int i = 0; i < matchString.length(); i++)
        {
            if ((i == 0) || (matchString.charAt(i) == ' '))
            {
                mat = Material.matchMaterial(matchString.substring(i).trim());

                if (mat != null)
                {
                    this.material = mat;
                    durability = parseDurability(matchString.substring(0, i).trim(), mat);

                    return;
                }
            }
        }

        throw new InvalidMaterialException();
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 97 * hash + this.durability;
        hash = 97 * hash + (this.material != null ? this.material.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }

        if (!(object instanceof ShopMaterial))
        {
            return false;
        }

        ShopMaterial item = (ShopMaterial) object;

        return (getMaterial() == item.getMaterial()) && (getDurability() == item.getDurability());
    }

    public short getDurability()
    {
        return (short) durability;
    }

    public Material getMaterial()
    {
        return material;
    }

    public ItemStack getStack(int amount)
    {
        return amount != 0 ? new ItemStack(getMaterial(), amount, getDurability()) : null;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        MaterialData data = material.getNewData(durability);

        switch (material)
        {
            
            case LOG:
                sb.append(new StringBuilder().append(((Tree) data).getSpecies().toString()).append("_").append(material.toString()).toString());

                break;
            case LEAVES:
                sb.append(new StringBuilder().append(((Leaves) data).getSpecies().toString()).append("_").append(material.toString()).toString());

                break;
            case STEP:
                sb.append(new StringBuilder().append(((Step) data).getMaterial().toString()).append("_").append(material.toString()).toString());
                break;
            /*case :
                sb.append(new StringBuilder().append(((Dye) data).getColor().toString()).append("_").append(material.toString()).toString());

                break;*/
            case WOOL:
                sb.append(new StringBuilder().append(((Wool) data).getColor().toString()).append("_").append(material.toString()).toString());

                break;
            default:
                sb.append(material.toString());
        }

        return toHumanReadableString(sb.toString());
    }
}