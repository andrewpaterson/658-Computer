package net.logicim.ui.circuit;

import net.common.dependency.Orderer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static net.common.util.CollectionUtil.newList;

public class CircuitInstanceOrderer
{
  protected LinkedHashMap<CircuitInstanceViewParent, List<String>> fulfillmentsMap;
  protected LinkedHashMap<CircuitInstanceViewParent, List<String>> requirementsMap;

  public CircuitInstanceOrderer(List<CircuitInstanceViewParent> viewParents)
  {
    fulfillmentsMap = new LinkedHashMap<>();
    requirementsMap = new LinkedHashMap<>();

    for (CircuitInstanceViewParent circuitInstanceViewParent : viewParents)
    {
      fulfillmentsMap.put(circuitInstanceViewParent, newList(circuitInstanceViewParent.getId()));
    }

    for (CircuitInstanceViewParent circuitInstanceViewParent : viewParents)
    {
      List<String> strings = requirementsMap.get(circuitInstanceViewParent);
      if (strings == null)
      {
        strings = new ArrayList<>();
        requirementsMap.put(circuitInstanceViewParent, strings);
      }
      CircuitInstanceViewParent parent = circuitInstanceViewParent.getParent();
      if (parent != null)
      {
        strings.add(parent.id);
      }
    }
  }

  public List<CircuitInstanceViewParent> order()
  {
    return Orderer.order(fulfillmentsMap, requirementsMap);
  }
}

