package net.logicim.common.type;

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

  @Override
  public String toString()
  {
    return super.toString();
  }

  public abstract boolean isZero();
}

