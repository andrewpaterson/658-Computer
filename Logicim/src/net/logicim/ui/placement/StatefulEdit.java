package net.logicim.ui.placement;

import net.logicim.ui.common.Viewport;

import java.awt.*;

public abstract class StatefulEdit
{
  public abstract void start(float x, float y, EditAction editAction);

  public abstract StatefulEdit move(float x, float y, EditAction editAction);

  public abstract StatefulEdit rotate(boolean right, EditAction editAction);

  public abstract void done(float x, float y, EditAction editAction);

  public abstract void discard(EditAction editAction);

  public abstract void paint(Graphics2D graphics, Viewport viewport);

  public abstract boolean canTransformComponents();
}

