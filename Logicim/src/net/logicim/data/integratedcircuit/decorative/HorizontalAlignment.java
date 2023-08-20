package net.logicim.data.integratedcircuit.decorative;

import net.common.SimulatorException;

public enum HorizontalAlignment
{
  LEFT,
  CENTER,
  RIGHT;

  public float getModifier()
  {
    if (this == LEFT)
    {
      return -1.0f;
    }
    else if (this == RIGHT)
    {
      return 1.0f;
    }
    else if (this == CENTER)
    {
      return 0.0f;
    }
    else
    {
      throw new SimulatorException("Cannot get HorizontalAlignment modifier for unknown alignment.");
    }
  }
}

