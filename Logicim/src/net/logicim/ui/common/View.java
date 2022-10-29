package net.logicim.ui.common;

import net.logicim.common.type.Int2D;
import net.logicim.domain.Simulation;
import net.logicim.ui.CircuitEditor;

import java.awt.*;

public abstract class View
{
  protected CircuitEditor circuitEditor;
  protected Int2D position;
  protected Rotation rotation;

  public View(CircuitEditor circuitEditor, Int2D position, Rotation rotation)
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

  public void enable(Simulation simulation)
  {
  }
}

