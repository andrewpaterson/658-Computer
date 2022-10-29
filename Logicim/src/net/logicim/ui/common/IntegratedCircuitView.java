package net.logicim.ui.common;

import net.logicim.common.SimulatorException;
import net.logicim.common.type.Int2D;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.Uniport;
import net.logicim.domain.common.trace.TraceNet;
import net.logicim.ui.CircuitEditor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class IntegratedCircuitView<IC extends IntegratedCircuit<?, ?>>
    extends View
{
  protected IC integratedCircuit;
  protected List<PortView> ports;

  public IntegratedCircuitView(CircuitEditor circuitEditor,
                               IC integratedCircuit,
                               Int2D position,
                               Rotation rotation)
  {
    super(circuitEditor, position, rotation);
    this.integratedCircuit = integratedCircuit;
    this.ports = new ArrayList<>();
    this.integratedCircuit.disable();
  }

  public void addPortView(PortView portView)
  {
    ports.add(portView);
  }

  public PortView getPortView(Port port)
  {
    for (PortView portView : ports)
    {
      if (portView.getPort() == port)
      {
        return portView;
      }
    }
    return null;
  }

  protected void validatePorts()
  {
    List<Port> missing = new ArrayList<>();
    for (Port port : integratedCircuit.getPorts())
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
      throw new SimulatorException("Ports [" + builder.toString() + "] not configured on IC view.");
    }
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport)
  {
    Int2D position = new Int2D();
    for (PortView portView : ports)
    {
      position.set(portView.position);
      rotation.transform(position);
      int x = viewport.transformGridToScreenSpaceX(position.x + this.position.x);
      int y = viewport.transformGridToScreenSpaceY(position.y + this.position.y);
      int lineWidth = (int) (viewport.getCircleRadius() * 3);

      Port port = portView.getPort();
      Uniport uniport = (Uniport) port;
      Color color = getColorForVoltage(viewport, uniport.getVoltage());

      graphics.setColor(color);
      graphics.fillOval(x - lineWidth,
                        y - lineWidth,
                        lineWidth * 2,
                        lineWidth * 2);
    }
  }

  protected Color getColorForVoltage(Viewport viewport, float voltage)
  {
    Color color;
    Colours colours = viewport.getColours();
    if (voltage == TraceNet.Unsettled)
    {
      color = colours.getTraceUnsettled();
    }
    else if (voltage == TraceNet.Undriven)
    {
      color = colours.getTraceUndriven();
    }
    else if ((voltage < 0.0f) || (voltage > 7.0f))
    {
      color = colours.getTraceError();
    }
    else
    {
      color = colours.getTraceVoltage(voltage);
    }
    return color;
  }

  public IC getIntegratedCircuit()
  {
    return integratedCircuit;
  }

  @Override
  public void enable(Simulation simulation)
  {
    super.enable(simulation);
    integratedCircuit.enable(simulation);
  }
}

