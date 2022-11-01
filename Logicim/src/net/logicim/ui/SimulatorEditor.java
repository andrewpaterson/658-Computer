package net.logicim.ui;

import net.logicim.common.type.Int2D;
import net.logicim.domain.common.LongTime;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.Uniport;
import net.logicim.domain.common.trace.TraceNet;
import net.logicim.ui.common.*;
import net.logicim.ui.editor.*;
import net.logicim.ui.input.KeyboardButtons;
import net.logicim.ui.input.action.InputAction;
import net.logicim.ui.input.action.InputActions;
import net.logicim.ui.input.mouse.MouseButtons;
import net.logicim.ui.input.mouse.MouseMotion;
import net.logicim.ui.input.mouse.MousePosition;
import net.logicim.ui.integratedcircuit.standard.clock.ClockViewFactory;
import net.logicim.ui.integratedcircuit.standard.clock.NotGateViewFactory;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static java.awt.event.MouseEvent.BUTTON1;
import static net.logicim.ui.input.action.ButtonState.*;

public class SimulatorEditor
    implements PanelSize
{
  private int width;
  private int height;

  protected Viewport viewport;
  protected InputActions actions;

  protected MouseMotion mouseMotion;
  protected MouseButtons mouseButtons;
  protected MousePosition mousePosition;
  protected KeyboardButtons keyboardButtons;

  protected CircuitEditor circuitEditor;
  protected DiscreteView placementView;

  protected TraceView hoverTraceView;
  protected DiscreteView hoverDiscreteView;
  protected ConnectionView hoverConnectionView;

  protected WirePull wirePull;

  protected boolean running;
  protected long runTimeStep;

  public SimulatorEditor()
  {
    this.viewport = new Viewport(this);

    this.mouseMotion = new MouseMotion();
    this.mouseButtons = new MouseButtons();
    this.mousePosition = new MousePosition();

    this.keyboardButtons = new KeyboardButtons();

    this.actions = new InputActions(keyboardButtons, mouseButtons);

    this.circuitEditor = new CircuitEditor();
    this.placementView = null;

    running = false;
    runTimeStep = LongTime.nanosecondsToTime(0.25f);

    addActions();
  }

  public void windowClosing()
  {
  }

  public boolean tick(int tickCount)
  {
    if (tickCount == 0)
    {
      if (wirePull != null)
      {
        WirePull localPull = wirePull;
        Int2D mousePosition = this.mousePosition.get();
        if (mousePosition != null)
        {
          int x = viewport.transformScreenToGridX(mousePosition.x);
          int y = viewport.transformScreenToGridY(mousePosition.y);
          localPull.update(x, y);
        }
      }

      if (running)
      {
        runToTime(runTimeStep);
      }
    }
    return true;
  }

  public void mousePressed(int x, int y, int button)
  {
    mouseButtons.set(button);

    if (button == BUTTON1)
    {
      if (placementView != null)
      {
        circuitEditor.ensureSimulation();
        connectAndClean(placementView);
        placementView.enable(circuitEditor.simulation);
        placementView = null;
      }
      else if (hoverConnectionView != null)
      {
        if (wirePull == null)
        {
          wirePull = new WirePull();
          wirePull.getFirstPosition().set(hoverConnectionView.getGridPosition());
        }
      }
    }
  }

  public void mouseReleased(int x, int y, int button)
  {
    mouseButtons.unset(button);

    if (button == BUTTON1)
    {
      if (wirePull != null)
      {
        Int2D firstPosition = wirePull.getFirstPosition();
        Int2D middlePosition = wirePull.getMiddlePosition();
        Int2D secondPosition = wirePull.getSecondPosition();

        TraceView firstTraceView = createTraceView(firstPosition, middlePosition);
        TraceView secondTraceView = createTraceView(middlePosition, secondPosition);
        connectAndClean(firstTraceView, secondTraceView);

        wirePull = null;
      }
    }
  }

  private void connectAndClean(DiscreteView discreteView)
  {
    Set<ConnectionView> connectionViews = new LinkedHashSet<>();
    List<PortView> portViews = discreteView.getPorts();
    for (PortView portView : portViews)
    {
        getConnectionsInGridSpace(connectionViews, portView.getGridPosition());
    }
  }

  private void connectAndClean(TraceView... traceViews)
  {
    Set<ConnectionView> connectionViews = new LinkedHashSet<>();
    for (TraceView traceView : traceViews)
    {
      if (traceView != null)
      {
        getConnectionsInGridSpace(connectionViews, traceView.getStart().getPosition());
        getConnectionsInGridSpace(connectionViews, traceView.getEnd().getPosition());
      }
    }

    if (connectionViews.size() > 0)
    {
      TraceNet traceNet = new TraceNet();
      for (ConnectionView connectionView : connectionViews)
      {
        if (connectionView instanceof PortView)
        {
          Port port = ((PortView) connectionView).getPort();
          if (port instanceof Uniport)
          {
            Uniport uniport = (Uniport) port;
            uniport.disconnect();
            uniport.connect(traceNet);
          }
        }
      }

      for (TraceView traceView : traceViews)
      {
        if (traceView != null)
        {
          traceView.setTrace(traceNet);
        }
      }
    }
  }

  private void getConnectionsInGridSpace(Set<ConnectionView> connectionViews, Int2D position)
  {
    List<DiscreteView> discreteViews = circuitEditor.getDiscreteViewsInGridSpace(position);
    for (DiscreteView discreteView : discreteViews)
    {
      PortView portView = discreteView.getPortInGrid(position.x, position.y);
      if (portView != null)
      {
        connectionViews.add(portView);
      }
    }

    List<BaseJunctionView> junctionViews = circuitEditor.getJunctionViewsInGridSpace(position);
    for (BaseJunctionView junctionView : junctionViews)
    {
      connectionViews.add(junctionView);
    }
  }

  private TraceView createTraceView(Int2D start, Int2D end)
  {
    if ((start != null) && (end != null))
    {
      if (((start.x == end.x) || (start.y == end.y)) && !((start.x == end.x) && (start.y == end.y)))
      {
        return new TraceView(circuitEditor, new JunctionView(start), new JunctionView(end));
      }
    }
    return null;
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

    mousePositionOnGridChanged();
  }

  private void paintDebugPosition(Graphics2D graphics)
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

      graphics.drawString("" + x + ", " + y, screenSpaceX - 30, screenSpaceY - 10);
    }
  }

  public void paint(Graphics2D graphics)
  {
    graphics.setColor(viewport.getColours().getBackground());
    graphics.fillRect(0, 0, width, height);

    viewport.paintGrid(graphics);
    circuitEditor.paint(graphics, viewport);

    if (hoverConnectionView != null)
    {
      hoverConnectionView.paintHoverPort(graphics, viewport);
    }

    if (wirePull != null)
    {
      wirePull.paint(graphics, viewport);
    }
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

    mousePositionOnGridChanged();
  }

  private void mousePositionOnGridChanged()
  {
    calculatePlacementViewPosition();
    calculateHighlightedPort();
  }

  private void calculateHighlightedPort()
  {
    if (placementView == null)
    {
      Int2D mousePosition = this.mousePosition.get();
      if (mousePosition != null)
      {
        hoverDiscreteView = getHoverView(mousePosition);
        if (hoverDiscreteView != null)
        {
          int x = viewport.transformScreenToGridX(mousePosition.x);
          int y = viewport.transformScreenToGridY(mousePosition.y);
          hoverConnectionView = hoverDiscreteView.getPortInGrid(x, y);
        }
        else
        {
          hoverConnectionView = null;
        }

        if (hoverConnectionView == null)
        {
          hoverTraceView = getHoverTrace(mousePosition);
          if (hoverTraceView != null)
          {
            int x = viewport.transformScreenToGridX(mousePosition.x);
            int y = viewport.transformScreenToGridY(mousePosition.y);
            hoverConnectionView = hoverTraceView.getJunctionInGrid(x,y);
          }
          else
          {
            hoverConnectionView = null;
          }
        }
        return;
      }
    }
    hoverDiscreteView = null;
    hoverConnectionView = null;
  }

  private TraceView getHoverTrace(Int2D mousePosition)
  {
    return circuitEditor.getTraceViewInScreenSpace(viewport, mousePosition);
  }

  private DiscreteView getHoverView(Int2D mousePosition)
  {
    return circuitEditor.getDiscreteViewInScreenSpace(viewport, mousePosition);
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

  private void addActions()
  {
    actions.add(new InputAction(new PlacementRotateLeft(this), KeyEvent.VK_R, Up, Up, Down));
    actions.add(new InputAction(new PlacementRotateRight(this), KeyEvent.VK_R, Up, Up, Up));
    actions.add(new InputAction(new StopCurrent(this), KeyEvent.VK_ESCAPE, DontCare, DontCare, DontCare));
    actions.add(new InputAction(new RunOneEvent(this), KeyEvent.VK_T, Up, Up, Up));
    actions.add(new InputAction(new CreatePlacementView(this, new ClockViewFactory()), KeyEvent.VK_C, Up, Down, Up));
    actions.add(new InputAction(new CreatePlacementView(this, new NotGateViewFactory()), KeyEvent.VK_N, Up, Down, Up));
    actions.add(new InputAction(new ToggleRunSimulation(this), KeyEvent.VK_K, Up, Up, Down));
  }

  public void keyPressed(int keyCode)
  {
    keyboardButtons.set(keyCode);

    if (!(keyCode == KeyEvent.VK_ALT || keyCode == KeyEvent.VK_SHIFT || keyCode == KeyEvent.VK_CONTROL))
    {
      actions.keyPressed(keyCode);
    }
  }

  public void createPlacementView(ViewFactory viewFactory)
  {
    Int2D position = getMousePositionOnGrid();
    if (position != null)
    {
      discardPlacement();
      placementView = viewFactory.create(circuitEditor, position, Rotation.NORTH);
    }
  }

  public void runToTime(long timeForward)
  {
    circuitEditor.runToTime(timeForward);
  }

  public void runOneEvent()
  {
    circuitEditor.runSimultaneous();
  }

  public void placementRotateRight()
  {
    if (placementView != null)
    {
      placementView.rotateRight();
    }
  }

  public void placementRotateLeft()
  {
    if (placementView != null)
    {
      placementView.rotateLeft();
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

  public void stopCurrentEdit()
  {
    discardPlacement();
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

  public void toggleTunSimulation()
  {
    running = !running;
  }
}

