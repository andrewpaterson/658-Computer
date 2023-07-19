package net.logicim.ui.common;

import net.logicim.common.SimulatorException;
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

  public void transform(Tuple2 p)
  {
    if (p instanceof Int2D)
    {
      transform((Int2D) p);
    }
    else if (p instanceof Float2D)
    {
      transform((Float2D) p);
    }
    else
    {
      throw new SimulatorException();
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

  public Rotation opposite()
  {
    switch (this)
    {
      case Cannot:
        return Cannot;
      case North:
        return South;
      case East:
        return West;
      case South:
        return North;
      default:
        return East;
    }
  }

  public boolean isStraight(Rotation other)
  {
    if ((this == Cannot) || (other == Cannot))
    {
      return false;
    }
    if (((this == North) || (this == South)) && ((other == North) || (other == South)))
    {
      return true;
    }
    if (((this == East) || (this == West)) && ((other == East) || (other == West)))
    {
      return true;
    }
    return false;
  }

  public boolean isNorthSouth()
  {
    return (this == North) || (this == South);
  }

  public boolean isEastWest()
  {
    return (this == West) || (this == East);
  }

  public boolean isSouth()
  {
    return this == South;
  }

  public boolean isEast()
  {
    return this == East;
  }

  public boolean isNorth()
  {
    return this == North;
  }

  public boolean isWest()
  {
    return this == West;
  }

  public boolean isCannot()
  {
    return this == Cannot;
  }

  public Rotation rotateRight(int rightRotations)
  {
    Rotation rotation = this;
    for (int i = 0; i < rightRotations; i++)
    {
      rotation = rotation.rotateRight();
    }
    return rotation;
  }
}

