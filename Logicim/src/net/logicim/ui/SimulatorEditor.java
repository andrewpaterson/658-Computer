package net.logicim.ui;

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
import net.logicim.ui.placement.*;
import net.logicim.ui.shape.common.BoundingBox;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.component.factory.ViewFactory;
import net.logicim.ui.simulation.selection.Selection;
import net.logicim.ui.simulation.selection.SelectionMode;
import net.logicim.ui.undo.Undo;
import net.logicim.ui.util.SimulatorActions;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.concurrent.ConcurrentLinkedDeque;

import static java.awt.event.MouseEvent.BUTTON1;

public class SimulatorEditor
    implements PanelSize,
               Undo
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

  protected TraceView hoverTraceView;
  protected StaticView<?> hoverComponentView;
  protected ConnectionView hoverConnectionView;

  protected StatefulEdit statefulEdit;

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
    this.statefulEdit = null;
    this.creationRotation = Rotation.North;

    this.running = false;
    this.runTimeStep = LongTime.nanosecondsToTime(0.25f);

    this.undoStack = new UndoStack();

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
    return statefulEdit == null;
  }

  public void mousePressed(int x, int y, int button, int clickCount)
  {
    mouseButtons.set(button);

    if (clickCount == 1 || clickCount == 2)
    {
      if (button == BUTTON1)
      {
        if (statefulEdit != null)
        {
          statefulEdit.done(viewport.transformScreenToGridX(x),
                            viewport.transformScreenToGridY(y));
          statefulEdit = null;
          calculateHighlightedPort();
        }
        else
        {
          StatefulMove edit;
          if (isInSelectedComponent(x, y))
          {
            edit = new InSelectedComponent(keyboardButtons);
          }
          else if ((hoverConnectionView != null))
          {
            edit = new InSelectedPort(keyboardButtons);
          }
          else
          {
            edit = new RectangleSelection(keyboardButtons);
          }
          statefulEdit = createStatefulEditor(edit, x, y);
        }
      }
    }
  }

  protected StatefulEdit createStatefulEditor(StatefulMove edit, int x, int y)
  {
    StatefulEdit statefulEdit = new StatefulEdit(toFloatingGridPosition(x, y), edit, circuitEditor, this);
    clearHover();
    return statefulEdit;
  }

  protected Float2D toFloatingGridPosition(float x, float y)
  {
    return new Float2D(viewport.transformScreenToGridX(x),
                       viewport.transformScreenToGridY(y));
  }

  public void mouseReleased(int x, int y, int button)
  {
    mouseButtons.unset(button);

    if (button == BUTTON1)
    {
      if (statefulEdit != null)
      {
        statefulEdit.done(viewport.transformScreenToGridX(x),
                          viewport.transformScreenToGridY(y));
        statefulEdit = null;
        calculateHighlightedPort();
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

    if (statefulEdit != null)
    {
      statefulEdit.move(viewport.transformScreenToGridX((float) x),
                        viewport.transformScreenToGridY((float) y));
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

    for (View view : circuitEditor.getSelection().getSelection())
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

  public void startPlaceComponent(ViewFactory<?, ?> viewFactory)
  {
    Int2D position = mousePosition.get();

    if (position != null)
    {
      if (statefulEdit != null)
      {
        statefulEdit.discard();
        statefulEdit = null;
      }

      circuitEditor.getSelection().clearSelection();
      StaticView<?> staticView = viewFactory.create(circuitEditor,
                                                    new Int2D(viewport.transformScreenToGridX(position.x),
                                                              viewport.transformScreenToGridY(position.y)),
                                                    creationRotation);
      statefulEdit = createStatefulEditor(new MoveComponents(staticView, true), position.x, position.y);
    }
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

    if (statefulEdit != null)
    {
      statefulEdit.paint(graphics, viewport);
    }

    SelectionMode selectionMode = Selection.calculateSelectionMode(keyboardButtons);
    circuitEditor.getSelection().paint(graphics, viewport, selectionMode);
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
    if ((statefulEdit == null) && !circuitEditor.isSelecting() && circuitEditor.isSelectionEmpty())
    {
      Int2D mousePosition = this.mousePosition.get();
      if (mousePosition != null)
      {
        hoverComponentView = calculateHoverView(mousePosition);
        if (hoverComponentView != null)
        {
          int x = viewport.transformScreenToGridX(mousePosition.x);
          int y = viewport.transformScreenToGridY(mousePosition.y);
          hoverConnectionView = circuitEditor.getConnection(x, y);
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

    mouseModifierKeys(keyCode);
  }

  public void keyReleased(int keyCode, boolean controlDown, boolean altDown, boolean shiftDown)
  {
    keyboardButtons.set(altDown, controlDown, shiftDown);

    mouseModifierKeys(keyCode);
  }

  protected void mouseModifierKeys(int keyCode)
  {
    if ((keyCode == KeyEvent.VK_ALT) || (keyCode == KeyEvent.VK_SHIFT) || (keyCode == KeyEvent.VK_CONTROL))
    {
      Int2D position = mousePosition.get();
      if (position != null)
      {
        mouseMoved(position.x, position.y);
      }
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

  public void rotateRight()
  {
    rotateMoveComponents(true);
  }

  public void rotateLeft()
  {
    rotateMoveComponents(false);
  }

  protected void rotateMoveComponents(boolean right)
  {
    if (statefulEdit != null)
    {
      statefulEdit.rotate(right);
    }
    else
    {
      if (hoverComponentView != null)
      {
        Int2D position = hoverComponentView.getPosition();

        createStatefulEditor(new MoveComponents(hoverComponentView, false),
                             viewport.transformGridToScreenSpaceX(position.x),
                             viewport.transformGridToScreenSpaceY(position.y));
        clearHover();
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
    if (statefulEdit != null)
    {
      statefulEdit.discard();
      statefulEdit = null;
    }
    clearSelection();
    calculateHighlightedPort();
  }

  protected void clearSelection()
  {
    circuitEditor.getSelection().clearSelection();
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

    clearHover();
    calculateHighlightedPort();
  }

  protected boolean editActionDeleteComponentIfPossible()
  {
    if (circuitEditor.getSelection().isSelectionEmpty())
    {
      if (hoverConnectionView != null)
      {
        if (hoverTraceView != null)
        {
          circuitEditor.editActionDeleteTraceView(hoverConnectionView, hoverTraceView);
        }
        else
        {
          boolean traceDeleted = circuitEditor.editActionDeleteTraceViews(hoverConnectionView);

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
    statefulEdit = null;

    clearHover();

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

  @Override
  public void pushUndo()
  {
    undoStack.push(save());
    circuitEditor.validateConsistency();
  }

  @Override
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

