package net.logicim.ui.common.integratedcircuit;

import net.logicim.common.SimulatorException;
import net.logicim.common.collection.linkedlist.LinkedList;
import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.IntegratedCircuitData;
import net.logicim.data.integratedcircuit.common.IntegratedCircuitProperties;
import net.logicim.data.integratedcircuit.event.IntegratedCircuitEventData;
import net.logicim.data.integratedcircuit.event.MultiIntegratedCircuitEventData;
import net.logicim.data.integratedcircuit.event.SimulationIntegratedCircuitEventData;
import net.logicim.data.simulation.SimulationStateData;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.event.IntegratedCircuitEvent;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.common.propagation.FamilyVoltageConfigurationStore;
import net.logicim.domain.common.propagation.VoltageConfiguration;
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
  protected Map<SubcircuitSimulation, IC> simulationIntegratedCircuits;

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

    simulationIntegratedCircuits = new LinkedHashMap<>();
  }

  public IC createComponent(SubcircuitSimulation circuit)
  {
    validateCanCreateComponent(circuit);

    FamilyVoltageConfiguration familyVoltageConfiguration = FamilyVoltageConfigurationStore.get(properties.family);
    IC integratedCircuit = createIntegratedCircuit(circuit, familyVoltageConfiguration);
    simulationIntegratedCircuits.put(circuit, integratedCircuit);

    createPowerPortsIfNecessary(circuit, familyVoltageConfiguration);

    postCreateComponent(circuit, integratedCircuit);
    return integratedCircuit;
  }

  protected void removeComponent(SubcircuitSimulation circuit)
  {
    simulationIntegratedCircuits.remove(circuit);
  }

  @Override
  public void paint(Graphics2D graphics,
                    Viewport viewport,
                    SubcircuitSimulation circuit)
  {
    super.paint(graphics, viewport, circuit);
  }

  @Override
  protected void finaliseView()
  {
    createPortViews();
    super.finaliseView();
    subcircuitView.addIntegratedCircuitView(this);
  }

  protected void createPowerPortsIfNecessary(SubcircuitSimulation circuit, FamilyVoltageConfiguration familyVoltageConfiguration)
  {
    if (!mustIncludeExplicitPowerPorts(familyVoltageConfiguration))
    {
      createPowerPorts(circuit, familyVoltageConfiguration);
    }
  }

  protected void createPowerPorts(SubcircuitSimulation circuit, FamilyVoltageConfiguration familyVoltageConfiguration)
  {
    VoltageConfiguration voltageConfiguration = familyVoltageConfiguration.getDefaultVoltageConfiguration(DefaultLogicLevels.get());
    IC integratedCircuit = getComponent(circuit);
    if (integratedCircuit != null)
    {
      Trace vccTrace = new Trace();
      integratedCircuit.getPins().getVoltageCommon().connect(vccTrace);

      PowerSource vccPowerSource = new PowerSource(circuit.getCircuit(), "", voltageConfiguration.getVcc());
      vccPowerSource.getPowerOutPort().connect(vccTrace);

      Trace gndTrace = new Trace();
      integratedCircuit.getPins().getVoltageGround().connect(gndTrace);

      PowerSource gndPowerSource = new PowerSource(circuit.getCircuit(), "", 0);
      gndPowerSource.getPowerOutPort().connect(gndTrace);
    }
  }

  protected SimulationIntegratedCircuitEventData saveEvents()
  {
    SimulationIntegratedCircuitEventData simulationEventData = new SimulationIntegratedCircuitEventData();
    for (Map.Entry<SubcircuitSimulation, IC> entry : simulationIntegratedCircuits.entrySet())
    {
      IC integratedCircuit = entry.getValue();
      SubcircuitSimulation circuit = entry.getKey();
      LinkedList<IntegratedCircuitEvent> integratedCircuitEvents = integratedCircuit.getEvents();
      List<IntegratedCircuitEventData<?>> eventDatas = new ArrayList<>(integratedCircuitEvents.size());
      for (IntegratedCircuitEvent event : integratedCircuitEvents)
      {
        IntegratedCircuitEventData<?> integratedCircuitEventData = event.save();
        eventDatas.add(integratedCircuitEventData);
      }
      simulationEventData.add(circuit.getId(), new MultiIntegratedCircuitEventData(eventDatas));
    }
    return simulationEventData;
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
    for (Map.Entry<SubcircuitSimulation, IC> entry : simulationIntegratedCircuits.entrySet())
    {
      IC integratedCircuit = entry.getValue();
      SubcircuitSimulation circuitSimulation = entry.getKey();
      STATE state = saveState(integratedCircuit);
      simulationState.add(circuitSimulation.getId(), state);
    }
    return simulationState;
  }

  @Override
  public IC getComponent(SubcircuitSimulation circuitSimulation)
  {
    return simulationIntegratedCircuits.get(circuitSimulation);
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

  protected abstract IC createIntegratedCircuit(SubcircuitSimulation circuit, FamilyVoltageConfiguration familyVoltageConfiguration);

  public abstract IntegratedCircuitData<?, ?> save(boolean selected);

  protected abstract boolean mustIncludeExplicitPowerPorts(FamilyVoltageConfiguration familyVoltageConfiguration);
}

