package net.logicim.ui.common;

import net.logicim.ui.CircuitEditor;

import java.awt.*;

public abstract class View
{
  protected CircuitEditor circuitEditor;
  protected Position position;
  protected Rotation rotation;

  public View(CircuitEditor circuitEditor, Position position, Rotation rotation)
  {
    this.circuitEditor = circuitEditor;
    this.position = position;
    this.rotation = rotation;
    circuitEditor.add(this);
  }

  public void setPosition(int x, int y)
  {
    this.position.set(x, y);
  }

  public abstract void paint(Graphics2D graphics, Viewport viewport);

  public void rotateRight()
  {
    rotation = rotation.rotateRight();
  }

  public void rotateLeft()
  {
    rotation = rotation.rotateLeft();
  }
}

