package net.logicim.ui.common.wire;

import net.logicim.common.type.Int2D;
import net.logicim.data.wire.TunnelData;
import net.logicim.domain.common.wire.Trace;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.Viewport;

import java.awt.*;
import java.util.List;

public class TunnelView
    extends WireView
{
  protected List<Trace> traces;
  protected ConnectionView connection;
  protected String name;
  protected Int2D position;

  @Override
  public ConnectionView getConnectionsInGrid(int x, int y)
  {
    if (position.equals(x, y))
    {
      return connection;
    }
    return null;
  }

  @Override
  public Int2D getConnectionGridPosition(ConnectionView connectionView)
  {
    if (this.connection == connectionView)
    {
      return position;
    }
    return null;
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport, long time)
  {
  }

  @Override
  public void paintSelected(Graphics2D graphics, Viewport viewport)
  {
  }

  @Override
  public String getName()
  {
    return name;
  }

  @Override
  public String getDescription()
  {
    return "Tunnel [" + name + "] (" + position + ")";
  }

  @Override
  public Int2D getPosition()
  {
    return position;
  }

  @Override
  public void setPosition(int x, int y)
  {
    position.set(x, y);
  }

  public TunnelData save(boolean selected)
  {
    long[] ids = new long[traces.size()];
    for (int i = 0; i < traces.size(); i++)
    {
      Trace trace = traces.get(i);
      ids[i] = Trace.getId(trace);
    }

    return new TunnelData(ids,
                          getPosition(),
                          selected);
  }
}

