package net.logicim.ui.placement;

import net.logicim.ui.common.Viewport;

import java.awt.*;

public abstract class StatefulMove
{
  public abstract void start(float x, float y, StatefulEdit statefulEdit);

  public abstract StatefulMove move(float x, float y, StatefulEdit statefulEdit);

  public abstract StatefulMove rotate(boolean right, StatefulEdit statefulEdit);

  public abstract void done(float x, float y, StatefulEdit statefulEdit);

  public abstract void discard(StatefulEdit statefulEdit);

  public abstract void paint(Graphics2D graphics, Viewport viewport);
}

