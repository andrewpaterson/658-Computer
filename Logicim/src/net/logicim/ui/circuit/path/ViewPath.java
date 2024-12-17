package net.logicim.ui.circuit.path;

import net.common.SimulatorException;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.circuit.CircuitInstanceView;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.simulation.component.common.InstanceView;

import java.util.*;

public class ViewPath
    implements Comparable<ViewPath>
{
  public static long nextId = 1L;

  protected List<CircuitInstanceView> path;
  protected Map<CircuitSimulation, SubcircuitSimulation> circuitSimulations;
  protected long id;

  protected ViewPath previous;

  public ViewPath(List<CircuitInstanceView> path)
  {
    this(nextId++, path, null);
  }

  public ViewPath(long id, List<CircuitInstanceView> path)
  {
    this(id, path, new LinkedHashMap<>());
  }

  protected ViewPath(long id,
                     List<CircuitInstanceView> path,
                     Map<CircuitSimulation, SubcircuitSimulation> circuitSimulations)
  {
    this.path = new ArrayList<>(path);
    this.circuitSimulations = circuitSimulations;
    this.id = id;
    this.previous = null;

    if (id >= nextId)
    {
      nextId = id + 1;
    }
  }

  public static void resetNextId()
  {
    nextId = 1;
  }

  public static String toPathString(List<CircuitInstanceView> path)
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
    if (!(obj instanceof ViewPath))
    {
      return false;
    }
    if (obj == this)
    {
      return true;
    }

    return equalsPath(((ViewPath) obj).getPath());
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
    return toPathString(path);
  }

  public CircuitInstanceView getLast()
  {
    return path.get(path.size() - 1);
  }

  public CircuitInstanceView getFirst()
  {
    return path.get(0);
  }

  public ViewPath getPrevious()
  {
    return previous;
  }

  public void setPrevious(ViewPath viewPath)
  {
    if ((previous == null) || (previous == viewPath))
    {
      previous = viewPath;
    }
    else
    {
      throw new SimulatorException("Previous already set on Path [%s].", getDescription());
    }
  }

  public void clearPrevious()
  {
    previous = null;
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

  public SubcircuitSimulation getSubcircuitSimulationOrNull(CircuitSimulation circuitSimulation)
  {
    return circuitSimulations.get(circuitSimulation);
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
  public int compareTo(ViewPath other)
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

  public List<SubcircuitSimulation> getSubcircuitSimulations()
  {
    return new ArrayList<>(circuitSimulations.values());
  }

  public List<CircuitSimulation> getCircuitSimulation()
  {
    return new ArrayList<>(circuitSimulations.keySet());
  }

  public long getId()
  {
    return id;
  }

  public Set<Map.Entry<CircuitSimulation, SubcircuitSimulation>> getCircuitSimulations()
  {
    return circuitSimulations.entrySet();
  }

  public void putDuringLoad(CircuitSimulation circuitSimulation, SubcircuitSimulation subcircuitSimulation)
  {
    SubcircuitSimulation existing = circuitSimulations.put(circuitSimulation, subcircuitSimulation);
    if (existing != null)
    {
      throw new SimulatorException("CircuitSimulation [%s] already set on Path [%s].", circuitSimulation.getDescription(), this.getDescription());
    }
  }
}

