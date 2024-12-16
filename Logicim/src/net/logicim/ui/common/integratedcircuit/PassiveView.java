package net.logicim.ui.common.integratedcircuit;

import net.common.SimulatorException;
import net.common.type.Int2D;
import net.logicim.data.common.properties.ComponentProperties;
import net.logicim.data.integratedcircuit.common.PassiveData;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.passive.common.Passive;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.circuit.path.ViewPath;
import net.logicim.ui.circuit.path.ViewPathComponentSimulation;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.DebugGlobalEnvironment;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public abstract class PassiveView<PASSIVE extends Passive, PROPERTIES extends ComponentProperties>
    extends ComponentView<PROPERTIES>
{
  protected ViewPathComponentSimulation<PASSIVE> simulationPassives;

  public PassiveView(SubcircuitView containingSubcircuitView,
                     Int2D position,
                     Rotation rotation,
                     PROPERTIES properties)
  {
    super(containingSubcircuitView,
          position,
          rotation,
          properties);
    this.simulationPassives = new ViewPathComponentSimulation<>();
  }

  @Override
  public PASSIVE createComponent(ViewPath path, CircuitSimulation circuitSimulation)
  {
    DebugGlobalEnvironment.validateCanCreateComponent();
    validateCanCreateComponent(path, circuitSimulation);

    PASSIVE passive = createPassive(path, circuitSimulation);
    simulationPassives.put(path, circuitSimulation, passive);

    postCreateComponent(path, circuitSimulation, passive);
    return passive;
  }

  @Override
  public void destroyComponent(ViewPath path, CircuitSimulation circuitSimulation)
  {
    PASSIVE removedPassive = simulationPassives.get(path, circuitSimulation);
    if (removedPassive == null)
    {
      throw new SimulatorException("[%s] could not find a component for Path [%s] for Simulation [%s].",
                                   getDescription(),
                                   path.getDescription(),
                                   circuitSimulation.getDescription());
    }
    destroyPortViewComponents(path, circuitSimulation);
    Circuit circuit = circuitSimulation.getCircuit();
    circuit.remove(removedPassive);
    simulationPassives.remove(path, circuitSimulation);
  }

  @Override
  public void destroyAllComponents()
  {
    for (Map.Entry<ViewPath, Map<CircuitSimulation, PASSIVE>> pathEntry : simulationPassives.getEntrySet())
    {
      Map<CircuitSimulation, PASSIVE> simulationMap = pathEntry.getValue();
      Set<Map.Entry<CircuitSimulation, PASSIVE>> simulationEntry = simulationMap.entrySet();
      for (Map.Entry<CircuitSimulation, PASSIVE> entry : simulationEntry)
      {
        CircuitSimulation circuitSimulation = entry.getKey();
        PASSIVE passive = entry.getValue();
        Circuit circuit = circuitSimulation.getCircuit();
        circuit.remove(passive);
      }
    }
    simulationPassives.clear();
  }

  @Override
  protected void finaliseView()
  {
    super.finaliseView();
    containingSubcircuitView.addPassiveView(this);
  }

  @Override
  public ViewPathComponentSimulation<PASSIVE> getViewPathComponentSimulation()
  {
    return simulationPassives;
  }

  @Override
  public String getComponentType()
  {
    if (simulationPassives != null)
    {
      return simulationPassives.getComponentType();
    }
    return "";
  }

  @Override
  public Set<? extends SubcircuitSimulation> getComponentSubcircuitSimulations()
  {
    return simulationPassives.getSimulations();
  }

  @Override
  public PASSIVE getComponent(ViewPath path, CircuitSimulation circuitSimulation)
  {
    return simulationPassives.get(path, circuitSimulation);
  }

  protected Set<Long> saveSimulationPassives()
  {
    LinkedHashSet<Long> simulationIDs = new LinkedHashSet<>();
    Set<? extends SubcircuitSimulation> simulations = simulationPassives.getSimulations();
    for (SubcircuitSimulation subcircuitSimulation : simulations)
    {
      simulationIDs.add(subcircuitSimulation.getId());
    }
    return simulationIDs;
  }

  @Override
  public String toDebugString()
  {
    return super.toDebugString() + String.format("Name [%s]\n", properties.name) + toSimulationsDebugString(simulationPassives.getSimulations());
  }

  public abstract PassiveData<?> save(boolean selected);

  protected abstract PASSIVE createPassive(ViewPath path, CircuitSimulation circuitSimulation);
}

