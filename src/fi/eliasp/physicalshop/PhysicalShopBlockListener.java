package fi.eliasp.physicalshop;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.block.Sign;
import java.util.List;

public class PhysicalShopBlockListener extends BlockListener
{
    public void onBlockBreak(BlockBreakEvent e)
    {
        if (e.isCancelled())
        {
            return;
        }

        Messaging.save(e.getPlayer());

        if (!PhysicalShop.getConf().isProtectBreak())
        {
            return;
        }
        
        Block b = e.getBlock();
        // Check for shops, this uses special case to find any shop signs not just valid chest shops
        // One consequence: Shops are removed from DB when either the chest or sign is removed, 
        // so they may be removed twice.  For server shops, they don't need chests placed under them
        // but this is a valid arrangement.  Removing the chest and not the sign will leave the database
        // out of sync until the sign is used again. 
        // TODO: Fix this issue.
        List<Shop> shops = ShopHelpers.getShops(b, true);
        boolean bIsShop = (shops.size() > 0);
        if (!ShopHelpers.isBlockDestroyable(b, e.getPlayer()))
        {            
            e.setCancelled(true);
            Messaging.log(e.getPlayer().getName() + " can't destroy this shop");
        }
        else if (bIsShop){
            //This WAS a shop block
        	for (Shop shop: shops) {
        		//Remove and log each shop found
        		Sign sign = shop.getSign();
        		String owner = sign.getLine(3);
        		Messaging.log(e.getPlayer().getName() + " removed shop owned by " + owner + " at(" + sign.getWorld().getName() + ") " + sign.getX() + " " + sign.getY() + " " + sign.getZ());
        		PhysicalShopMap.removeShop(owner, sign.getWorld().getName(), sign.getX(), sign.getY(), sign.getZ());
        	}
        }
            
    }

    public void onBlockPlace(BlockPlaceEvent e)
    {
        if (e.isCancelled())
        {
            return;
        }

        if (!PhysicalShop.getConf().isProtectChestUse())
        {
            return;
        }

        if (PhysicalShop.getPermissions().hasAdmin(e.getPlayer()))
        {
            return;
        }

        if (e.getBlock().getType() != Material.CHEST)
        {
            return;
        }

        Messaging.save(e.getPlayer());

        Block block = e.getBlock();

        Block[] blocks =
        {
            block.getRelative(BlockFace.NORTH), block.getRelative(BlockFace.EAST), block.getRelative(BlockFace.SOUTH), block.getRelative(BlockFace.WEST)
        };

        for (Block b : blocks)
        {
            if (b.getType() == Material.CHEST)
            {
                Shop shop = ShopHelpers.getShop(b.getRelative(BlockFace.UP));

                if (shop != null) {
                    if(shop.isShopBlock(b)){
                        Messaging.send(Messaging.CANT_PLACE_CHEST);
                        e.setCancelled(true);
                        break;
                    }
                }
            }
        }
    }

    public void onSignChange(SignChangeEvent e)
    {
        if (e.isCancelled())
        {
            return;
        }
        
        Messaging.save(e.getPlayer());

        String[] lines = e.getLines();

        if (Shop.getMaterial(lines) == null)
        {
            return;
        }

        if (!PhysicalShop.getPermissions().hasBuild(e.getPlayer()))
        {
            Messaging.log("Player " + Messaging.CANT_BUILD);
            Messaging.send(Messaging.CANT_BUILD);
            e.setCancelled(true);
            return;
        }

        String ownerName = Shop.getOwnerName(lines);
        if (ownerName.equalsIgnoreCase(PhysicalShop.getConf().getServerOwner()))
        {
            if (!PhysicalShop.getPermissions().hasAdmin(e.getPlayer()))
            {
                Messaging.send(Messaging.CANT_BUILD_SERVER);
                e.setCancelled(true);
                return;
            }
            Block b = e.getBlock();
            Messaging.log(e.getPlayer().getName() + " placed [Server] shop at(" + b.getWorld().getName() + ") " + b.getX() + " " + b.getY() + " " + b.getZ()); 
            PhysicalShopMap.addShop("[Server]", Shop.getMaterial(lines).getMaterial(), Shop.getRate(lines,1), Shop.getRate(lines, 2), b.getWorld().getName(), b.getX(), b.getY(), b.getZ());
        }
        else
        {
            if (e.getBlock().getRelative(BlockFace.DOWN).getType() == Material.CHEST)
            {
                Messaging.send(Messaging.EXISTING_CHEST);
                e.setCancelled(true);
                return;
            }

            if (PhysicalShop.getConf().isAutoFillName())
            {
                e.setLine(3, e.getPlayer().getName());
            }
            Block b = e.getBlock();
            Messaging.log(e.getPlayer().getName() + " placed shop at(" + b.getWorld().getName() + ") " + b.getX() + " " + b.getY() + " " + b.getZ()); 
            /* Add shop to DB here */
            PhysicalShopMap.addShop(e.getPlayer().getName(), Shop.getMaterial(lines).getMaterial(), Shop.getRate(lines,1), Shop.getRate(lines, 2), b.getWorld().getName(), b.getX(), b.getY(), b.getZ());
        }
    }
}