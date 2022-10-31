package net.logicim.common.type;

public abstract class Tuple2
{
  public abstract Tuple2 clone();

  public abstract void set(Tuple2 p);

  public abstract float getX();

  public abstract float getY();

  public abstract void add(Tuple2 p);

  public abstract void set(int x, int y);

  public abstract void set(float x, float y);

  public abstract void add(int x, int y);
}
