package net.logicim.ui.subcircuit;

import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

import java.util.List;

public interface SubcircuitEditorListHolder
{
  SubcircuitEditorList getSubcircuitEditorList();

  List<SubcircuitEditor> getSubcircuitEditors();
}
