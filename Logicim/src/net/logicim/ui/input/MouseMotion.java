package net.logicim.ui.input;

import net.logicim.ui.common.Position;

public class MouseMotion
{
  protected Position previous;
  protected Position current;
  protected Position relative;
  protected boolean previousValid;

  public MouseMotion()
  {
    previous = new Position(0, 0);
    current = new Position(0, 0);
    relative = new Position(0, 0);
    previousValid = false;
  }

  public Position moved(int x, int y)
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

