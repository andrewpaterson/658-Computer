package net.logicim.ui.simulation.component.common;

import net.common.type.Float2D;
import net.logicim.ui.common.Colours;
import net.logicim.ui.common.ShapeHolder;
import net.logicim.ui.shape.rectangle.RectangleView;
import net.logicim.ui.shape.text.TextView;

import java.util.List;

import static java.awt.Font.MONOSPACED;
import static java.awt.Font.SANS_SERIF;
import static net.logicim.data.integratedcircuit.decorative.HorizontalAlignment.LEFT;
import static net.logicim.data.integratedcircuit.decorative.HorizontalAlignment.RIGHT;

public abstract class DetailView
{
  public static TextView create(ShapeHolder shapeHolder,
                          List<TextView> labels,
                          List<RectangleView> rectangles,
                          float y,
                          int width,
                          String name,
                          String value,
                          int fontSize)
  {
    createDetailLabel(shapeHolder, labels, name, y, fontSize);
    createDetailRectangle(shapeHolder, rectangles, y, width);
    return createDetailValue(shapeHolder, labels, value, y, fontSize);
  }

  private static RectangleView createDetailRectangle(ShapeHolder shapeHolder,
                                              List<RectangleView> rectangles,
                                              float y,
                                              int width)
  {
    RectangleView rectangle = new RectangleView(shapeHolder,
                                                new Float2D(y - 0.6f, 0),
                                                new Float2D(y + 0.6f, -width),
                                                true,
                                                true).setLineWidth(1).setFillColour(Colours.getInstance().getBackground());
    rectangles.add(rectangle);
    return rectangle;
  }

  @SuppressWarnings("SuspiciousNameCombination")
  private static TextView createDetailLabel(ShapeHolder shapeHolder,
                                     List<TextView> labels,
                                     String label,
                                     float y,
                                     float fontSize)
  {
    TextView textView = new TextView(shapeHolder,
                                     new Float2D(y, 0),
                                     label + ": ",
                                     SANS_SERIF,
                                     fontSize,
                                     false,
                                     RIGHT);
    labels.add(textView);
    return textView;
  }

  @SuppressWarnings("SuspiciousNameCombination")
  private static TextView createDetailValue(ShapeHolder shapeHolder,
                                     List<TextView> labels,
                                     String value,
                                     float y,
                                     float fontSize)
  {
    TextView textView = new TextView(shapeHolder,
                                     new Float2D(y, -0.6f),
                                     value,
                                     MONOSPACED,
                                     fontSize,
                                     false,
                                     LEFT).setColor(Colours.getInstance().getCommentText());
    labels.add(textView);
    return textView;
  }
}
