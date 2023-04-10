package net.logicim.ui.common.integratedcircuit;

import net.logicim.common.SimulatorException;
import net.logicim.common.collection.linkedlist.LinkedList;
import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.IntegratedCircuitData;
import net.logicim.data.integratedcircuit.common.IntegratedCircuitProperties;
import net.logicim.data.integratedcircuit.event.IntegratedCircuitEventData;
import net.logicim.data.integratedcircuit.event.SimulationIntegratedCircuitEventData;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.event.IntegratedCircuitEvent;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.common.propagation.FamilyVoltageConfigurationStore;
import net.logicim.domain.common.propagation.VoltageConfiguration;
import net.logicim.domain.common.state.SimulationState;
import net.logicim.domain.common.state.State;
import net.logicim.domain.common.wire.Trace;
import net.logicim.domain.passive.power.PowerSource;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.defaults.DefaultLogicLevels;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class IntegratedCircuitView<IC extends IntegratedCircuit<?, ?>, PROPERTIES extends IntegratedCircuitProperties>
    extends ComponentView<PROPERTIES>
{
  protected Map<CircuitSimulation, IC> simulationIntegratedCircuits;

  public IntegratedCircuitView(SubcircuitView subcircuitView,
                               Int2D position,
                               Rotation rotation,
                               PROPERTIES properties)
  {
    super(subcircuitView, position, rotation, properties);
    if (properties.family == null)
    {
      throw new SimulatorException("Family may not be null on IC [%s].", getDescription());
    }

    subcircuitView.addIntegratedCircuitView(this);  //Shouldn't this be in finaliseView?
    simulationIntegratedCircuits = new LinkedHashMap<>();
  }

  protected void createComponent(CircuitSimulation simulation)
  {
    FamilyVoltageConfiguration familyVoltageConfiguration = FamilyVoltageConfigurationStore.get(properties.family);
    IC integratedCircuit = createIntegratedCircuit(simulation, familyVoltageConfiguration);
    simulationIntegratedCircuits.put(simulation, integratedCircuit);
    createPowerPortsIfNecessary(simulation, familyVoltageConfiguration);
    integratedCircuit.disable();
  }

  protected void validatePorts(CircuitSimulation simulation)
  {
    IC integratedCircuit = simulationIntegratedCircuits.get(simulation);
    if (integratedCircuit != null)
    {
      validatePorts(simulation, integratedCircuit.getPorts(), portViews);
    }
  }

  private void validateComponent(CircuitSimulation simulation)
  {
    if (getComponent(simulation) == null)
    {
      throw new SimulatorException("Integrated Circuit not configured in simulation [%s] on [%s].  Call create().", simulation.getDescription(), getClass().getSimpleName());
    }
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport, CircuitSimulation simulation)
  {
    super.paint(graphics, viewport, simulation);
  }

  @Override
  public void simulationStarted(CircuitSimulation simulation)
  {
    IC integratedCircuit = simulationIntegratedCircuits.get(simulation);
    integratedCircuit.simulationStarted(simulation.getSimulation());
  }

  @Override
  protected void finaliseView(CircuitSimulation simulation)
  {
    createComponent(simulation);
    createPortViews();
    super.finaliseView(simulation);
    validateComponent(simulation);
    validatePorts(simulation);
  }

  protected void createPowerPortsIfNecessary(CircuitSimulation simulation, FamilyVoltageConfiguration familyVoltageConfiguration)
  {
    if (!mustIncludeExplicitPowerPorts(familyVoltageConfiguration))
    {
      createPowerPorts(simulation, familyVoltageConfiguration);
    }
  }

  protected void createPowerPorts(CircuitSimulation simulation, FamilyVoltageConfiguration familyVoltageConfiguration)
  {
    VoltageConfiguration voltageConfiguration = familyVoltageConfiguration.getDefaultVoltageConfiguration(DefaultLogicLevels.get());
    IC integratedCircuit = simulationIntegratedCircuits.get(simulation);
    if (integratedCircuit != null)
    {
      Trace vccTrace = new Trace();
      integratedCircuit.getPins().getVoltageCommon().connect(vccTrace);

      PowerSource vccPowerSource = new PowerSource(simulation.getCircuit(), "", voltageConfiguration.getVcc());
      vccPowerSource.getPowerOutPort().connect(vccTrace);

      Trace gndTrace = new Trace();
      integratedCircuit.getPins().getVoltageGround().connect(gndTrace);

      PowerSource gndPowerSource = new PowerSource(simulation.getCircuit(), "", 0);
      gndPowerSource.getPowerOutPort().connect(gndTrace);
    }
  }

  protected SimulationIntegratedCircuitEventData saveEvents()
  {
    SimulationIntegratedCircuitEventData simulationEventData = new SimulationIntegratedCircuitEventData();
    for (Map.Entry<CircuitSimulation, IC> entry : simulationIntegratedCircuits.entrySet())
    {
      IC integratedCircuit = entry.getValue();
      CircuitSimulation simulation = entry.getKey();
      LinkedList<IntegratedCircuitEvent> integratedCircuitEvents = integratedCircuit.getEvents();
      List<IntegratedCircuitEventData<? extends IntegratedCircuitEvent>> eventDatas = new ArrayList<>(integratedCircuitEvents.size());
      for (IntegratedCircuitEvent event : integratedCircuitEvents)
      {
        IntegratedCircuitEventData<?> integratedCircuitEventData = event.save();
        eventDatas.add(integratedCircuitEventData);
      }
      simulationEventData.add(simulation.getId(), eventDatas);
    }
    return simulationEventData;
  }

  protected <STATE extends State> STATE saveState(IC integratedCircuit)
  {
    if (integratedCircuit.isStateless())
    {
      return null;
    }
    else
    {
      throw new SimulatorException("saveState must be implemented on [" + getClass().getSimpleName() + "].");
    }
  }

  protected <STATE extends State> SimulationState<STATE> saveSimulationState()
  {
    SimulationState<STATE> simulationState = new SimulationState<>();
    for (Map.Entry<CircuitSimulation, IC> entry : simulationIntegratedCircuits.entrySet())
    {
      IC integratedCircuit = entry.getValue();
      CircuitSimulation simulation = entry.getKey();
      STATE state = saveState(integratedCircuit);
      simulationState.add(simulation.getId(), state);
    }
    return simulationState;
  }

  @Override
  public IC getComponent(CircuitSimulation simulation)
  {
    return simulationIntegratedCircuits.get(simulation);
  }

  @Override
  public String getComponentType()
  {
    for (IC integratedCircuit : simulationIntegratedCircuits.values())
    {
      return integratedCircuit.getType();
    }
    return "";
  }

  protected List<String> getPortNames(String prefix, int portNumber, int inputWidth)
  {
    ArrayList<String> portNames = new ArrayList<>();
    for (int i = portNumber * inputWidth; i < (portNumber + 1) * inputWidth; i++)
    {
      String portName = prefix + i;
      portNames.add(portName);
    }
    return portNames;
  }

  protected abstract IC createIntegratedCircuit(CircuitSimulation simulation, FamilyVoltageConfiguration familyVoltageConfiguration);

  public abstract IntegratedCircuitData<?, ?> save(boolean selected);

  protected abstract boolean mustIncludeExplicitPowerPorts(FamilyVoltageConfiguration familyVoltageConfiguration);
}

