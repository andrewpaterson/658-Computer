package net.logicim.ui.connection;

import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.circuit.CircuitInstanceView;
import net.logicim.ui.common.ConnectionView;

public class LocalConnectionToProcess
{
  public CircuitInstanceView circuitInstanceView;
  public SubcircuitSimulation subcircuitSimulation;
  public ConnectionView inputConnectionView;

  public LocalConnectionToProcess(CircuitInstanceView circuitInstanceView, SubcircuitSimulation subcircuitSimulation, ConnectionView inputConnectionView)
  {
    this.circuitInstanceView = circuitInstanceView;
    this.subcircuitSimulation = subcircuitSimulation;
    this.inputConnectionView = inputConnectionView;
  }
}

