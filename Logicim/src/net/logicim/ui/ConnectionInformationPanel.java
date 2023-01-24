package net.logicim.ui;

import net.logicim.domain.common.wire.Trace;
import net.logicim.ui.common.Colours;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.integratedcircuit.View;
import net.logicim.ui.common.wire.TraceView;

import java.awt.*;
import java.util.List;

public class ConnectionInformationPanel
{
  protected ConnectionView connectionView;
  protected Graphics2D graphics;
  protected Viewport viewport;

  public ConnectionInformationPanel(ConnectionView connectionView,
                                    Graphics2D graphics,
                                    Viewport viewport)
  {
    this.connectionView = connectionView;
    this.graphics = graphics;
    this.viewport = viewport;
  }

  public void drawConnectionDetails(int left, int top, int width, int height)
  {
    graphics.setColor(Colours.getInstance().getInfoBackground());
    graphics.fillRect(left, top, width, height);
    graphics.setColor(Colours.getInstance().getInfoBorder());
    graphics.drawRect(left, top, width, height);

    Font font = viewport.getFont(10, false);
    FontMetrics metrics = graphics.getFontMetrics(font);
    List<View> connectedComponents = connectionView.getConnectedComponents();
    int y = top + 10 + metrics.getAscent();
    int x = left + 10;
    graphics.drawString("Position: " + connectionView.getGridPosition().x + ", " + connectionView.getGridPosition().y + "", x, y);
    y += metrics.getHeight();
    for (View connectedComponent : connectedComponents)
    {
      graphics.drawString(connectedComponent.getType() + getComponentNameString(connectedComponent) + getComponentDetailString(connectedComponent), x, y);
      y += metrics.getHeight();
    }
  }

  private String getComponentDetailString(View connectedComponent)
  {
    StringBuilder builder = new StringBuilder();
    if (connectedComponent instanceof TraceView)
    {
      TraceView traceView = (TraceView) connectedComponent;
      List<Trace> traces = traceView.getTraces();
      boolean first = true;
      for (Trace trace : traces)
      {
        if (first)
        {
          first = false;
        }
        else
        {
          builder.append(", ");
        }
        builder.append(trace.getId());
      }
    }
    return " " + builder.toString();
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
