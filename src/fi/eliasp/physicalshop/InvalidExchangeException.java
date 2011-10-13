package fi.eliasp.physicalshop;

public class InvalidExchangeException extends Exception
{
  private static final long serialVersionUID = -728948381002934316L;
  private final Type type;

  public InvalidExchangeException(Type type)
  {
    this.type = type;
  }

  public Type getType() {
    return type;
  }

  public static enum Type
  {
    ADD, REMOVE;
  }
}