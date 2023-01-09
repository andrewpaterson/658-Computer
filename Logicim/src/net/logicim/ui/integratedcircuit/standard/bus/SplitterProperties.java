package net.logicim.ui.integratedcircuit.standard.bus;

import net.logicim.ui.common.integratedcircuit.ComponentProperties;

public class SplitterProperties
    extends ComponentProperties
{
  protected int endCount;
  protected int endOffset;
  protected int spacing;

  public SplitterProperties()
  {
  }

  public SplitterProperties(String name,
                            int endCount,
                            int endOffset,
                            int spacing)
  {
    super(name);
    this.endCount = endCount;
    this.endOffset = endOffset;
    this.spacing = spacing;
  }
}

