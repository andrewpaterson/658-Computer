package net.logicim.ui;

import net.logicim.main.Logicim;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class SimulatorPanel
    extends JPanel
{
  protected Image backBufferImage;
  protected Graphics2D backBuffer;
  protected boolean running;
  protected long period;

  private Logicim logicim;

  public static int NANOS_IN_MILLI = 1000000;

  public SimulatorPanel()
  {
    running = false;
    period = 16 * NANOS_IN_MILLI;

    logicim = new Logicim();
  }

  public void loop()
  {
    running = true;
    long overtime = 0;
    while (running)
    {
      long startTime = System.nanoTime();

      int tickCount = 0;
      while (update(startTime, overtime, tickCount))
      {
        tickCount++;
      }

      render();
      paint();

      overtime = sleep(startTime, overtime);
    }
  }

  private void paint()
  {
    Graphics g = this.getGraphics();
    if ((g != null) && (backBufferImage != null))
    {
      g.drawImage(backBufferImage, 0, 0, null);
      Toolkit.getDefaultToolkit().sync();
      g.dispose();
    }
  }

  private void render()
  {
    if ((backBufferImage != null) && (backBuffer != null))
    {
      logicim.paint(backBuffer);
    }
  }

  private boolean update(long beforeTime, long overtime, int tickCount)
  {
    logicim.tick(tickCount);

    long afterTime = System.nanoTime();
    long timeDiff = afterTime - beforeTime;
    long sleepTime = (period - timeDiff) - overtime;
    return sleepTime > 0;
  }

  private long sleep(long beforeTime, long overtime)
  {
    long afterTime = System.nanoTime();
    long timeDiff = afterTime - beforeTime;
    long sleepTime = (period - timeDiff) - overtime;
    if (sleepTime > 0)
    {
      try
      {
        Thread.sleep(sleepTime / NANOS_IN_MILLI);
      }
      catch (InterruptedException ignored)
      {
      }
      overtime = (System.nanoTime() - afterTime) - sleepTime;
    }
    else
    {
      Thread.yield();
      overtime = 0;
    }
    return overtime;
  }

  public void componentResized(int width, int height)
  {
    backBufferImage = createImage(width, height);
    backBuffer = (Graphics2D) backBufferImage.getGraphics();

    logicim.resized(width, height);
  }

  public void mousePressed(int x, int y, int button)
  {
    logicim.mousePressed(x, y, button);
  }

  public void mouseReleased(int x, int y, int button)
  {
    logicim.mouseReleased(x, y, button);
  }

  public void mouseMoved(int x, int y)
  {
    logicim.mouseMoved(x, y);
  }

  public void windowClosing()
  {
    running = false;
    System.exit(0);
  }

  public void mouseExited()
  {
    logicim.mouseExited();
  }

  public void mouseEntered(int x, int y)
  {
    logicim.mouseEntered(x, y);
  }
}

