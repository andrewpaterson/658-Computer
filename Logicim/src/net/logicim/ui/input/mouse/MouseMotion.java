package net.logicim.ui.input.mouse;

import net.common.type.Int2D;

public class MouseMotion
{
  protected Int2D previous;
  protected Int2D current;
  protected Int2D relative;
  protected boolean previousValid;

  public MouseMotion()
  {
    previous = new Int2D(0, 0);
    current = new Int2D(0, 0);
    relative = new Int2D(0, 0);
    previousValid = false;
  }

  public Int2D moved(int x, int y)
  {
    current.set(x, y);

    boolean relativeValid = false;
    if (previousValid)
    {
      relative.set(previous);
      relative.subtract(current);
      relativeValid = true;
    }

    previous.set(current);
    previousValid = true;

    if (relativeValid)
    {
      return relative;
    }
    else
    {
      return null;
    }
  }

  public void invalidate()
  {
    previousValid = false;
  }
}

