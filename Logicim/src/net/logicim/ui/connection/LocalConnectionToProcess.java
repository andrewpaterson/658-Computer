package net.logicim.ui.connection;

import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.common.ConnectionView;

public class LocalConnectionToProcess
{
  public SubcircuitSimulation subcircuitSimulation;
  public ConnectionView inputConnectionView;

  public LocalConnectionToProcess(SubcircuitSimulation subcircuitSimulation, ConnectionView inputConnectionView)
  {
    this.subcircuitSimulation = subcircuitSimulation;
    this.inputConnectionView = inputConnectionView;
  }
}

