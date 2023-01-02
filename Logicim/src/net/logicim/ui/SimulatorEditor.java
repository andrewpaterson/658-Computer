package net.logicim.ui;

import net.logicim.common.geometry.Line;
import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.data.circuit.CircuitData;
import net.logicim.domain.common.LongTime;
import net.logicim.ui.common.*;
import net.logicim.ui.common.integratedcircuit.ComponentView;
import net.logicim.ui.common.integratedcircuit.DiscreteView;
import net.logicim.ui.input.KeyboardButtons;
import net.logicim.ui.input.action.InputAction;
import net.logicim.ui.input.action.InputActions;
import net.logicim.ui.input.event.SimulatorEditorEvent;
import net.logicim.ui.input.mouse.MouseButtons;
import net.logicim.ui.input.mouse.MouseMotion;
import net.logicim.ui.input.mouse.MousePosition;
import net.logicim.ui.integratedcircuit.factory.ViewFactory;
import net.logicim.ui.shape.common.BoundingBox;
import net.logicim.ui.util.SimulatorActions;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;

import static java.awt.event.MouseEvent.BUTTON1;

public class SimulatorEditor
    implements PanelSize
{
  private ConcurrentLinkedDeque<SimulatorEditorEvent> inputEvents;

  private int width;
  private int height;

  protected Viewport viewport;
  protected InputActions actions;

  protected MouseMotion mouseMotion;
  protected MouseButtons mouseButtons;
  protected MousePosition mousePosition;
  protected KeyboardButtons keyboardButtons;

  protected CircuitEditor circuitEditor;

  protected DiscreteView<?> placementView;

  protected TraceView hoverTraceView;
  protected DiscreteView<?> hoverDiscreteView;
  protected ConnectionView hoverConnectionView;

  protected SelectionRectangle selectionRectangle;

  protected WirePull wirePull;

  protected MoveComponents moveComponents;
  protected UndoStack undoStack;

  protected boolean running;
  protected long runTimeStep;

  protected Rotation creationRotation;

  public SimulatorEditor(SimulatorPanel simulatorPanel)
  {
    this.inputEvents = new ConcurrentLinkedDeque<>();

    this.viewport = new Viewport(this);

    this.mouseMotion = new MouseMotion();
    this.mouseButtons = new MouseButtons();
    this.mousePosition = new MousePosition();

    this.keyboardButtons = new KeyboardButtons();

    this.actions = new InputActions(keyboardButtons, mouseButtons);

    this.circuitEditor = new CircuitEditor(viewport.getColours());
    this.placementView = null;
    this.creationRotation = Rotation.North;

    this.running = false;
    this.runTimeStep = LongTime.nanosecondsToTime(0.25f);

    this.moveComponents = null;
    this.undoStack = new UndoStack();

    addActions(simulatorPanel);
  }

  public void windowClosing()
  {
    running = false;
    System.exit(0);
  }

  public boolean tick(int tickCount)
  {
    SimulatorEditorEvent editorEvent = inputEvents.poll();
    while (editorEvent != null)
    {
      editorEvent.execute(this);
      editorEvent = inputEvents.poll();
    }

    if (tickCount == 0)
    {
      if (running && canRun())
      {
        runToTime(runTimeStep);
      }
    }
    return true;
  }

  private boolean canRun()
  {
    if (moveComponents != null)
    {
      return false;
    }
    if (placementView != null)
    {
      return false;
    }
    if (wirePull != null)
    {
      return false;
    }
    return true;
  }

  public void mousePressed(int x, int y, int button)
  {
    mouseButtons.set(button);

    if (button == BUTTON1)
    {
      if (isInSelectedComponent(x, y))
      {
        pushUndo();

        startMoveComponents(x, y);
      }
      else if ((hoverConnectionView != null && placementView == null))
      {
        if (wirePull == null)
        {
          pushUndo();

          wirePull = new WirePull();
          wirePull.getFirstPosition().set(hoverConnectionView.getGridPosition());
        }
      }
      else
      {
        if (placementView == null)
        {
          selectionRectangle = new SelectionRectangle();
          selectionRectangle.start(viewport, x, y);
          circuitEditor.updateSelection(selectionRectangle);
        }
      }
    }
  }

  protected void pushUndo()
  {
    undoStack.push(save());
  }

  public void mouseReleased(int x, int y, int button)
  {
    mouseButtons.unset(button);

    if (button == BUTTON1)
    {
      if (placementView != null)
      {
        executePlacement(placementView);
        placementView = null;
        calculateHighlightedPort();
      }
      else if (wirePull != null)
      {
        if (!wirePull.isEmpty())
        {
          executeWirePull(wirePull);
          wirePull = null;
          calculateHighlightedPort();
        }
        else
        {
          discardWirePull();

          selectionRectangle = new SelectionRectangle();
          selectionRectangle.start(viewport, x, y);
          circuitEditor.updateSelection(selectionRectangle);
          selectionRectangle = null;
        }
      }
      else if (moveComponents != null)
      {
        doneMoveComponents();
      }

      if (selectionRectangle != null)
      {
        selectionRectangle.drag(viewport, x, y);
        circuitEditor.updateSelection(selectionRectangle);

        selectionRectangle = null;
      }
    }
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

    if (placementView != null)
    {
      placementView.setPosition(viewport.transformScreenToGridX(x),
                                viewport.transformScreenToGridY(y));
    }

    if (wirePull != null)
    {
      wirePull.update(viewport.transformScreenToGridX(x),
                      viewport.transformScreenToGridY(y));

      if (!wirePull.isEmpty())
      {
        circuitEditor.clearSelection();
      }
    }

    if (selectionRectangle != null)
    {
      selectionRectangle.drag(viewport, x, y);
      circuitEditor.updateSelection(selectionRectangle);
    }
    else
    {
      if (moveComponents != null)
      {
        moveComponents(x, y);
      }
    }

    calculateHighlightedPort();
  }

  protected void startMoveComponents(int mouseX, int mouseY)
  {
    moveComponents = new MoveComponents(viewport, mouseX, mouseY, circuitEditor.getSelection());
    circuitEditor.startMoveComponents(moveComponents.getComponents());

    clearHover();
  }

  protected void moveComponents(int mouseX, int mouseY)
  {
    moveComponents.calculateDiff(viewport, mouseX, mouseY);
    List<ComponentView> components = moveComponents.getComponents();
    for (ComponentView componentView : components)
    {
      Int2D position = moveComponents.getPosition(componentView);
      componentView.setPosition(position.x, position.y);
    }
  }

  protected void doneMoveComponents()
  {
    circuitEditor.doneMoveComponents(moveComponents.getComponents());
    moveComponents = null;

    calculateHighlightedPort();
  }

  private boolean isInSelectedComponent(int mouseX, int mouseY)
  {
    float fx = viewport.transformScreenToGridX((float) mouseX);
    float fy = viewport.transformScreenToGridY((float) mouseY);
    int ix = viewport.transformScreenToGridX(mouseX);
    int iy = viewport.transformScreenToGridY(mouseY);

    Float2D boundBoxPosition = new Float2D();
    Float2D boundBoxDimension = new Float2D();

    for (ComponentView componentView : circuitEditor.getSelection())
    {
      if (componentView instanceof DiscreteView)
      {
        DiscreteView<?> discreteView = (DiscreteView<?>) componentView;
        discreteView.getBoundingBoxInGridSpace(boundBoxPosition, boundBoxDimension);
        if (BoundingBox.containsPoint(new Int2D(fx, fy), boundBoxPosition, boundBoxDimension))
        {
          return true;
        }
      }
      else if (componentView instanceof TraceView)
      {
        TraceView traceView = (TraceView) componentView;
        if (traceView.getLine().isPositionOn(ix, iy))
        {
          return true;
        }
      }
    }
    return false;
  }

  private void executePlacement(DiscreteView<?> placementView)
  {
    circuitEditor.placeDiscreteView(placementView);
}

  private void executeWirePull(WirePull wirePull)
  {
    Int2D firstPosition = wirePull.getFirstPosition();
    Int2D middlePosition = wirePull.getMiddlePosition();
    Int2D secondPosition = wirePull.getSecondPosition();

    Line firstLine = Line.createLine(firstPosition, middlePosition);
    Line secondLine = Line.createLine(middlePosition, secondPosition);

    Set<TraceView> traceViews;
    if (secondLine == null)
    {
      if (firstLine != null)
      {
        traceViews = circuitEditor.createTraceViews(firstLine);
      }
      else
      {
        traceViews = new LinkedHashSet<>();
      }
    }
    else
    {
      traceViews = new LinkedHashSet<>();
      traceViews.addAll(circuitEditor.createTraceViews(firstLine));
      traceViews.addAll(circuitEditor.createTraceViews(secondLine));
    }

    circuitEditor.finaliseCreatedTraces(traceViews);
  }

  public void resized(int width, int height)
  {
    this.width = width;
    this.height = height;
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

    if ((hoverDiscreteView != null) && (hoverConnectionView == null))
    {
      hoverDiscreteView.paintHover(graphics, viewport);
    }

    if (hoverConnectionView != null)
    {
      hoverConnectionView.paintHoverPort(graphics, viewport);
    }

    if (wirePull != null)
    {
      wirePull.paint(graphics, viewport);
    }

    if (selectionRectangle != null)
    {
      selectionRectangle.paint(graphics, viewport);
    }

    for (ComponentView componentView : circuitEditor.getSelection())
    {
      componentView.paintSelected(graphics, viewport);
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

    viewport.zoomTo(mousePosition.get(), (float) rotation / 10.0f);

    calculateHighlightedPort();
  }

  private void calculateHighlightedPort()
  {
    if ((placementView == null) && (selectionRectangle == null) && circuitEditor.isSelectionEmpty())
    {
      Int2D mousePosition = this.mousePosition.get();
      if (mousePosition != null)
      {
        hoverDiscreteView = getHoverView(mousePosition);
        if (hoverDiscreteView != null)
        {
          int x = viewport.transformScreenToGridX(mousePosition.x);
          int y = viewport.transformScreenToGridY(mousePosition.y);
          PortView portView = hoverDiscreteView.getPortInGrid(x, y);
          if (portView != null)
          {
            hoverConnectionView = portView.getConnection();
          }
          else
          {
            hoverConnectionView = null;
          }
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
            hoverConnectionView = hoverTraceView.getPotentialConnectionsInGrid(x, y);
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

  private DiscreteView<?> getHoverView(Int2D mousePosition)
  {
    return circuitEditor.getDiscreteViewInScreenSpace(viewport, mousePosition);
  }

  private void addActions(SimulatorPanel simulatorPanel)
  {
    SimulatorActions.create(this, simulatorPanel);
  }

  public void addAction(InputAction inputAction)
  {
    actions.add(inputAction);
  }

  public void keyPressed(int keyCode)
  {
    keyboardButtons.set(keyCode);

    if (!(keyCode == KeyEvent.VK_ALT || keyCode == KeyEvent.VK_SHIFT || keyCode == KeyEvent.VK_CONTROL))
    {
      actions.keyPressed(keyCode);
    }
  }

  public void createPlacementView(ViewFactory<?, ?> viewFactory)
  {
    pushUndo();

    Int2D position = getMousePositionOnGrid();
    if (position != null)
    {
      discardPlacement();
      placementView = viewFactory.create(circuitEditor, position, creationRotation);
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
      creationRotation = placementView.getRotation();
    }
  }

  public void placementRotateLeft()
  {
    if (placementView != null)
    {
      placementView.rotateLeft();
      creationRotation = placementView.getRotation();
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
    discardWirePull();
    clearSelection();

    calculateHighlightedPort();
  }

  protected void clearSelection()
  {
   if (!circuitEditor.isSelectionEmpty())
   {
     pushUndo();

     circuitEditor.clearSelection();
   }
  }

  private void discardWirePull()
  {
    if (wirePull != null)
    {
      wirePull = null;

      undoStack.discard();
    }
  }

  private void discardPlacement()
  {
    if (placementView != null)
    {
      circuitEditor.deleteDiscreteView(placementView);
      placementView = null;

      undoStack.discard();
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

  public void deleteComponent()
  {
    if (circuitEditor.isSelectionEmpty())
    {
      if (hoverConnectionView != null)
      {
        if (hoverTraceView != null)
        {
          circuitEditor.deleteTraceView(hoverConnectionView, hoverTraceView);
        }
        else
        {
          boolean traceDeleted = circuitEditor.deleteTraces(hoverConnectionView);

          if (!traceDeleted && (hoverDiscreteView != null))
          {
            circuitEditor.deleteDiscreteView(hoverDiscreteView);
          }
        }
      }
      else if (hoverDiscreteView != null)
      {
        circuitEditor.deleteDiscreteView(hoverDiscreteView);
      }
    }
    else
    {
      circuitEditor.deleteSelection();
    }

    clearHover();
    calculateHighlightedPort();
  }

  public void increaseSimulationSpeed()
  {
    runTimeStep *= 2;
  }

  public void decreaseSimulationSpeed()
  {
    runTimeStep /= 2;
  }

  public void addInputEvent(SimulatorEditorEvent event)
  {
    inputEvents.add(event);
  }

  public void resetSimulation()
  {
    circuitEditor.reset();
  }

  public CircuitData save()
  {
    return circuitEditor.save();
  }

  public void load(CircuitData circuitData)
  {
    placementView = null;
    moveComponents = null;
    clearHover();
    wirePull = null;

    circuitEditor = new CircuitEditor(viewport.getColours());
    circuitEditor.load(circuitData);
  }

  protected void clearHover()
  {
    hoverTraceView = null;
    hoverDiscreteView = null;
    hoverConnectionView = null;
  }

  public DiscreteView<?> getHoverDiscreteView()
  {
    return hoverDiscreteView;
  }

  public Colours getColours()
  {
    return viewport.getColours();
  }

  public CircuitEditor getCircuitEditor()
  {
    return circuitEditor;
  }

  public void clearButtons()
  {
    mouseButtons.clear();
    keyboardButtons.clear();
  }

  public void undo()
  {
    CircuitData circuitData = undoStack.pop();
    if (circuitData != null)
    {
      load(circuitData);
    }
  }

  public void redo()
  {
    CircuitData circuitData = undoStack.unpop();
    if (circuitData != null)
    {
      load(circuitData);
    }
  }
}

