package net.logicim.data.common;

public abstract class ReflectiveData
    extends SaveData
{
  public ReflectiveData()
  {
  }

  @Override
  public Object getObject()
  {
    return this;
  }
}

