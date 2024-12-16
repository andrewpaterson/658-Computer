package net.logicim.domain.common.wire;

import net.logicim.domain.common.CircuitElement;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;

import java.util.List;

public class Traces
    implements CircuitElement
{
  protected List<Trace> traces;
  protected SubcircuitSimulation containingSubcircuitSimulation;

  public Traces(List<Trace> traces, SubcircuitSimulation containingSubcircuitSimulation)
  {
    this.containingSubcircuitSimulation = containingSubcircuitSimulation;
    this.traces = traces;
  }

  public int size()
  {
    return traces.size();
  }

  public List<Trace> getTraces()
  {
    return traces;
  }

  @Override
  public String getDescription()
  {
    return "Wire";
  }

  @Override
  public String getType()
  {
    return "Wire";
  }

  @Override
  public SubcircuitSimulation getContainingSubcircuitSimulation()
  {
    return containingSubcircuitSimulation;
  }
}

