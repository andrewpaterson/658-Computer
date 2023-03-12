package net.logicim.ui.common.integratedcircuit;

import net.logicim.common.SimulatorException;
import net.logicim.common.collection.linkedlist.LinkedList;
import net.logicim.common.type.Int2D;
import net.logicim.common.util.StringUtil;
import net.logicim.data.integratedcircuit.common.IntegratedCircuitData;
import net.logicim.data.integratedcircuit.event.IntegratedCircuitEventData;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.event.IntegratedCircuitEvent;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.common.propagation.FamilyVoltageConfigurationStore;
import net.logicim.domain.common.state.State;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.port.PortView;

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
    this.integratedCircuit = createIntegratedCircuit(circuit, familyVoltageConfiguration);
    this.integratedCircuit.disable();
  }

  protected void validatePorts()
  {
    List<Port> ports = integratedCircuit.getPorts();
    if ((ports.size() > 0) && (portViews.size() == 0))
    {
      throw new SimulatorException("Ports not configured on IC view.  Call new PortView(Port) for each Port on the IntegratedCircuit.");
    }

    validateAtLeastOnePort(portViews);
    validateNoMissingPorts(ports);
    validateNoDuplicatePorts(ports);
  }

  private void validateNoMissingPorts(List<Port> ports)
  {
    List<Port> missing = new ArrayList<>();
    for (Port port : ports)
    {
      if (port.isLogicPort())
      {
        PortView portView = getPortView(port);
        if (portView == null)
        {
          missing.add(port);
        }
      }
    }

    if (missing.size() > 0)
    {
      List<String> missingNames = new ArrayList<>();
      for (Port port : missing)
      {
        missingNames.add(port.getName());
      }
      throw new SimulatorException("Ports [%s] not configured on view.  Call new PortView(Port) for each Port on view [%s].", StringUtil.commaSeparateList(missingNames), getDescription());
    }
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
}

