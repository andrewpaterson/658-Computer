package net.logicim.ui.common;

import net.logicim.common.SimulatorException;
import net.logicim.common.collection.linkedlist.LinkedList;
import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.IntegratedCircuitData;
import net.logicim.data.integratedcircuit.event.IntegratedCircuitEventData;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.event.IntegratedCircuitEvent;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.propagation.Family;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.common.propagation.FamilyVoltageConfigurationStore;
import net.logicim.domain.common.state.State;
import net.logicim.ui.CircuitEditor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class IntegratedCircuitView<IC extends IntegratedCircuit<?, ?>>
    extends DiscreteView
{
  protected IC integratedCircuit;
  protected Family family;

  public IntegratedCircuitView(CircuitEditor circuitEditor,
                               Int2D position,
                               Rotation rotation,
                               String name,
                               Family family)
  {
    super(circuitEditor, position, rotation, name);
    this.family = family;
    circuitEditor.add(this);
  }

  protected void createIntegratedCircuit()
  {
    FamilyVoltageConfiguration familyVoltageConfiguration = FamilyVoltageConfigurationStore.get(family);
    this.integratedCircuit = createIntegratedCircuit(familyVoltageConfiguration);
    this.integratedCircuit.disable();
  }

  protected void validatePorts()
  {
    if ((integratedCircuit.getPorts().size() > 0) && (ports.size() == 0))
    {
      throw new SimulatorException("Ports not configured on IC view.  Call new PortView(Port) for each Port on the IntegratedCircuit.");
    }

    List<Port> missing = new ArrayList<>();
    for (Port port : integratedCircuit.getPorts())
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
      StringBuilder builder = new StringBuilder();
      boolean first = true;
      for (Port port : missing)
      {
        if (first)
        {
          first = false;
        }
        else
        {
          builder.append(", ");
        }
        builder.append(port.getName());

      }
      throw new SimulatorException("Ports [" + builder.toString() + "] not configured on IC view.  Call new PortView(Port) for each Port on the IntegratedCircuit.");
    }
  }

  private void validateIntegratedCircuit()
  {
    if (integratedCircuit == null)
    {
      throw new SimulatorException("Integrated Circuit not configured on IC view.  Call create().");
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
  public void enable(Simulation simulation)
  {
    integratedCircuit.enable(simulation);
  }

  @Override
  public void simulationStarted(Simulation simulation)
  {
    integratedCircuit.simulationStarted(simulation);
  }

  @Override
  public boolean isEnabled()
  {
    return integratedCircuit.isEnabled();
  }

  @Override
  public ConnectionView getConnectionsInGrid(Int2D p)
  {
    return getConnectionsInGrid(p.x, p.y);
  }

  @Override
  protected void finaliseView()
  {
    createIntegratedCircuit();
    createPortViews();
    super.finaliseView();
    validateIntegratedCircuit();
    validatePorts();
  }

  @Override
  public String getDescription()
  {
    return integratedCircuit.getType() + " " + getName() + " (" + getPosition() + ")";
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

  protected abstract IC createIntegratedCircuit(FamilyVoltageConfiguration familyVoltageConfiguration);

  public abstract IntegratedCircuitData<?, ?> save();
}

