package net.logicim.ui.input.event;

import net.logicim.ui.Logicim;

public class KeyPressedEvent
    extends SimulatorEditorEvent
{
  protected int keyCode;
  protected boolean controlDown;
  protected boolean altDown;
  protected boolean shiftDown;
  protected boolean metaDown;

  public KeyPressedEvent(int keyCode,
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
    logicim.keyPressed(keyCode,
                       controlDown,
                       altDown,
                       shiftDown
    );
  }
}

