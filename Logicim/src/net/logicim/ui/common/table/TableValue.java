package net.logicim.ui.common.table;

public class TableValue
{
  protected Class<?> valueClass;
  protected Object value;

  public TableValue(Object value)
  {
    this.value = value;
    this.valueClass = value.getClass();
  }

  public TableValue(Class valueClass)
  {
    this.value = null;
    this.valueClass = valueClass;
  }

  public Class<?> getValueClass()
  {
    return valueClass;
  }

  public Object getValue()
  {
    return value;
  }

  @Override
  public String toString()
  {
    if (value != null)
    {
      return value.toString();
    }
    else
    {
      return "";
    }
  }
}

