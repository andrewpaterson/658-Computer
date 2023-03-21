package net.logicim.data.editor;

import net.logicim.data.common.ReflectiveData;
import net.logicim.data.common.properties.ComponentProperties;

public class DefaultComponentPropertiesData
    extends ReflectiveData
{
  public String propertiesClassName;
  public ComponentProperties properties;

  public DefaultComponentPropertiesData()
  {
  }

  public DefaultComponentPropertiesData(String propertiesClassName, ComponentProperties properties)
  {
    this.propertiesClassName = propertiesClassName;
    this.properties = properties;
  }
}

