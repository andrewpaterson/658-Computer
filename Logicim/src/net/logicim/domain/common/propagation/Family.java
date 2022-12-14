package net.logicim.domain.common.propagation;

public class Family
{
  protected String family;

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

