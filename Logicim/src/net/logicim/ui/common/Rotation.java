package net.logicim.ui.common;

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
  public void transform(Position p)
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
}

