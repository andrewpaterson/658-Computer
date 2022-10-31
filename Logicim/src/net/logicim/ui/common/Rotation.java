package net.logicim.ui.common;

import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.common.type.Tuple2;

public enum Rotation
{
  CANNOT,
  NORTH,
  EAST,
  SOUTH,
  WEST;

  public Rotation rotateRight()
  {
    switch (this)
    {
      case CANNOT:
        return CANNOT;
      case NORTH:
        return WEST;
      case WEST:
        return SOUTH;
      case SOUTH:
        return EAST;
      default:
        return NORTH;
    }
  }

  public Rotation rotateLeft()
  {
    switch (this)
    {
      case CANNOT:
        return CANNOT;
      case NORTH:
        return EAST;
      case EAST:
        return SOUTH;
      case SOUTH:
        return WEST;
      default:
        return NORTH;
    }
  }

  public int rotationToDegrees()
  {
    switch (this)
    {
      case CANNOT:
      case NORTH:
        return 0;
      case EAST:
        return 90;
      case SOUTH:
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
      case EAST:
        p.set(p.y, -p.x);
        break;
      case SOUTH:
        p.set(-p.x, -p.y);
        break;
      case WEST:
        p.set(-p.y, p.x);
        break;
    }
  }
  @SuppressWarnings("SuspiciousNameCombination")
  public void transform(Float2D p)
  {
    switch (this)
    {
      case EAST:
        p.set(p.y, -p.x);
        break;
      case SOUTH:
        p.set(-p.x, -p.y);
        break;
      case WEST:
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
}

