package fi.eliasp.physicalshop;

import org.bukkit.block.Block;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityListener;

public class PhysicalShopEntityListener extends EntityListener
{
    public void onEntityExplode(EntityExplodeEvent e)
    {
        if (e.isCancelled())
        {
            return;
        }

        Messaging.save(null);

        if (!PhysicalShop.getConf().isProtectExplode())
        {
            return;
        }

        for (Block block : e.blockList())
        {
            if (!ShopHelpers.isBlockDestroyable(block, null))
            {
                e.setCancelled(true);
                return;
            }
        }
    }
}