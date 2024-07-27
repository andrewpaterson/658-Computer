package net.logicim.ui.property;

import net.logicim.data.common.properties.ComponentProperties;
import net.logicim.ui.common.integratedcircuit.StaticView;

import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultComponentProperties
{
  private static DefaultComponentProperties instance;

  protected Map<Class<? extends StaticView<?>>, ComponentProperties> properties;

  public static DefaultComponentProperties getInstance()
  {
    if (instance == null)
    {
      instance = new DefaultComponentProperties();
    }
    return instance;
  }

  public DefaultComponentProperties()
  {
    properties = new LinkedHashMap<>();
  }

  public static <T extends ComponentProperties> T get(Class<? extends StaticView<T>> aClass)
  {
    return (T) getInstance().getProperties(aClass);
  }

  private ComponentProperties getProperties(Class<? extends StaticView<?>> aClass)
  {
    return properties.get(aClass);
  }

  public void put(Class<? extends StaticView<?>> componentViewClass, ComponentProperties componentProperties)
  {
    properties.put(componentViewClass, componentProperties);
  }

  public Map<Class<? extends StaticView<?>>, ComponentProperties> findAll()
  {
    return properties;
  }
}

