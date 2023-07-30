package net.logicim.ui.circuit;

import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;

import java.util.List;

public interface SubcircuitInstanceViewFinder
{
  List<SubcircuitInstanceView> getSubcircuitInstanceViews(SubcircuitView subcircuitView);
}
