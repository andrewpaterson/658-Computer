package net.logicim.ui.subcircuit;

import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;

import javax.swing.*;
import java.util.List;

public class SimulationListModel
    extends AbstractListModel<SubcircuitSimulation>
{
  protected SimulationListHolder simulationListHolder;

  public SimulationListModel(SimulationListHolder simulationListHolder)
  {
    this.simulationListHolder = simulationListHolder;
  }

  @Override
  public int getSize()
  {
    return simulationListHolder.getSubcircuitSimulations().size();
  }

  @Override
  public SubcircuitSimulation getElementAt(int index)
  {
    return simulationListHolder.getSubcircuitSimulations().get(index);
  }

  @Override
  protected void fireContentsChanged(Object source, int index0, int index1)
  {
    super.fireContentsChanged(source, index0, index1);
  }

  public int getSimulationIndex()
  {
    List<? extends SubcircuitSimulation> subcircuitSimulationList = simulationListHolder.getSubcircuitSimulations();
    SubcircuitSimulation subcircuitSimulation = simulationListHolder.getCurrentSimulation();
    return subcircuitSimulationList.indexOf(subcircuitSimulation);
  }
}

