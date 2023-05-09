package net.logicim.data.common;

public class ValueData
    extends ReflectiveData
{
  public Object object;

  public ValueData()
  {
  }

  public ValueData(Object object)
  {
    this.object = object;
  }

  @Override
  public Object getObject()
  {
    return object;
  }
}

