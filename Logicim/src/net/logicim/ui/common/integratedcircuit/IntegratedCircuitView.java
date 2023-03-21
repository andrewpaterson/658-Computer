package net.logicim.ui.common.integratedcircuit;

import net.logicim.common.SimulatorException;
import net.logicim.common.collection.linkedlist.LinkedList;
import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.IntegratedCircuitData;
import net.logicim.data.integratedcircuit.event.IntegratedCircuitEventData;
import net.logicim.data.integratedcircuit.common.IntegratedCircuitProperties;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
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
import java.util.List;

public abstract class IntegratedCircuitView<IC extends IntegratedCircuit<?, ?>, PROPERTIES extends IntegratedCircuitProperties>
    extends ComponentView<PROPERTIES>
{
  protected IC integratedCircuit;

  public IntegratedCircuitView(SubcircuitView subcircuitView,
                               Circuit circuit,
                               Int2D position,
                               Rotation rotation,
                               PROPERTIES properties)
  {
    super(subcircuitView, circuit, position, rotation, properties);
    if (properties.family == null)
    {
      throw new SimulatorException("Family may not be null on IC [%s].", getDescription());
    }

    subcircuitView.addIntegratedCircuitView(this);
  }

  protected void createComponent(Circuit circuit)
  {
    FamilyVoltageConfiguration familyVoltageConfiguration = FamilyVoltageConfigurationStore.get(properties.family);
    integratedCircuit = createIntegratedCircuit(circuit, familyVoltageConfiguration);
    createPowerPortsIfNecessary(circuit, familyVoltageConfiguration);
    integratedCircuit.disable();
  }

  protected void validatePorts()
  {
    validatePorts(integratedCircuit.getPorts(), portViews);
  }

  private void validateComponent()
  {
    if (getComponent() == null)
    {
      throw new SimulatorException("Integrated Circuit not configured on [%s].  Call create().", getClass().getSimpleName());
    }
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport, long time)
  {
    super.paint(graphics, viewport, time);
  }

  public IC getIntegratedCircuit()
  {
    return integratedCircuit;
  }

  @Override
  public void simulationStarted(Simulation simulation)
  {
    integratedCircuit.simulationStarted(simulation);
  }

  @Override
  protected void finaliseView(Circuit circuit)
  {
    createComponent(circuit);
    createPortViews();
    super.finaliseView(circuit);
    validateComponent();
    validatePorts();
  }

  protected void createPowerPortsIfNecessary(Circuit circuit, FamilyVoltageConfiguration familyVoltageConfiguration)
  {
    if (!mustIncludeExplicitPowerPorts(familyVoltageConfiguration))
    {
      createPowerPorts(circuit, familyVoltageConfiguration);
    }
  }

  protected void createPowerPorts(Circuit circuit, FamilyVoltageConfiguration familyVoltageConfiguration)
  {
    VoltageConfiguration voltageConfiguration = familyVoltageConfiguration.getDefaultVoltageConfiguration(DefaultLogicLevels.get());

    Trace vccTrace = new Trace();
    integratedCircuit.getPins().getVoltageCommon().connect(vccTrace);

    PowerSource vccPowerSource = new PowerSource(circuit, "", voltageConfiguration.getVcc());
    vccPowerSource.getPowerOutPort().connect(vccTrace);

    Trace gndTrace = new Trace();
    integratedCircuit.getPins().getVoltageGround().connect(gndTrace);

    PowerSource gndPowerSource = new PowerSource(circuit, "", 0);
    gndPowerSource.getPowerOutPort().connect(gndTrace);
  }

  protected List<IntegratedCircuitEventData<?>> saveEvents()
  {
    LinkedList<IntegratedCircuitEvent> integratedCircuitEvents = integratedCircuit.getEvents();
    ArrayList<IntegratedCircuitEventData<?>> eventDatas = new ArrayList<>(integratedCircuitEvents.size());
    for (IntegratedCircuitEvent event : integratedCircuitEvents)
    {
      IntegratedCircuitEventData<?> integratedCircuitEventData = event.save();
      eventDatas.add(integratedCircuitEventData);
    }
    return eventDatas;
  }

  protected State saveState()
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

  @Override
  public IC getComponent()
  {
    return integratedCircuit;
  }

  protected abstract IC createIntegratedCircuit(Circuit circuit, FamilyVoltageConfiguration familyVoltageConfiguration);

  public abstract IntegratedCircuitData<?, ?> save(boolean selected);

  protected abstract boolean mustIncludeExplicitPowerPorts(FamilyVoltageConfiguration familyVoltageConfiguration);
}

