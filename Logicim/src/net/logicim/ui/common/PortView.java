package net.logicim.ui.common;

import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.Uniport;
import net.logicim.ui.shape.BoundingBox;

import java.awt.*;

public class PortView
    extends ConnectionView
{
  protected IntegratedCircuitView<?> integratedCircuitView;
  protected Port port;
  protected Int2D position;
  protected Int2D transformedPosition;
  protected boolean inverting;
  protected String text;
  protected boolean overline;
  protected Float2D bubbleCenter;
  protected Float2D transformedBubbleCenter;
  protected float bubbleDiameter;

  public PortView(IntegratedCircuitView<?> integratedCircuitView, Port port, Int2D position)
  {
    this.integratedCircuitView = integratedCircuitView;
    this.port = port;
    this.position = position;
    this.integratedCircuitView.addPortView(this);
    this.bubbleCenter = null;
    this.transformedPosition = position.clone();
    this.bubbleDiameter = 0.9f;
  }

  public PortView setInverting(boolean inverting, Rotation facing)
  {
    this.inverting = inverting;
    Int2D portOffset = new Int2D(0, -1);
    facing.transform(portOffset);
    bubbleCenter = new Float2D(position);
    position.add(portOffset);
    Float2D bubbleOffset = new Float2D(0, -0.5f);
    facing.transform(bubbleOffset);
    bubbleCenter.add(bubbleOffset);
    transformedBubbleCenter = bubbleCenter.clone();
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

  public void paint(Graphics2D graphics, Viewport viewport, Rotation rotation, Int2D position)
  {
    if (inverting)
    {
      transformedBubbleCenter.set(this.bubbleCenter);
      rotation.transform(transformedBubbleCenter);
      int x = viewport.transformGridToScreenSpaceX(transformedBubbleCenter.x + position.x);
      int y = viewport.transformGridToScreenSpaceY(transformedBubbleCenter.y + position.y);

      graphics.setStroke(new BasicStroke(viewport.getLineWidth()));
      graphics.setColor(viewport.getColours().getShapeBorder());
      int diameter = viewport.transformGridToScreenWidth(bubbleDiameter);
      graphics.drawOval(x - diameter / 2,
                        y - diameter / 2,
                        diameter,
                        diameter);
    }

    transformedPosition.set(this.position);
    rotation.transform(transformedPosition);
    int x = viewport.transformGridToScreenSpaceX(transformedPosition.x + position.x);
    int y = viewport.transformGridToScreenSpaceY(transformedPosition.y + position.y);
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

  public void updateBoundingBox(BoundingBox boundingBox)
  {
    if (inverting)
    {
      boundingBox.include(bubbleCenter, bubbleDiameter / 2);
    }
    boundingBox.include(position);
  }

  public void getGridPosition(Int2D destination)
  {
    destination.set(integratedCircuitView.getPosition());
    destination.add(transformedPosition);
  }

  public void paintHoverPort(Graphics2D graphics, Viewport viewport)
  {
    Int2D destination = new Int2D();
    getGridPosition(destination);
    int x = viewport.transformGridToScreenSpaceX(destination.x);
    int y = viewport.transformGridToScreenSpaceY(destination.y);
    int radius = (int) (viewport.getCircleRadius() * 5);
    graphics.setColor(viewport.getColours().getPortHover());
    graphics.setStroke(new BasicStroke(viewport.getLineWidth()));
    graphics.drawOval(x - radius, y - radius, radius * 2, radius * 2);
  }
}

