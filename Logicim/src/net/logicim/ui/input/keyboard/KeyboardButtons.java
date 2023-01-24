package net.logicim.ui.input.keyboard;

public class KeyboardButtons
{
  public boolean alt;
  public boolean control;
  public boolean shift;

  public KeyboardButtons()
  {
  }

  public void set(boolean alt, boolean control, boolean shift)
  {
    this.alt = alt;
    this.control = control;
    this.shift = shift;
  }

  public boolean isAltDown()
  {
    return alt;
  }

  public boolean isControlDown()
  {
    return control;
  }

  public boolean isShiftDown()
  {
    return shift;
  }
}

