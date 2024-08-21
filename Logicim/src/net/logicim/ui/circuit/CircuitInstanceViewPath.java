package net.logicim.ui.circuit;

import net.common.SimulatorException;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CircuitInstanceViewPath
{
  protected List<CircuitInstanceView> path;
  protected Map<CircuitSimulation, SubcircuitSimulation> circuitSimulations;

  protected CircuitInstanceViewPath previous;
  protected CircuitInstanceViewPath next;

  public CircuitInstanceViewPath(List<CircuitInstanceView> path, List<CircuitSimulation> circuitSimulations)
  {
    this.path = new ArrayList<>(path);
    this.circuitSimulations = new LinkedHashMap<>();
  }

  public boolean equalsPath(List<CircuitInstanceView> path)
  {
    if (this.path.size() != path.size())
    {
      return false;
    }
    else
    {
      for (int i = 0; i < path.size(); i++)
      {
        if (this.path.get(i) != path.get(i))
        {
          return false;
        }
      }
      return true;
    }
  }

  public List<CircuitInstanceView> getPath()
  {
    return path;
  }

  public int size()
  {
    return path.size();
  }

  @Override
  public String toString()
  {
    StringBuilder builder = new StringBuilder();
    boolean first = true;
    for (CircuitInstanceView circuitInstanceView : path)
    {
      if (!first)
      {
        builder.append(" - ");
      }
      else
      {
        first = false;
      }
      builder.append(circuitInstanceView.getDescription());
    }
    return builder.toString();
  }

  public CircuitInstanceView getLast()
  {
    return path.get(path.size() - 1);
  }

  public CircuitInstanceView getSecondLast()
  {
    if (path.size() > 1)
    {
      return path.get(path.size() - 2);
    }
    else
    {
      return null;
    }
  }

  public void setPrevious(CircuitInstanceViewPath path)
  {
    if ((previous == null) || (previous == path))
    {
      previous = path;
    }
    else
    {
      throw new SimulatorException("Previous path already set.");
    }
  }

  public CircuitInstanceViewPath getPrevious()
  {
    return previous;
  }

  public void setNext(CircuitInstanceViewPath next)
  {
    this.next = next;
  }

  public void addSubcircuitSimulation(SubcircuitSimulation subcircuitSimulation)
  {
    CircuitSimulation circuitSimulation = subcircuitSimulation.getCircuitSimulation();
    SubcircuitSimulation existing = circuitSimulations.get(circuitSimulation);
    if (existing != null)
    {
      throw new SimulatorException("SubcircuitSimulation already set for circuit CircuitSimulation.");
    }
    circuitSimulations.put(circuitSimulation, subcircuitSimulation);
  }

  public SubcircuitSimulation getSubcircuitSimulation(CircuitSimulation circuitSimulation)
  {
    return circuitSimulations.get(circuitSimulation);
  }
}

