package net.logicim.ui.input.action;

import java.util.ArrayList;
import java.util.List;

public class InputActions
{
  protected List<KeyInput> actions;

  public InputActions()
  {
    actions = new ArrayList<>();
  }

  public void add(KeyInput keyInput)
  {
    actions.add(keyInput);
  }

  public void keyPressed(int keyCode, boolean controlDown, boolean altDown, boolean shiftDown)
  {
    for (KeyInput action : actions)
    {
      if (action.matched(keyCode, altDown, shiftDown, controlDown))
      {
        action.execute();
      }
    }
  }

  public List<KeyInput> getActions()
  {
    return actions;
  }
}

