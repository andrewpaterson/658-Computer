package net.logicim.ui.input.action;

import net.logicim.common.SimulatorException;
import net.logicim.ui.editor.EditorAction;

import java.awt.event.KeyEvent;

public class InputAction
{
  protected EditorAction action;

  protected ButtonState altHeld;
  protected ButtonState shiftHeld;
  protected ButtonState ctrlHeld;
  protected int keyPressedCode;

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

  public ButtonState getAltHeld()
  {
    return altHeld;
  }

  public ButtonState getShiftHeld()
  {
    return shiftHeld;
  }

  public ButtonState getCtrlHeld()
  {
    return ctrlHeld;
  }

  public int getKeyPressedCode()
  {
    return keyPressedCode;
  }

  public boolean isSame(InputAction other)
  {
    if (this == other)
    {
      return true;
    }
    return keyPressedCode == other.keyPressedCode &&
           isSame(altHeld, other.altHeld) &&
           isSame(shiftHeld, other.shiftHeld) &&
           isSame(ctrlHeld, other.ctrlHeld);
  }

  private boolean isSame(ButtonState thisHeld, ButtonState otherHeld)
  {
    if (thisHeld == otherHeld)
    {
      return true;
    }
    else if ((thisHeld == ButtonState.DontCare) ||
             (otherHeld == ButtonState.DontCare))
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  public String toKeyString()
  {
    String keyText = KeyEvent.getKeyText(keyPressedCode);
    return String.format("Key [%s]: Alt [%s], Ctrl [%s], Shift [%s]", keyText, toModifierString(altHeld), toModifierString(ctrlHeld), toModifierString(shiftHeld));
  }

  private String toModifierString(ButtonState buttonState)
  {
    if (buttonState == ButtonState.DontCare)
    {
      return "Don't Care";
    }
    if (buttonState == ButtonState.Up)
    {
      return "Up";
    }
    if (buttonState == ButtonState.Down)
    {
      return "Down";
    }
    throw new SimulatorException("Don't know how to convert modifier string.");
  }

  public String toActionDescriptionString()
  {
    return action.getDescription();
  }
}

