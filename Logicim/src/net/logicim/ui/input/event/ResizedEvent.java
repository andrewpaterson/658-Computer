package net.logicim.ui.input.event;

import net.logicim.ui.Logicim;

public class ResizedEvent
    extends SimulatorEditorEvent
{
  protected int width;
  protected int height;

  public ResizedEvent(int width, int height)
  {
    super();
    this.width = width;
    this.height = height;
  }

  @Override
  public void execute(Logicim logicim)
  {
    logicim.resized(width, height);
  }
}

