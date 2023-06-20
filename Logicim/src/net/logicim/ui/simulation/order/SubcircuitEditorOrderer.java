package net.logicim.ui.simulation.order;

import net.logicim.common.dependency.Orderer;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

public class SubcircuitEditorOrderer
{
  protected LinkedHashMap<SubcircuitEditor, List<String>> fulfillmentsMap;
  protected LinkedHashMap<SubcircuitEditor, List<String>> requirementsMap;

  public SubcircuitEditorOrderer(Collection<SubcircuitEditor> subcircuitEditors)
  {
    fulfillmentsMap = new LinkedHashMap<>();
    requirementsMap = new LinkedHashMap<>();
    for (SubcircuitEditor subcircuitEditor : subcircuitEditors)
    {
      addFulfilment(subcircuitEditor);
      addRequirement(subcircuitEditor);
    }
  }

  protected void addFulfilment(SubcircuitEditor subcircuitEditor)
  {
    fulfillmentsMap.put(subcircuitEditor, subcircuitEditor.getTypeNameAsList());
  }

  protected void addRequirement(SubcircuitEditor subcircuitEditor)
  {
    requirementsMap.put(subcircuitEditor, subcircuitEditor.getSubcircuitInstanceNames());
  }

  public void addRequirement(SubcircuitEditor subcircuitEditor, String subcircuitTypeName)
  {
    List<String> strings = requirementsMap.get(subcircuitEditor);
    if (strings == null)
    {
      strings = new ArrayList<>();
      requirementsMap.put(subcircuitEditor, strings);
    }
    if (!strings.contains(subcircuitTypeName))
    {
      strings.add(subcircuitTypeName);
    }
  }

  public List<SubcircuitEditor> order()
  {
    return Orderer.order(fulfillmentsMap, requirementsMap);
  }
}

