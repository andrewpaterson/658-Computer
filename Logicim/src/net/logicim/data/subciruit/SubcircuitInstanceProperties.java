package net.logicim.data.subciruit;

import net.logicim.data.common.properties.ComponentProperties;

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
    if (!(o instanceof SubcircuitInstanceProperties))
    {
      return false;
    }
    if (!super.equals(o))
    {
      return false;
    }
    SubcircuitInstanceProperties that = (SubcircuitInstanceProperties) o;
    return width == that.width &&
           height == that.height &&
           Objects.equals(subcircuitTypeName, that.subcircuitTypeName) &&
           Objects.equals(comment, that.comment);
  }
}

