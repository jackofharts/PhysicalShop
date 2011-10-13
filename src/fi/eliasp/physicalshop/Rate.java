package fi.eliasp.physicalshop;

public class Rate
{
    private final int amount;
    private final int price;

    Rate(int amount, int price)
    {
        this.amount = amount;
        this.price = price;
    }

    public int getAmount()
    {
        return amount;
    }

    public int getAmount(int currency)
    {
        return price != 0 ? currency / price * amount : 0;
    }

    public int getPrice()
    {
        return price;
    }

    public int getPrice(int items)
    {
        return amount != 0 ? items / amount * price : 0;
    }
}