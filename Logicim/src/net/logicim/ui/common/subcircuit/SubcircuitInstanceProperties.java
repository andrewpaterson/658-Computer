package net.logicim.ui.common.subcircuit;

import net.logicim.ui.common.integratedcircuit.ComponentProperties;

import java.util.Objects;

public class SubcircuitInstanceProperties
    extends ComponentProperties
{
  public String subcircuitTypeName;
  public String comment;
  public int width;
  public int height;

  public SubcircuitInstanceProperties()
  {
  }

  public SubcircuitInstanceProperties(String name,
                                      String subcircuitTypeName,
                                      String comment,
                                      int width,
                                      int height)
  {
    super(name);
    this.subcircuitTypeName = subcircuitTypeName;
    this.comment = comment;
    this.width = width;
    this.height = height;
  }

  @Override
  public ComponentProperties duplicate()
  {
    return new SubcircuitInstanceProperties(name,
                                            subcircuitTypeName,
                                            comment,
                                            width,
                                            height);
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (o == null || getClass() != o.getClass())
    {
      return false;
    }
    SubcircuitInstanceProperties other = (SubcircuitInstanceProperties) o;
    return width == other.width &&
           height == other.height &&
           Objects.equals(subcircuitTypeName, other.subcircuitTypeName) &&
           Objects.equals(comment, other.comment);
  }
}

