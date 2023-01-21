package net.logicim.ui.simulation.component.decorative.label;

import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.common.type.Tuple2;
import net.logicim.data.ReflectiveData;
import net.logicim.data.integratedcircuit.decorative.LabelData;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.shape.rectangle.RectangleView;
import net.logicim.ui.shape.text.TextView;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.component.decorative.common.DecorativeView;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class LabelView
    extends DecorativeView<LabelProperties>
{
  protected TextView textView;
  protected RectangleView rectangleView;

  public LabelView(CircuitEditor circuitEditor, Int2D position, Rotation rotation, LabelProperties properties)
  {
    super(circuitEditor, position, rotation, properties);
    finaliseView();
  }

  private void createGraphics(Graphics2D graphics, Viewport viewport)
  {
    int size = 10;
    boolean bold = false;
    textView = new TextView(this,
                            new Int2D(0, 0),
                            properties.name,
                            size,
                            bold,
                            true,
                            Rotation.Cannot);
    textView.updateDimension(graphics, viewport);

    Float2D topLeft = textView.getTextOffset();
    Tuple2 bottomRight = textView.getTextDimension();
    bottomRight.add(topLeft);
    rectangleView = new RectangleView(this,
                                      topLeft,
                                      bottomRight,
                                      true,
                                      true);

    updateBoundingBox();
  }

  @Override
  public void propertyChanged()
  {
  }

  @Override
  public ReflectiveData save(boolean selected)
  {
    return new LabelData(properties.name, position, rotation, selected);
  }

  @Override
  public String getType()
  {
    return "Label";
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport, long time)
  {
    super.paint(graphics, viewport, time);

    Color color = graphics.getColor();
    Stroke stroke = graphics.getStroke();
    Font font = graphics.getFont();

    if (textView == null)
    {
      createGraphics(graphics, viewport);
    }

    if (rectangleView != null)
    {
      rectangleView.paint(graphics, viewport);
    }

    if (textView != null)
    {
      graphics.setColor(Color.BLACK);
      textView.paint(graphics, viewport);
    }

    graphics.setFont(font);
    graphics.setColor(color);
    graphics.setStroke(stroke);
  }
}

