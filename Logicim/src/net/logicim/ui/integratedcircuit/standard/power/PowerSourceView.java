package net.logicim.ui.integratedcircuit.standard.power;

import net.logicim.common.SimulatorException;
import net.logicim.common.type.Int2D;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.power.PowerSource;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.PortView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.component.DiscreteProperties;
import net.logicim.ui.common.component.DiscreteView;

import java.util.ArrayList;
import java.util.List;

public abstract class PowerSourceView<PROPERTIES extends DiscreteProperties>
    extends DiscreteView<PROPERTIES>
{
  protected PowerSource powerSource;
  protected boolean enabled;

  public PowerSourceView(CircuitEditor circuitEditor,
                         Int2D position,
                         Rotation rotation,
                         String name)
  {
    super(circuitEditor,
          position,
          rotation,
          name);
    circuitEditor.add(this);
  }

  @Override
  protected void finaliseView()
  {
    createPowerSource();
    createPortViews();
    super.finaliseView();
    validatePowerSource();
    validatePorts();
  }

  private void validatePowerSource()
  {
  }

  protected void createPowerSource()
  {
    powerSource = new PowerSource(circuitEditor.getCircuit(),
                                  properties.name,
                                  getVoltageOut());
    powerSource.disable();
  }

  @Override
  public boolean isEnabled()
  {
    return enabled;
  }

  @Override
  public void enable(Simulation simulation)
  {
    enabled = true;
  }

  @Override
  public void simulationStarted(Simulation simulation)
  {
  }

  @Override
  public ConnectionView getConnectionsInGrid(int x, int y)
  {
    return null;
  }

  @Override
  public ConnectionView getConnectionsInGrid(Int2D p)
  {
    return null;
  }

  @Override
  public String getDescription()
  {
    return powerSource.getType() + " " + properties.name + " (" + getPosition() + ")";
  }

  protected void validatePorts()
  {
    if ((powerSource.getPorts().size() > 0) && (ports.size() == 0))
    {
      throw new SimulatorException("Ports not configured on IC view.  Call new PortView(Port) for each Port on the IntegratedCircuit.");
    }

    List<Port> missing = new ArrayList<>();
    for (Port port : powerSource.getPorts())
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

  public PowerSource getPowerSource()
  {
    return powerSource;
  }

  public abstract float getVoltageOut();
}
