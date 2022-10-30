package net.logicim.ui.trace;

import net.logicim.common.type.Int2D;
import net.logicim.domain.common.trace.TraceNet;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.VoltageColour;

import java.awt.*;

public class TraceView
{
  protected Int2D start;
  protected Int2D end;

  protected TraceNet trace;

  public TraceView(CircuitEditor circuitEditor, Int2D start, Int2D end)
  {
    this.start = start;
    this.end = end;
    this.trace = null;
    circuitEditor.add(this);
  }

  public void paint(Graphics2D graphics, Viewport viewport)
  {
    graphics.setStroke(new BasicStroke(viewport.getLineWidth()));
    Color color;
    if (trace == null)
    {
      color = viewport.getColours().getDisconnectedTrace();
    }
    else
    {
      float voltage = trace.getVoltage();
      color = VoltageColour.getColorForVoltage(viewport.getColours(), voltage);
    }
    graphics.setColor(color);
    int x1 = viewport.transformGridToScreenSpaceX(start.x);
    int y1 = viewport.transformGridToScreenSpaceY(start.y);
    int x2 = viewport.transformGridToScreenSpaceX(end.x);
    int y2 = viewport.transformGridToScreenSpaceY(end.y);

    graphics.drawLine(x1, y1, x2, y2);
  }

  public Int2D getStart()
  {
    return start;
  }

  public Int2D getEnd()
  {
    return end;
  }

  public void setTrace(TraceNet trace)
  {
    this.trace = trace;
  }
}

