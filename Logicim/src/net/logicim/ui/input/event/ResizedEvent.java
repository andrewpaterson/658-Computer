package net.logicim.ui.input.event;

import net.logicim.ui.SimulatorEditor;

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
  public void execute(SimulatorEditor simulatorEditor)
  {
    simulatorEditor.resized(width, height);
  }
}

