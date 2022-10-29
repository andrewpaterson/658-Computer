package net.logicim.common.type;

public abstract class Tuple2
{
  public abstract Tuple2 clone();

  public abstract void set(Tuple2 source);

  public abstract float getX();

  public abstract float getY();
}
