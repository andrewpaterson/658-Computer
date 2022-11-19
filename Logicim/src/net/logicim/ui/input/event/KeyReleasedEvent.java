package net.logicim.ui.input.event;

import net.logicim.ui.SimulatorEditor;

public class KeyReleasedEvent
    extends SimulatorEditorEvent
{
  private int keyCode;

  public KeyReleasedEvent(int keyCode)
  {
    super();
    this.keyCode = keyCode;
  }

  @Override
  public void execute(SimulatorEditor simulatorEditor)
  {
    simulatorEditor.keyReleased(keyCode);
  }
}

