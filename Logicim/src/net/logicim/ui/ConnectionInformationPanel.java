package net.logicim.ui;

import net.logicim.common.util.StringUtil;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.wire.Trace;
import net.logicim.ui.common.Colours;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.integratedcircuit.ComponentView;
import net.logicim.ui.common.integratedcircuit.View;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.common.wire.TraceView;
import net.logicim.ui.common.wire.TunnelView;

import java.awt.*;
import java.util.List;

public class ConnectionInformationPanel
{
  protected ConnectionView connectionView;
  protected Graphics2D graphics;
  protected Viewport viewport;
  private int width;
  private int height;

  public ConnectionInformationPanel(ConnectionView connectionView,
                                    Graphics2D graphics,
                                    Viewport viewport,
                                    int width,
                                    int height)
  {
    this.connectionView = connectionView;
    this.graphics = graphics;
    this.viewport = viewport;
    this.width = width;
    this.height = height;
  }

  public void drawConnectionDetails(int left, int top)
  {
    graphics.setColor(Colours.getInstance().getInfoBackground());
    graphics.fillRect(left, top, width, height);
    graphics.setColor(Colours.getInstance().getInfoBorder());
    graphics.drawRect(left, top, width, height);
    Shape clip = graphics.getClip();

    graphics.setClip(left, top, width, height);
    Font font = viewport.getFont(10, false);
    FontMetrics metrics = graphics.getFontMetrics(font);
    List<View> connectedComponents = connectionView.getConnectedComponents();
    int fontHeight = metrics.getHeight();
    int ySpacing = fontHeight / 2;
    int yOffset = top + 10 + metrics.getAscent();
    int y = 0;
    int xOffset = left + 10;
    graphics.drawString("  < Position: " + connectionView.getGridPosition().x + ", " + connectionView.getGridPosition().y + " >", xOffset, y + yOffset);
    y += fontHeight + ySpacing;
    View previousComponent = null;
    for (View connectedComponent : connectedComponents)
    {
      String componentString = connectedComponent.getType() +
                               getComponentNameString(connectedComponent) +
                               getComponentDetailString(connectedComponent);

      y = drawMultilineString(fontHeight,
                              xOffset,
                              y,
                              yOffset,
                              componentString);

      if (!(previousComponent instanceof TraceView && connectedComponent instanceof TraceView))
      {
        y += ySpacing;
      }

      previousComponent = connectedComponent;
    }

    graphics.setClip(clip);
  }

  protected int drawMultilineString(int fontHeight, int x, int y, int yOffset, String string)
  {
    String[] strings = string.split("\n");
    for (int i = 0; i < strings.length; i++)
    {
      String s = strings[i];
      if (!((i == (strings.length - 1)) && ((s == null) || s.trim().isEmpty())))
      {
        graphics.drawString(s, x, y + yOffset);
        y += fontHeight;
      }
    }
    return y;
  }

  private String getComponentDetailString(View connectedComponent)
  {
    if (connectedComponent instanceof TraceView)
    {
      return toTraceDetailString((TraceView) connectedComponent);
    }
    else if (connectedComponent instanceof ComponentView)
    {
      return toComponentDetailString((ComponentView<?>) connectedComponent);
    }
    else if (connectedComponent instanceof TunnelView)
    {
      return toTunnelDetailString((TunnelView) connectedComponent);
    }
    return "";
  }

  private String toComponentDetailString(ComponentView<?> componentView)
  {
    StringBuilder builder = new StringBuilder();
    builder.append(" ");
    PortView portView = componentView.getPort(connectionView);
    String padding = "  ";
    String text = portView.getText();
    if (!StringUtil.isEmptyOrNull(text))
    {
      builder.append("\n" + padding + text);
      padding += "    ";
    }
    for (Port port : portView.getPorts())
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

  private String toTraceDetailString(TraceView traceView)
  {
    StringBuilder builder = new StringBuilder();
    builder.append(" ");
    List<Trace> traces = traceView.getTraces();
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

  private String toTunnelDetailString(TunnelView tunnelView)
  {
    StringBuilder builder = new StringBuilder();
    builder.append(" ");
    List<Trace> traces = tunnelView.getTraces();
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

  private boolean appendComma(StringBuilder builder, boolean first)
  {
    if (first)
    {
      first = false;
    }
    else
    {
      builder.append(", ");
    }
    return first;
  }
}