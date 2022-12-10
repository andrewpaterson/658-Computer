package net.logicim.data;

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

