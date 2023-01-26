package net.logicim.ui.common.integratedcircuit;

import net.logicim.common.SimulatorException;
import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.PassiveData;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.passive.common.Passive;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.simulation.CircuitEditor;

import java.util.ArrayList;
import java.util.List;

public abstract class PassiveView<PASSIVE extends Passive, PROPERTIES extends ComponentProperties>
    extends ComponentView<PROPERTIES>
{
  protected PASSIVE passive;

  public PassiveView(CircuitEditor circuitEditor,
                     Int2D position,
                     Rotation rotation,
                     PROPERTIES properties)
  {
    super(circuitEditor, position, rotation, properties);
  }

  protected void createComponent()
  {
    this.passive = createPassive();
    this.passive.disable();
    circuitEditor.addPassiveView(this);
  }

  protected void validateComponent()
  {
    if (getComponent() == null)
    {
      throw new SimulatorException("Component not configured on [%s].  Call create().", getClass().getSimpleName());
    }
  }

  @Override
  protected void finaliseView()
  {
    createComponent();
    createPortViews();
    super.finaliseView();
    validateComponent();
    validatePorts();
  }

  protected void validatePorts()
  {
    List<Port> ports = getComponent().getPorts();
    if ((ports.size() > 0) && (this.ports.size() == 0))
    {
      throw new SimulatorException("Ports not configured on IC view.  Call new PortView(Port) for each Port on the IntegratedCircuit.");
    }

    List<Port> missing = new ArrayList<>();
    for (Port port : ports)
    {
      PortView portView = getPortView(port);
      if (portView == null)
      {
        missing.add(port);
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

  public PASSIVE getComponent()
  {
    return passive;
  }

  public abstract PassiveData<?> save(boolean selected);

  protected abstract PASSIVE createPassive();
}

