package net.logicim.common.type;

public class Int2D
    extends Tuple2
{
  public int x;
  public int y;

  public Int2D()
  {
  }

  public Int2D(Int2D p)
  {
    this.x = p.x;
    this.y = p.y;
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

  public void set(Float2D p)
  {
    this.x = (int) p.x;
    this.y = (int) p.y;
  }

  @Override
  public void set(int x, int y)
  {
    this.x = x;
    this.y = y;
  }

  @Override
  public void set(float x, float y)
  {
    this.x = (int) x;
    this.y = (int) y;
  }

  public void add(Int2D p)
  {
    this.x += p.x;
    this.y += p.y;
  }

  public void add(Float2D p)
  {
    this.x += p.x;
    this.y += p.y;
  }

  @Override
  public void add(int x, int y)
  {
    this.x += x;
    this.y += y;
  }

  @Override
  public void add(float x, float y)
  {
    this.x += x;
    this.y += y;
  }

  public void subtract(Int2D p)
  {
    this.x -= p.x;
    this.y -= p.y;
  }

  public void subtract(Float2D p)
  {
    this.x -= p.x;
    this.y -= p.y;
  }

  @Override
  public void subtract(Tuple2 p)
  {
    if (p instanceof Float2D)
    {
      subtract((Float2D) p);
    }
    else if (p instanceof Int2D)
    {
      subtract((Int2D) p);
    }
  }

  @Override
  public void subtract(int x, int y)
  {
    this.x -= x;
    this.y -= y;
  }

  @Override
  public void subtract(float x, float y)
  {
    this.x -= x;
    this.y -= y;
  }

  @Override
  public void divide(int i)
  {
    this.x /= i;
    this.y /= i;
  }

  @Override
  public Int2D clone()
  {
    return new Int2D(x, y);
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

  public boolean equals(int x, int y)
  {
    return this.x == x && this.y == y;
  }

  @Override
  public boolean equals(Object p)
  {
    if (p instanceof Int2D)
    {
      return this.x == ((Int2D) p).x && this.y == ((Int2D) p).y;
    }
    if (p instanceof Float2D)
    {
      return this.x == ((Float2D) p).x && this.y == ((Float2D) p).y;
    }
    else
    {
      return false;
    }
  }

  @Override
  public String toString()
  {
    return "" + x + ", " + y;
  }
}

