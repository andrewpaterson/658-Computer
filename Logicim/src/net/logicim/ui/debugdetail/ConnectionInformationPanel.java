package net.logicim.ui.debugdetail;

import net.common.util.StringUtil;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.wire.Trace;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.HoverConnectionView;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.integratedcircuit.ComponentView;
import net.logicim.ui.common.integratedcircuit.View;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.common.wire.TraceView;
import net.logicim.ui.common.wire.TunnelView;

import java.awt.*;
import java.util.List;

public class ConnectionInformationPanel
    extends InformationPanel
{
  protected ConnectionView connectionView;

  public ConnectionInformationPanel(ConnectionView connectionView,
                                    Graphics2D graphics,
                                    Viewport viewport,
                                    int width,
                                    int height)
  {
    super(graphics, viewport, width, height);
    this.connectionView = connectionView;
  }

  @Override
  protected void paintDetail(SubcircuitSimulation subcircuitSimulation, int fontHeight, int x, int y)
  {
    int ySpacing = fontHeight / 2;

    List<View> connectedComponents = connectionView.getConnectedComponents();
    String hover = connectionView instanceof HoverConnectionView ? "(hover) " : "";

    graphics.drawString("  < Position: " + connectionView.getGridPosition().x + ", " + connectionView.getGridPosition().y + " " + hover + ">", x, y);
    y += fontHeight + ySpacing;
    View previousView = null;
    for (View connectedView : connectedComponents)
    {
      String componentString = connectedView.getType() +
                               getComponentNameString(connectedView) +
                               getComponentDetailString(subcircuitSimulation, connectedView);

      y = drawMultilineString(fontHeight,
                              x,
                              y,
                              componentString);

      if (!((previousView instanceof TraceView || previousView == null) &&
            connectedView instanceof TraceView))
      {
        y += ySpacing;
      }

      previousView = connectedView;
    }

    for (View connectedView : connectedComponents)
    {
      if (connectedView instanceof ComponentView<?>)
      {
        ComponentView<?> componentView = (ComponentView<?>) connectedView;
        PortView portView = componentView.getPortView(connectionView);
        if (portView != null)
        {
          String simulationsString = componentView.toSimulationsDebugString(portView.getPorts().keySet());
          y = drawMultilineString(fontHeight,
                                  x,
                                  y,
                                  simulationsString);
        }
      }
    }
  }

  private String getComponentDetailString(SubcircuitSimulation subcircuitSimulation, View connectedComponent)
  {
    if (connectedComponent instanceof TraceView)
    {
      return toTraceDetailString(subcircuitSimulation, (TraceView) connectedComponent);
    }
    else if (connectedComponent instanceof ComponentView)
    {
      return toComponentDetailString(subcircuitSimulation, (ComponentView<?>) connectedComponent);
    }
    else if (connectedComponent instanceof TunnelView)
    {
      return toTunnelDetailString(subcircuitSimulation, (TunnelView) connectedComponent);
    }
    return "";
  }

  private String toComponentDetailString(SubcircuitSimulation subcircuitSimulation, ComponentView<?> componentView)
  {
    StringBuilder builder = new StringBuilder();
    builder.append(" ");
    PortView portView = componentView.getPortView(connectionView);
    String padding = "  ";
    String text = portView.getText();
    if (!StringUtil.isEmptyOrNull(text))
    {
      builder.append("\n" + padding + text);
      padding += "    ";
    }
    List<? extends Port> ports = portView.getPorts(subcircuitSimulation);
    if (ports != null)
    {
      for (Port port : ports)
      {
        String portName = port.getName();
        if (StringUtil.isEmptyOrNull(portName))
        {
          portName = port.getType() + ": ";
        }
        else
        {
          portName = port.getType() + " \"" + portName + "\"" + ": ";
        }
        builder.append("\n" + padding + portName + toPortTraceIdString(port));
      }
    }
    return builder.toString();
  }

  private String toPortTraceIdString(Port port)
  {
    long traceId = port.getTraceId();
    if (traceId != 0)
    {
      return Long.toString(traceId);
    }
    else
    {
      return "n.c.";
    }
  }

  private String toTraceDetailString(SubcircuitSimulation subcircuitSimulation, TraceView traceView)
  {
    StringBuilder builder = new StringBuilder();
    builder.append(" ");
    List<Trace> traces = traceView.getTraces(subcircuitSimulation);
    if (traces != null)
    {
      boolean multiline = traces.size() > 8;
      if (multiline)
      {
        builder.append("\n  ");
      }

      boolean first = true;
      int count = 0;
      for (Trace trace : traces)
      {
        first = appendComma(builder, first);
        builder.append(trace.getId());
        count++;
        if (count == 8)
        {
          first = true;
          count = 0;
          builder.append("\n  ");
        }
      }
    }
    return builder.toString();
  }

  private String toTunnelDetailString(SubcircuitSimulation subcircuitSimulation, TunnelView tunnelView)
  {
    StringBuilder builder = new StringBuilder();
    builder.append(" ");
    List<Trace> traces = tunnelView.getTraces(subcircuitSimulation);
    boolean multiline = traces.size() > 8;
    if (multiline)
    {
      builder.append("\n  ");
    }

    boolean first = true;
    int count = 0;
    for (Trace trace : traces)
    {
      first = appendComma(builder, first);
      builder.append(trace.getId());
      count++;
      if (count == 8)
      {
        first = true;
        count = 0;
        builder.append("\n  ");
      }
    }
    return builder.toString();
  }

  private String getComponentNameString(View connectedComponent)
  {
    String name = connectedComponent.getName();
    if (name != null)
    {
      return " \"" + name + "\"";
    }
    else
    {
      return "";
    }
  }
}

