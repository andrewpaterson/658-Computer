package net.logicim.ui.simulation.component.decorative.label;

import net.logicim.common.type.Int2D;
import net.logicim.data.common.ReflectiveData;
import net.logicim.data.decorative.label.LabelProperties;
import net.logicim.data.integratedcircuit.decorative.LabelData;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.Component;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.shape.rectangle.RectangleView;
import net.logicim.ui.shape.text.TextView;
import net.logicim.ui.simulation.component.decorative.common.DecorativeView;

import java.awt.*;

import static java.awt.Font.SANS_SERIF;

public class LabelView
    extends DecorativeView<LabelProperties>
{
  public static int FONT_SIZE = 10;

  protected TextView textView;
  protected RectangleView rectangleView;

  public LabelView(SubcircuitView subcircuitView,
                   Int2D position,
                   Rotation rotation,
                   LabelProperties properties)
  {
    super(subcircuitView, position, rotation, properties);
    createGraphics();
    finaliseView();
  }

  private void createGraphics()
  {
    int size = FONT_SIZE;
    textView = new TextView(this,
                            new Int2D(0, 0),
                            properties.name,
                            SANS_SERIF,
                            size,
                            properties.bold,
                            properties.alignment);

    rectangleView = new RectangleView(this,
                                      textView.calculateTopLeft(),
                                      textView.calculateBottomRight(),
                                      properties.border,
                                      properties.fill);
  }

  @Override
  public void clampProperties(LabelProperties newProperties)
  {
  }

  @Override
  public Component createComponent(SubcircuitSimulation subcircuitSimulation)
  {
    return null;
  }

  @Override
  public void destroyComponent(CircuitSimulation circuitSimulation)
  {
  }

  @Override
  public void destroyComponent(SubcircuitSimulation subcircuitSimulation)
  {
  }

  @Override
  public ReflectiveData save(boolean selected)
  {
    return new LabelData(properties.name,
                         position,
                         rotation,
                         id,
                         enabled,
                         selected,
                         properties.alignment,
                         properties.bold,
                         properties.fill,
                         properties.border);
  }

  @Override
  public String getType()
  {
    return "Label";
  }

  @Override
  public void paint(Graphics2D graphics,
                    Viewport viewport,
                    SubcircuitSimulation subcircuitSimulation)
  {
    super.paint(graphics, viewport, subcircuitSimulation);

    Color color = graphics.getColor();
    Stroke stroke = graphics.getStroke();
    Font font = graphics.getFont();

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

  @Override
  public String toDebugString()
  {
    return super.toDebugString() + String.format("Label [%s]\nAlignment [%s]\nBold [%s]\nFill [%s]\nBorder[%s]\n" +
                                                 properties.name,
                                                 properties.alignment,
                                                 properties.bold,
                                                 properties.fill,
                                                 properties.border);
  }
}

