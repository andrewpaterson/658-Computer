package net.logicim.ui.common.integratedcircuit;

import net.common.SimulatorException;
import net.common.collection.linkedlist.LinkedList;
import net.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.IntegratedCircuitData;
import net.logicim.data.integratedcircuit.common.IntegratedCircuitProperties;
import net.logicim.data.integratedcircuit.event.IntegratedCircuitEventData;
import net.logicim.data.integratedcircuit.event.MultiIntegratedCircuitEventData;
import net.logicim.data.integratedcircuit.event.SimulationIntegratedCircuitEventData;
import net.logicim.data.simulation.SimulationStateData;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.defaults.DefaultLogicLevels;
import net.logicim.domain.common.event.IntegratedCircuitEvent;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.common.propagation.FamilyVoltageConfigurationStore;
import net.logicim.domain.common.propagation.VoltageConfiguration;
import net.logicim.domain.common.state.State;
import net.logicim.domain.common.wire.Trace;
import net.logicim.domain.passive.power.PowerSource;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.circuit.path.ViewPath;
import net.logicim.ui.circuit.path.ViewPathComponentSimulation;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.simulation.DebugGlobalEnvironment;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class IntegratedCircuitView<IC extends IntegratedCircuit<?, ?>, PROPERTIES extends IntegratedCircuitProperties>
    extends ComponentView<PROPERTIES>
{
  protected ViewPathComponentSimulation<IC> simulationIntegratedCircuits;

  public IntegratedCircuitView(SubcircuitView containingSubcircuitView,
                               Int2D position,
                               Rotation rotation,
                               PROPERTIES properties)
  {
    super(containingSubcircuitView,
          position,
          rotation,
          properties);
    if (properties.family == null)
    {
      throw new SimulatorException("Family may not be null on IC [%s].", getDescription());
    }

    simulationIntegratedCircuits = new ViewPathComponentSimulation<>();
  }

  public IC createComponent(ViewPath path, CircuitSimulation circuitSimulation)
  {
    DebugGlobalEnvironment.validateCanCreateComponent();
    validateCanCreateComponent(path, circuitSimulation);

    FamilyVoltageConfiguration familyVoltageConfiguration = FamilyVoltageConfigurationStore.get(properties.family);
    IC integratedCircuit = createIntegratedCircuit(path, circuitSimulation, familyVoltageConfiguration);
    simulationIntegratedCircuits.put(path, circuitSimulation, integratedCircuit);

    createPowerPortsIfNecessary(path, circuitSimulation, familyVoltageConfiguration);

    postCreateComponent(path, circuitSimulation, integratedCircuit);
    return integratedCircuit;
  }

  @Override
  public void destroyComponent(ViewPath path, CircuitSimulation circuitSimulation)
  {
    IC removedIntegratedCircuit = simulationIntegratedCircuits.get(path, circuitSimulation);
    if (removedIntegratedCircuit == null)
    {
      throw new SimulatorException("[%s] could not find a component for Path [%s] for Simulation [%s].",
                                   getDescription(),
                                   path.getDescription(),
                                   circuitSimulation.getDescription());
    }
    destroyPortViewComponents(path, circuitSimulation);
    Circuit circuit = circuitSimulation.getCircuit();
    circuit.remove(removedIntegratedCircuit);
    simulationIntegratedCircuits.remove(path, circuitSimulation);
  }

  @Override
  public void destroyAllComponents()
  {
    for (Map.Entry<ViewPath, Map<CircuitSimulation, IC>> pathEntry : simulationIntegratedCircuits.getEntrySet())
    {
      Map<CircuitSimulation, IC> simulationMap = pathEntry.getValue();
      Set<Map.Entry<CircuitSimulation, IC>> simulationEntry = simulationMap.entrySet();
      for (Map.Entry<CircuitSimulation, IC> entry : simulationEntry)
      {
        CircuitSimulation circuitSimulation = entry.getKey();
        IC integratedCircuit = entry.getValue();
        Circuit circuit = circuitSimulation.getCircuit();
        circuit.remove(integratedCircuit);
      }
    }

    simulationIntegratedCircuits.clear();
  }

  @Override
  public void paint(Graphics2D graphics,
                    Viewport viewport,
                    ViewPath path,
                    CircuitSimulation circuitSimulation)
  {
    super.paint(graphics,
                viewport,
                path,
                circuitSimulation);
  }

  @Override
  protected void finaliseView()
  {
    super.finaliseView();
    containingSubcircuitView.addIntegratedCircuitView(this);
  }

  @Override
  public ViewPathComponentSimulation<IC> getViewPathComponentSimulation()
  {
    return simulationIntegratedCircuits;
  }

  protected void createPowerPortsIfNecessary(ViewPath path, CircuitSimulation circuitSimulation, FamilyVoltageConfiguration familyVoltageConfiguration)
  {
    if (!mustIncludeExplicitPowerPorts(familyVoltageConfiguration))
    {
      createPowerPorts(path, circuitSimulation, familyVoltageConfiguration);
    }
  }

  protected void createPowerPorts(ViewPath path, CircuitSimulation circuitSimulation, FamilyVoltageConfiguration familyVoltageConfiguration)
  {
    SubcircuitSimulation containingSubcircuitSimulation = path.getSubcircuitSimulation(circuitSimulation);
    VoltageConfiguration voltageConfiguration = familyVoltageConfiguration.getDefaultVoltageConfiguration(DefaultLogicLevels.get());
    IC integratedCircuit = getComponent(path, circuitSimulation);
    if (integratedCircuit != null)
    {
      Trace vccTrace = new Trace();
      integratedCircuit.getPins().getVoltageCommon().connect(vccTrace, false);

      PowerSource vccPowerSource = new PowerSource(containingSubcircuitSimulation, "", voltageConfiguration.getVcc());
      vccPowerSource.getPowerOutPort().connect(vccTrace, false);

      Trace gndTrace = new Trace();
      integratedCircuit.getPins().getVoltageGround().connect(gndTrace, false);

      PowerSource gndPowerSource = new PowerSource(containingSubcircuitSimulation, "", 0);
      gndPowerSource.getPowerOutPort().connect(gndTrace, false);
    }
  }

  protected SimulationIntegratedCircuitEventData saveEvents()
  {
    SimulationIntegratedCircuitEventData simulationEventData = new SimulationIntegratedCircuitEventData();
    for (Map.Entry<ViewPath, Map<CircuitSimulation, IC>> pathEntry : simulationIntegratedCircuits.getEntrySet())
    {
      Map<CircuitSimulation, IC> simulationMap = pathEntry.getValue();
      ViewPath path = pathEntry.getKey();
      for (Map.Entry<CircuitSimulation, IC> simulationEntry : simulationMap.entrySet())
      {
        CircuitSimulation circuitSimulation = simulationEntry.getKey();
        IC integratedCircuit = simulationEntry.getValue();
        SubcircuitSimulation subcircuitSimulation = path.getSubcircuitSimulation(circuitSimulation);
        List<IntegratedCircuitEventData<?>> eventDatas = saveEvents(integratedCircuit);
        simulationEventData.add(subcircuitSimulation.getId(), new MultiIntegratedCircuitEventData(eventDatas));
      }
    }

    return simulationEventData;
  }

  private List<IntegratedCircuitEventData<?>> saveEvents(IC integratedCircuit)
  {
    LinkedList<IntegratedCircuitEvent> integratedCircuitEvents = integratedCircuit.getEvents();
    List<IntegratedCircuitEventData<?>> eventDatas = new ArrayList<>(integratedCircuitEvents.size());
    for (IntegratedCircuitEvent event : integratedCircuitEvents)
    {
      IntegratedCircuitEventData<?> integratedCircuitEventData = event.save();
      eventDatas.add(integratedCircuitEventData);
    }
    return eventDatas;
  }

  protected <STATE extends State> STATE saveState(IC integratedCircuit)
  {
    if (integratedCircuit.isStateless())
    {
      return (STATE) integratedCircuit.getState();
    }
    else
    {
      throw new SimulatorException("saveState must be implemented on [" + getClass().getSimpleName() + "].");
    }
  }

  protected <STATE extends State> SimulationStateData<STATE> saveSimulationState()
  {
    SimulationStateData<STATE> simulationState = new SimulationStateData<>();
    for (Map.Entry<ViewPath, Map<CircuitSimulation, IC>> pathEntry : simulationIntegratedCircuits.getEntrySet())
    {
      Map<CircuitSimulation, IC> simulationMap = pathEntry.getValue();
      ViewPath path = pathEntry.getKey();
      for (Map.Entry<CircuitSimulation, IC> simulationEntry : simulationMap.entrySet())
      {
        CircuitSimulation circuitSimulation = simulationEntry.getKey();
        IC integratedCircuit = simulationEntry.getValue();
        SubcircuitSimulation subcircuitSimulation = path.getSubcircuitSimulation(circuitSimulation);
        STATE state = saveState(integratedCircuit);
        simulationState.add(subcircuitSimulation.getId(), state);
      }
    }

    return simulationState;
  }

  @Override
  public IC getComponent(ViewPath path, CircuitSimulation circuitSimulation)
  {
    return simulationIntegratedCircuits.get(path, circuitSimulation);
  }

  @Override
  public String getComponentType()
  {
    if (simulationIntegratedCircuits != null)
    {
      return simulationIntegratedCircuits.getComponentType();
    }
    return "";
  }

  protected List<String> getPortNames(String name, int portNumber, int inputWidth, int inputCount)
  {
    ArrayList<String> portNames = new ArrayList<>();
    int numPorts = (portNumber + 1) * inputWidth;
    int maxPorts = (portNumber + 1) * (inputWidth * inputCount);
    for (int i = portNumber * inputWidth; i < numPorts; i++)
    {
      String portName = calculatePortName(name, i, maxPorts);
      portNames.add(portName);
    }
    return portNames;
  }

  @Override
  public String toDebugString()
  {
    return super.toDebugString() + toSimulationsDebugString(simulationIntegratedCircuits.getSimulations());
  }

  @Override
  public Set<? extends SubcircuitSimulation> getComponentSubcircuitSimulations()
  {
    return simulationIntegratedCircuits.getSimulations();
  }

  protected abstract IC createIntegratedCircuit(ViewPath path, CircuitSimulation circuitSimulation, FamilyVoltageConfiguration familyVoltageConfiguration);

  public abstract IntegratedCircuitData<?, ?> save(boolean selected);

  protected abstract boolean mustIncludeExplicitPowerPorts(FamilyVoltageConfiguration familyVoltageConfiguration);
}

