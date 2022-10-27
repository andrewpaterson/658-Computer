package net.logicim.domain.common.propagation;

public abstract class Propagation
{
  protected String family;

  public Propagation(String family)
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

