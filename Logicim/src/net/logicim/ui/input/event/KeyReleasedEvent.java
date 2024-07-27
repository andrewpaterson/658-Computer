package net.logicim.ui.input.event;

import net.logicim.ui.Logicim;

public class KeyReleasedEvent
    extends SimulatorEditorEvent
{
  protected int keyCode;
  protected boolean controlDown;
  protected boolean altDown;
  protected boolean shiftDown;
  protected boolean metaDown;

  public KeyReleasedEvent(int keyCode,
                          boolean controlDown,
                          boolean altDown,
                          boolean shiftDown,
                          boolean metaDown)
  {
    super();
    this.keyCode = keyCode;
    this.controlDown = controlDown;
    this.altDown = altDown;
    this.shiftDown = shiftDown;
    this.metaDown = metaDown;
  }

  @Override
  public void execute(Logicim logicim)
  {
    logicim.keyReleased(keyCode,
                        controlDown,
                        altDown,
                        shiftDown
    );
  }
}

