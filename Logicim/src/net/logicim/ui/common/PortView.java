package net.logicim.ui.common;

import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.Uniport;
import net.logicim.domain.common.trace.TraceNet;
import net.logicim.ui.shape.BoundingBox;

import java.awt.*;

public class PortView
{
  protected IntegratedCircuitView<?> owner;
  protected Port port;
  protected Int2D positionRelativeToIC;

  protected boolean inverting;
  protected String text;
  protected boolean overline;
  protected Float2D bubbleCenter;
  protected float bubbleDiameter;

  protected ConnectionView connections;

  protected PortViewGridCache gridCache;

  public PortView(IntegratedCircuitView<?> integratedCircuit, Port port, Int2D positionRelativeToIC)
  {
    this.owner = integratedCircuit;
    this.port = port;
    this.positionRelativeToIC = positionRelativeToIC;
    this.owner.addPortView(this);

    this.bubbleCenter = null;
    this.bubbleDiameter = 0.9f;

    connections = new ConnectionView(owner);

    gridCache = new PortViewGridCache();
  }

  public PortView setInverting(boolean inverting, Rotation facing)
  {
    this.inverting = inverting;
    Int2D portOffset = new Int2D(0, -1);
    facing.transform(portOffset);
    bubbleCenter = new Float2D(positionRelativeToIC);
    positionRelativeToIC.add(portOffset);
    Float2D bubbleOffset = new Float2D(0, -0.5f);
    facing.transform(bubbleOffset);
    bubbleCenter.add(bubbleOffset);

    gridCache.invalidate();
    return this;
  }

  public PortView setDrawBar(boolean drawBar)
  {
    this.overline = drawBar;
    return this;
  }

  public PortView setText(String text)
  {
    this.text = text;
    return this;
  }

  public Port getPort()
  {
    return port;
  }

  public void paint(Graphics2D graphics, Viewport viewport)
  {
    updateGridCache();

    if (inverting)
    {
      Float2D gridBubbleCenter = gridCache.getBubbleCenter();
      if (gridBubbleCenter != null)
      {
        int x = viewport.transformGridToScreenSpaceX(gridBubbleCenter.x);
        int y = viewport.transformGridToScreenSpaceY(gridBubbleCenter.y);

        graphics.setStroke(new BasicStroke(viewport.getLineWidth()));
        graphics.setColor(viewport.getColours().getShapeBorder());
        int diameter = viewport.transformGridToScreenWidth(bubbleDiameter);
        graphics.drawOval(x - diameter / 2,
                          y - diameter / 2,
                          diameter,
                          diameter);
      }
    }

    Int2D gridPosition = gridCache.getPosition();
    if (gridPosition != null)
    {
      int x = viewport.transformGridToScreenSpaceX(gridPosition.x);
      int y = viewport.transformGridToScreenSpaceY(gridPosition.y);
      int lineWidth = (int) (viewport.getCircleRadius() * 3);

      Port port = getPort();
      Uniport uniport = (Uniport) port;
      Color color = VoltageColour.getColorForVoltage(viewport.getColours(), uniport.getVoltage());

      graphics.setColor(color);
      graphics.fillOval(x - lineWidth,
                        y - lineWidth,
                        lineWidth * 2,
                        lineWidth * 2);
    }
  }

  private void updateGridCache()
  {
    if (!gridCache.isValid())
    {
      gridCache.update(bubbleCenter,
                       positionRelativeToIC,
                       inverting,
                       owner.getRotation(),
                       owner.getPosition());
    }
  }

  public void invalidateCache()
  {
    gridCache.invalidate();
  }

  public void updateBoundingBox(BoundingBox boundingBox)
  {
    if (inverting)
    {
      boundingBox.include(bubbleCenter, bubbleDiameter / 2);
    }
    boundingBox.include(positionRelativeToIC);
  }

  public Int2D getGridPosition()
  {
    updateGridCache();

    return gridCache.getPosition();
  }

  public boolean equals(int x, int y)
  {
    updateGridCache();

    return gridCache.getPosition().equals(x, y);
  }

  public ConnectionView getConnections()
  {
    return connections;
  }

  public void connect(TraceNet trace)
  {
    if (port instanceof Uniport)
    {
      Uniport uniport = (Uniport) port;
      uniport.disconnect();
      uniport.connect(trace);
    }
  }
}

