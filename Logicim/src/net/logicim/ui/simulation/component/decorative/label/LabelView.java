package net.logicim.ui.simulation.component.decorative.label;

import net.logicim.common.type.Int2D;
import net.logicim.data.ReflectiveData;
import net.logicim.data.integratedcircuit.decorative.HorizontalAlignment;
import net.logicim.data.integratedcircuit.decorative.LabelData;
import net.logicim.data.integratedcircuit.decorative.VerticalAlignment;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.shape.text.TextView;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.component.decorative.common.DecorativeView;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class LabelView
    extends DecorativeView<LabelProperties>
{
  protected TextView textView;

  public LabelView(CircuitEditor circuitEditor, Int2D position, Rotation rotation, LabelProperties properties)
  {
    super(circuitEditor, position, rotation, properties);
    createGraphics();
    finaliseView();
  }

  private void createGraphics()
  {
    textView = new TextView(this, new Int2D(0, 0), properties.name, 10, false, true, Rotation.Cannot, HorizontalAlignment.LEFT, VerticalAlignment.TOP);
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

    if (textView != null)
    {
      AffineTransform transform = graphics.getTransform();
      Color color = graphics.getColor();
      Stroke stroke = graphics.getStroke();
      Font font = graphics.getFont();

      graphics.setColor(Color.BLACK);
      textView.paint(graphics, viewport);

      graphics.setFont(font);
      graphics.setColor(color);
      graphics.setStroke(stroke);
      graphics.setTransform(transform);
    }
  }
}

