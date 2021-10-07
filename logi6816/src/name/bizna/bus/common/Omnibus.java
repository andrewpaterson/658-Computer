package name.bizna.bus.common;

import name.bizna.util.EmulatorException;

public class Omnibus
    extends Connector
{
  protected long width;
  protected int value;

  public Omnibus(int width)
  {
    this.width = 1L << width;
  }

  public void set(int value)
  {
    if (value >= 0 && value < width)
    {
      this.value = value;
    }
    else
    {
      throw new EmulatorException("Bus value exceeds allowed bus width.");
    }
  }

  public int get()
  {
    return value;
  }
}

