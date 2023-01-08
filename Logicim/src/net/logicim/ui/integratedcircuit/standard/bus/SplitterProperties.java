package net.logicim.ui.integratedcircuit.standard.bus;

import net.logicim.ui.common.integratedcircuit.ComponentProperties;

public class SplitterProperties
    extends ComponentProperties
{
  protected int outputCount;
  protected int outputOffset;
  protected int spacing;

  public SplitterProperties()
  {
  }

  public SplitterProperties(String name, int outputCount, int outputOffset, int spacing)
  {
    super(name);
    this.outputCount = outputCount;
    this.outputOffset = outputOffset;
    this.spacing = spacing;
  }
}
