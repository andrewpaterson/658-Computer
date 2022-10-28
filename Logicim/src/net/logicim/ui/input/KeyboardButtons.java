package net.logicim.ui.input;

import net.logicim.common.SimulatorException;

import java.awt.event.KeyEvent;

public class KeyboardButtons
{
  public boolean[] buttons;

  public KeyboardButtons()
  {
    this.buttons = new boolean[0x300];
  }

  public void set(int button)
  {
    button = getButtonIndex(button);
    buttons[button] = true;
  }

  private int getButtonIndex(int button)
  {
    if ((button & 0xFF00) == 0xFF00)
    {
      button = button & 0xFF00 | 0x200;
    }
    if (button >= 0x300)
    {
      throw new SimulatorException("Button out of range.");
    }
    return button;
  }

  public void unset(int button)
  {
    button = getButtonIndex(button);
    buttons[button] = false;
  }

  public boolean pressed(int button)
  {
    button = getButtonIndex(button);
    return buttons[button];
  }
}

