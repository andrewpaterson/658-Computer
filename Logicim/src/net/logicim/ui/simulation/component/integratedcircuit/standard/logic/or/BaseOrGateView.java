package net.logicim.ui.simulation.component.integratedcircuit.standard.logic.or;

import net.common.type.Float2D;
import net.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.logic.common.LogicGateProperties;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.shape.arc.ArcView;
import net.logicim.ui.shape.common.BoundingBox;
import net.logicim.ui.shape.polygon.PolygonView;
import net.logicim.ui.simulation.component.integratedcircuit.standard.logic.common.LogicGateView;

import java.awt.*;

public abstract class BaseOrGateView<IC extends IntegratedCircuit<?, ?>>
    extends LogicGateView<IC>
{
  protected ArcView arcViewRight;
  protected ArcView arcViewLeft;
  protected ArcView arcViewBottom;
  protected ArcView arcViewFillRight;
  protected ArcView arcViewFillLeft;
  protected PolygonView polygonViewFillRight;
  protected PolygonView polygonViewFillLeft;

  public BaseOrGateView(SubcircuitView subcircuitView,
                        Int2D position,
                        Rotation rotation,
                        LogicGateProperties properties)
  {
    super(subcircuitView,
          position,
          rotation,
          properties);
    createGraphics();
    createPortViews();
    finaliseView();
  }

  protected void createGraphics()
  {
    float width = calculateWidth(properties.inputCount);
    float radius = width * 2;
    float yTop = 0.6f;
    float yBottom = 3.5f;
    float yMiddle = 0.9f;
    float yZero = 0.0f;

    arcViewRight = new ArcView(this, new Float2D(-width, yTop), radius, 3, OR_ARC_RIGHT_START, OR_ARC_SIDE_LENGTH, true, false);
    arcViewLeft = new ArcView(this, new Float2D(width, yTop), radius, 3, OR_ARC_LEFT_START, OR_ARC_SIDE_LENGTH, true, false);
    arcViewBottom = new ArcView(this, new Float2D(0, yBottom), radius, 3, OR_ARC_BOTTOM_START, OR_ARC_BOTTOM_LENGTH, true, false);
    arcViewFillRight = new ArcView(this, new Float2D(-width, yTop), radius, 3, OR_ARC_RIGHT_START + OR_ARC_FILL_OFFSET, OR_ARC_SIDE_LENGTH - OR_ARC_FILL_OFFSET, false, true);
    arcViewFillLeft = new ArcView(this, new Float2D(width, yTop), radius, 3, OR_ARC_LEFT_START, OR_ARC_SIDE_LENGTH - OR_ARC_FILL_OFFSET, false, true);

    polygonViewFillLeft = new PolygonView(this,
                                          false,
                                          true,
                                          new Float2D(-width, yMiddle),
                                          new Float2D(-width + OR_FILL_OFFSET, yZero),
                                          new Float2D(0, yZero));
    polygonViewFillRight = new PolygonView(this,
                                           false,
                                           true,
                                           new Float2D(width, yMiddle),
                                           new Float2D(width - OR_FILL_OFFSET, yZero),
                                           new Float2D(0, yZero));
  }

  @Override
  protected void updateBoundingBoxFromShapes(BoundingBox boundingBox)
  {
    super.updateBoundingBoxFromShapes(boundingBox);
    boundingBox.include(1.5f, 0f);
    boundingBox.include(-1.5f, 0f);
  }

  @Override
  public void paint(Graphics2D graphics,
                    Viewport viewport,
                    SubcircuitSimulation subcircuitSimulation)
  {
    super.paint(graphics, viewport, subcircuitSimulation);
    if ((arcViewRight != null) && (arcViewLeft != null) && (arcViewBottom != null))
    {
      Stroke stroke = graphics.getStroke();
      Color color = graphics.getColor();

      arcViewFillLeft.paint(graphics, viewport);
      arcViewFillRight.paint(graphics, viewport);
      polygonViewFillRight.paint(graphics, viewport);
      polygonViewFillLeft.paint(graphics, viewport);
      arcViewLeft.paint(graphics, viewport);
      arcViewRight.paint(graphics, viewport);
      arcViewBottom.paint(graphics, viewport);
      paintPorts(graphics, viewport, subcircuitSimulation);

      graphics.setStroke(stroke);
      graphics.setColor(color);
    }
  }
}

