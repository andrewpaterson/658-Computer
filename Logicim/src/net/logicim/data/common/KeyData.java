package net.logicim.data.common;

public class KeyData
    extends ReflectiveData
{
  public Object object;

  public KeyData()
  {
  }

  public KeyData(Object object)
  {
    this.object = object;
  }

  @Override
  public Object getObject()
  {
    return object;
  }
}

