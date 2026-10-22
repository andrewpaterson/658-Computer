package net.common.type;

import net.common.SimulatorException;

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

  public Float2D(double x, double y)
  {
    this.x = (float) x;
    this.y = (float) y;
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

  public Float2D(Tuple2 p)
  {
    this.x = p.getX();
    this.y = p.getY();
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

  @Override
  public void divide(float f)
  {
    this.x /= f;
    this.y /= f;
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

  public void subtract(float x, float y)
  {
    this.x -= x;
    this.y -= y;
  }

  public void subtract(int x, int y)
  {
    this.x -= x;
    this.y -= y;
  }

  public void subtract(Float2D p)
  {
    this.x -= p.x;
    this.y -= p.y;
  }

  public void subtract(Int2D p)
  {
    this.x -= p.x;
    this.y -= p.y;
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
  public String toString()
  {
    return "" + x + ", " + y;
  }

  @Override
  public boolean isZero()
  {
    return x == 0 &&
           y == 0;
  }

  @Override
  public boolean equals(int x, int y)
  {
    return this.x == x &&
           this.y == y;
  }

  @Override
  public Tuple1 getXTuple()
  {
    return new Float1D(x);
  }

  @Override
  public Tuple1 getYTuple()
  {
    return new Float1D(y);
  }

  @Override
  public void setX(Tuple1 x)
  {
    if (x instanceof Float1D)
    {
      this.x = ((Float1D) x).f;
    }
    else if (x instanceof Int1D)
    {
      this.x = ((Int1D) x).i;
    }
    else
    {
      throw new SimulatorException("Cannot call setX on unknown tuple.");
    }
  }

  @Override
  public void setY(Tuple1 y)
  {
    if (y instanceof Float1D)
    {
      this.y = ((Float1D) y).f;
    }
    else if (y instanceof Int1D)
    {
      this.y = ((Int1D) y).i;
    }
    else
    {
      throw new SimulatorException("Cannot call setY on unknown tuple.");
    }
  }

  @Override
  public void setX(int x)
  {
    this.x = x;
  }

  @Override
  public void setY(int y)
  {
    this.y = y;
  }

  @Override
  public int getIntX()
  {
    return (int) x;
  }

  @Override
  public Int2D cloneAsInt2D()
  {
    return new Int2D(x, y);
  }

  @Override
  public int getIntY()
  {
    return (int) y;
  }

  public static Float2D safeClone(Float2D d)
  {
    if (d != null)
    {
      return d.clone();
    }
    return null;
  }
}

