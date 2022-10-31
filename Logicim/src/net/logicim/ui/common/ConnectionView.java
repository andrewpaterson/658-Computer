package net.logicim.ui.common;

import net.logicim.common.type.Int2D;

import java.awt.*;

public abstract class ConnectionView
{
  public abstract void invalidateCache();

  public abstract void getGridPosition(Int2D destination);

  public void paintHoverPort(Graphics2D graphics, Viewport viewport)
  {
    Int2D destination = new Int2D();
    getGridPosition(destination);
    int x = viewport.transformGridToScreenSpaceX(destination.x);
    int y = viewport.transformGridToScreenSpaceY(destination.y);
    int radius = (int) (viewport.getCircleRadius() * 5);
    graphics.setColor(viewport.getColours().getPortHover());
    graphics.setStroke(new BasicStroke(viewport.getLineWidth()));
    graphics.drawOval(x - radius, y - radius, radius * 2, radius * 2);
  }

  public abstract boolean equals(int x, int y);
}

