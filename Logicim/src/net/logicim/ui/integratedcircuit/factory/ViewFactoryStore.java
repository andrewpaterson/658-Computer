package net.logicim.ui.integratedcircuit.factory;

import net.logicim.ui.common.integratedcircuit.DiscreteView;

import java.util.LinkedHashMap;
import java.util.Map;

public class ViewFactoryStore
{
  protected Map<Class<? extends DiscreteView<?>>, ViewFactory> factories;

  protected static ViewFactoryStore instance;

  public ViewFactoryStore()
  {
    this.factories = new LinkedHashMap<>();
  }

  public static ViewFactoryStore getInstance()
  {
    if (instance == null)
    {
      instance = new ViewFactoryStore();
    }
    return instance;
  }

  public void add(ViewFactory viewFactory)
  {
    factories.put(viewFactory.getViewClass(), viewFactory);
  }

  public void addAll(ViewFactory... viewFactories)
  {
    for (ViewFactory viewFactory : viewFactories)
    {
      add(viewFactory);
    }
  }

  public ViewFactory get(Class<? extends DiscreteView<?>> discreteViewClass)
  {
    return factories.get(discreteViewClass);
  }
}
