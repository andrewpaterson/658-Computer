package net.logicim.ui.common.integratedcircuit;

import net.common.type.Int2D;
import net.logicim.domain.common.Described;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.Viewport;

import java.awt.*;
import java.util.Collection;
import java.util.List;

public abstract class View
    implements Described
{
  public static long nextId = 1L;

  protected long id;
  protected boolean enabled;

  public View()
  {
    this(nextId++);
  }

  public View(long id)
  {
    this.id = id;
    if (id >= nextId)
    {
      nextId = id + 1;
    }
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

  public abstract void paint(Graphics2D graphics, Viewport viewport, SubcircuitSimulation subcircuitSimulation);

  public abstract String getName();

  public abstract String getType();

  public abstract String getDescription();

  public abstract Int2D getPosition();

  public String toIdentifierString()
  {
    return getClass().getSimpleName() + " [" + getName() + "]";
  }

  public void setId(long id)
  {
    this.id = id;
    if (id >= nextId)
    {
      nextId = id + 1;
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

  public abstract List<ConnectionView> getConnectionViews();

  public long getId()
  {
    return id;
  }

  public static void resetNextId()
  {
    nextId = 1L;
  }

  public String toSimulationsDebugString(Collection<? extends SubcircuitSimulation> subcircuitSimulations)
  {
    StringBuilder builder = new StringBuilder();
    for (SubcircuitSimulation simulation : subcircuitSimulations)
    {
      builder.append("Simulation [");
      builder.append(simulation.getDescription());
      builder.append("]\n");
    }
    return builder.toString();
  }
}

