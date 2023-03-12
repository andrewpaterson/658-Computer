package net.logicim.ui.common.integratedcircuit;

import net.logicim.common.SimulatorException;
import net.logicim.common.type.Int2D;
import net.logicim.common.util.StringUtil;
import net.logicim.data.integratedcircuit.common.PassiveData;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.passive.common.Passive;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.port.PortView;

import java.util.*;

public abstract class PassiveView<PASSIVE extends Passive, PROPERTIES extends ComponentProperties>
    extends ComponentView<PROPERTIES>
{
  protected PASSIVE passive;

  public PassiveView(SubcircuitView subcircuitView,
                     Circuit circuit,
                     Int2D position,
                     Rotation rotation,
                     PROPERTIES properties)
  {
    super(subcircuitView,
          circuit,
          position,
          rotation,
          properties);
  }

  protected void createComponent(Circuit circuit)
  {
    this.passive = createPassive(circuit);
    this.passive.disable();
    subcircuitView.addPassiveView(this);
  }

  protected void validateComponent()
  {
    if (getComponent() == null)
    {
      throw new SimulatorException("Component not configured on [%s].  Call create().", getClass().getSimpleName());
    }
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

  protected void validatePorts()
  {
    List<Port> ports = passive.getPorts();

    if ((ports.size() > 0) && (portViews.size() == 0))
    {
      throw new SimulatorException("Ports not configured on Pas view.  Call new PortView(Port) for each Port on the IntegratedCircuit.");
    }

    validateAtLeastOnePort(portViews);
    validateNoMissingPorts(ports);
    validateNoDuplicatePorts(ports);
  }

  protected void validateNoMissingPorts(List<Port> ports)
  {
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
      List<String> missingNames = new ArrayList<>();
      for (Port port : missing)
      {
        missingNames.add(port.getName());
      }
      throw new SimulatorException("Ports [%s] not configured on view.  Call new PortView(Port) for each Port on view [%s].", StringUtil.commaSeparateList(missingNames), getDescription());
    }
  }

  public PASSIVE getComponent()
  {
    return passive;
  }

  public abstract PassiveData<?> save(boolean selected);

  protected abstract PASSIVE createPassive(Circuit circuit);
}

