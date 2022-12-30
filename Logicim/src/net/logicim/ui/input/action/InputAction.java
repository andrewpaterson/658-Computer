package net.logicim.ui.input.action;

import net.logicim.ui.editor.EditorAction;

public class InputAction
{
  protected EditorAction action;

  protected ButtonState altHeld;
  protected ButtonState shiftHeld;
  protected ButtonState ctrlHeld;
  protected int keyPressedCode;
  protected int mousePressedCode;

  public InputAction(EditorAction action,
                     int keyPressedCode,
                     ButtonState altHeld,
                     ButtonState shiftHeld,
                     ButtonState ctrlHeld)
  {
    this.action = action;
    this.altHeld = altHeld;
    this.shiftHeld = shiftHeld;
    this.ctrlHeld = ctrlHeld;
    this.keyPressedCode = keyPressedCode;
    this.mousePressedCode = 0;
  }

  public void execute()
  {
    action.executeEditorAction();
  }

  public boolean matched(int keyCode, boolean alt, boolean shift, boolean ctrl)
  {
    if (keyCode == keyPressedCode)
    {
      return matches(altHeld, alt) &&
             matches(ctrlHeld, ctrl) &&
             matches(shiftHeld, shift);
    }
    return false;
  }

  private boolean matches(ButtonState buttonState, boolean down)
  {
    if (buttonState == ButtonState.DontCare)
    {
      return true;
    }
    if (buttonState == ButtonState.Down && down)
    {
      return true;
    }
    if (buttonState == ButtonState.Up && !down)
    {
      return true;
    }
    return false;
  }
}

