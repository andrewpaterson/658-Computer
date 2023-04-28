package net.logicim.ui.common.integratedcircuit;

import net.logicim.common.type.Int2D;
import net.logicim.domain.CircuitSimulation;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.Viewport;

import java.awt.*;
import java.util.List;

public abstract class View
{
  public static long nextId = 1L;

  protected long id;
  protected boolean enabled;

  public View()
  {
    id = 0;
    enabled = false;
  }

  protected void paintSelectionRectangle(Graphics2D graphics, Viewport viewport, int x, int y, Color viewHover)
  {
    float zoom = viewport.getZoom();
    float radius = zoom * 3;
    int left = (int) (x - radius);
    int top = (int) (y - radius);
    int width = (int) (radius * 2);
    int height = (int) (radius * 2);
    graphics.setColor(viewHover);
    graphics.fillRect(left, top, width, height);
    graphics.setColor(Color.BLACK);
    graphics.drawRect(left, top, width, height);
  }

  public abstract void paintSelected(Graphics2D graphics, Viewport viewport);

  public abstract void paint(Graphics2D graphics, Viewport viewport, CircuitSimulation simulation);

  public abstract String getName();

  public abstract String getType();

  public abstract String getDescription();

  public abstract Int2D getPosition();

  public String toIdentifierString()
  {
    return getClass().getSimpleName() + " [" + getName() + "]";
  }

  protected void updateId()
  {
    id = nextId;
    nextId++;
  }

  public void setId(long id)
  {
    this.id = id;
    if (id >= nextId)
    {
      nextId = id + 1;
    }
  }

  public void updateId(boolean appendIds, long id)
  {
    if (appendIds)
    {
      updateId();
    }
    else
    {
      setId(id);
    }
  }

  public void enable()
  {
    enabled = true;
  }

  public void disable()
  {
    enabled = false;
  }

  public boolean isEnabled()
  {
    return enabled;
  }

  public abstract void setPosition(int x, int y);

  public abstract List<ConnectionView> getConnections();

  public long getId()
  {
    return id;
  }

  public static void resetNextId()
  {
    nextId = 1L;
  }
}

