package net.logicim.main;

import java.awt.*;
import java.awt.event.MouseEvent;

public class Logicim
{
  private int width;
  private int height;

  protected Point circleCenter;
  protected Color circleColour;

  public Logicim()
  {
    circleCenter = new Point();
    circleColour = Color.GRAY;
  }

  public void windowClosing()
  {

  }

  public void tick(int tickCount)
  {

  }

  public void mousePressed(int x, int y, int button)
  {
    if (button == MouseEvent.BUTTON1)
    {
      circleColour = Color.GREEN;
    }
    if (button == MouseEvent.BUTTON2)
    {
      circleColour = Color.RED;
    }
    if (button == MouseEvent.BUTTON3)
    {
      circleColour = Color.BLUE;
    }

  }

  public void mouseReleased(int x, int y, int button)
  {
    circleColour = Color.LIGHT_GRAY;
  }

  public void resized(int width, int height)
  {
    this.width = width;
    this.height = height;
  }

  public void mouseMoved(int x, int y)
  {
    circleCenter.setLocation(x, y);
  }

  public void paint(Graphics2D backBuffer)
  {
    backBuffer.setColor(Color.DARK_GRAY);
    backBuffer.fillRect(0, 0, width, height);

    backBuffer.setColor(Color.WHITE);
    backBuffer.drawLine(0, 0, width, height);

    backBuffer.setColor(circleColour);
    backBuffer.fillOval(circleCenter.x - 10, circleCenter.y - 10, 20, 20);
  }

  public void mouseExited()
  {
    circleColour = Color.DARK_GRAY;
  }

  public void mouseEntered(int x, int y)
  {
    circleColour = Color.LIGHT_GRAY;
  }
}

