package fi.eliasp.physicalshop;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

public class PhysicalShopPlayerListener extends PlayerListener
{
    public void onPlayerInteract(PlayerInteractEvent e)
    {
        if (e.isCancelled())
        {
            return;
        }

        Messaging.save(e.getPlayer());

        Block block = e.getClickedBlock();

        if ((PhysicalShop.getConf().isProtectChestUse()) && (e.getAction() == Action.RIGHT_CLICK_BLOCK) && (block.getType() == Material.CHEST))
        {
            Shop shop = ShopHelpers.getShop(block.getRelative(BlockFace.UP));

            if ((shop != null) && (shop.isShopBlock(block)) && (!shop.isOwner(e.getPlayer())))
            {
                Messaging.send(Messaging.CANT_USE_CHEST);
                e.setCancelled(true);
                return;
            }
        }

        Shop shop = ShopHelpers.getShop(block);

        if (shop == null)
        {
            return;
        }

        if (!PhysicalShop.getPermissions().hasUse(e.getPlayer()))
        {
            Messaging.send(Messaging.CANT_USE);
            return;
        }

        if (e.getAction() == Action.LEFT_CLICK_BLOCK)
        {
            shop.status();
        }
        else if (e.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            shop.interact(e.getPlayer());
            e.setCancelled(true);
        }
    }
}