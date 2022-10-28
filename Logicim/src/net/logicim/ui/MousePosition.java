package net.logicim.ui;

import net.logicim.ui.common.Position;

public class MousePosition
{
  Position position;
  boolean valid;

  public MousePosition()
  {
    position = new Position(0, 0);
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

  public Position get()
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

