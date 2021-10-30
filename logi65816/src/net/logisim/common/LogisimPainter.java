package net.logisim.common;

import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.data.Direction;
import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.util.GraphicsUtil;
import net.common.IntegratedCircuit;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;

import static net.logisim.common.PortPosition.*;

public interface LogisimPainter<T extends LogisimPins<?, ?, ?>>
{
  ComponentDescription getDescription();

  void paint(T instance, Graphics2D graphics2D);

  default void paintInstance(InstancePainter painter, T instance)
  {
    Graphics g = painter.getGraphics();
    if (g instanceof Graphics2D)
    {
      ComponentDescription description = getDescription();
      Graphics2D graphics2D = (Graphics2D) g;
      paintPorts(painter, instance);

      Font oldFont = graphics2D.getFont();
      graphics2D.setFont(oldFont.deriveFont(Font.BOLD));
      Bounds bounds = painter.getBounds();

      AffineTransform oldTransform = graphics2D.getTransform();
      AffineTransform newTransform = (AffineTransform) oldTransform.clone();

      newTransform.translate(bounds.getX() + bounds.getWidth() / 2.0, bounds.getY() + bounds.getHeight() / 2.0);
      graphics2D.setTransform(newTransform);
      IntegratedCircuit<?, ?> integratedCircuit = instance.getIntegratedCircuit();
      String type = integratedCircuit.getType();
      String part = integratedCircuit.getPart();
      GraphicsUtil.drawCenteredText(graphics2D, type, 0, description.getTopYPlusMargin());
      GraphicsUtil.drawCenteredText(graphics2D, part, 0, description.getBottomYMinusMargin());

      paint(instance, graphics2D);

      graphics2D.setTransform(oldTransform);
      graphics2D.setFont(oldFont);
    }
  }

  default void paintPorts(InstancePainter painter, T instance)
  {
    ComponentDescription description = getDescription();
    boolean clock = !instance.isClockHigh();
    painter.drawBounds();
    List<PortDescription> ports = description.getPorts();
    for (PortDescription port : ports)
    {
      if (port.notBlank())
      {
        String label = !clock ? port.getLowName() : port.getHighName();
        painter.drawPort(port.index(), label, getDirection(port));
      }
    }
  }

  private Direction getDirection(PortDescription portDescription)
  {
    Direction dir = null;
    if (portDescription.isPosition(RIGHT))
    {
      dir = Direction.WEST;
    }
    else if (portDescription.isPosition(LEFT))
    {
      dir = Direction.EAST;
    }
    else if (portDescription.isPosition(TOP))
    {
      dir = Direction.NORTH;
    }
    else if (portDescription.isPosition(BOTTOM))
    {
      dir = Direction.SOUTH;
    }
    return dir;
  }

  default void drawField(Graphics g,
                         int topOffset,
                         int rectangleWidth,
                         String label,
                         String value,
                         boolean black)
  {
    ComponentDescription description = getDescription();
    int y = description.getTopYPlusMargin() + topOffset;
    g.drawRect(5, y - 5, rectangleWidth, 15);
    GraphicsUtil.drawText(g, label, -10, y, GraphicsUtil.H_RIGHT, GraphicsUtil.V_CENTER);
    Color oldColour = setColour(g, black);
    GraphicsUtil.drawText(g, value, 10, y, GraphicsUtil.H_LEFT, GraphicsUtil.V_CENTER);
    g.setColor(oldColour);
  }

  default Color setColour(Graphics g, boolean black)
  {
    Color oldColour = g.getColor();
    if (black)
    {
      g.setColor(Color.black);
    }
    else
    {
      g.setColor(Color.lightGray);
    }
    return oldColour;
  }
}

