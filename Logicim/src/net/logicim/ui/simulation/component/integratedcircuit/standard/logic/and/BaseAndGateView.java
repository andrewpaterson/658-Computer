package net.logicim.ui.simulation.component.integratedcircuit.standard.logic.and;

import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.logic.common.LogicGateProperties;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.shape.arc.ArcView;
import net.logicim.ui.shape.common.BoundingBox;
import net.logicim.ui.shape.line.LineView;
import net.logicim.ui.shape.rectangle.RectangleView;
import net.logicim.ui.simulation.component.integratedcircuit.standard.logic.common.LogicGateView;

import java.awt.*;

public abstract class BaseAndGateView<IC extends IntegratedCircuit<?, ?>>
    extends LogicGateView<IC>
{
  protected ArcView arcViewFront;
  protected RectangleView rectangleViewBack;
  protected LineView lineView1;
  protected LineView lineView2;
  protected LineView lineView3;

  public BaseAndGateView(SubcircuitView subcircuitView,
                         Int2D position,
                         Rotation rotation,
                         LogicGateProperties properties)
  {
    super(subcircuitView, position, rotation, properties);
    createGraphics();
  }

  protected void createGraphics()
  {
    float height = 1.4f;
    float bottom = -0.5f + height;
    float width = calculateWidth(properties.inputCount);
    arcViewFront = new ArcView(this, new Float2D(0, -0.4f), width, 1.5f, 0, 180, true, true);
    rectangleViewBack = new RectangleView(this, new Float2D(-width, -0.5f), new Float2D(width, bottom), false, true);
    lineView1 = new LineView(this, new Float2D(-width, -0.5f), new Float2D(-width, bottom));
    lineView2 = new LineView(this, new Float2D(-width, bottom), new Float2D(width, bottom));
    lineView3 = new LineView(this, new Float2D(width, bottom), new Float2D(width, -0.5f));
  }

  @Override
  protected void updateBoundingBoxFromShapes(BoundingBox boundingBox)
  {
    super.updateBoundingBoxFromShapes(boundingBox);
    boundingBox.include(1.5f, 0f);
    boundingBox.include(-1.5f, 0f);
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport, CircuitSimulation circuitSimulation)
  {
    super.paint(graphics, viewport, circuitSimulation);
    if ((arcViewFront != null) && (rectangleViewBack != null))
    {
      Stroke stroke = graphics.getStroke();
      Color color = graphics.getColor();

      rectangleViewBack.paint(graphics, viewport);
      arcViewFront.paint(graphics, viewport);
      lineView1.paint(graphics, viewport);
      lineView2.paint(graphics, viewport);
      lineView3.paint(graphics, viewport);
      paintPorts(graphics, viewport, circuitSimulation);

      graphics.setStroke(stroke);
      graphics.setColor(color);
    }
  }
}

