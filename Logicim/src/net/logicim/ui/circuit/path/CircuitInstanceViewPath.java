package net.logicim.ui.circuit.path;

import net.common.SimulatorException;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.circuit.CircuitInstanceView;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.simulation.component.common.InstanceView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CircuitInstanceViewPath
    implements Comparable<CircuitInstanceViewPath>
{
  protected List<CircuitInstanceView> path;
  protected Map<CircuitSimulation, SubcircuitSimulation> circuitSimulations;

  protected CircuitInstanceViewPath previous;
  protected CircuitInstanceViewPath next;

  public CircuitInstanceViewPath(List<CircuitInstanceView> path)
  {
    this.path = new ArrayList<>(path);
    this.circuitSimulations = null;
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

  @Override
  public boolean equals(Object obj)
  {
    if (!(obj instanceof CircuitInstanceViewPath))
    {
      return false;
    }
    if (obj == this)
    {
      return true;
    }

    return equalsPath(((CircuitInstanceViewPath) obj).getPath());
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

  public CircuitInstanceView getFirst()
  {
    return path.get(0);
  }

  public CircuitInstanceViewPath getPrevious()
  {
    return previous;
  }

  public void clearPrevious()
  {
    previous = null;
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

  public Map<CircuitSimulation, SubcircuitSimulation> getCircuitSimulations()
  {
    return circuitSimulations;
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

  public boolean endsWithSubcircuitView(SubcircuitView subcircuitView)
  {
    CircuitInstanceView pathInstanceView = path.get(path.size() - 1);
    if (pathInstanceView.getInstanceSubcircuitView() == subcircuitView)
    {
      return true;
    }
    return false;
  }

  @Override
  public int compareTo(CircuitInstanceViewPath other)
  {
    int result = Integer.compare(path.size(), other.path.size());
    if (result != 0)
    {
      return result;
    }

    int length = path.size();
    for (int i = 0; i < length; i++)
    {
      InstanceView circuitInstanceViewThis = (InstanceView) path.get(i);
      InstanceView circuitInstanceViewOther = (InstanceView) other.path.get(i);
      result = circuitInstanceViewThis.compareTo(circuitInstanceViewOther);
      if (result != 0)
      {
        return result;
      }
    }
    return 0;
  }

  public void clearCircuitSimulations()
  {
    this.circuitSimulations = new LinkedHashMap<>();
  }

  public CircuitInstanceViewPath getNext()
  {
    return next;
  }
}
