package net.logicim.ui.placement;

import net.logicim.ui.Edit;
import net.logicim.ui.common.Viewport;

import java.awt.*;

public abstract class StatefulEdit
{
  public abstract void start(float x, float y, Edit edit);

  public abstract StatefulEdit move(float x, float y, Edit edit);

  public abstract StatefulEdit rotate(boolean right, Edit edit);

  public abstract void done(float x, float y, Edit edit);

  public abstract void discard(Edit edit);

  public abstract void paint(Graphics2D graphics, Viewport viewport);

  public abstract boolean canTransformComponents();
}

