package net.logicim.ui.common.integratedcircuit;

import net.logicim.common.type.Int2D;
import net.logicim.domain.Simulation;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.shape.common.BoundingBox;
import net.logicim.ui.shape.common.ShapeView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class View
{
  public abstract void paintSelected(Graphics2D graphics, Viewport viewport);

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

  public ConnectionView getConnectionsInGrid(Int2D p)
  {
    return getConnectionsInGrid(p.x, p.y);
  }

  public abstract ConnectionView getConnectionsInGrid(int x, int y);

  public abstract Int2D getConnectionGridPosition(ConnectionView connectionView);

  public abstract void paint(Graphics2D graphics, Viewport viewport, long time);

  public abstract String getName();

  public abstract String getDescription();

  public abstract Int2D getPosition();

  public abstract void enable(Simulation simulation);

  public abstract void disable();

  public abstract void setPosition(int x, int y);
}

