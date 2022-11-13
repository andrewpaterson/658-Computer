package net.logicim.domain.common.propagation;

public abstract class VoltageConfiguration
{
  protected String family;

  public VoltageConfiguration(String family)
  {
    this.family = family;
  }

  public boolean isInput()
  {
    return false;
  }

  public boolean isOutput()
  {
    return false;
  }
}

