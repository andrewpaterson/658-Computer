package net.logicim.ui.simulation.component.decorative.label;

import net.logicim.data.integratedcircuit.decorative.HorizontalAlignment;
import net.logicim.ui.simulation.component.decorative.common.DecorativeProperties;

public class LabelProperties
    extends DecorativeProperties
{
  protected HorizontalAlignment alignment;
  protected boolean bold;
  protected boolean fill;
  protected boolean border;

  public LabelProperties()
  {
  }

  public LabelProperties(String name, HorizontalAlignment alignment, boolean bold, boolean fill, boolean border)
  {
    super(name);
    this.alignment = alignment;
    this.bold = bold;
    this.fill = fill;
    this.border = border;
  }

  @Override
  public LabelProperties duplicate()
  {
    return new LabelProperties(name,
                               alignment,
                               bold,
                               fill,
                               border);
  }
}

