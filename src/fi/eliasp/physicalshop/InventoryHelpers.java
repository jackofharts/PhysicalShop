package fi.eliasp.physicalshop;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryHelpers
{
    private static boolean add(Inventory inventory, ItemStack stack)
    {
        int left = stack.getAmount();
        int maxStackSize = stack.getType().getMaxStackSize();

        for (int pass = 0; pass < 2; pass++)
        {
            ItemStack[] contents = inventory.getContents();

            for (int i = 0; i < contents.length; i++)
            {
                if (left == 0)
                {
                    break;
                }
                ItemStack s = contents[i];

                if (s == null
                        ? pass == 0
                        : (s.getType() != stack.getType()) || (s.getDurability() != stack.getDurability()))
                {
                    continue;
                }

                int size = s == null ? 0 : s.getAmount();
                int newSize = Math.min(maxStackSize, size + left);

                stack.setAmount(newSize);

                inventory.setItem(i, stack);

                left -= newSize - size;
            }
        }

        return left == 0;
    }

    public static void exchange(Inventory inventory, ItemStack addStack, ItemStack removeStack)
            throws InvalidExchangeException
    {
        ShopItemStack[] oldItems = getItems(inventory);

        if (!remove(inventory, removeStack))
        {
            setItems(inventory, oldItems);
            throw new InvalidExchangeException(InvalidExchangeException.Type.REMOVE);
        }

        if (!add(inventory, addStack))
        {
            setItems(inventory, oldItems);
            throw new InvalidExchangeException(InvalidExchangeException.Type.ADD);
        }
    }

    public static int getCount(Inventory inventory, ShopMaterial material)
    {
        int amount = 0;

        for (ItemStack i : inventory.getContents())
        {
            if ((i == null) || (i.getType() != material.getMaterial()) || (i.getDurability() != material.getDurability()))
            {
                continue;
            }
            amount += i.getAmount();
        }

        return amount;
    }

    public static ShopItemStack[] getItems(Inventory inventory)
    {
        ItemStack[] contents = inventory.getContents();
        ShopItemStack[] items = new ShopItemStack[contents.length];

        for (int i = 0; i < items.length; i++)
        {
            ItemStack stack = contents[i];
            items[i] = (stack == null ? null : new ShopItemStack(stack));
        }

        return items;
    }

    private static boolean remove(Inventory inventory, ItemStack stack)
    {
        int left = stack.getAmount();
        ItemStack[] contents = inventory.getContents();

        for (int i = 0; (i < contents.length)
                && (left != 0); i++)
        {
            ItemStack s = contents[i];

            if ((s == null) || (s.getType() != stack.getType()) || (s.getDurability() != stack.getDurability()))
            {
                continue;
            }

            int size = s.getAmount();
            int newSize = size - Math.min(size, left);

            if (newSize == 0)
            {
                inventory.setItem(i, null);
            }
            else
            {
                s.setAmount(newSize);
                inventory.setItem(i, s);
            }

            left -= size - newSize;
        }

        return left == 0;
    }

    public static void setItems(Inventory inventory, ShopItemStack[] items)
    {
        for (int i = 0; i < items.length; i++)
        {
            ShopItemStack stack = items[i];
            inventory.setItem(i, stack == null ? null : stack.getStack());
        }
    }
}