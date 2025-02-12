package net.logicim.data.decorative.label;

import net.logicim.data.decorative.common.DecorativeProperties;
import net.logicim.data.integratedcircuit.decorative.HorizontalAlignment;

public class LabelProperties
    extends DecorativeProperties
{
  public HorizontalAlignment alignment;
  public boolean bold;
  public boolean fill;
  public boolean border;

  public LabelProperties()
  {
  }

  public LabelProperties(String name,
                         HorizontalAlignment alignment,
                         boolean bold,
                         boolean fill,
                         boolean border)
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

  @Override
  public boolean equals(Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (!(o instanceof LabelProperties))
    {
      return false;
    }
    if (!super.equals(o))
    {
      return false;
    }
    LabelProperties that = (LabelProperties) o;
    return bold == that.bold &&
           fill == that.fill &&
           border == that.border &&
           alignment == that.alignment;
  }
}

