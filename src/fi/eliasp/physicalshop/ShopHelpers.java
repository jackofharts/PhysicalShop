package fi.eliasp.physicalshop;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ShopHelpers
{
    static Shop getShop(Block block)
    {
        if (block == null)
        {
            return null;
        }

        if ((block.getType() != Material.SIGN_POST) && (block.getType() != Material.WALL_SIGN))
        {
            return null;
        }

        Sign sign = (Sign) block.getState();

        if (sign == null)
        {
            return null;
        }

        String ownerName = Shop.getOwnerName(sign.getLines());
        try
        {
            if (block.getRelative(BlockFace.DOWN).getType() == Material.CHEST)
            {
                return new ChestShop(sign);
            }
            if (ownerName.equalsIgnoreCase(PhysicalShop.getConf().getServerOwner()))
            {
                return new Shop(sign);
            }
            return null;
        }
        catch (InvalidSignException e)
        {
        }
        return null;
    }

    static List<Shop> getShops(Block block)
    {
        ArrayList shops = new ArrayList();

        Block[] blocks =
        {
            block, block.getRelative(BlockFace.UP), block.getRelative(BlockFace.DOWN), block.getRelative(BlockFace.NORTH), block.getRelative(BlockFace.EAST), block.getRelative(BlockFace.SOUTH), block.getRelative(BlockFace.WEST)
        };

        for (Block b : blocks)
        {
            Shop shop = getShop(b);

            if ((shop != null) && (shop.isShopBlock(block)))
            {
                shops.add(shop);
            }
        }

        return shops;
    }

    static boolean isBlockDestroyable(Block block, Player player)
    {
        List<Shop> shops = getShops(block);

        for (Shop shop : shops)
        {
            if (!shop.isOwner(player))
            {
                Messaging.send(Messaging.CANT_DESTROY);
                shop.getSign().update();
                return false;
            }
        }

        return true;
    }
}