package fi.eliasp.physicalshop;

import org.bukkit.inventory.ItemStack;

public class ShopItemStack
{
    private final int amount;
    private final ShopMaterial material;

    ShopItemStack(ItemStack itemStack)
    {
        material = new ShopMaterial(itemStack);
        amount = itemStack.getAmount();
    }

    public ItemStack getStack()
    {
        return material.getStack(amount);
    }
}