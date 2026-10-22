package net.logicim.ui.simulation.component.passive.pin;

import net.logicim.ui.common.integratedcircuit.StaticView;

import java.util.*;

public class ComponentNameHelper
{
  public String getUniqueName(Set<StaticView<?>> views, String propertiesName)
  {
    Map<String, List<StaticViewNumber>> nameMap = groupPinsByName(views);
    StaticViewNumber staticViewNumber = createStaticViewNumber(null, propertiesName);
    Integer number = staticViewNumber.number;
    String name = staticViewNumber.name;

    List<StaticViewNumber> staticViewNumbers = nameMap.get(name);
    Integer maxNumber = null;
    boolean exactNumber = false;
    if (staticViewNumbers != null)
    {
      for (StaticViewNumber viewNumber : staticViewNumbers)
      {
        if (viewNumber.number.equals(number))
        {
          exactNumber = true;
        }
        if (maxNumber == null)
        {
          maxNumber = viewNumber.number;
        }
        if (viewNumber.number > maxNumber)
        {
          maxNumber = viewNumber.number;
        }
      }
      if (maxNumber == null)
      {
        maxNumber = 0;
      }
      if (exactNumber || maxNumber == 0)
      {
        propertiesName = name + " " + (maxNumber + 1);
      }
    }
    return propertiesName;
  }

  private Map<String, List<StaticViewNumber>> groupPinsByName(Set<StaticView<?>> views)
  {
    LinkedHashMap<String, List<StaticViewNumber>> pinNamesMap = new LinkedHashMap<>();
    for (StaticView<?> staticView : views)
    {
      StaticViewNumber staticViewNumber = createStaticViewNumber(staticView, staticView.getProperties().name);

      String name = staticViewNumber.name;
      List<StaticViewNumber> staticViews = pinNamesMap.get(name);
      if (staticViews == null)
      {
        staticViews = new ArrayList<>();
        pinNamesMap.put(name, staticViews);
      }
      staticViews.add(staticViewNumber);
    }
    return pinNamesMap;
  }

  private StaticViewNumber createStaticViewNumber(StaticView<?> staticView, String propertiesName)
  {
    String name = propertiesName;
    name = stripNumber(name);
    String number = propertiesName.substring(name.length()).trim();
    int parseInt;
    try
    {
      parseInt = Integer.parseInt(number);
    }
    catch (NumberFormatException ignored)
    {
      parseInt = 0;
    }
    return new StaticViewNumber(staticView, name, parseInt);
  }

  private String stripNumber(String name)
  {
    name = name.trim();
    int index;
    for (index = name.length() - 1; index >= 0; index--)
    {
      char c = name.charAt(index);
      if (!(c >= '0' && c <= '9'))
      {
        break;
      }
    }

    if (name.length() - 1 == index)
    {
      return name;
    }
    else
    {
      return name.substring(0, index + 1).trim();
    }
  }
}

