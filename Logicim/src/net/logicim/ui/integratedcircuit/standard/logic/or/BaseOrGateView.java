package net.logicim.ui.integratedcircuit.standard.logic.or;

import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.integratedcircuit.standard.logic.common.LogicGateProperties;
import net.logicim.ui.integratedcircuit.standard.logic.common.LogicGateView;
import net.logicim.ui.shape.arc.ArcView;
import net.logicim.ui.shape.common.BoundingBox;
import net.logicim.ui.shape.polygon.PolygonView;

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

  public BaseOrGateView(CircuitEditor circuitEditor,
                        Int2D position,
                        Rotation rotation,
                        LogicGateProperties properties)
  {
    super(circuitEditor, position, rotation, properties);
    createGraphics();
  }

  protected void createGraphics()
  {
    arcViewRight = new ArcView(this, new Float2D(-1.5f, 0.6f), 3, 357, 61, true, false);
    arcViewLeft = new ArcView(this, new Float2D(1.5f, 0.6f), 3, 122, 61, true, false);
    arcViewBottom = new ArcView(this, new Float2D(0, 3.5f), 3, 61, 58, true, false);
    arcViewFillRight = new ArcView(this, new Float2D(-1.5f, 0.6f), 3, 0, 58, false, true);
    arcViewFillLeft = new ArcView(this, new Float2D(1.5f, 0.6f), 3, 122, 58, false, true);

    polygonViewFillLeft = new PolygonView(this, false, true, new Float2D(-1.5f, 0.9f), new Float2D(-1.5f, 0.0f), new Float2D(0, 0.0f));
    polygonViewFillRight = new PolygonView(this, false, true, new Float2D(1.5f, 0.9f), new Float2D(1.5f, 0.0f), new Float2D(0, 0.0f));
  }

  @Override
  protected void updateBoundingBoxFromShapes(BoundingBox boundingBox)
  {
    super.updateBoundingBoxFromShapes(boundingBox);
    this.boundingBox.include(1.5f, 0f);
    this.boundingBox.include(-1.5f, 0f);
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport, long time)
  {
    super.paint(graphics, viewport, time);
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
      paintPorts(graphics, viewport, time);

      graphics.setStroke(stroke);
      graphics.setColor(color);
    }
  }
}

