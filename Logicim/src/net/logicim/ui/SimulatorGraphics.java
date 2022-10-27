package net.logicim.ui;

import net.logicim.ui.common.PanelSize;
import net.logicim.ui.common.Position;
import net.logicim.ui.common.Viewport;

import java.awt.*;

public class SimulatorGraphics
    implements PanelSize
{
  private int width;
  private int height;

  protected Viewport viewport;
  protected CircuitEditor circuitEditor;
  protected MouseMotion mouseMotion;

  public SimulatorGraphics(CircuitEditor circuitEditor)
  {
    this.circuitEditor = circuitEditor;
    this.viewport = new Viewport(this);
    this.mouseMotion = new MouseMotion();
  }

  public void windowClosing()
  {

  }

  public void tick(int tickCount)
  {

  }

  public void mousePressed(int x, int y, int button)
  {

  }

  public void mouseReleased(int x, int y, int button)
  {
  }

  public void resized(int width, int height)
  {
    this.width = width;
    this.height = height;
  }

  public void mouseMoved(int x, int y)
  {
    Position moved = mouseMotion.moved(x, y);
    if (moved != null)
    {
      System.out.println("" + moved.x + ", " + moved.y);
    }
  }

  public void paint(Graphics2D graphics)
  {
    graphics.setColor(Color.WHITE);
    graphics.fillRect(0, 0, width, height);

    viewport.paintGrid(graphics);
    //circuitEditor.paint(viewport);
  }

  public void mouseExited()
  {
    mouseMotion.invalidate();
  }

  public void mouseEntered(int x, int y)
  {
    mouseMotion.invalidate();
  }

  public int getWidth()
  {
    return width;
  }

  public int getHeight()
  {
    return height;
  }
}

