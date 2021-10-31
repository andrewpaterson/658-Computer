package net.logisim.common;

import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.data.Location;
import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.util.GraphicsUtil;
import net.common.IntegratedCircuit;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;

import static net.logisim.common.ComponentDescription.PIXELS_PER_PIN;
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
      paintPorts(painter, instance, graphics2D);

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

  default void paintPorts(InstancePainter painter, T instance, Graphics2D graphics2D)
  {
    ComponentDescription description = getDescription();
    boolean clock = !instance.isClockHigh();
    painter.drawBounds();
    Location location = painter.getLocation();

    List<PortDescription> ports = description.getPorts();
    Font font = graphics2D.getFont();
    FontMetrics metrics = graphics2D.getFontMetrics(font);
    for (PortDescription portDescription : ports)
    {
      if (portDescription.notBlank())
      {
        String label = !clock ? portDescription.getLowName() : portDescription.getHighName();
        painter.drawPort(portDescription.index());

        int width = metrics.stringWidth(label);
        int height = metrics.getHeight();
        int x = description.getPortX(portDescription, false) + location.getX();
        int y = description.getPortY(portDescription) + location.getY();
        int vCenter = y - height / 2 + 3;
        if (portDescription.isPosition(LEFT))
        {
          GraphicsUtil.drawText(graphics2D, label, x + 3, y, GraphicsUtil.H_LEFT, GraphicsUtil.V_CENTER);
          if (portDescription.isDrawBar())
          {
            graphics2D.drawLine(x + 3, vCenter, x + width + 3, vCenter);
          }

          if (portDescription.isInverting())
          {
            GraphicsUtil.switchToWidth(graphics2D, 2);
            int halfPixels = PIXELS_PER_PIN / 2 - 1;
            graphics2D.drawOval(x - halfPixels - 1, y - halfPixels / 2, halfPixels, halfPixels);
            GraphicsUtil.switchToWidth(graphics2D, 1);
          }
        }
        else if (portDescription.isPosition(RIGHT))
        {
          GraphicsUtil.drawText(graphics2D, label, x - 3, y, GraphicsUtil.H_RIGHT, GraphicsUtil.V_CENTER);
          if (portDescription.isDrawBar())
          {
            graphics2D.drawLine(x - 3 - width, vCenter, x - 3, vCenter);
          }

          if (portDescription.isInverting())
          {
            GraphicsUtil.switchToWidth(graphics2D, 2);
            int halfPixels = PIXELS_PER_PIN / 2 - 1;
            graphics2D.drawOval(x + 1, y - halfPixels / 2, halfPixels, halfPixels);
            GraphicsUtil.switchToWidth(graphics2D, 1);
          }
        }
        else if (portDescription.isPosition(BOTTOM))
        {
          GraphicsUtil.drawText(graphics2D, label, x, y - 3, GraphicsUtil.H_CENTER, GraphicsUtil.V_BASELINE);
        }
        else if (portDescription.isPosition(TOP))
        {
          GraphicsUtil.drawText(graphics2D, label, x, y + 3, GraphicsUtil.H_CENTER, GraphicsUtil.V_TOP);
        }
      }
    }
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
    g.drawRect(0, y - 5, rectangleWidth, 15);
    GraphicsUtil.drawText(g, label, -5, y, GraphicsUtil.H_RIGHT, GraphicsUtil.V_CENTER);
    Color oldColour = setColour(g, black);
    GraphicsUtil.drawText(g, value, 5, y, GraphicsUtil.H_LEFT, GraphicsUtil.V_CENTER);
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

