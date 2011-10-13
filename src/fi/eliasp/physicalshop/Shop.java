package fi.eliasp.physicalshop;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.regex.Matcher;

public class Shop
{
    private final Rate buyRate;
    private final ShopMaterial material;
    private final String ownerName;
    private final Rate sellRate;
    private final org.bukkit.block.Sign sign;

    public static ShopMaterial getMaterial(String[] lines)
    {
        try
        {
            return new ShopMaterial(lines[0]);
        }
        catch (InvalidMaterialException e)
        {
        }
        return null;
    }

    public static String getOwnerName(String[] lines)
    {
        return lines[3];
    }

    private static Rate getRate(org.bukkit.block.Sign sign, int line)
    {
        Matcher m = PhysicalShop.getConf().getRatePattern().matcher(sign.getLine(line));

        if (m.find())
        {
            int amount = Integer.parseInt(m.group(1));
            int price = Integer.parseInt(m.group(2));
            return new Rate(amount, price);
        }
        return null;
    }

    public Shop(org.bukkit.block.Sign sign)
            throws InvalidSignException
    {
        this.sign = sign;

        material = getMaterial(sign.getLines());

        if (material == null)
        {
            throw new InvalidSignException();
        }

        buyRate = getRate(sign, 1);
        sellRate = getRate(sign, 2);

        if ((buyRate == null) && (sellRate == null))
        {
            throw new InvalidSignException();
        }

        ownerName = getOwnerName(sign.getLines());

        if ((ownerName == null) || (ownerName.isEmpty()))
        {
            throw new InvalidSignException();
        }
    }

    protected boolean buy(Player player)
    {
        if (!canBuy())
        {
            Messaging.send(Messaging.NO_BUY);
            return false;
        }

        Inventory inventory = player.getInventory();

        int price = getBuyRate().getPrice();
        int amount = getBuyRate().getAmount();
        try
        {
            InventoryHelpers.exchange(inventory, material.getStack(amount), PhysicalShop.getCurrency().getStack(price));
        }
        catch (InvalidExchangeException e)
        {
            switch (e.getType().ordinal())
            {
                case 1:
                    Messaging.send(Messaging.PLAYER_INVENTORY_FULL);
                    break;
                case 2:
                    Messaging.send(Messaging.NOT_ENOUGH_PLAYER_MONEY, new Object[]
                            {
                                PhysicalShop.getCurrency()
                            });
            }

            return false;
        }

        Messaging.send(Messaging.BUY, new Object[]
                {
                    Integer.valueOf(amount), material, Integer.valueOf(price), PhysicalShop.getCurrency()
                });

        player.updateInventory();

        return true;
    }

    public boolean canBuy()
    {
        return buyRate != null;
    }

    public boolean canSell()
    {
        return sellRate != null;
    }

    public Rate getBuyRate()
    {
        return buyRate;
    }

    public ShopMaterial getMaterial()
    {
        return material;
    }

    public String getOwnerName()
    {
        return ownerName;
    }

    public Rate getSellRate()
    {
        return sellRate;
    }

    public int getShopCurrency()
    {
        return 2147483647;
    }

    public int getShopItems()
    {
        return 2147483647;
    }

    public org.bukkit.block.Sign getSign()
    {
        return sign;
    }

    public void interact(Player player)
    {
        ShopMaterial item = new ShopMaterial(player.getItemInHand());

        if (item.equals(PhysicalShop.getCurrency()))
        {
            buy(player);
        }
        else if (item.equals(material))
        {
            sell(player);
        }
    }

    public boolean isOwner(Player player)
    {
        return (player != null) && (PhysicalShop.getPermissions().hasAdmin(player));
    }

    public boolean isShopBlock(Block block)
    {
        Block signBlock = sign.getBlock();

        if (block == signBlock)
        {
            return true;
        }

        org.bukkit.material.Sign signData = (org.bukkit.material.Sign) sign.getData();

        return signBlock.getRelative(signData.getAttachedFace()) == block;
    }

    protected boolean sell(Player player)
    {
        if (!canSell())
        {
            Messaging.send(Messaging.NO_SELL);
            return false;
        }

        Inventory inventory = player.getInventory();

        int price = getSellRate().getPrice();
        int amount = getSellRate().getAmount();
        try
        {
            InventoryHelpers.exchange(inventory, PhysicalShop.getCurrency().getStack(price), material.getStack(amount));
        }
        catch (InvalidExchangeException e)
        {
            switch (e.getType().ordinal())
            {
                case 1:
                    Messaging.send(Messaging.PLAYER_INVENTORY_FULL);
                    break;
                case 2:
                    Messaging.send(Messaging.NOT_ENOUGH_PLAYER_ITEMS, new Object[]
                            {
                                material
                            });
            }

            return false;
        }

        player.updateInventory();

        Messaging.send(Messaging.SELL, new Object[]
                {
                    Integer.valueOf(amount), material, Integer.valueOf(price), PhysicalShop.getCurrency()
                });

        return true;
    }

    public void status()
    {
        if ((canBuy()) && (getShopItems() >= buyRate.getAmount()))
        {
            Messaging.send(Messaging.BUY_RATE, new Object[]
                    {
                        Integer.valueOf(buyRate.getAmount()), material, Integer.valueOf(buyRate.getPrice()), PhysicalShop.getCurrency()
                    });
        }

        if ((canSell()) && (getShopCurrency() >= sellRate.getPrice()))
        {
            Messaging.send(Messaging.SELL_RATE, new Object[]
                    {
                        Integer.valueOf(sellRate.getAmount()), material, Integer.valueOf(sellRate.getPrice()), PhysicalShop.getCurrency()
                    });
        }
    }
}