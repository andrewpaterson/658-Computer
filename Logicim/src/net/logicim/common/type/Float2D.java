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

  public Float2D(Float2D p)
  {
    this.x = p.x;
    this.y = p.y;
  }

  public void set(Float2D p)
  {
    this.x = p.x;
    this.y = p.y;
  }

  public void set(Int2D p)
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

  @Override
  public void add(int x, int y)
  {
    this.x += x;
    this.y += y;
  }

  @Override
  public void divide(int i)
  {
    this.x /= i;
    this.y /= i;
  }

  public void add(Float2D p)
  {
    this.x += p.x;
    this.y += p.y;
  }

  public void add(Int2D p)
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
    if (source instanceof Float2D)
    {
      set((Float2D) source);
    }
    else if (source instanceof Int2D)
    {
      set((Int2D) source);
    }
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

  @Override
  public void add(Tuple2 p)
  {
    if (p instanceof Float2D)
    {
      add((Float2D) p);
    }
    else if (p instanceof Int2D)
    {
      add((Int2D) p);
    }
  }

  @Override
  public void set(int x, int y)
  {
    this.x = x;
    this.y = y;
  }

  public void add(float x, float y)
  {
    this.x += x;
    this.y += y;
  }

  public void subtract(float x, float y)
  {
    this.x -= x;
    this.y -= y;
  }

  @Override
  public String toString()
  {
    return "" + x + ", " + y;
  }
}
