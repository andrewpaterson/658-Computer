package net.logicim.ui.placement;

import net.logicim.ui.common.Viewport;

import java.awt.*;

public abstract class StatefulEdit
{
  public abstract void start(float x, float y, SimulatorEdit simulatorEdit);

  public abstract StatefulEdit move(float x, float y, SimulatorEdit simulatorEdit);

  public abstract StatefulEdit rotate(boolean right, SimulatorEdit simulatorEdit);

  public abstract void done(float x, float y, SimulatorEdit simulatorEdit);

  public abstract void discard(SimulatorEdit simulatorEdit);

  public abstract void paint(Graphics2D graphics, Viewport viewport);
}

