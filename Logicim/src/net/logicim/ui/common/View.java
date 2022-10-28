package net.logicim.ui.common;

import net.logicim.ui.CircuitEditor;

import java.awt.*;

public abstract class View
{
  protected CircuitEditor circuitEditor;
  protected Position position;

  public View(CircuitEditor circuitEditor, Position position)
  {
    this.circuitEditor = circuitEditor;
    this.position = position;
    circuitEditor.add(this);
  }

  public abstract void paint(Graphics2D graphics, Viewport viewport);

  public void setAnchor(int x, int y)
  {
    this.position.set(x, y);
  }
}

