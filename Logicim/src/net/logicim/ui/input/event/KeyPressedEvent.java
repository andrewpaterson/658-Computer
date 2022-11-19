package net.logicim.ui.input.event;

import net.logicim.ui.SimulatorEditor;

public class KeyPressedEvent
    extends SimulatorEditorEvent
{
  private int keyCode;

  public KeyPressedEvent(int keyCode)
  {
    super();
    this.keyCode = keyCode;
  }

  @Override
  public void execute(SimulatorEditor simulatorEditor)
  {
    simulatorEditor.keyPressed(keyCode);
  }
}

