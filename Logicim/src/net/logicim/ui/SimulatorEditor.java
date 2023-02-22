package net.logicim.ui;

import net.logicim.common.SimulatorException;
import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.data.circuit.CircuitData;
import net.logicim.domain.common.LongTime;
import net.logicim.ui.clipboard.ClipboardData;
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
import net.logicim.ui.simulation.selection.SelectionEdit;
import net.logicim.ui.undo.Undo;
import net.logicim.ui.util.SimulatorActions;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

  protected SimulatorEdit simulatorEdit;

  protected UndoStack undoStack;

  protected boolean running;
  protected long runTimeStep;

  protected Rotation creationRotation;

  protected ClipboardData clipboard;

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
    this.simulatorEdit = null;
    this.creationRotation = Rotation.North;

    this.running = false;
    this.runTimeStep = LongTime.nanosecondsToTime(0.25f);

    this.undoStack = new UndoStack();
    this.clipboard = null;

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
    return simulatorEdit == null;
  }

  public void mousePressed(int x, int y, int button, int clickCount)
  {
    mouseButtons.set(button);

    if (clickCount == 1 || clickCount == 2)
    {
      if (button == BUTTON1)
      {
        if (simulatorEdit != null)
        {
          simulatorEdit.done(viewport.transformScreenToGridX(x),
                             viewport.transformScreenToGridY(y));
          simulatorEdit = null;
          calculateHighlightedPort();
        }
        else
        {
          StatefulEdit edit;
          if (isInSelectedComponent(x, y))
          {
            edit = new StartEditInComponent(keyboardButtons);
          }
          else if ((hoverConnectionView != null))
          {
            edit = new StartEditInPort(keyboardButtons);
          }
          else
          {
            edit = new SelectionEdit(keyboardButtons);
          }
          simulatorEdit = createStatefulEditor(edit, x, y);
        }
      }
    }
  }

  protected SimulatorEdit createStatefulEditor(StatefulEdit edit, int x, int y)
  {
    SimulatorEdit simulatorEdit = new SimulatorEdit(toFloatingGridPosition(x, y),
                                                    edit,
                                                    circuitEditor,
                                                    this);
    clearHover();
    return simulatorEdit;
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
      if (simulatorEdit != null)
      {
        simulatorEdit.done(viewport.transformScreenToGridX(x),
                           viewport.transformScreenToGridY(y));
        simulatorEdit = null;
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

    if (simulatorEdit != null)
    {
      simulatorEdit.move(viewport.transformScreenToGridX((float) x),
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
      if (simulatorEdit != null)
      {
        simulatorEdit.discard();
        simulatorEdit = null;
      }

      circuitEditor.getSelection().clearSelection();
      StaticView<?> staticView = viewFactory.create(circuitEditor,
                                                    new Int2D(viewport.transformScreenToGridX(position.x),
                                                              viewport.transformScreenToGridY(position.y)),
                                                    creationRotation);
      simulatorEdit = createStatefulEditor(new MoveComponents(staticView, true), position.x, position.y);
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

    if (simulatorEdit != null)
    {
      simulatorEdit.paint(graphics, viewport);
    }

    circuitEditor.getSelection().paint(graphics, viewport);
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
    if ((simulatorEdit == null) && circuitEditor.isSelectionEmpty())
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
    validateActionKeyBindings();
  }

  private void validateActionKeyBindings()
  {
    Map<Integer, List<InputAction>> keyActionMap = getActionsByKeyCode();
    for (List<InputAction> inputActions : keyActionMap.values())
    {
      for (InputAction inputActionOuter : inputActions)
      {
        for (InputAction inputActionInner : inputActions)
        {
          if (inputActionInner != inputActionOuter)
          {
            if (inputActionInner.isSame(inputActionOuter))
            {
              String innerKeyString = inputActionInner.toKeyString();
              String outerKeyString = inputActionOuter.toKeyString();
              String innerActionString = inputActionInner.toActionDescriptionString();
              String outerActionString = inputActionOuter.toActionDescriptionString();
              throw new SimulatorException("%s bound to action [%s] and also %s bound to action [%s].", innerKeyString, innerActionString, outerKeyString, outerActionString);
            }
          }
        }
      }
    }
  }

  private Map<Integer, List<InputAction>> getActionsByKeyCode()
  {
    Map<Integer, List<InputAction>> keyActionMap = new LinkedHashMap<>();
    for (InputAction inputAction : actions.getActions())
    {
      int keyPressedCode = inputAction.getKeyPressedCode();
      List<InputAction> inputActionList = keyActionMap.get(keyPressedCode);
      if (inputActionList == null)
      {
        inputActionList = new ArrayList<>();
        keyActionMap.put(keyPressedCode, inputActionList);
      }
      inputActionList.add(inputAction);
    }
    return keyActionMap;
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
    creationRotation = creationRotation.rotateRight();
  }

  public void rotateLeft()
  {
    rotateMoveComponents(false);
    creationRotation = creationRotation.rotateLeft();
  }

  protected void rotateMoveComponents(boolean right)
  {
    if (simulatorEdit != null)
    {
      simulatorEdit.rotate(right);
    }
    else
    {
      if (hoverComponentView != null)
      {
        rotateSelection(right, new MoveComponents(hoverComponentView, false));
      }
      else
      {
        List<View> selection = getCircuitEditor().getSelection().getSelection();
        if (!selection.isEmpty())
        {
          rotateSelection(right, new MoveComponents(selection, false));
        }
      }
    }
  }

  private void rotateSelection(boolean right, MoveComponents moveComponents)
  {
    Int2D position = mousePosition.get();

    simulatorEdit = createStatefulEditor(moveComponents, position.x, position.y);
    simulatorEdit.rotate(right);
    simulatorEdit.done(viewport.transformScreenToGridX(position.x),
                       viewport.transformScreenToGridY(position.y));
    simulatorEdit = null;
    calculateHighlightedPort();
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

  public void stopSimulatorEdit()
  {
    if (simulatorEdit != null)
    {
      simulatorEdit.discard();
      simulatorEdit = null;
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

  public void editActionDelete()
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
    simulatorEdit = null;

    clearHover();

    circuitEditor = new CircuitEditor();
    circuitEditor.load(circuitData);
    calculateHighlightedPort();
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

  private List<View> duplicateViews(List<View> views)
  {
    ArrayList<View> duplicates = new ArrayList<>();
    for (View view : views)
    {
      View duplicate = view.duplicate(circuitEditor);
      duplicates.add(duplicate);
    }
    return duplicates;
  }

  public void editActionCopy()
  {
    if (simulatorEdit == null)
    {
      Int2D position = mousePosition.get();
      if (position != null)
      {
        List<View> selection = circuitEditor.getSelection().getSelection();
        if (selection.size() > 0)
        {
          clipboard = circuitEditor.copyViews(selection);
        }
      }
    }
  }

  public void editActionCut()
  {
    if (simulatorEdit == null)
    {
      Int2D position = mousePosition.get();
      if (position != null)
      {
        List<View> selection = circuitEditor.getSelection().getSelection();
        if (selection.size() > 0)
        {
          clipboard = circuitEditor.copyViews(selection);
          circuitEditor.deleteSelection();
        }
      }
    }
  }

  public void editActionDuplicate()
  {
    if (simulatorEdit == null)
    {
      Int2D position = mousePosition.get();
      if (position != null)
      {
        List<View> selection = circuitEditor.getSelection().getSelection();
        if (selection.size() > 0)
        {
          List<View> duplicates = duplicateViews(selection);
          simulatorEdit = createStatefulEditor(new MoveComponents(duplicates, true), position.x, position.y);
        }
      }
    }
  }

  public void editActionPaste()
  {
    if (simulatorEdit == null)
    {
      Int2D position = mousePosition.get();
      if (position != null)
      {
        if (clipboard != null)
        {
          List<View> views = circuitEditor.loadViews(clipboard.getTraces(), clipboard.getComponents(), false);
          simulatorEdit = createStatefulEditor(new MoveComponents(views, false), position.x, position.y);
        }
      }
    }
  }

  public void editActionMove()
  {
    if (simulatorEdit == null)
    {
      Int2D position = mousePosition.get();

      if (position != null)
      {
        List<View> selection = circuitEditor.getSelection().getSelection();
        if (selection.size() > 0)
        {
          simulatorEdit = createStatefulEditor(new MoveComponents(selection, false), position.x, position.y);
        }
      }
    }
  }
}

