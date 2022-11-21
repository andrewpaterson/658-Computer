package net.logicim.ui.integratedcircuit.standard.logic.and;

import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.integratedcircuit.standard.logic.common.LogicGateView;
import net.logicim.ui.shape.arc.ArcView;
import net.logicim.ui.shape.line.LineView;
import net.logicim.ui.shape.rectangle.RectangleView;

import java.awt.*;

public abstract class BaseAndGateView<IC extends IntegratedCircuit<?, ?>>
    extends LogicGateView<IC>
{
  protected ArcView arcViewFront;
  protected RectangleView rectangleViewBack;
  protected LineView lineView1;
  protected LineView lineView2;
  protected LineView lineView3;

  public BaseAndGateView(CircuitEditor circuitEditor,
                         int inputCount,
                         Int2D position,
                         Rotation rotation)
  {
    super(circuitEditor, inputCount, position, rotation);

    float height = 1.4f;
    arcViewFront = new ArcView(this, new Float2D(0, -0.4f), 1.5f, 0, 180, true, true);
    rectangleViewBack = new RectangleView(this, new Float2D(-1.5f, -0.5f), new Float2D(3, height), false, true);
    lineView1 = new LineView(this, new Float2D(-1.5f, -0.5f), new Float2D(-1.5f, -0.5f + height));
    lineView2 = new LineView(this, new Float2D(-1.5f, -0.5f + height), new Float2D(1.5f, -0.5f + height));
    lineView3 = new LineView(this, new Float2D(1.5f, -0.5f + height), new Float2D(1.5f, -0.5f));
  }

  @Override
  protected void updateBoundingBox()
  {
    boundingBox.include(1.5f, 0f);
    boundingBox.include(-1.5f, 0f);
    super.updateBoundingBox();
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport, long time)
  {
    if ((arcViewFront != null) && (rectangleViewBack != null))
    {
      Stroke stroke = graphics.getStroke();
      Color color = graphics.getColor();

      rectangleViewBack.paint(graphics, viewport);
      arcViewFront.paint(graphics, viewport);
      lineView1.paint(graphics, viewport);
      lineView2.paint(graphics, viewport);
      lineView3.paint(graphics, viewport);
      super.paint(graphics, viewport, time);

      graphics.setStroke(stroke);
      graphics.setColor(color);
    }
  }
}

