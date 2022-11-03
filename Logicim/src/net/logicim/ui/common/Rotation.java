package net.logicim.ui.common;

import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.common.type.Tuple2;

public enum Rotation
{
  Cannot,
  North,
  East,
  South,
  West;

  public Rotation rotateRight()
  {
    switch (this)
    {
      case Cannot:
        return Cannot;
      case North:
        return West;
      case West:
        return South;
      case South:
        return East;
      default:
        return North;
    }
  }

  public Rotation rotateLeft()
  {
    switch (this)
    {
      case Cannot:
        return Cannot;
      case North:
        return East;
      case East:
        return South;
      case South:
        return West;
      default:
        return North;
    }
  }

  public int rotationToDegrees()
  {
    switch (this)
    {
      case Cannot:
      case North:
        return 0;
      case East:
        return 90;
      case South:
        return 180;
      default:
        return 270;
    }
  }

  @SuppressWarnings("SuspiciousNameCombination")
  public void transform(Int2D p)
  {
    switch (this)
    {
      case East:
        p.set(p.y, -p.x);
        break;
      case South:
        p.set(-p.x, -p.y);
        break;
      case West:
        p.set(-p.y, p.x);
        break;
    }
  }
  @SuppressWarnings("SuspiciousNameCombination")
  public void transform(Float2D p)
  {
    switch (this)
    {
      case East:
        p.set(p.y, -p.x);
        break;
      case South:
        p.set(-p.x, -p.y);
        break;
      case West:
        p.set(-p.y, p.x);
        break;
    }
  }

  public void rotate(Tuple2 dest, Tuple2 source)
  {
    dest.set(source);
    if (dest instanceof Int2D)
    {
      transform((Int2D) dest);
    }
    else if (dest instanceof Float2D)
    {
      transform((Float2D) dest);
    }
  }

  public static Rotation calculateDirection(int startX, int startY, int endX, int endY)
  {
    if (startX == endX)
    {
      if (startY < endY)
      {
        return Rotation.North;
      }
      if (startY > endY)
      {
        return Rotation.South;
      }
    }
    if (startY == endY)
    {
      if (startX < endX)
      {
        return Rotation.West;
      }
      if (startX > endX)
      {
        return Rotation.East;
      }
    }

    return Rotation.Cannot;
  }
}

