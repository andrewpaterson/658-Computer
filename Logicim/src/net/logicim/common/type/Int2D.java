package net.logicim.common.type;

public class Int2D
    extends Tuple2
{
  public int x;
  public int y;

  public Int2D()
  {
  }

  public Int2D(int x, int y)
  {
    this.x = x;
    this.y = y;
  }

  public void set(Int2D position)
  {
    this.x = position.x;
    this.y = position.y;
  }

  public void set(int x, int y)
  {
    this.x = x;
    this.y = y;
  }

  public void subtract(Int2D position)
  {
    this.x -= position.x;
    this.y -= position.y;
  }

  @Override
  public Int2D clone()
  {
    return new Int2D(x,y);
  }

  @Override
  public void set(Tuple2 source)
  {
    set((Int2D) source);
  }
}

