package net.logisim.common;

import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.util.GraphicsUtil;
import net.common.IntegratedCircuit;

import java.awt.*;
import java.awt.geom.AffineTransform;

public interface LogisimPainter<T extends LogisimPins<?, ?, ?>>
{
  ComponentDescription getDescription();

  void paint(T instance, Graphics2D graphics2D);

  default void paintInstance(InstancePainter painter, T instance)
  {
    Graphics g = painter.getGraphics();
    if (g instanceof Graphics2D)
    {
      Graphics2D graphics2D = (Graphics2D) g;

      ComponentDescription description = getDescription();
      boolean clock = !instance.isClockHigh();

      AffineTransform oldTransform = graphics2D.getTransform();
      AffineTransform newTransform = (AffineTransform) oldTransform.clone();

      SimpleInstancePainter.paintPorts(painter, graphics2D, description, clock);

      Font oldFont = graphics2D.getFont();
      graphics2D.setFont(oldFont.deriveFont(Font.BOLD));
      Bounds bounds = painter.getBounds();

      newTransform.translate(bounds.getX() + bounds.getWidth() / 2.0, bounds.getY() + bounds.getHeight() / 2.0);
      graphics2D.setTransform(newTransform);
      GraphicsUtil.drawCenteredText(graphics2D, description.type, 0, description.getTopYPlusMargin());
      GraphicsUtil.drawCenteredText(graphics2D, description.name, 0, description.getBottomYMinusMargin());

      paint(instance, graphics2D);

      graphics2D.setTransform(oldTransform);
      graphics2D.setFont(oldFont);
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
    SimpleInstancePainter.drawField(g,
                                    topOffset,
                                    rectangleWidth,
                                    label,
                                    value,
                                    black,
                                    description);
  }
}

