package net.logicim.data.common;

public abstract class ViewData
    extends ReflectiveData
{
  public long id;

  public ViewData()
  {
  }

  public ViewData(long id)
  {
    this.id = id;
  }

  public abstract boolean appliesToSimulation(long id);
}

