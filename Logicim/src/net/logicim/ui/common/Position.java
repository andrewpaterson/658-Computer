package net.logicim.ui.common;

public class Position
{
  public int x;
  public int y;

  public Position(int x, int y)
  {
    this.x = x;
    this.y = y;
  }

  public void set(Position position)
  {
    this.x = position.x;
    this.y = position.y;
  }

  public void set(int x, int y)
  {
    this.x = x;
    this.y = y;
  }

  public void subtract(Position position)
  {
    this.x -= position.x;
    this.y -= position.y;
  }
}

