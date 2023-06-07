package net.logicim.ui;

import net.logicim.common.SimulatorException;
import net.logicim.common.reflect.PackageInspector;
import net.logicim.common.reflect.PackageInspectorStore;
import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.common.util.StringUtil;
import net.logicim.data.circuit.CircuitData;
import net.logicim.data.common.properties.ComponentProperties;
import net.logicim.data.editor.BookmarkData;
import net.logicim.data.editor.DefaultComponentPropertiesData;
import net.logicim.data.editor.EditorData;
import net.logicim.data.editor.SubcircuitParameterData;
import net.logicim.domain.CircuitSimulation;
import net.logicim.ui.circuit.SubcircuitInstanceViewFactory;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.clipboard.ClipboardData;
import net.logicim.ui.common.*;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.common.integratedcircuit.View;
import net.logicim.ui.common.wire.TraceView;
import net.logicim.ui.editor.EditorAction;
import net.logicim.ui.editor.SimulationSpeed;
import net.logicim.ui.editor.SubcircuitViewParameters;
import net.logicim.ui.info.InfoLabel;
import net.logicim.ui.info.InfoLabels;
import net.logicim.ui.input.EditorActionsFactory;
import net.logicim.ui.input.KeyInputsFactory;
import net.logicim.ui.input.action.InputActions;
import net.logicim.ui.input.action.KeyInput;
import net.logicim.ui.input.button.ButtonInput;
import net.logicim.ui.input.event.SimulatorEditorEvent;
import net.logicim.ui.input.keyboard.KeyboardButtons;
import net.logicim.ui.input.mouse.MouseButtons;
import net.logicim.ui.input.mouse.MouseMotion;
import net.logicim.ui.input.mouse.MousePosition;
import net.logicim.ui.panels.SimulatorPanel;
import net.logicim.ui.placement.*;
import net.logicim.ui.property.DefaultComponentProperties;
import net.logicim.ui.shape.common.BoundingBox;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.SubcircuitEditor;
import net.logicim.ui.simulation.TopLevelSubcircuitSimulation;
import net.logicim.ui.simulation.component.factory.ViewFactory;
import net.logicim.ui.simulation.component.factory.ViewFactoryStore;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;
import net.logicim.ui.simulation.order.SubcircuitOrderer;
import net.logicim.ui.simulation.selection.Selection;
import net.logicim.ui.simulation.selection.SelectionEdit;
import net.logicim.ui.undo.Undo;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

import static java.awt.event.MouseEvent.BUTTON1;

