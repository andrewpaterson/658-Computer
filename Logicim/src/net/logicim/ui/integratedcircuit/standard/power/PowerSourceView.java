package net.logicim.ui.integratedcircuit.standard.power;

import net.logicim.common.SimulatorException;
import net.logicim.common.type.Int2D;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.port.BasePort;
import net.logicim.domain.power.PowerSource;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.DiscreteView;
import net.logicim.ui.common.PortView;
import net.logicim.ui.common.Rotation;

import java.util.ArrayList;
import java.util.List;

public abstract class PowerSourceView
    extends DiscreteView
{
  protected PowerSource powerSource;

  public PowerSourceView(CircuitEditor circuitEditor,
                         Int2D position,
                         Rotation rotation,
                         String name)
  {
    super(circuitEditor,
          position,
          rotation,
          name);
  }

  @Override
  protected void finaliseView()
  {
    createPowerSource();
    createPortViews();
    super.finaliseView();
    validatePorts();
  }

  protected PowerSource createPowerSource()
  {
    return new PowerSource(circuitEditor.getCircuit(), name, getSourceVoltage());
  }

  @Override
  public PortView getPortInGrid(int x, int y)
  {
    return null;
  }

  @Override
  public boolean isEnabled()
  {
    return false;
  }

  @Override
  public List<PortView> getPorts()
  {
    return null;
  }

  @Override
  public void enable(Simulation simulation)
  {
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
  public Int2D getGridPosition(ConnectionView connectionView)
  {
    return null;
  }

  @Override
  public String getDescription()
  {
    return powerSource.getType() + " " + name + " (" + getPosition() + ")";
  }

  protected void validatePorts()
  {
    if ((powerSource.getPorts().size() > 0) && (ports.size() == 0))
    {
      throw new SimulatorException("Ports not configured on IC view.  Call new PortView(Port) for each Port on the IntegratedCircuit.");
    }

    List<BasePort> missing = new ArrayList<>();
    for (BasePort port : powerSource.getPorts())
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
      for (BasePort port : missing)
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

  public abstract float getSourceVoltage();
}

