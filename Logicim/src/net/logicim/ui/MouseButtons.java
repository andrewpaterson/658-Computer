package net.logicim.ui;

public class MouseButtons
{
  public boolean[] buttons;
  public int mouseWheel;

  public MouseButtons()
  {
    buttons = new boolean[4];
  }

  public void set(int button)
  {
    buttons[button] = true;
  }

  public void unset(int button)
  {
    buttons[button] = false;
  }

  public boolean pressed(int button)
  {
    return buttons[button];
  }

  public void wheel(int wheelRotation)
  {
    mouseWheel += wheelRotation;
  }

  public int getRotation()
  {
    int mouseWheel = this.mouseWheel;
    this.mouseWheel = 0;
    return mouseWheel;
  }

  public void invalidate()
  {
    mouseWheel = 0;
  }
}

