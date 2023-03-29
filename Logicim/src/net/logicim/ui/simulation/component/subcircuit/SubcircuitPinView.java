package net.logicim.ui.simulation.component.subcircuit;

import net.logicim.common.SimulatorException;
import net.logicim.common.type.Int2D;
import net.logicim.common.type.Tuple2;
import net.logicim.data.integratedcircuit.decorative.HorizontalAlignment;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.shape.point.PointGridCache;
import net.logicim.ui.shape.text.TextView;
import net.logicim.ui.simulation.component.passive.pin.PinView;

import java.awt.*;

public class SubcircuitPinView
{
  protected ConnectionView connection;

  protected PinView pinView;
  protected SubcircuitInstanceView subcircuitInstanceView;

  protected Int2D positionRelativeToIC;
  protected PointGridCache positionCache;

  protected TextView textView;

  public SubcircuitPinView(PinView pinView,
                           SubcircuitInstanceView subcircuitInstanceView,
                           Int2D positionRelativeToIC,
                           String fontName,
                           int size,
                           HorizontalAlignment horizontalAlignment)
  {
    this.pinView = pinView;
    this.subcircuitInstanceView = subcircuitInstanceView;
    this.connection = null;
    this.positionRelativeToIC = positionRelativeToIC.clone();
    this.positionCache = new PointGridCache(positionRelativeToIC);
    this.textView = new TextView(subcircuitInstanceView,
                                 positionRelativeToIC,
                                 pinView.getName(),
                                 fontName,
                                 size,
                                 false,
                                 horizontalAlignment);
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
      positionCache.update(positionRelativeToIC, subcircuitInstanceView.getRotation(), subcircuitInstanceView.getPosition());
    }
  }

  public void paint(Graphics2D graphics, Viewport viewport)
  {
    textView.paint(graphics, viewport);

    updateGridCache();
    Tuple2 transformedPosition = positionCache.getTransformedPosition();
  }

  public ConnectionView getOrAddConnection(SubcircuitView subcircuitView)
  {
    if (connection == null)
    {
      updateGridCache();

      ConnectionView connection = subcircuitView.getOrAddConnection((Int2D) positionCache.getTransformedPosition(), subcircuitInstanceView);
      this.connection = connection;
      return connection;
    }
    else
    {
      throw new SimulatorException("Connection is already set.");
    }
  }

  public void disconnect()
  {
    connection = null;
  }
}

