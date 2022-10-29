package net.logicim.common.type;

public class Float2D
    extends Tuple2
{
  public float x;
  public float y;

  public Float2D()
  {
  }

  public Float2D(float x, float y)
  {
    this.x = x;
    this.y = y;
  }

  public Float2D(Int2D p)
  {
    this.x = p.x;
    this.y = p.y;
  }

  public void set(Float2D p)
  {
    this.x = p.x;
    this.y = p.y;
  }

  public void set(float x, float y)
  {
    this.x = x;
    this.y = y;
  }

  public void subtract(Float2D p)
  {
    this.x -= p.x;
    this.y -= p.y;
  }

  public void add(Float2D p)
  {
    this.x += p.x;
    this.y += p.y;
  }

  @Override
  public Float2D clone()
  {
    return new Float2D(x, y);
  }

  @Override
  public void set(Tuple2 source)
  {
    set((Float2D) source);
  }
}

