package net.common.type;

import net.common.SimulatorException;

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

  public Int2D(float x, float y)
  {
    this.x = Math.round(x);
    this.y = Math.round(y);
  }

  public Int2D(Float2D p)
  {
    this.x = Math.round(p.x);
    this.y = Math.round(p.y);
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
  public void divide(float f)
  {
    this.x /= f;
    this.y /= f;
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
  public Tuple1 getXTuple()
  {
    return new Int1D(x);
  }

  @Override
  public Tuple1 getYTuple()
  {
    return new Int1D(y);
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

  @Override
  public boolean isZero()
  {
    return x == 0 &&
           y == 0;
  }

  @Override
  public void setX(Tuple1 x)
  {
    if (x instanceof Float1D)
    {
      this.x = Math.round(((Float1D) x).f);
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
      this.y = Math.round(((Float1D) y).f);
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
  public int getIntY()
  {
    return y;
  }

  @Override
  public int getIntX()
  {
    return x;
  }

  @Override
  public Int2D cloneAsInt2D()
  {
    return clone();
  }

  public static Int2D safeClone(Int2D d)
  {
    if (d != null)
    {
      return d.clone();
    }
    return null;
  }

  public static String toString(Int2D p)
  {
    if (p != null)
    {
      return p.toString();
    }
    return "null";
  }
}

