package net.logicim.ui.simulation.component.subcircuit;

import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.decorative.HorizontalAlignment;
import net.logicim.domain.CircuitSimulation;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.shape.circle.CircleView;
import net.logicim.ui.shape.common.ShapeView;
import net.logicim.ui.shape.line.LineView;
import net.logicim.ui.shape.point.PointGridCache;
import net.logicim.ui.shape.polygon.PolygonView;
import net.logicim.ui.shape.text.TextView;
import net.logicim.ui.simulation.component.passive.pin.PinView;

import java.awt.*;
import java.util.List;

public class SubcircuitPinView
{
  protected PortView portView;

  protected PinView pinView;
  protected SubcircuitInstanceView subcircuitInstanceView;

  protected Int2D positionRelativeToIC;
  protected PointGridCache positionCache;

  protected TextView textView;
  protected ShapeView shapeView;
  protected PolygonView clockView;

  public SubcircuitPinView(PinView pinView,
                           SubcircuitInstanceView subcircuitInstanceView,
                           Int2D positionRelativeToIC,
                           String fontName,
                           int size,
                           HorizontalAlignment horizontalAlignment,
                           int additionalRotations)
  {
    this.pinView = pinView;
    this.subcircuitInstanceView = subcircuitInstanceView;
    this.portView = null;
    this.positionRelativeToIC = calculatePosition(positionRelativeToIC, horizontalAlignment, additionalRotations, 0.0f, -1.0f).cloneAsInt2D();
    this.positionCache = new PointGridCache(positionRelativeToIC);
    this.textView = new TextView(subcircuitInstanceView,
                                 calculatePosition(positionRelativeToIC, horizontalAlignment, additionalRotations, 0.0f, 0.5f),
                                 pinView.getName(),
                                 fontName,
                                 size,
                                 false,
                                 horizontalAlignment);
    this.textView.setRelativeRightRotations(additionalRotations);
    this.shapeView = createPinShape(pinView, subcircuitInstanceView, positionRelativeToIC, horizontalAlignment, additionalRotations);

    if (pinView.getProperties().clockNotch)
    {
      clockView = new PolygonView(subcircuitInstanceView,
                                  null,
                                  true,
                                  false,
                                  1.0f,
                                  calculatePosition(positionRelativeToIC, horizontalAlignment, additionalRotations, -0.5f, 0.0f),
                                  calculatePosition(positionRelativeToIC, horizontalAlignment, additionalRotations, 0.0f, 0.5f),
                                  calculatePosition(positionRelativeToIC, horizontalAlignment, additionalRotations, 0.5f, 0.0f));
    }
  }

  protected ShapeView createPinShape(PinView pinView, SubcircuitInstanceView subcircuitInstanceView, Int2D positionRelativeToIC, HorizontalAlignment horizontalAlignment, int additionalRotations)
  {
    if (!pinView.getProperties().inverting)
    {
      return new LineView(subcircuitInstanceView, this.positionRelativeToIC, positionRelativeToIC);
    }
    else
    {
      return new CircleView(subcircuitInstanceView, calculatePosition(positionRelativeToIC, horizontalAlignment, additionalRotations, 0.0f, -0.5f), 0.5f, true, false);
    }
  }

  protected Float2D calculatePosition(Int2D positionRelativeToIC,
                                      HorizontalAlignment horizontalAlignment,
                                      int additionalRotations,
                                      float xOffset,
                                      float yOffset)
  {
    Float2D clone = new Float2D(positionRelativeToIC);
    Float2D offset2D = new Float2D(xOffset, yOffset * horizontalAlignment.getModifier());
    Rotation rotation = Rotation.North;
    rotation = rotation.rotateRight(additionalRotations);
    rotation.transform(offset2D);
    clone.add(offset2D);
    return clone;
  }

  public void updateGridCache()
  {
    if (!positionCache.isValid())
    {
      positionCache.update(positionRelativeToIC, subcircuitInstanceView.getRotation(), subcircuitInstanceView.getPosition());
    }
  }

  public void paint(Graphics2D graphics, Viewport viewport)
  {
    textView.paint(graphics, viewport);
    shapeView.paint(graphics, viewport);
    if (clockView != null)
    {
      clockView.paint(graphics, viewport);
    }
  }

  public void disconnectView()
  {
    portView.disconnectView();
  }

  protected PortView createPortView(List<String> tracePortNames)
  {
    portView = new PortView(subcircuitInstanceView, tracePortNames, positionRelativeToIC);
    return portView;
  }

  public ConnectionView getConnection()
  {
    return portView.getConnection();
  }

  public PinView getPinView()
  {
    return pinView;
  }
}

