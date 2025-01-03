package net.logicim.ui.common.integratedcircuit;

public class PropertyClamp
{
  public static final int MAX_WIDTH = 64;

  public static int clamp(int value, int min, int max)
  {
    if (value > max)
    {
      return max;
    }
    if (value < min)
    {
      return min;
    }
    return value;
  }

  public static long clamp(long value, long min, long max)
  {
    if (value > max)
    {
      return max;
    }
    if (value < min)
    {
      return min;
    }
    return value;
  }

  public static float clamp(float value, float min, float max)
  {
    if (value > max)
    {
      return max;
    }
    if (value < min)
    {
      return min;
    }
    return value;
  }

  public static double clamp(double value, double min, double max)
  {
    if (value > max)
    {
      return max;
    }
    if (value < min)
    {
      return min;
    }
    return value;
  }
}

