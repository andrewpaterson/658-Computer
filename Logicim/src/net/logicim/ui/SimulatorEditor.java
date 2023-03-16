package net.logicim.ui;

import net.logicim.common.SimulatorException;
import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.common.util.StringUtil;
import net.logicim.data.circuit.CircuitData;
import net.logicim.domain.common.LongTime;
import net.logicim.ui.clipboard.ClipboardData;
import net.logicim.ui.common.*;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.common.integratedcircuit.View;
import net.logicim.ui.common.wire.TraceView;
import net.logicim.ui.editor.SubcircuitViewParameters;
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
import net.logicim.ui.simulation.SubcircuitEditor;
import net.logicim.ui.simulation.component.factory.ViewFactory;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;
import net.logicim.ui.simulation.selection.Selection;
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
  public static final String MAIN_SUBCIRCUIT_TYPE_NAME = "Main";

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

  protected EditAction editAction;

  protected UndoStack undoStack;

  protected boolean running;
  protected long runTimeStep;

  protected Rotation creationRotation;

  protected ClipboardData clipboard;

  protected Map<Integer, SubcircuitEditor> subcircuitBookmarks;
  protected Map<String, SubcircuitViewParameters> subcircuitViewParameters;

  public SimulatorEditor(SimulatorPanel simulatorPanel)
  {
    this.inputEvents = new ConcurrentLinkedDeque<>();

    this.viewport = new Viewport(this);

    this.mouseMotion = new MouseMotion();
    this.mouseButtons = new MouseButtons();
    this.mousePosition = new MousePosition();

    this.keyboardButtons = new KeyboardButtons();

    this.actions = new InputActions(mouseButtons);

    this.circuitEditor = new CircuitEditor(MAIN_SUBCIRCUIT_TYPE_NAME);
    this.editAction = null;
    this.creationRotation = Rotation.North;

    this.running = false;
    this.runTimeStep = LongTime.nanosecondsToTime(0.25f);

    this.undoStack = new UndoStack();
    this.clipboard = null;

    this.subcircuitBookmarks = new LinkedHashMap<>();
    this.subcircuitViewParameters = new LinkedHashMap<>();

    setSubcircuitParameters(MAIN_SUBCIRCUIT_TYPE_NAME);

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
    return editAction == null;
  }

  public void mousePressed(int x, int y, int button, int clickCount)
  {
    mouseButtons.set(button);

    if (clickCount == 1 || clickCount == 2)
    {
      if (button == BUTTON1)
      {
        if (editAction != null)
        {
          editAction.done(viewport.transformScreenToGridX(x),
                          viewport.transformScreenToGridY(y));
          editAction = null;
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
            edit = new StartEditInPort(keyboardButtons, getCircuitEditor().getCircuitSimulation());
          }
          else
          {
            edit = new SelectionEdit(keyboardButtons);
          }
          editAction = createEdit(edit, toFloatingGridPosition(x, y));
        }
      }
    }
  }

  protected EditAction createEdit(StatefulEdit edit, Float2D start)
  {
    EditAction editAction = new EditAction(start,
                                           edit,
                                           circuitEditor,
                                           this);
    clearHover();
    return editAction;
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
      if (editAction != null)
      {
        editAction.done(viewport.transformScreenToGridX(x),
                        viewport.transformScreenToGridY(y));
        editAction = null;
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
        setSubcircuitParameters(circuitEditor.getCurrentSubcircuitEditor().getTypeName());
      }
    }

    if (editAction != null)
    {
      editAction.move(viewport.transformScreenToGridX((float) x),
                      viewport.transformScreenToGridY((float) y));
    }

    calculateHighlightedPort();
  }

  protected void setSubcircuitParameters(String typeName)
  {
    SubcircuitViewParameters subcircuitViewParameters = this.subcircuitViewParameters.get(typeName);
    if (subcircuitViewParameters == null)
    {
      this.subcircuitViewParameters.put(typeName,
                                        new SubcircuitViewParameters(viewport.getPosition().clone(),
                                                                     viewport.getZoom()));
    }
    else
    {
      subcircuitViewParameters.set(viewport.getPosition().clone(),
                                   viewport.getZoom());
    }
  }

  private boolean isInSelectedComponent(int mouseX, int mouseY)
  {
    float fx = viewport.transformScreenToGridX((float) mouseX);
    float fy = viewport.transformScreenToGridY((float) mouseY);
    int ix = viewport.transformScreenToGridX(mouseX);
    int iy = viewport.transformScreenToGridY(mouseY);

    Float2D boundBoxPosition = new Float2D();
    Float2D boundBoxDimension = new Float2D();

    for (View view : circuitEditor.getCurrentSelection().getSelection())
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
      if (editAction != null)
      {
        editAction.discard();
        editAction = null;
      }

      circuitEditor.getCurrentSelection().clearSelection();
      StaticView<?> staticView = viewFactory.create(circuitEditor.getCurrentSubcircuitView(),
                                                    circuitEditor.getCircuit(),
                                                    new Int2D(viewport.transformScreenToGridX(position.x),
                                                              viewport.transformScreenToGridY(position.y)),
                                                    creationRotation);
      editAction = createEdit(new MoveComponents(staticView, true), toFloatingGridPosition(position.x, position.y));
    }
  }

  public void startPlaceSubcircuit(int bookmarkIndex)
  {
    SubcircuitEditor subcircuitEditor = subcircuitBookmarks.get(bookmarkIndex);
    if (subcircuitEditor != null)
    {
      startPlaceSubcircuit(subcircuitEditor);
    }
  }

  public void startPlaceSubcircuit(SubcircuitEditor subcircuitEditor)
  {
    Int2D position = mousePosition.get();

    if (position != null)
    {
      if (editAction != null)
      {
        editAction.discard();
        editAction = null;
      }

      circuitEditor.getCurrentSelection().clearSelection();
      SubcircuitInstanceView subcircuitView = new SubcircuitInstanceView(subcircuitEditor.getSubcircuitView(),
                                                                         circuitEditor.getCircuit(),
                                                                         new Int2D(viewport.transformScreenToGridX(position.x),
                                                                                   viewport.transformScreenToGridY(position.y)),
                                                                         Rotation.North);
      editAction = createEdit(new MoveComponents(subcircuitView, true), toFloatingGridPosition(position.x, position.y));
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

    if (editAction != null)
    {
      editAction.paint(graphics, viewport);
    }

    circuitEditor.getCurrentSelection().paint(graphics, viewport);
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
    setSubcircuitParameters(circuitEditor.getCurrentSubcircuitEditor().getTypeName());

    calculateHighlightedPort();
  }

  private void calculateHighlightedPort()
  {
    if ((editAction == null) && circuitEditor.isCurrentSelectionEmpty())
    {
      Int2D mousePosition = this.mousePosition.get();
      if (mousePosition != null)
      {
        hoverComponentView = calculateHoverView(mousePosition);
        if (hoverComponentView != null)
        {
          int x = viewport.transformScreenToGridX(mousePosition.x);
          int y = viewport.transformScreenToGridY(mousePosition.y);
          hoverConnectionView = circuitEditor.getConnectionInCurrentSubcircuit(x, y);
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
    return circuitEditor.getCurrentSubcircuitEditor().getTraceViewInScreenSpace(viewport, mousePosition);
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
    if (editAction != null)
    {
      editAction.rotate(right);
    }
    else
    {
      if (hoverComponentView != null)
      {
        rotateSelection(right, new MoveComponents(hoverComponentView, false));
      }
      else
      {
        List<View> selection = getCircuitEditor().getCurrentSelection().getSelection();
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

    editAction = createEdit(moveComponents, toFloatingGridPosition(position.x, position.y));
    editAction.rotate(right);
    editAction.done(viewport.transformScreenToGridX(position.x),
                    viewport.transformScreenToGridY(position.y));
    editAction = null;
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
    if (editAction != null)
    {
      editAction.discard();
      editAction = null;
    }
    clearSelection();
    calculateHighlightedPort();
  }

  protected void clearSelection()
  {
    circuitEditor.getCurrentSelection().clearSelection();
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
    if (circuitEditor.getCurrentSelection().isSelectionEmpty())
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
    editAction = null;

    clearHover();

    circuitEditor = new CircuitEditor("Main");
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

  public void replaceSelectionInCurrentSubcircuitView(View newView, View oldView)
  {
    circuitEditor.replaceSelection(newView, oldView);
  }

  private List<View> duplicateViews(List<View> views)
  {
    return circuitEditor.duplicateViews(views);
  }

  public void editActionCopy()
  {
    if (editAction == null)
    {
      Int2D position = mousePosition.get();
      if (position != null)
      {
        List<View> selection = circuitEditor.getCurrentSelection().getSelection();
        if (selection.size() > 0)
        {
          clipboard = circuitEditor.copyViews(selection);
        }
      }
    }
  }

  public void editActionCut()
  {
    if (editAction == null)
    {
      Int2D position = mousePosition.get();
      if (position != null)
      {
        List<View> selection = circuitEditor.getCurrentSelection().getSelection();
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
    if (editAction == null)
    {
      List<View> selection = circuitEditor.getCurrentSelection().getSelection();
      if (selection.size() > 0)
      {
        List<View> duplicates = duplicateViews(selection);
        Int2D center = Selection.getViewsCenter(duplicates);
        if (center != null)
        {
          Float2D floatingCenter = new Float2D(center);
          editAction = createEdit(new MoveComponents(duplicates, true), floatingCenter);
        }
      }
    }
  }

  public void editActionPaste()
  {
    if (editAction == null)
    {
      if (clipboard != null)
      {
        List<View> views = circuitEditor.pasteClipboardViews(clipboard.getTraces(), clipboard.getComponents());
        Int2D center = Selection.getViewsCenter(views);
        if (center != null)
        {
          Float2D floatingCenter = new Float2D(center);
          editAction = createEdit(new MoveComponents(views, false), floatingCenter);
        }
      }
    }
  }

  public void editActionMove()
  {
    if (editAction == null)
    {
      Int2D position = mousePosition.get();

      if (position != null)
      {
        List<View> selection = circuitEditor.getCurrentSelection().getSelection();
        if (selection.size() > 0)
        {
          editAction = createEdit(new MoveComponents(selection, false), toFloatingGridPosition(position.x, position.y));
        }
      }
    }
  }

  public void newSubcircuitAction(String subcircuitTypeName)
  {
    circuitEditor.addNewSubcircuit(subcircuitTypeName);
    setViewportParameters(subcircuitTypeName);
  }

  public void deleteSubcircuitAction(String subcircuitTypeName)
  {
    subcircuitViewParameters.remove(subcircuitTypeName);
    List<Integer> bookmarkIds = new ArrayList<>(subcircuitBookmarks.keySet());
    for (Integer bookmarkId : bookmarkIds)
    {
      SubcircuitEditor subcircuitEditor = subcircuitBookmarks.get(bookmarkId);

      if (subcircuitEditor.getTypeName().equals(subcircuitTypeName))
      {
        subcircuitBookmarks.remove(bookmarkId);
      }
    }
  }

  public void renameSubcircuit(String oldSubcircuitTypeName, String newSubcircuitTypeName)
  {
    if (!oldSubcircuitTypeName.equals(newSubcircuitTypeName))
    {
      SubcircuitViewParameters subcircuitViewParameters = this.subcircuitViewParameters.get(oldSubcircuitTypeName);
      if (subcircuitViewParameters != null)
      {
        this.subcircuitViewParameters.remove(oldSubcircuitTypeName);
        this.subcircuitViewParameters.put(newSubcircuitTypeName, subcircuitViewParameters);
      }
    }
  }

  public void gotoSubcircuit(int bookmarkIndex)
  {
    SubcircuitEditor subcircuitEditor = subcircuitBookmarks.get(bookmarkIndex);
    if (subcircuitEditor != null)
    {
      if (editAction != null)
      {
        editAction.discard();
        editAction = null;
      }

      String subcircuitTypeName = circuitEditor.gotoSubcircuit(subcircuitEditor);
      setViewportParameters(subcircuitTypeName);
    }
  }

  private void setViewportParameters(String subcircuitTypeName)
  {
    if (!StringUtil.isEmptyOrNull(subcircuitTypeName))
    {
      SubcircuitViewParameters parameters = this.subcircuitViewParameters.get(subcircuitTypeName);
      if (parameters != null)
      {
        viewport.setParameters(parameters.getPosition(), parameters.getZoom());
      }
    }
  }

  public void gotoPreviousSubcircuit()
  {
    if (circuitEditor.hasMultipleSubcircuits())
    {
      if (editAction != null)
      {
        editAction.discard();
        editAction = null;
      }

      String subcircuitTypeName = circuitEditor.gotoPreviousSubcircuit();
      setViewportParameters(subcircuitTypeName);
    }
  }

  public void gotoNextSubcircuit()
  {
    if (circuitEditor.hasMultipleSubcircuits())
    {
      if (editAction != null)
      {
        editAction.discard();
        editAction = null;
      }

      String subcircuitTypeName = circuitEditor.gotoNextSubcircuit();
      setViewportParameters(subcircuitTypeName);
    }
  }

  public void bookmarkSubcircuit(int bookmarkIndex)
  {
    SubcircuitEditor subcircuitEditor = circuitEditor.getCurrentSubcircuitEditor();
    subcircuitBookmarks.put(bookmarkIndex, subcircuitEditor);
  }

  public void leaveSubcircuit()
  {
    throw new SimulatorException();
  }

  public void reenterSubcircuit()
  {
    throw new SimulatorException();
  }
}