public class Logicim
    implements PanelSize,
               Undo
{
  public static final String MAIN_SUBCIRCUIT_TYPE_NAME = "Main";

  private ConcurrentLinkedDeque<SimulatorEditorEvent> inputEvents;

  private int width;
  private int height;

  protected Viewport viewport;
  protected EditorActions actions;
  protected InputActions inputActions;
  protected InfoLabels labels;

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

  protected SimulationSpeed simulationSpeed;

  protected Rotation creationRotation;

  protected ClipboardData clipboard;

  protected Map<Integer, SubcircuitEditor> subcircuitBookmarks;
  protected Map<String, SubcircuitViewParameters> subcircuitViewParameters;

  protected boolean drawPointGrid;

  public Logicim(SimulatorPanel simulatorPanel)
  {
    this.inputEvents = new ConcurrentLinkedDeque<>();

    this.viewport = new Viewport(this);

    this.mouseMotion = new MouseMotion();
    this.mouseButtons = new MouseButtons();
    this.mousePosition = new MousePosition();

    this.keyboardButtons = new KeyboardButtons();

    this.actions = new EditorActions();
    this.inputActions = new InputActions();
    this.labels = new InfoLabels();

    this.circuitEditor = new CircuitEditor(MAIN_SUBCIRCUIT_TYPE_NAME);
    this.editAction = null;
    this.creationRotation = Rotation.West;

    this.simulationSpeed = new SimulationSpeed();

    this.undoStack = new UndoStack();
    this.clipboard = null;

    this.subcircuitBookmarks = new LinkedHashMap<>();
    this.subcircuitViewParameters = new LinkedHashMap<>();

    this.drawPointGrid = true;

    setSubcircuitParameters(MAIN_SUBCIRCUIT_TYPE_NAME);

    addActions(simulatorPanel);

    pushUndo();
  }

  public void windowClosing()
  {
    simulationSpeed.setRunning(false);
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
      if (simulationSpeed.isRunning() && canRun())
      {
        runToTime(simulationSpeed.getRunTimeStep());
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
            edit = new StartEditInPort(keyboardButtons, getCircuitSimulation());
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

  protected CircuitSimulation getCircuitSimulation()
  {
    return circuitEditor.getCircuitSimulation();
  }

  protected EditAction createEdit(StatefulEdit edit, Float2D start)
  {
    EditAction editAction = new EditAction(start,
                                           edit,
                                           circuitEditor,
                                           this);
    updateHighlighted();

    return editAction;
  }

  public void updateHighlighted()
  {
    clearHover();
    calculateHighlightedPort();
  }

  protected Float2D toFloatingGridPosition(float x, float y)
  {
    return new Float2D(viewport.transformScreenToGridX(x),
                       viewport.transformScreenToGridY(y));
  }

  protected Int2D toIntegerGridPosition(int x, int y)
  {
    return new Int2D(viewport.transformScreenToGridX(x),
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
        setSubcircuitParameters(getCurrentSubcircuitEditor().getTypeName());
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

    for (View view : getCurrentSelection())
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
      StaticView<?> staticView = viewFactory.create(circuitEditor, toIntegerGridPosition(position.x, position.y), creationRotation);
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
      SubcircuitView instanceSubcircuitView = subcircuitEditor.getSubcircuitView();
      SubcircuitInstanceViewFactory viewFactory = (SubcircuitInstanceViewFactory) ViewFactoryStore.getInstance().get(SubcircuitInstanceView.class);
      viewFactory.setSubcircuitTypeName(instanceSubcircuitView.getTypeName());
      SubcircuitInstanceView subcircuitInstanceView = viewFactory.create(circuitEditor, toIntegerGridPosition(position.x, position.y), creationRotation);
      editAction = createEdit(new MoveComponents(subcircuitInstanceView, true), toFloatingGridPosition(position.x, position.y));
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

    if (drawPointGrid)
    {
      viewport.paintGrid(graphics);
    }

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

            new ConnectionInformationPanel(hoverConnectionView, graphics, viewport, infoWidth, infoHeight).drawConnectionDetails(circuitEditor.getCircuitSimulation(), x, y);
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
    setSubcircuitParameters(getCurrentSubcircuitEditor().getTypeName());

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
    return getCurrentSubcircuitEditor().getTraceViewInScreenSpace(viewport, mousePosition);
  }

  private StaticView<?> calculateHoverView(Int2D mousePosition)
  {
    return circuitEditor.getComponentViewInScreenSpace(viewport, mousePosition);
  }

  private void addActions(SimulatorPanel simulatorPanel)
  {
    EditorActionsFactory.create(this, simulatorPanel);
    KeyInputsFactory.create(this);
    inputActions.validate();
  }

  public void addKeyInput(KeyInput keyInput)
  {
    inputActions.addKeyInputs(keyInput);
  }

  public void addButtonInput(ButtonInput buttonInput)
  {
    inputActions.addButtonInput(buttonInput);
  }

  public void keyPressed(int keyCode, boolean controlDown, boolean altDown, boolean shiftDown)
  {
    keyboardButtons.set(altDown, controlDown, shiftDown);
    inputActions.keyPressed(keyCode, controlDown, altDown, shiftDown);

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

  public boolean canTransformComponents()
  {
    if (editAction != null)
    {
      if (editAction.canTransformComponents())
      {
        return true;
      }
    }
    else
    {
      if (hoverComponentView != null)
      {
        return true;
      }
      else if (!getCurrentSelection().isEmpty())
      {
        return true;
      }
    }
    return false;
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

  protected Int2D getMousePositionOnGrid()
  {
    Int2D position = mousePosition.get();
    if (position != null)
    {
      return toIntegerGridPosition(position.x, position.y);
    }
    else
    {
      return null;
    }
  }

  public float getMouseXOnGrid()
  {
    Int2D position = mousePosition.get();
    if (position != null)
    {
      return viewport.transformScreenToGridX((float) position.x);
    }
    else
    {
      return Float.NaN;
    }
  }

  public float getMouseYOnGrid()
  {
    Int2D position = mousePosition.get();
    if (position != null)
    {
      return viewport.transformScreenToGridY((float) position.y);
    }
    else
    {
      return Float.NaN;
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
    boolean running = simulationSpeed.toggleTunSimulation();
    if (running)
    {
      TopLevelSubcircuitSimulation simulation = circuitEditor.getCurrentTopLevelSimulation();
      if (simulation == null)
      {
        throw new SimulatorException("Cannot run simulation with a [null] simulation.");
      }
    }
  }

  public boolean canDelete()
  {
    if (circuitEditor.getCurrentSelection().isSelectionEmpty())
    {
      if (hoverConnectionView != null)
      {
        return true;
      }
      else if (hoverComponentView != null)
      {
        return true;
      }
    }
    else
    {
      if (getCurrentSelection().size() > 0)
      {
        return true;
      }
    }
    return false;
  }

  public boolean editActionDeleteComponentIfPossible()
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
    simulationSpeed.increaseSimulationSpeed();
  }

  public void decreaseSimulationSpeed()
  {
    simulationSpeed.decreaseSimulationSpeed();
  }

  public void addEditorEvent(SimulatorEditorEvent event)
  {
    inputEvents.add(event);
  }

  public void resetSimulation()
  {
    circuitEditor.reset();
  }

  public EditorData save()
  {
    CircuitData circuitData = circuitEditor.save();
    ArrayList<BookmarkData> subcircuitBookmarks = saveBookmarks();
    ArrayList<SubcircuitParameterData> subcircuitParameters = saveSubcircuitViewParameters();
    List<DefaultComponentPropertiesData> defaultProperties = saveDefaultComponentProperties();
    String currentSubcircuit = getCurrentSubcircuitEditor().getTypeName();
    return new EditorData(circuitData,
                          simulationSpeed.getDefaultRunTimeStep(),
                          simulationSpeed.getRunTimeStep(),
                          simulationSpeed.isRunning(),
                          creationRotation,
                          subcircuitBookmarks,
                          subcircuitParameters,
                          currentSubcircuit,
                          defaultProperties,
                          drawPointGrid);
  }

  protected List<DefaultComponentPropertiesData> saveDefaultComponentProperties()
  {
    ArrayList<DefaultComponentPropertiesData> defaultProperties = new ArrayList<>();
    Map<Class<? extends StaticView<?>>, ComponentProperties> propertiesMap = DefaultComponentProperties.getInstance().findAll();
    for (Map.Entry<Class<? extends StaticView<?>>, ComponentProperties> entry : propertiesMap.entrySet())
    {
      String className = entry.getKey().getSimpleName();
      ComponentProperties properties = entry.getValue();
      defaultProperties.add(new DefaultComponentPropertiesData(className, properties));
    }
    return defaultProperties;
  }

  protected ArrayList<SubcircuitParameterData> saveSubcircuitViewParameters()
  {
    ArrayList<SubcircuitParameterData> subcircuitParameters = new ArrayList<>();
    for (Map.Entry<String, SubcircuitViewParameters> entry : this.subcircuitViewParameters.entrySet())
    {
      String subcircuitTypeName = entry.getKey();
      SubcircuitViewParameters viewParameters = entry.getValue();
      subcircuitParameters.add(new SubcircuitParameterData(subcircuitTypeName,
                                                           viewParameters.getPosition().clone(),
                                                           viewParameters.getZoom()));
    }
    return subcircuitParameters;
  }

  protected ArrayList<BookmarkData> saveBookmarks()
  {
    ArrayList<BookmarkData> subcircuitBookmarks = new ArrayList<>();
    for (Map.Entry<Integer, SubcircuitEditor> entry : this.subcircuitBookmarks.entrySet())
    {
      Integer bookmarkKey = entry.getKey();
      SubcircuitEditor subcircuitEditor = entry.getValue();
      subcircuitBookmarks.add(new BookmarkData(bookmarkKey, subcircuitEditor.getTypeName()));
    }
    return subcircuitBookmarks;
  }

  public void load(EditorData editorData)
  {
    editAction = null;

    clearHover();

    circuitEditor = new CircuitEditor();
    circuitEditor.load(editorData.circuit);

    simulationSpeed.setRunning(editorData.running);
    simulationSpeed.setRunTimeStep(editorData.runTimeStep);
    simulationSpeed.setDefaultRunTimeStep(editorData.defaultRunTimeStep);

    creationRotation = editorData.creationRotation;
    subcircuitViewParameters = loadSubcircuitViewParameters(editorData);
    subcircuitBookmarks = loadSubcircuitBookmarks(editorData);

    SubcircuitEditor subcircuitEditor = circuitEditor.getSubcircuitEditor(editorData.currentSubcircuit);
    String subcircuitTypeName = circuitEditor.setCurrentSubcircuitEditor(subcircuitEditor);
    setViewportParameters(subcircuitTypeName);

    drawPointGrid = editorData.drawPointGrid;

    PackageInspector packageInspector = PackageInspectorStore.getInstance().getPackageInspector("net.logicim.ui");
    for (DefaultComponentPropertiesData defaultProperty : editorData.defaultProperties)
    {
      Class<? extends StaticView<?>> staticViewClass = (Class<? extends StaticView<?>>) packageInspector.findClassBySimpleName(defaultProperty.propertiesClassName);
      DefaultComponentProperties.getInstance().put(staticViewClass, defaultProperty.properties);
    }

    calculateHighlightedPort();
  }

  protected Map<Integer, SubcircuitEditor> loadSubcircuitBookmarks(EditorData editorData)
  {
    Map<Integer, SubcircuitEditor> subcircuitBookmarks = new LinkedHashMap<>();
    for (BookmarkData subcircuitBookmark : editorData.subcircuitBookmarks)
    {
      subcircuitBookmarks.put(subcircuitBookmark.bookmarkKey, getSubcircuit(subcircuitBookmark.subcircuitTypeName));
    }
    return subcircuitBookmarks;
  }

  protected Map<String, SubcircuitViewParameters> loadSubcircuitViewParameters(EditorData editorData)
  {
    Map<String, SubcircuitViewParameters> subcircuitViewParameters = new LinkedHashMap<>();
    for (SubcircuitParameterData subcircuitParameter : editorData.subcircuitParameters)
    {
      subcircuitViewParameters.put(subcircuitParameter.subcircuitTypeName, new SubcircuitViewParameters(subcircuitParameter.position,
                                                                                                        subcircuitParameter.zoom));
    }
    return subcircuitViewParameters;
  }

  private SubcircuitEditor getSubcircuit(String subcircuitTypeName)
  {
    return circuitEditor.getSubcircuitEditor(subcircuitTypeName);
  }

  public void loadFile(EditorData editorData)
  {
    load(editorData);
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
    EditorData editorData = undoStack.pop();
    if (editorData != null)
    {
      boolean running = simulationSpeed.isRunning();
      load(editorData);
      simulationSpeed.setRunning(running);
    }
  }

  public void redo()
  {
    EditorData editorData = undoStack.unpop();
    if (editorData != null)
    {
      load(editorData);
    }
  }

  public void replaceSelectionInCurrentSubcircuitView(View newView, View oldView)
  {
    circuitEditor.replaceSelection(newView, oldView);
  }

  public boolean canCopy()
  {
    if (editAction == null)
    {
      Int2D position = mousePosition.get();
      if (position != null)
      {
        List<View> selection = getCurrentSelection();
        if (selection.size() > 0)
        {
          return true;
        }
      }
    }
    return false;
  }

  public void editActionCopy()
  {
    if (canCopy())
    {
      clipboard = circuitEditor.copyViews(getCurrentSelection());
    }
  }

  public void editActionCut()
  {
    if (canCopy())
    {
      clipboard = circuitEditor.copyViews(getCurrentSelection());
      circuitEditor.deleteSelection();
    }
  }

  protected List<View> getCurrentSelection()
  {
    Selection currentSelection = circuitEditor.getCurrentSelection();
    if (currentSelection != null)
    {
      return currentSelection.getSelection();
    }
    else
    {
      return new ArrayList<>();
    }
  }

  public boolean canDuplicate()
  {
    if (editAction == null)
    {
      List<View> selection = getCurrentSelection();
      if (selection.size() > 0)
      {
        return true;
      }
    }
    return false;
  }

  public void editActionDuplicate()
  {
    if (canDuplicate())
    {
      List<View> duplicates = circuitEditor.duplicateViews(getCurrentSelection());
      Int2D center = Selection.getViewsCenter(duplicates);
      if (center != null)
      {
        Float2D floatingCenter = new Float2D(center);
        editAction = createEdit(new MoveComponents(duplicates, true), floatingCenter);
      }
    }
  }

  public boolean canPaste()
  {
    if (editAction == null)
    {
      if (clipboard != null)
      {
        return true;
      }
    }
    return false;
  }

  public void editActionPaste()
  {
    if (canPaste())
    {
      List<View> views = circuitEditor.pasteClipboardViews(clipboard.getTraces(), clipboard.getComponents());
      Int2D center = Selection.getViewsCenter(views);
      if (center != null)
      {
        Float2D floatingCenter = new Float2D(center);
        editAction = createEdit(new MoveComponents(views, true), floatingCenter);
      }
    }
  }

  public boolean canMove()
  {
    if (editAction == null)
    {
      Int2D position = mousePosition.get();

      if (position != null)
      {
        List<View> selection = getCurrentSelection();
        if (selection.size() > 0)
        {
          return true;
        }
      }
    }
    return false;
  }

  public void editActionMove()
  {
    if (canMove())
    {
      List<View> selection = getCurrentSelection();
      Int2D position = mousePosition.get();
      editAction = createEdit(new MoveComponents(selection, false), toFloatingGridPosition(position.x, position.y));
    }
  }

  public void newSubcircuitAction(String subcircuitTypeName)
  {
    circuitEditor.addNewSubcircuit(subcircuitTypeName);
    setSubcircuitParameters(subcircuitTypeName);
  }

  public void deleteSubcircuitAction(String subcircuitTypeName, CircuitSimulation circuitSimulation)
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

    for (SubcircuitEditor subcircuitEditor : circuitEditor.getSubcircuitEditors())
    {
      Set<SubcircuitInstanceView> subcircuitInstanceViews = subcircuitEditor.getSubcircuitView().findAllSubcircuitInstanceViews();
      for (SubcircuitInstanceView subcircuitInstanceView : subcircuitInstanceViews)
      {
        if (subcircuitInstanceView.getTypeName().equals(subcircuitTypeName))
        {
          SubcircuitView subcircuitView = subcircuitInstanceView.getSubcircuitView();
          subcircuitView.deleteComponentView(subcircuitInstanceView, circuitSimulation);
        }
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

      for (SubcircuitEditor subcircuitEditor : circuitEditor.getSubcircuitEditors())
      {
        Set<SubcircuitInstanceView> subcircuitInstanceViews = subcircuitEditor.getSubcircuitView().findAllSubcircuitInstanceViews();
        for (SubcircuitInstanceView subcircuitInstanceView : subcircuitInstanceViews)
        {
          if (subcircuitInstanceView.getTypeName().equals(oldSubcircuitTypeName))
          {
            subcircuitInstanceView.updateTypeName();
          }
        }
      }
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

  public boolean canGotoSubcircuit(int bookmarkIndex)
  {
    SubcircuitEditor subcircuitEditor = subcircuitBookmarks.get(bookmarkIndex);
    return subcircuitEditor != null;
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
      updateHighlighted();
    }
  }

  public void gotoPreviousSubcircuit()
  {
    if (canGotoNextSubcircuit())
    {
      if (editAction != null)
      {
        editAction.discard();
        editAction = null;
      }

      String subcircuitTypeName = circuitEditor.gotoPreviousSubcircuit();
      setViewportParameters(subcircuitTypeName);
      updateHighlighted();
    }
  }

  public void gotoNextSubcircuit()
  {
    if (canGotoNextSubcircuit())
    {
      if (editAction != null)
      {
        editAction.discard();
        editAction = null;
      }

      String subcircuitTypeName = circuitEditor.gotoNextSubcircuit();
      setViewportParameters(subcircuitTypeName);
      updateHighlighted();
    }
  }

  public boolean canGotoNextSubcircuit()
  {
    return circuitEditor.hasMultipleSubcircuits();
  }

  public void bookmarkSubcircuit(int bookmarkIndex)
  {
    SubcircuitEditor subcircuitEditor = getCurrentSubcircuitEditor();
    subcircuitBookmarks.put(bookmarkIndex, subcircuitEditor);
  }

  public void leaveSubcircuit()
  {
    updateHighlighted();
    throw new SimulatorException();
  }

  public void reenterSubcircuit()
  {
    updateHighlighted();
    throw new SimulatorException();
  }

  public List<SubcircuitEditor> getSubcircuitEditors()
  {
    return circuitEditor.getSubcircuitEditors();
  }

  public List<String> getAllowedSubcircuitTypeNamesForSubcircuitInstance()
  {
    SubcircuitEditor currentSubcircuitEditor = getCurrentSubcircuitEditor();
    ArrayList<String> result = new ArrayList<>();
    for (SubcircuitEditor subcircuitEditor : getSubcircuitEditors())
    {
      SubcircuitOrderer orderer = new SubcircuitOrderer(circuitEditor.getSubcircuitEditors());
      if (subcircuitEditor != currentSubcircuitEditor)
      {
        orderer.addRequirement(currentSubcircuitEditor, subcircuitEditor.getTypeName());
        List<SubcircuitEditor> order = orderer.order();
        if (order != null)
        {
          result.add(subcircuitEditor.getTypeName());
        }
      }
    }
    Collections.sort(result);
    return result;
  }

  public SubcircuitEditor getCurrentSubcircuitEditor()
  {
    return circuitEditor.getCurrentSubcircuitEditor();
  }

  public TopLevelSubcircuitSimulation getCurrentTopLevelSimulation()
  {
    return circuitEditor.getTopLevelSimulation();
  }

  public CircuitSimulation getCurrentCircuitSimulation()
  {
    return circuitEditor.getCircuitSimulation();
  }

  public int getSimulationCount()
  {
    return circuitEditor.getSimulations().size();
  }

  public List<TopLevelSubcircuitSimulation> getSimulations()
  {
    return circuitEditor.getSimulations();
  }

  public void resetZoom()
  {
    viewport.resetZoom(mousePosition.get());
    setSubcircuitParameters(getCurrentSubcircuitEditor().getTypeName());

    calculateHighlightedPort();
  }

  public void setRunning(boolean running)
  {
    simulationSpeed.setRunning(running);
  }

  public void setCurrentSimulation(TopLevelSubcircuitSimulation simulation)
  {
    this.circuitEditor.setCurrentSimulation(simulation);
  }

  public void addAction(String name, EditorAction action)
  {
    actions.addAction(name, action);
    action.setName(name);
  }

  public EditorAction getAction(String description)
  {
    return actions.getAction(description);
  }

  public void setDefaultSimulationSpeed()
  {
    this.simulationSpeed.setDefaultSimulationSpeed();
  }

  public void flipVertically()
  {
    throw new SimulatorException();
  }

  public void flipHorizontally()
  {
    throw new SimulatorException();
  }

  public void updateButtonsEnabled()
  {
    inputActions.updateButtonsEnabled();
  }

  public void updateLabels()
  {
    labels.updateLabels();
  }

  public boolean canPauseSimulation()
  {
    return simulationSpeed.isRunning();
  }

  public boolean canRedo()
  {
    return undoStack.canUnpop();
  }

  public boolean canUndo()
  {
    return undoStack.canPop();
  }

  public boolean canResetZoom()
  {
    return viewport.canResetZoom();
  }

  public boolean canRunSimulation()
  {
    return !simulationSpeed.isRunning();
  }

  public float getZoom()
  {
    return viewport.getZoom();
  }

  public void addInfoLabel(InfoLabel infoLabel)
  {
    labels.add(infoLabel);
  }

  public void togglePointGrid()
  {
    drawPointGrid = !drawPointGrid;
  }
}

