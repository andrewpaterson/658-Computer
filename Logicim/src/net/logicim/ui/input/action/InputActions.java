package net.logicim.ui.input.action;

import net.logicim.ui.input.mouse.MouseButtons;

import java.util.ArrayList;
import java.util.List;

public class InputActions
{
  protected MouseButtons mouseButtons;

  protected List<InputAction> actions;

  public InputActions(MouseButtons mouseButtons)
  {
    this.mouseButtons = mouseButtons;

    actions = new ArrayList<>();
  }

  public void add(InputAction inputAction)
  {
    actions.add(inputAction);
  }

  public void keyPressed(int keyCode, boolean controlDown, boolean altDown, boolean shiftDown)
  {
    for (InputAction action : actions)
    {
      if (action.matched(keyCode, altDown, shiftDown, controlDown))
      {
        action.execute();
      }
    }
  }

  public List<InputAction> getActions()
  {
    return actions;
  }
}

