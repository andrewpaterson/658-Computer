package net.common.type;

public abstract class Tuple2
{
  public static Tuple2 safeClone(Tuple2 d)
  {
    if (d != null)
    {
      return d.clone();
    }
    return null;
  }

  @Override
  public String toString()
  {
    return super.toString();
  }

  public void setMinX(int x)
  {
    setX(Math.min(x, getIntX()));
  }

  public void setMinY(int y)
  {
    setY(Math.min(y, getIntY()));
  }

  public void setMaxX(int x)
  {
    setX(Math.max(x, getIntX()));
  }

  public void setMaxY(int y)
  {
    setY(Math.max(y, getIntY()));
  }

  public abstract Tuple2 clone();

  public abstract float getX();

  public abstract float getY();

  public abstract void set(Tuple2 p);

  public abstract void set(int x, int y);

  public abstract void set(float x, float y);

  public abstract void add(Tuple2 p);

  public abstract void add(int x, int y);

  public abstract void add(float x, float y);

  public abstract void subtract(Tuple2 p);

  public abstract void subtract(int x, int y);

  public abstract void subtract(float x, float y);

  public abstract void divide(int i);

  public abstract void divide(float f);

  public abstract boolean isZero();

  public abstract boolean equals(int x, int y);

  public abstract Tuple1 getXTuple();

  public abstract Tuple1 getYTuple();

  public abstract void setX(Tuple1 x);

  public abstract void setY(Tuple1 y);

  public abstract void setX(int x);

  public abstract void setY(int y);

  public abstract int getIntY();

  public abstract int getIntX();

  public abstract Int2D cloneAsInt2D();
}

