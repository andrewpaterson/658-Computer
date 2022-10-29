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

  public void set(Int2D p)
  {
    this.x = p.x;
    this.y = p.y;
  }

  public void set(int x, int y)
  {
    this.x = x;
    this.y = y;
  }

  public void subtract(Int2D p)
  {
    this.x -= p.x;
    this.y -= p.y;
  }

  public void add(Int2D p)
  {
    this.x += p.x;
    this.y += p.y;
  }

  @Override
  public Int2D clone()
  {
    return new Int2D(x, y);
  }

  @Override
  public void set(Tuple2 source)
  {
    set((Int2D) source);
  }

  @Override
  public float getX()
  {
    return x;
  }

  @Override
  public float getY()
  {
    return y;
  }
}

