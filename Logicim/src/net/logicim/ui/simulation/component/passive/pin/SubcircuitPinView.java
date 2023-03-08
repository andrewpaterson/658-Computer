package net.logicim.ui.simulation.component.passive.pin;

import net.logicim.common.type.Int2D;
import net.logicim.common.type.Tuple2;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.shape.point.PointGridCache;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;

import java.awt.*;

public class SubcircuitPinView
{
  protected long pinViewId;
  protected ConnectionView connection;

  protected PinView pinView;
  protected SubcircuitInstanceView subcircuitView;

  protected Int2D positionRelativeToIC;
  protected PointGridCache positionCache;

  public SubcircuitPinView(PinView pinView, SubcircuitInstanceView subcircuitView, Int2D positionRelativeToIC)
  {
    this.pinViewId = pinView.id;
    this.pinView = pinView;
    this.subcircuitView = subcircuitView;
    this.connection = null;
    this.positionRelativeToIC = positionRelativeToIC.clone();
    this.positionCache =  new PointGridCache(positionRelativeToIC);
  }

  public ConnectionView getConnection()
  {
    return connection;
  }

  public Int2D getPinPosition()
  {
    return pinView.getRelativeInstancePosition();
  }

  public void updateGridCache()
  {
    if (!positionCache.isValid())
    {
      positionCache.update(positionRelativeToIC, subcircuitView.getRotation(), subcircuitView.getPosition());
    }
  }

  public void paint(Graphics2D graphics, Viewport viewport)
  {
    Tuple2 transformedPosition = positionCache.getTransformedPosition();
  }
}

