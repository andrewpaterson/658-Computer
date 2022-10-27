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

  public SimulatorGraphics()
  {
    viewport = new Viewport();
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
  }

  public void paint(Graphics2D backBuffer)
  {
    backBuffer.setColor(Color.WHITE);
    backBuffer.fillRect(0, 0, width, height);

    Position position = new Position(0, 0);
    int left = viewport.transformX(position.x);
    int top = viewport.transformY(position.y);
    int width = viewport.transformWidth(2);
    int height = viewport.transformHeight(2);
    backBuffer.setStroke(new BasicStroke(2));
    backBuffer.setColor(Color.BLACK);
    backBuffer.drawRect(left, top, width, height);
  }

  public void mouseExited()
  {
  }

  public void mouseEntered(int x, int y)
  {
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

