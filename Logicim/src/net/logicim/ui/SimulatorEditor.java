package net.logicim.ui;

import net.logicim.common.type.Int2D;
import net.logicim.ui.common.*;
import net.logicim.ui.input.KeyboardButtons;
import net.logicim.ui.input.MouseButtons;
import net.logicim.ui.input.MouseMotion;
import net.logicim.ui.input.MousePosition;
import net.logicim.ui.integratedcircuit.standard.clock.ClockView;
import net.logicim.ui.integratedcircuit.standard.logic.NotGateView;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class SimulatorEditor
    implements PanelSize
{
  private int width;
  private int height;

  protected Viewport viewport;

  protected MouseMotion mouseMotion;
  protected MouseButtons mouseButtons;
  protected MousePosition mousePosition;
  protected KeyboardButtons keyboardButtons;

  protected CircuitEditor circuitEditor;
  protected View placementView;

  public SimulatorEditor()
  {
    this.viewport = new Viewport(this);

    this.mouseMotion = new MouseMotion();
    this.mouseButtons = new MouseButtons();
    this.mousePosition = new MousePosition();

    this.keyboardButtons = new KeyboardButtons();

    this.circuitEditor = new CircuitEditor();
    this.placementView = null;
  }

  public void windowClosing()
  {
  }

  public void tick(int tickCount)
  {
  }

  public void mousePressed(int x, int y, int button)
  {
    mouseButtons.set(button);

    if (placementView != null)
    {
      circuitEditor.ensureSimulation();
      placementView.enable(circuitEditor.simulation);
      placementView = null;
    }
  }

  public void mouseReleased(int x, int y, int button)
  {
    mouseButtons.unset(button);
  }

  public void resized(int width, int height)
  {
    this.width = width;
    this.height = height;
  }

  public void mouseMoved(int x, int y)
  {
    mousePosition.set(x, y);

    Int2D moved = mouseMotion.moved(x, y);
    if (mouseButtons.pressed(MouseEvent.BUTTON2))
    {
      if (moved != null)
      {
        viewport.scroll(moved);
      }
    }

    calculatePlacementViewPosition();
  }

  private void debugPosition(Graphics2D graphics)
  {
    Int2D position = mousePosition.get();
    if (position != null)
    {
      int x = viewport.transformScreenToGridX(position.x);
      int y = viewport.transformScreenToGridY(position.y);

      int screenSpaceX = viewport.transformGridToScreenSpaceX(x);
      int screenSpaceY = viewport.transformGridToScreenSpaceY(y);

      graphics.setColor(Color.RED);
      graphics.drawOval(screenSpaceX - 3, screenSpaceY - 3, 6, 6);

      graphics.drawString("" + x + ", " + y, screenSpaceX - 30, screenSpaceY- 10);
    }
  }

  public void paint(Graphics2D graphics)
  {
    graphics.setColor(viewport.getColours().getBackground());
    graphics.fillRect(0, 0, width, height);

    viewport.paintGrid(graphics);
    circuitEditor.paint(graphics, viewport);

    debugPosition(graphics);
  }

  public void mouseExited()
  {
    mouseMotion.invalidate();
    mouseButtons.invalidate();
  }

  public void mouseEntered(int x, int y)
  {
    mouseMotion.invalidate();
    mouseButtons.invalidate();
  }

  public int getWidth()
  {
    return width;
  }

  public int getHeight()
  {
    return height;
  }

  public void mouseWheel(int wheelRotation)
  {
    mouseButtons.wheel(wheelRotation);
    int rotation = mouseButtons.getRotation();
    viewport.zoom((float) rotation / 10.0f);

    calculatePlacementViewPosition();
  }

  private void calculatePlacementViewPosition()
  {
    if (placementView != null)
    {
      Int2D position = mousePosition.get();
      if (position != null)
      {
        int x = viewport.transformScreenToGridX(position.x);
        int y = viewport.transformScreenToGridY(position.y);
        placementView.setPosition(x, y);
      }
    }
  }

  public void keyPressed(int keyCode)
  {
    keyboardButtons.set(keyCode);

    if (placementView != null)
    {
      if (keyCode == KeyEvent.VK_R)
      {
        if (keyboardButtons.pressed(KeyEvent.VK_SHIFT))
        {
          placementView.rotateLeft();
        }
        else
        {
          placementView.rotateRight();
        }
      }

      if (keyCode == KeyEvent.VK_ESCAPE)
      {
        discardPlacement();
      }
    }

    if ((keyCode == KeyEvent.VK_T) && keyboardButtons.pressed(KeyEvent.VK_CONTROL))
    {
      circuitEditor.runSimultaneous();
    }

    if ((keyCode == KeyEvent.VK_C) && keyboardButtons.pressed(KeyEvent.VK_SHIFT))
    {
      Int2D position = getMousePositionOnGrid();
      if (position != null)
      {
        discardPlacement();
        placementView = new ClockView(circuitEditor, position, Rotation.NORTH);
      }
    }

    if ((keyCode == KeyEvent.VK_N) && keyboardButtons.pressed(KeyEvent.VK_SHIFT))
    {
      Int2D position = getMousePositionOnGrid();
      if (position != null)
      {
        discardPlacement();
        placementView = new NotGateView(circuitEditor, position, Rotation.NORTH);
      }
    }
  }

  private Int2D getMousePositionOnGrid()
  {
    Int2D position = mousePosition.get();
    if (position != null)
    {
      int x = viewport.transformScreenToGridX(position.x);
      int y = viewport.transformScreenToGridY(position.y);
      return new Int2D(x, y);
    }
    else
    {
      return null;
    }
  }

  private void discardPlacement()
  {
    if (placementView != null)
    {
      circuitEditor.remove((IntegratedCircuitView<?>) placementView);
      placementView = null;
    }
  }

  public void keyReleased(int keyCode)
  {
    keyboardButtons.unset(keyCode);
  }
}

