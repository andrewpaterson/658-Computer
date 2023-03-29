package net.logicim.ui.simulation.component.subcircuit;

import net.logicim.common.type.Int2D;
import net.logicim.common.type.Tuple2;
import net.logicim.data.integratedcircuit.decorative.HorizontalAlignment;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.port.TracePort;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.shape.point.PointGridCache;
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

  public SubcircuitPinView(PinView pinView,
                           SubcircuitInstanceView subcircuitInstanceView,
                           Int2D positionRelativeToIC,
                           String fontName,
                           int size,
                           HorizontalAlignment horizontalAlignment)
  {
    this.pinView = pinView;
    this.subcircuitInstanceView = subcircuitInstanceView;
    this.portView = null;
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

  public void disconnect(Simulation simulation)
  {
    portView.disconnect(simulation);
  }

  protected PortView createPortView(List<TracePort> tracePorts)
  {
    portView = new PortView(subcircuitInstanceView, tracePorts, positionRelativeToIC);
    return portView;
  }

  public ConnectionView getConnection()
  {
    return portView.getConnection();
  }

  public String getPinName()
  {
    return pinView.getName();
  }
}

