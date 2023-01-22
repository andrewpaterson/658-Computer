package net.logicim.ui.input.event;

import net.logicim.ui.SimulatorEditor;

public class KeyReleasedEvent
    extends SimulatorEditorEvent
{
  protected int keyCode;
  protected boolean controlDown;
  protected boolean altDown;
  protected boolean shiftDown;

  public KeyReleasedEvent(int keyCode,
                          boolean controlDown,
                          boolean altDown,
                          boolean shiftDown)
  {
    super();
    this.keyCode = keyCode;
    this.controlDown = controlDown;
    this.altDown = altDown;
    this.shiftDown = shiftDown;
  }

  @Override
  public void execute(SimulatorEditor simulatorEditor)
  {
    simulatorEditor.keyReleased(keyCode, controlDown, altDown, shiftDown);
  }
}

