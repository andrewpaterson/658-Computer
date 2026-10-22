package net.logicim.ui.input.mouse;

import net.common.type.Int2D;

public class MousePosition
{
  Int2D position;
  boolean valid;

  public MousePosition()
  {
    position = new Int2D(0, 0);
    valid = false;
  }

  public void set(int x, int y)
  {
    position.set(x, y);
    valid = true;
  }

  public void invalidate()
  {
    valid = false;
  }

  public Int2D get()
  {
    if (valid)
    {
      return position;
    }
    else
    {
      return null;
    }
  }
}

