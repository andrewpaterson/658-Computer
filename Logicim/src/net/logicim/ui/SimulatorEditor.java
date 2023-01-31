package net.logicim.ui;

import net.logicim.common.geometry.Line;
import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.data.circuit.CircuitData;
import net.logicim.domain.common.LongTime;
import net.logicim.ui.common.*;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.common.integratedcircuit.View;
import net.logicim.ui.common.wire.TraceView;
import net.logicim.ui.input.action.InputAction;
import net.logicim.ui.input.action.InputActions;
import net.logicim.ui.input.event.SimulatorEditorEvent;
import net.logicim.ui.input.keyboard.KeyboardButtons;
import net.logicim.ui.input.mouse.MouseButtons;
import net.logicim.ui.input.mouse.MouseMotion;
import net.logicim.ui.input.mouse.MousePosition;
import net.logicim.ui.shape.common.BoundingBox;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.component.factory.ViewFactory;
import net.logicim.ui.util.SimulatorActions;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.HashSet;
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

  protected StaticView<?> placementView;

  protected TraceView hoverTraceView;
  protected StaticView<?> hoverComponentView;
  protected ConnectionView hoverConnectionView;

  protected SelectionRectangle selectionRectangle;
  protected Set<View> previousSelection;

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

    this.actions = new InputActions(mouseButtons);

    this.circuitEditor = new CircuitEditor();
    this.placementView = null;
    this.creationRotation = Rotation.North;

    this.running = false;
    this.runTimeStep = LongTime.nanosecondsToTime(0.25f);

    this.moveComponents = null;
    this.undoStack = new UndoStack();

    this.selectionRectangle = null;
    this.previousSelection = new HashSet<>();

    addActions(simulatorPanel);

    pushUndo();
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
        startMoveComponents(x, y);
      }
      else if ((hoverConnectionView != null && placementView == null))
      {
        if (wirePull == null)
        {
          startWirePull();
        }
      }
      else
      {
        if (placementView == null)
        {
          startSelection(x, y);
        }
      }
    }
  }

  protected void startSelection(int x, int y)
  {
    previousSelection = new HashSet<>(circuitEditor.getSelection());
    selectionRectangle = new SelectionRectangle();
    selectionRectangle.start(viewport, x, y);
    circuitEditor.updateSelection(selectionRectangle);
  }

  protected void doneSelection(int x, int y)
  {
    selectionRectangle.drag(viewport, x, y);
    circuitEditor.updateSelection(selectionRectangle);

    if (hasSelectionChanged())
    {
      pushUndo();
    }

    selectionRectangle = null;
  }

  private boolean hasSelectionChanged()
  {
    Set<View> selection = new HashSet<>(circuitEditor.getSelection());
    if (selection.isEmpty() && previousSelection.isEmpty())
    {
      return false;
    }
    if (selection.size() != previousSelection.size())
    {
      return true;
    }

    for (View view : selection)
    {
      if (!previousSelection.contains(view))
      {
        return true;
      }
    }

    return false;
  }

  public void mouseReleased(int x, int y, int button)
  {
    mouseButtons.unset(button);

    if (button == BUTTON1)
    {
      if (placementView != null)
      {
        donePlaceComponent(placementView);
        placementView = null;
        calculateHighlightedPort();
      }
      else if (wirePull != null)
      {
        if (!wirePull.isEmpty())
        {
          doneWirePull(wirePull);
          wirePull = null;
          calculateHighlightedPort();
        }
        else
        {
          discardWirePull();

          startSelection(x, y);
          selectionRectangle = null;
        }
      }
      else if (moveComponents != null)
      {
        doneMoveComponents();
      }

      if (selectionRectangle != null)
      {
        doneSelection(x, y);
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
        moveComponents.calculateDiff(viewport, x, y);
        moveComponents();
      }
    }

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

    for (View view : circuitEditor.getSelection())
    {
      if (view instanceof StaticView)
      {
        StaticView<?> componentView = (StaticView<?>) view;
        componentView.getBoundingBoxInGridSpace(boundBoxPosition, boundBoxDimension);
        if (BoundingBox.containsPoint(new Int2D(fx, fy), boundBoxPosition, boundBoxDimension))
        {
          return true;
        }
      }
      else if (view instanceof TraceView)
      {
        TraceView traceView = (TraceView) view;
        if (traceView.getLine().isPositionOn(ix, iy))
        {
          return true;
        }
      }
    }
    return false;
  }

  protected void startMoveComponents(int mouseX, int mouseY)
  {
    moveComponents = new MoveComponents(circuitEditor.getSelection(), new Int2D(viewport.transformScreenToGridX(mouseX),
                                                                                viewport.transformScreenToGridY(mouseY)));

    clearHover();
  }

  protected void moveComponents()
  {
    if (moveComponents.hasMoved())
    {
      circuitEditor.startMoveComponents(moveComponents.getStaticViews(), moveComponents.getTraces());
    }

    moveComponents.moveComponents();
  }

  protected void doneMoveComponents()
  {
    if (moveComponents.hadDiff())
    {
      circuitEditor.doneMoveComponents(moveComponents.getStaticViews(),
                                       moveComponents.getTraces(),
                                       moveComponents.getSelectedViews());
      if (moveComponents.hasDiff())
      {
        pushUndo();
      }
    }
    else
    {
      if (!moveComponents.getSelectedViews().isEmpty())
      {
        startSelection(mousePosition.get().x, mousePosition.get().y);
        doneSelection(mousePosition.get().x, mousePosition.get().y);
      }
    }
    moveComponents = null;

    calculateHighlightedPort();
  }

  public void startPlaceComponent(ViewFactory<?, ?> viewFactory)
  {
    Int2D position = getMousePositionOnGrid();
    if (position != null)
    {
      discardPlacement();
      placementView = viewFactory.create(circuitEditor, position, creationRotation);
    }
  }

  private void donePlaceComponent(StaticView<?> placementView)
  {
    circuitEditor.placeComponentView(placementView);
    pushUndo();
  }

  protected void startWirePull()
  {
    wirePull = new WirePull();
    wirePull.getFirstPosition().set(hoverConnectionView.getGridPosition());
  }

  private void doneWirePull(WirePull wirePull)
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

    pushUndo();
  }

  public void resized(int width, int height)
  {
    this.width = width;
    this.height = height;
  }

  public void paint(Graphics2D graphics)
  {
    graphics.setColor(Colours.getInstance().getBackground());
    graphics.fillRect(0, 0, width, height);

    viewport.paintGrid(graphics);
    circuitEditor.paint(graphics, viewport);

    if ((hoverComponentView != null) && (hoverConnectionView == null))
    {
      hoverComponentView.paintHover(graphics, viewport);
    }

    if (hoverConnectionView != null)
    {
      hoverConnectionView.paintHoverPort(graphics, viewport);

      drawConnectionDetails(graphics);
    }

    if (wirePull != null)
    {
      wirePull.paint(graphics, viewport);
    }

    if (selectionRectangle != null)
    {
      selectionRectangle.paint(graphics, viewport);
    }

    for (View view : circuitEditor.getSelection())
    {
      view.paintSelected(graphics, viewport);
    }
  }

  protected void drawConnectionDetails(Graphics2D graphics)
  {
    if (keyboardButtons.isAltDown())
    {
      int infoWidth = 300;
      int infoHeight = 300;
      if (width > infoWidth &&
          height > infoHeight)
      {
        Int2D mousePosition = this.mousePosition.get();
        if (mousePosition != null)
        {
          Font font = graphics.getFont();
          Color color = graphics.getColor();

          Int2D mousePositionOnGrid = getMousePositionOnGrid();
          if (mousePositionOnGrid != null)
          {
            int x = mousePosition.x - infoWidth / 2;
            int y = mousePosition.y;
            if (x < 0)
            {
              x = 0;
            }
            if (x > width - infoWidth)
            {
              x = width - infoWidth;
            }

            if (y < height / 2)
            {
              y = height - infoHeight;
            }
            else
            {
              y = 0;
            }

            new ConnectionInformationPanel(hoverConnectionView, graphics, viewport, infoWidth, infoHeight).drawConnectionDetails(x, y);
          }

          graphics.setFont(font);
          graphics.setColor(color);
        }
      }
    }
  }

  public void mouseExited()
  {
    mouseMotion.invalidate();
    mouseButtons.invalidate();
  }

  public void mouseEntered(int x, int y)
  {
    mousePosition.set(x, y);
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
        hoverComponentView = calculateHoverView(mousePosition);
        if (hoverComponentView != null)
        {
          int x = viewport.transformScreenToGridX(mousePosition.x);
          int y = viewport.transformScreenToGridY(mousePosition.y);
          hoverConnectionView = hoverComponentView.getConnectionsInGrid(x, y);
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
    hoverComponentView = null;
    hoverConnectionView = null;
  }

  private TraceView getHoverTrace(Int2D mousePosition)
  {
    return circuitEditor.getTraceViewInScreenSpace(viewport, mousePosition);
  }

  private StaticView<?> calculateHoverView(Int2D mousePosition)
  {
    return circuitEditor.getComponentViewInScreenSpace(viewport, mousePosition);
  }

  private void addActions(SimulatorPanel simulatorPanel)
  {
    SimulatorActions.create(this, simulatorPanel);
  }

  public void addAction(InputAction inputAction)
  {
    actions.add(inputAction);
  }

  public void keyPressed(int keyCode, boolean controlDown, boolean altDown, boolean shiftDown)
  {
    keyboardButtons.set(altDown, controlDown, shiftDown);
    actions.keyPressed(keyCode, controlDown, altDown, shiftDown);
  }

  public void runToTime(long timeForward)
  {
    circuitEditor.runToTime(timeForward);
  }

  public void runOneEvent()
  {
    circuitEditor.runSimultaneous();
  }

  public void rotateRight()
  {
    if (placementView != null)
    {
      placementView.rotateRight();
      Rotation rotation = placementView.getRotation();
      if ((rotation != Rotation.Cannot))
      {
        creationRotation = rotation;
      }
    }
    else
    {
      rotateMoveComponents(true);
    }
  }

  public void rotateLeft()
  {
    if (placementView != null)
    {
      placementView.rotateLeft();
      Rotation rotation = placementView.getRotation();
      if ((rotation != Rotation.Cannot))
      {
        creationRotation = rotation;
      }
    }
    else
    {
      rotateMoveComponents(false);
    }
  }

  protected void rotateMoveComponents(boolean right)
  {
    boolean moveComponentsNull = moveComponents == null;
    if (moveComponentsNull)
    {
      if (hoverComponentView != null)
      {
        moveComponents = new MoveComponents(hoverComponentView);
        clearHover();
      }
      else
      {
        Int2D position = circuitEditor.getSelectionCenter();
        if (position != null)
        {
          List<View> selection = circuitEditor.getSelection();
          moveComponents = new MoveComponents(selection, position);
          clearHover();
        }
      }
    }

    if (moveComponents != null)
    {
      moveComponents.rotate(right);
      moveComponents();
    }

    if (moveComponentsNull)
    {
      if (moveComponents != null)
      {
        doneMoveComponents();
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
      circuitEditor.clearSelection();
    }
  }

  private void discardWirePull()
  {
    if (wirePull != null)
    {
      wirePull = null;
    }
  }

  private void discardPlacement()
  {
    if (placementView != null)
    {
      circuitEditor.deleteComponentView(placementView);
      placementView = null;
    }
  }

  public void keyReleased(boolean controlDown, boolean altDown, boolean shiftDown)
  {
    keyboardButtons.set(altDown, controlDown, shiftDown);
  }

  public void toggleTunSimulation()
  {
    running = !running;
  }

  public void editActionDeleteComponent()
  {
    boolean componentDeleted = editActionDeleteComponentIfPossible();
    if (componentDeleted)
    {
      pushUndo();
    }
    else
    {
      discardPlacement();
    }

    clearHover();
    calculateHighlightedPort();
  }

  protected boolean editActionDeleteComponentIfPossible()
  {
    if (circuitEditor.isSelectionEmpty())
    {
      if (hoverConnectionView != null)
      {
        if (hoverTraceView != null)
        {
          circuitEditor.editActionDeleteTraceView(hoverConnectionView, hoverTraceView);
        }
        else
        {
          boolean traceDeleted = circuitEditor.deleteTraceViews(hoverConnectionView);

          if (!traceDeleted && (hoverComponentView != null))
          {
            circuitEditor.deleteComponentView(hoverComponentView);
          }
        }
        return true;
      }
      else if (hoverComponentView != null)
      {
        circuitEditor.deleteComponentView(hoverComponentView);
        return true;
      }
    }
    else
    {
      circuitEditor.deleteSelection();
      return true;
    }
    return false;
  }

  public void increaseSimulationSpeed()
  {
    runTimeStep *= 2;
  }

  public void decreaseSimulationSpeed()
  {
    runTimeStep /= 2;
  }

  public void addEditorEvent(SimulatorEditorEvent event)
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

    circuitEditor = new CircuitEditor();
    circuitEditor.load(circuitData);
  }

  public void loadFile(CircuitData circuitData)
  {
    load(circuitData);
    pushUndo();
  }

  protected void clearHover()
  {
    hoverTraceView = null;
    hoverComponentView = null;
    hoverConnectionView = null;
  }

  public StaticView<?> getHoverComponentView()
  {
    return hoverComponentView;
  }

  public CircuitEditor getCircuitEditor()
  {
    return circuitEditor;
  }

  public void clearButtons()
  {
    mouseButtons.clear();
  }

  public void pushUndo()
  {
    undoStack.push(save());
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

  public void replaceSelection(View newView, View oldView)
  {
    circuitEditor.replaceSelection(newView, oldView);
  }
}

