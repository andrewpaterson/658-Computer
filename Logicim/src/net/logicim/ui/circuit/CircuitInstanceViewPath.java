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

  public CircuitInstanceViewPath(List<CircuitInstanceView> path)
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

  public CircuitInstanceViewPath getPrevious()
  {
    return previous;
  }

  public void setPrevious(CircuitInstanceViewPath path)
  {
    if ((previous == null) || (previous == path))
    {
      previous = path;
    }
    else
    {
      throw new SimulatorException("Previous already set on Path [%s].", getDescription());
    }
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
      throw new SimulatorException("SubcircuitSimulation already set for circuit CircuitSimulation [%s] on Path [%s].", circuitSimulation.getDescription(), getDescription());
    }
    circuitSimulations.put(circuitSimulation, subcircuitSimulation);
  }

  public boolean containsCircuitSimulation(CircuitSimulation circuitSimulation)
  {
    return circuitSimulations.containsKey(circuitSimulation);
  }

  public SubcircuitSimulation getSubcircuitSimulation(CircuitSimulation circuitSimulation)
  {
    SubcircuitSimulation subcircuitSimulation = circuitSimulations.get(circuitSimulation);
    if (subcircuitSimulation == null)
    {
      throw new SimulatorException("Path [%s] does not contain CircuitSimulation [%s].", getDescription(), circuitSimulation.getDescription());
    }
    return subcircuitSimulation;
  }

  public String getDescription()
  {
    StringBuilder builder = new StringBuilder();
    boolean first = true;
    for (CircuitInstanceView circuitInstanceView : path)
    {
      if (first)
      {
        first = false;
      }
      else
      {
        builder.append(" -> ");
      }
      builder.append(circuitInstanceView.getDescription());
    }
    return builder.toString();
  }

  public boolean containsSubcircuitView(SubcircuitView subcircuitView)
  {
    for (CircuitInstanceView circuitInstanceView : path)
    {
      if (circuitInstanceView.getInstanceSubcircuitView() == subcircuitView)
      {
        return true;
      }
    }
    return false;
  }

  public boolean endsWithSubcircuitView(SubcircuitView subcircuitView)
  {
    CircuitInstanceView pathInstanceView = path.get(path.size() - 1);
    if (pathInstanceView.getInstanceSubcircuitView() == subcircuitView)
    {
      return true;
    }
    return false;
  }
}

