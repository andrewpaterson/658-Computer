package net.logicim.ui;

import javax.swing.*;
import java.awt.*;

import static net.logicim.domain.common.Units.nS_IN_mS;

public class SimulatorPanel
    extends JPanel
{
  protected Image backBufferImage;
  protected Graphics2D backBuffer;
  protected boolean running;
  protected long period;

  private SimulatorEditor simulatorEditor;

  public SimulatorPanel()
  {
    running = false;
    period = 16 * nS_IN_mS;

    simulatorEditor = new SimulatorEditor();
  }

  public void loop()
  {
    running = true;
    long overtime = 0;
    while (running)
    {
      long startTime = System.nanoTime();

      int tickCount = 0;
      while (!update(startTime, overtime, tickCount))
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
      simulatorEditor.paint(backBuffer);
    }
  }

  private boolean update(long beforeTime, long overtime, int tickCount)
  {
    boolean done = simulatorEditor.tick(tickCount);

    long afterTime = System.nanoTime();
    long timeDiff = afterTime - beforeTime;
    long sleepTime = (period - timeDiff) - overtime;
    return (sleepTime > 0) || done;
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
        Thread.sleep(sleepTime / nS_IN_mS);
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

    simulatorEditor.resized(width, height);
  }

  public void mousePressed(int x, int y, int button)
  {
    simulatorEditor.mousePressed(x, y, button);
  }

  public void mouseReleased(int x, int y, int button)
  {
    simulatorEditor.mouseReleased(x, y, button);
  }

  public void mouseMoved(int x, int y)
  {
    simulatorEditor.mouseMoved(x, y);
  }

  public void windowClosing()
  {
    running = false;
    System.exit(0);
  }

  public void mouseExited()
  {
    simulatorEditor.mouseExited();
  }

  public void mouseEntered(int x, int y)
  {
    simulatorEditor.mouseEntered(x, y);
  }

  public void mouseWheel(int wheelRotation)
  {
    simulatorEditor.mouseWheel(wheelRotation);
  }

  public void keyPressed(int keyCode)
  {
    simulatorEditor.keyPressed(keyCode);
  }

  public void keyReleased(int keyCode)
  {
    simulatorEditor.keyReleased(keyCode);
  }
}

