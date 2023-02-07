package net.logicim.ui.simulation.selection;

import net.logicim.ui.input.keyboard.KeyboardButtons;

public class Selection
{
  public static SelectionMode calculateSelectionMode(KeyboardButtons keyboardButtons)
  {
    boolean altDown = keyboardButtons.isAltDown();
    boolean shiftDown = keyboardButtons.isShiftDown();
    if (altDown && shiftDown)
    {
      return SelectionMode.SUBTRACT;
    }
    else if (shiftDown)
    {
      return SelectionMode.ADD;
    }
    else
    {
      return SelectionMode.REPLACE;
    }
  }
}

