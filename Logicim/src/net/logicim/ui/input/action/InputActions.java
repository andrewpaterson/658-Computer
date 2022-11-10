package net.logicim.ui.input.action;

import net.logicim.ui.input.KeyboardButtons;
import net.logicim.ui.input.mouse.MouseButtons;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class InputActions
{
  protected KeyboardButtons keyboardButtons;
  protected MouseButtons mouseButtons;

  protected List<InputAction> actions;

  public InputActions(KeyboardButtons keyboardButtons, MouseButtons mouseButtons)
  {
    this.keyboardButtons = keyboardButtons;
    this.mouseButtons = mouseButtons;

    actions = new ArrayList<>();
  }

  public void add(InputAction inputAction)
  {
    actions.add(inputAction);
  }

  public void keyPressed(int keyCode)
  {
    boolean alt = keyboardButtons.isDown(KeyEvent.VK_ALT);
    boolean ctrl = keyboardButtons.isDown(KeyEvent.VK_CONTROL);
    boolean shift = keyboardButtons.isDown(KeyEvent.VK_SHIFT);

    for (InputAction action : actions)
    {
      if (action.matched(keyCode, alt, shift, ctrl))
      {
        action.execute();
      }
    }
  }
}

