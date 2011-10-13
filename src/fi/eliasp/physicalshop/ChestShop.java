package fi.eliasp.physicalshop;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public class ChestShop extends Shop
{
    private final Chest chest;

    public ChestShop(Sign sign)
            throws InvalidSignException
    {
        super(sign);

        Block chestBlock = sign.getBlock().getRelative(BlockFace.DOWN);

        if (chestBlock.getType() != Material.CHEST)
        {
            throw new InvalidSignException();
        }

        chest = ((Chest) chestBlock.getState());
    }

    protected boolean buy(Player player)
    {
        ShopItemStack[] items = InventoryHelpers.getItems(chest.getInventory());
        try
        {
            InventoryHelpers.exchange(chest.getInventory(), PhysicalShop.getCurrency().getStack(getBuyRate().getPrice()), getMaterial().getStack(getBuyRate().getAmount()));
        }
        catch (InvalidExchangeException e)
        {
            switch (e.getType().ordinal())
            {
                case 1:
                    Messaging.send(Messaging.CHEST_INVENTORY_FULL);
                    break;
                case 2:
                    Messaging.send(Messaging.NOT_ENOUGH_SHOP_ITEMS, new Object[]
                            {
                                getMaterial()
                            });
            }

            return false;
        }

        if (!super.buy(player))
        {
            InventoryHelpers.setItems(chest.getInventory(), items);
            return false;
        }

        return true;
    }

    public int getShopCurrency()
    {
        return InventoryHelpers.getCount(chest.getInventory(), PhysicalShop.getCurrency());
    }

    public int getShopItems()
    {
        return InventoryHelpers.getCount(chest.getInventory(), getMaterial());
    }

    public boolean isOwner(Player player)
    {
        return (player != null) && ((player.getName().equals(getOwnerName())) || (PhysicalShop.getPermissions().hasAdmin(player)));
    }

    public boolean isShopBlock(Block block)
    {
        if (super.isShopBlock(block))
        {
            return true;
        }

        Block down = getSign().getBlock().getRelative(BlockFace.DOWN);

        return (down.getType() == Material.CHEST) && (down == block);
    }

    public boolean sell(Player player)
    {
        ShopItemStack[] items = InventoryHelpers.getItems(chest.getInventory());
        try
        {
            InventoryHelpers.exchange(chest.getInventory(), getMaterial().getStack(getSellRate().getAmount()), PhysicalShop.getCurrency().getStack(getSellRate().getPrice()));
        }
        catch (InvalidExchangeException e)
        {
            switch (e.getType().ordinal())
            {
                case 1:
                    Messaging.send(Messaging.CHEST_INVENTORY_FULL);
                    break;
                case 2:
                    Messaging.send(Messaging.NOT_ENOUGH_SHOP_MONEY, new Object[]
                            {
                                PhysicalShop.getCurrency()
                            });
            }

            return false;
        }

        if (!super.sell(player))
        {
            InventoryHelpers.setItems(chest.getInventory(), items);
            return false;
        }

        return true;
    }

    public void status()
    {
        Messaging.send(Messaging.STATUS, new Object[]
                {
                    Integer.valueOf(getShopCurrency()), PhysicalShop.getCurrency(), Integer.valueOf(getShopItems()), getMaterial()
                });

        super.status();
    }
}