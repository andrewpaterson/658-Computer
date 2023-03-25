package net.logicim.ui.simulation.component.passive.pin;

import net.logicim.data.circuit.SubcircuitPinAlignment;
import net.logicim.data.circuit.SubcircuitPinAnchour;
import net.logicim.data.passive.wire.PinProperties;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PinPropertyHelper
{
  private List<PinView> subCircuitPins;

  public PinPropertyHelper(List<PinView> subCircuitPins)
  {
    this.subCircuitPins = subCircuitPins;
  }

  public void updatePinProperties(PinProperties properties)
  {
    updatePinWeight(properties);
    updatePinName(properties);
  }

  protected void updatePinWeight(PinProperties properties)
  {
    Map<SubcircuitPinAlignment, Map<SubcircuitPinAnchour, List<PinView>>> alignmentMap = groupPinsByLocation();
    Map<SubcircuitPinAnchour, List<PinView>> anchourMap = alignmentMap.get(properties.alignment);
    if (anchourMap != null)
    {
      List<PinView> similarPinViews = anchourMap.get(properties.anchour);
      if (similarPinViews != null)
      {
        int maxWeight = -1;
        for (PinView similarPinView : similarPinViews)
        {
          int weight = similarPinView.getProperties().weight;
          if (weight > maxWeight)
          {
            maxWeight = weight;
          }
        }
        properties.weight = maxWeight + 1;
      }
    }
  }

  protected void updatePinName(PinProperties properties)
  {
    Map<String, List<PinViewNumber>> nameMap = groupPinsByName();
    PinViewNumber pinViewNumber = createPinViewNumber(null, properties.name);
    Integer number = pinViewNumber.number;
    String name = pinViewNumber.name;

    List<PinViewNumber> pinViewNumbers = nameMap.get(name);
    Integer maxNumber = null;
    boolean exactNumber = false;
    if (pinViewNumbers != null)
    {
      for (PinViewNumber viewNumber : pinViewNumbers)
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
        properties.name = name + " " + (maxNumber + 1);
      }
    }
  }

  private Map<String, List<PinViewNumber>> groupPinsByName()
  {
    LinkedHashMap<String, List<PinViewNumber>> pinNamesMap = new LinkedHashMap<>();
    for (PinView pinView : subCircuitPins)
    {
      PinViewNumber pinViewNumber = createPinViewNumber(pinView, pinView.getProperties().name);

      String name = pinViewNumber.name;
      List<PinViewNumber> pinViews = pinNamesMap.get(name);
      if (pinViews == null)
      {
        pinViews = new ArrayList<>();
        pinNamesMap.put(name, pinViews);
      }
      pinViews.add(pinViewNumber);
    }
    return pinNamesMap;
  }

  private PinViewNumber createPinViewNumber(PinView pinView, String propertiesName)
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
    return new PinViewNumber(pinView, name, parseInt);
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

  protected Map<SubcircuitPinAlignment, Map<SubcircuitPinAnchour, List<PinView>>> groupPinsByLocation()
  {
    Map<SubcircuitPinAlignment, Map<SubcircuitPinAnchour, List<PinView>>> alignmentMap = new LinkedHashMap<>();
    for (PinView pinView : subCircuitPins)
    {
      SubcircuitPinAlignment alignment = pinView.getProperties().alignment;
      Map<SubcircuitPinAnchour, List<PinView>> anchourMap = alignmentMap.get(alignment);
      if (anchourMap == null)
      {
        anchourMap = new LinkedHashMap<>();
        alignmentMap.put(alignment, anchourMap);
      }

      SubcircuitPinAnchour anchour = pinView.getProperties().anchour;
      List<PinView> pinViews = anchourMap.get(anchour);
      if (pinViews == null)
      {
        pinViews = new ArrayList<>();
        anchourMap.put(anchour, pinViews);
      }

      pinViews.add(pinView);
    }
    return alignmentMap;
  }
}

