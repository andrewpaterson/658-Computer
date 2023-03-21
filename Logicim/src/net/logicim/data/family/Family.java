package net.logicim.data.family;

import net.logicim.data.common.ReflectiveData;

public class Family
    extends ReflectiveData
{
  protected String family;

  public Family()
  {
  }

  public Family(String family)
  {
    this.family = family;
  }

  public String getFamily()
  {
    return family;
  }

  @Override
  public String toString()
  {
    return family;
  }
}

