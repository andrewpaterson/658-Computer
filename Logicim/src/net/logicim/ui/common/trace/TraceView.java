package net.logicim.ui.common.trace;

import net.logicim.common.SimulatorException;
import net.logicim.common.type.Int2D;
import net.logicim.data.trace.TraceData;
import net.logicim.domain.common.trace.TraceNet;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Colours;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.VoltageColour;
import net.logicim.ui.common.integratedcircuit.ComponentView;

import java.awt.*;
import java.util.List;

public class TraceView
    extends BaseTraceView
{
  protected TraceNet trace;

  public TraceView(CircuitEditor circuitEditor, Int2D start, Int2D end)
  {
    super(circuitEditor, start, end);
    this.trace = null;
    circuitEditor._addTraceView(this);
  }

  public void paint(Graphics2D graphics, Viewport viewport, long time)
  {
    graphics.setStroke(viewport.getStroke());
    Color color = VoltageColour.getColourForTrace(Colours.getInstance(), trace, time);
    graphics.setColor(color);
    int x1 = viewport.transformGridToScreenSpaceX(line.getStart().x);
    int y1 = viewport.transformGridToScreenSpaceY(line.getStart().y);
    int x2 = viewport.transformGridToScreenSpaceX(line.getEnd().x);
    int y2 = viewport.transformGridToScreenSpaceY(line.getEnd().y);

    graphics.drawLine(x1, y1, x2, y2);

    int lineWidth = (int) (viewport.getCircleRadius() * viewport.getConnectionSize());

    if (!isRemoved())
    {
      if (!startConnection.isNonJunctionTracesOnly())
      {
        graphics.fillOval(x1 - lineWidth,
                          y1 - lineWidth,
                          lineWidth * 2,
                          lineWidth * 2);
      }
      if (!endConnection.isNonJunctionTracesOnly())
      {
        graphics.fillOval(x2 - lineWidth,
                          y2 - lineWidth,
                          lineWidth * 2,
                          lineWidth * 2);
      }
    }
  }

  public void connectTraceNet(TraceNet trace)
  {
    this.trace = trace;
  }

  @Override
  public String getName()
  {
    return "Trace";
  }

  @Override
  public String getDescription()
  {
    return "Trace (" + getStartPosition() + ") to (" + getEndPosition() + ")";
  }

  public TraceNet getTrace()
  {
    return trace;
  }

  public void disconnectTraceNet()
  {
    trace = null;
  }

  public void removed()
  {
    startConnection = null;
    endConnection = null;

    if (trace != null)
    {
      throw new SimulatorException("Trace must be disconnected before removal.");
    }
  }

  public TraceView getOtherTrace(List<ComponentView> connectedComponents)
  {
    TraceView otherTrace = null;
    if (connectedComponents.size() == 2)
    {
      for (ComponentView connectedComponent : connectedComponents)
      {
        if (connectedComponent != this)
        {
          if (connectedComponent instanceof TraceView)
          {
            otherTrace = (TraceView) connectedComponent;
          }
        }
      }
    }
    return otherTrace;
  }

  public TraceData save(boolean selected)
  {
    return new TraceData(getTraceId(),
                         getStartPosition(),
                         getEndPosition(),
                         selected);
  }

  protected long getTraceId()
  {
    if (trace != null)
    {
      return trace.getId();
    }
    else
    {
      return 0L;
    }
  }

}

