package net.logicim.ui.input.event;

import net.logicim.ui.Logicim;
import net.logicim.ui.editor.EditorAction;

public class ButtonPressedEvent
    extends SimulatorEditorEvent
{
  private EditorAction action;

  public ButtonPressedEvent(EditorAction action)
  {
    this.action = action;
  }

  @Override
  public void execute(Logicim logicim)
  {
    action.executeEditorAction();
  }
}

