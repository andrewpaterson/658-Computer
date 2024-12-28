package net.logicim.ui.simulation.component.subcircuit;

import net.common.SimulatorException;
import net.common.type.Float2D;
import net.common.type.Int2D;
import net.common.type.Tuple2;
import net.common.util.StringUtil;
import net.logicim.data.integratedcircuit.decorative.HorizontalAlignment;
import net.logicim.data.subciruit.SubcircuitInstanceData;
import net.logicim.data.subciruit.SubcircuitInstanceProperties;
import net.logicim.data.subciruit.SubcircuitInstanceSimulationSimulationData;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.port.TracePort;
import net.logicim.domain.passive.subcircuit.SubcircuitInstance;
import net.logicim.domain.passive.subcircuit.SubcircuitInstanceSimulation;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.circuit.CircuitInstanceView;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.circuit.path.ViewPath;
import net.logicim.ui.circuit.path.ViewPathComponentSimulation;
import net.logicim.ui.circuit.path.ViewPaths;
import net.logicim.ui.common.Colours;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.integratedcircuit.ComponentView;
import net.logicim.ui.shape.common.BoundingBox;
import net.logicim.ui.shape.rectangle.Rectangle;
import net.logicim.ui.shape.rectangle.RectangleView;
import net.logicim.ui.shape.text.TextView;
import net.logicim.ui.simulation.DebugGlobalEnvironment;
import net.logicim.ui.simulation.component.passive.pin.PinPropertyHelper;
import net.logicim.ui.simulation.component.passive.pin.PinView;

import java.awt.*;
import java.util.List;
import java.util.*;

import static java.awt.Font.SANS_SERIF;

public class SubcircuitInstanceView
    extends ComponentView<SubcircuitInstanceProperties>
    implements CircuitInstanceView
{
  public static final String SUBCIRCUIT_INSTANCE = "Subcircuit";

  protected List<SubcircuitPinView> pinViews;
  protected SubcircuitView instanceSubcircuitView;

  protected RectangleView rectangle;
  protected TextView typeName;
  protected TextView name;
  protected TextView comment;

  protected ViewPathComponentSimulation<SubcircuitInstance> simulationSubcircuitInstances;  //These are the simulations from the containing subcircuit view.

  public SubcircuitInstanceView(SubcircuitView containingSubcircuitView,
                                SubcircuitView instanceSubcircuitView,
                                Int2D position,
                                Rotation rotation,
                                SubcircuitInstanceProperties properties)
  {
    super(containingSubcircuitView,
          position,
          rotation,
          properties);
    this.instanceSubcircuitView = instanceSubcircuitView;
    this.pinViews = new ArrayList<>();
    this.simulationSubcircuitInstances = new ViewPathComponentSimulation<>();

    createPinsAndGraphics();
    createPortViews();
    finaliseView();
  }

  private void createPinsAndGraphics()
  {
    Rectangle rectangle = createTypeGraphics();

    PinPropertyHelper helper = new PinPropertyHelper(instanceSubcircuitView.findAllPins());
    QuadPinViewLists pinLists = new QuadPinViewLists(helper);

    LeftRightPinViewOffsets leftRightPinViewOffsets = new LeftRightPinViewOffsets(rectangle, pinLists.topViewLists, pinLists.bottomViewLists);
    TopBottomPinViewOffsets topBottomPinViewOffsets = new TopBottomPinViewOffsets(rectangle, pinLists.leftViewLists, pinLists.rightViewLists);

    int left = (int) Math.ceil(rectangle.getBottomRight().getX() + calculateMaxSize(topBottomPinViewOffsets.leftPinViewOffsets));
    int right = (int) Math.floor(rectangle.getTopLeft().getX() - calculateMaxSize(topBottomPinViewOffsets.rightPinViewOffsets));

    rectangle.getBottomRight().setMaxX(left);
    rectangle.getTopLeft().setMinX(right);

    createPins(rectangle,
               leftRightPinViewOffsets.topPinViewOffsets,
               leftRightPinViewOffsets.bottomPinViewOffsets,
               topBottomPinViewOffsets.leftPinViewOffsets,
               topBottomPinViewOffsets.rightPinViewOffsets,
               left,
               right);

    this.rectangle = createRectangleView(rectangle);
  }

  private RectangleView createRectangleView(Rectangle rectangle)
  {
    return new RectangleView(this,
                             new Int2D(rectangle.getTopLeft().getY(), rectangle.getTopLeft().getX()),
                             new Int2D(rectangle.getBottomRight().getY(), rectangle.getBottomRight().getX()),
                             true,
                             true);
  }

  private void createPins(Rectangle rectangle,
                          List<PinViewOffset> topPinViewOffsets,
                          List<PinViewOffset> bottomPinViewOffsets,
                          List<PinViewOffset> leftPinViewOffsets,
                          List<PinViewOffset> rightPinViewOffsets,
                          int leftPos,
                          int rightPos)
  {
    int bottom = rectangle.getBottomRight().getIntY();
    int top = rectangle.getTopLeft().getIntY();

    createTopBottomSubcircuitPinViews(topPinViewOffsets, top, HorizontalAlignment.LEFT);
    createTopBottomSubcircuitPinViews(bottomPinViewOffsets, bottom, HorizontalAlignment.RIGHT);

    createLeftRightSubcircuitPinViews(leftPinViewOffsets, leftPos, HorizontalAlignment.LEFT);
    createLeftRightSubcircuitPinViews(rightPinViewOffsets, rightPos, HorizontalAlignment.RIGHT);
  }

  private void createLeftRightSubcircuitPinViews(List<PinViewOffset> pinViewOffsets, int offset, HorizontalAlignment alignment)
  {
    for (PinViewOffset pinViewOffset : pinViewOffsets)
    {
      pinViews.add(new SubcircuitPinView(pinViewOffset.pinView,
                                         this,
                                         new Int2D(pinViewOffset.pinOffset, offset),
                                         SANS_SERIF,
                                         10,
                                         alignment,
                                         0));
    }
  }

  private void createTopBottomSubcircuitPinViews(List<PinViewOffset> pinViewOffsets, int offset, HorizontalAlignment alignment)
  {
    for (PinViewOffset pinViewOffset : pinViewOffsets)
    {
      pinViews.add(new SubcircuitPinView(pinViewOffset.pinView,
                                         this,
                                         new Int2D(offset, pinViewOffset.pinOffset),
                                         SANS_SERIF,
                                         10,
                                         alignment,
                                         1));
    }
  }

  private float calculateMaxSize(List<PinViewOffset> pinViewOffsets)
  {
    float maxWidth = 0;
    for (PinViewOffset pinViewOffset : pinViewOffsets)
    {
      Tuple2 dimension = pinViewOffset.pinView.getLabelView().getTextDimension();
      if (dimension.getX() > maxWidth)
      {
        maxWidth = dimension.getX();
      }
    }
    return maxWidth;
  }

  private Rectangle createTypeGraphics()
  {
    typeName = new TextView(this, new Float2D(0, 0), properties.subcircuitTypeName, SANS_SERIF, 13, true, HorizontalAlignment.CENTER);

    comment = null;
    if (!StringUtil.isEmptyOrNull(properties.comment))
    {
      comment = new TextView(this, new Float2D(0, 0), properties.comment, SANS_SERIF, 10, false, HorizontalAlignment.CENTER);
      comment.setColor(Colours.getInstance().getCommentText());
    }

    name = null;
    if (!StringUtil.isEmptyOrNull(properties.name))
    {
      name = new TextView(this, new Float2D(0, 0), properties.name, SANS_SERIF, 10, false, HorizontalAlignment.CENTER);
    }

    TextView.centerHorizontally(typeName, comment, name);
    BoundingBox boundingBox = new BoundingBox();
    BoundingBox.calculateBoundingBox(boundingBox, typeName, comment, name);

    Float2D topLeft = new Float2D(boundingBox.getTopLeft());
    Float2D bottomRight = new Float2D(boundingBox.getBottomRight());

    topLeft.x = (float) Math.floor(topLeft.x);
    bottomRight.x = (float) Math.ceil(bottomRight.x);
    topLeft.y = (float) Math.floor(topLeft.y - 0.4f);
    bottomRight.y = (float) Math.ceil(bottomRight.y + 0.4f);

    Rectangle rectangle = new Rectangle(new Int2D(topLeft), new Int2D(bottomRight));
    rectangle.rotateRight();
    return rectangle;
  }

  protected void validatePorts(ViewPath viewPath, CircuitSimulation circuitSimulation)
  {
  }

  @Override
  public SubcircuitInstanceData save(boolean selected)
  {
    return new SubcircuitInstanceData(getTypeName(),
                                      position,
                                      rotation,
                                      properties.name,
                                      id,
                                      enabled,
                                      selected,
                                      saveSimulationSubcircuitInstances(),
                                      savePorts(),
                                      properties.comment,
                                      properties.width,
                                      properties.height);
  }

  @Override
  public SubcircuitInstance createComponent(ViewPath viewPath, CircuitSimulation circuitSimulation)
  {
    DebugGlobalEnvironment.validateCanCreateComponent();
    SubcircuitSimulation containingSubcircuitSimulation = viewPath.getSubcircuitSimulation(circuitSimulation);
    validateCanCreateComponent(viewPath, circuitSimulation);

    SubcircuitInstance subcircuitInstance = new SubcircuitInstance(containingSubcircuitSimulation, properties.name);
    SubcircuitInstanceSimulation subcircuitInstanceSimulation = new SubcircuitInstanceSimulation(circuitSimulation, subcircuitInstance);
    instanceSubcircuitView.addSubcircuitSimulation(subcircuitInstanceSimulation);
    subcircuitInstance.setSubcircuitInstanceSimulation(subcircuitInstanceSimulation);

    putContainingSubcircuitSimulation(containingSubcircuitSimulation, subcircuitInstance, viewPath, circuitSimulation);

    return subcircuitInstance;
  }

  public void createComponentsForSubcircuitInstanceView(ViewPath viewPath,
                                                        CircuitSimulation circuitSimulation,
                                                        SubcircuitInstance subcircuitInstance)
  {
    createTracePorts(subcircuitInstance);
    postCreateComponent(viewPath, circuitSimulation, subcircuitInstance);
    subcircuitInstance.reset(circuitSimulation.getSimulation());
  }

  public SubcircuitInstanceCreation createComponentInSubcircuitInstanceCreation(ViewPath viewPath, CircuitSimulation circuitSimulation)
  {
    ViewPath thisViewPath = getViewPaths().getViewPath(viewPath, this);

    SubcircuitSimulation containingSubcircuitSimulation = viewPath.getSubcircuitSimulation(circuitSimulation);
    SubcircuitInstance subcircuitInstance = createComponent(viewPath, circuitSimulation);

    SubcircuitInstanceSimulation subcircuitInstanceSimulation = subcircuitInstance.getSubcircuitInstanceSimulation();
    thisViewPath.addSubcircuitSimulation(subcircuitInstanceSimulation);

    return new SubcircuitInstanceCreation(this,
                                          viewPath,
                                          circuitSimulation,
                                          containingSubcircuitSimulation,
                                          subcircuitInstance
    );
  }

  //Called from SubcircuitInstanceData.
  public SubcircuitInstance createSubcircuitInstance(ViewPath viewPath,
                                                     CircuitSimulation circuitSimulation,
                                                     SubcircuitInstanceSimulation subcircuitInstanceSimulation)
  {
    SubcircuitSimulation containingSubcircuitSimulation = viewPath.getSubcircuitSimulation(circuitSimulation);
    SubcircuitInstance subcircuitInstance = new SubcircuitInstance(containingSubcircuitSimulation, properties.name);
    subcircuitInstanceSimulation.setSubcircuitInstance(subcircuitInstance);
    subcircuitInstance.setSubcircuitInstanceSimulation(subcircuitInstanceSimulation);

    createTracePorts(subcircuitInstance);
    putContainingSubcircuitSimulation(containingSubcircuitSimulation, subcircuitInstance, viewPath, circuitSimulation);
    postCreateComponent(viewPath, circuitSimulation, subcircuitInstance);

    return subcircuitInstance;
  }

  private void createTracePorts(SubcircuitInstance subcircuitInstance)
  {
    List<PinView> pins = instanceSubcircuitView.findAllPins();
    for (PinView pinView : pins)
    {
      List<TracePort> tracePorts = new ArrayList<>();
      List<String> pinPortNames = getPinPortNames(pinView);
      for (String pinPortName : pinPortNames)
      {
        TracePort tracePort = new TracePort(pinPortName, subcircuitInstance);
        tracePorts.add(tracePort);
      }

      subcircuitInstance.addTracePorts(pinView.getName(), tracePorts);
    }
  }

  protected void putContainingSubcircuitSimulation(SubcircuitSimulation subcircuitSimulation,
                                                   SubcircuitInstance subcircuitInstance,
                                                   ViewPath viewPath,
                                                   CircuitSimulation circuitSimulation)
  {
    if (simulationSubcircuitInstances.get(viewPath, circuitSimulation) != null)
    {
      throw new SimulatorException("A subcircuit instance [%s] for simulation [%s] already exists.", subcircuitInstance.getDescription(), subcircuitSimulation.getDescription());
    }

    simulationSubcircuitInstances.put(viewPath, circuitSimulation, subcircuitInstance);
  }

  private List<String> getPinPortNames(PinView pinView)
  {
    ArrayList<String> result = new ArrayList<>();
    for (String portName : pinView.getPortNames())
    {
      result.add(pinView.getName() + " " + portName);
    }
    return result;
  }

  @Override
  public String getName()
  {
    return properties.name;
  }

  @Override
  public String getType()
  {
    return SUBCIRCUIT_INSTANCE;
  }

  public String getTypeName()
  {
    return instanceSubcircuitView.getTypeName();
  }

  @Override
  public String getDescription()
  {
    return "Instance " + getShortDescription();
  }

  @Override
  public ViewPathComponentSimulation<SubcircuitInstance> getViewPathComponentSimulation()
  {
    return simulationSubcircuitInstances;
  }

  //Is this method name right?
  @Override
  public Collection<? extends SubcircuitSimulation> getInstanceSubcircuitSimulations(CircuitSimulation circuitSimulation)
  {
    return simulationSubcircuitInstances.getSimulations(circuitSimulation);
  }

  @Override
  public SubcircuitSimulation getSubcircuitInstanceSimulationForParent(SubcircuitSimulation wantedParentSubcircuitSimulation)
  {
    SubcircuitInstance subcircuitInstance = simulationSubcircuitInstances.getComponentSlow(wantedParentSubcircuitSimulation);
    if (subcircuitInstance != null)
    {
      return subcircuitInstance.getSubcircuitInstanceSimulation();
    }
    else
    {
      return null;
    }
  }

  @Override
  public List<? extends SubcircuitSimulation> getInstanceSubcircuitSimulations()
  {
    List<SubcircuitSimulation> subcircuitSimulations = new ArrayList<>();
    Set<Map.Entry<ViewPath, Map<CircuitSimulation, SubcircuitInstance>>> entrySet = simulationSubcircuitInstances.getEntrySet();
    for (Map.Entry<ViewPath, Map<CircuitSimulation, SubcircuitInstance>> pathEntry : entrySet)
    {
      Map<CircuitSimulation, SubcircuitInstance> value = pathEntry.getValue();
      Collection<SubcircuitInstance> subcircuitInstances = value.values();
      for (SubcircuitInstance subcircuitInstance : subcircuitInstances)
      {
        subcircuitSimulations.add(subcircuitInstance.getSubcircuitInstanceSimulation());
      }
    }

    return subcircuitSimulations;
  }

  public String getShortDescription()
  {
    String name = getName();
    if (!StringUtil.isEmptyOrNull(name))
    {
      name = "(" + name + ") ";
    }
    else
    {
      name = "";
    }
    return getTypeName() + " " + name + "(" + getPosition() + ")";
  }

  public String toString()
  {
    String name = getName();
    if (!StringUtil.isEmptyOrNull(name))
    {
      name = "(" + name + ")";
    }
    else
    {
      name = "";
    }
    return getTypeName() + name;
  }

  @Override
  public void clampProperties(SubcircuitInstanceProperties newProperties)
  {
  }

  @Override
  public List<ConnectionView> getConnectionViews()
  {
    List<ConnectionView> connections = new ArrayList<>();
    for (SubcircuitPinView pinView : pinViews)
    {
      ConnectionView connection = pinView.getConnection();
      if (connection != null)
      {
        connections.add(connection);
      }
    }
    return connections;
  }

  @Override
  protected void createPortViews()
  {
    for (SubcircuitPinView pinView : pinViews)
    {
      pinView.createPortView(getPinPortNames(pinView.getPinView()));
    }
  }

  public SubcircuitInstance getComponent(ViewPath viewPath, CircuitSimulation circuitSimulation)
  {
    return simulationSubcircuitInstances.get(viewPath, circuitSimulation);
  }

  @Override
  public void destroyComponent(ViewPath viewPath, CircuitSimulation circuitSimulation)
  {
    SubcircuitInstance removedSubcircuitInstance = simulationSubcircuitInstances.remove(viewPath, circuitSimulation);
    if (removedSubcircuitInstance == null)
    {
      throw new SimulatorException("[%s] could not find a component for Path [%s] for Simulation [%s].",
                                   getDescription(),
                                   viewPath.getDescription(),
                                   circuitSimulation.getDescription());
    }

    ViewPath nextViewPath = getViewPaths().getViewPath(viewPath, this);
    instanceSubcircuitView.destroySubcircuitInstanceComponentsAndSimulations(nextViewPath, circuitSimulation);
    destroyPortViewComponents(viewPath, circuitSimulation);
    Circuit circuit = circuitSimulation.getCircuit();
    circuit.remove(removedSubcircuitInstance);
    simulationSubcircuitInstances.remove(viewPath, circuitSimulation);
  }

  @Override
  public void destroyAllComponents()
  {
    ViewPaths viewPaths = instanceSubcircuitView.getCircuitEditor().getViewPaths();
    Set<Map.Entry<ViewPath, Map<CircuitSimulation, SubcircuitInstance>>> entrySet = simulationSubcircuitInstances.getEntrySet();
    for (Map.Entry<ViewPath, Map<CircuitSimulation, SubcircuitInstance>> pathEntry : entrySet)
    {
      ViewPath viewPath = pathEntry.getKey();
      ViewPath fullPath = viewPaths.getViewPath(viewPath, this);

      Map<CircuitSimulation, SubcircuitInstance> map = pathEntry.getValue();
      for (CircuitSimulation circuitSimulation : map.keySet())
      {
        instanceSubcircuitView.destroySubcircuitInstanceComponentsAndSimulations(fullPath, circuitSimulation);
      }
    }

    for (Map.Entry<ViewPath, Map<CircuitSimulation, SubcircuitInstance>> pathEntry : entrySet)
    {
      Map<CircuitSimulation, SubcircuitInstance> map = pathEntry.getValue();
      for (SubcircuitInstance subcircuitInstance : map.values())
      {
        Circuit circuit = subcircuitInstance.getCircuit();
        circuit.remove(subcircuitInstance);
      }
    }

    simulationSubcircuitInstances.clear();
  }

  @Override
  protected void finaliseView()
  {
    super.finaliseView();
    containingSubcircuitView.addSubcircuitInstanceView(this);
  }

  @Override
  public String getComponentType()
  {
    if (simulationSubcircuitInstances != null)
    {
      return simulationSubcircuitInstances.getComponentType();
    }
    return "";

  }

  @Override
  public void paint(Graphics2D graphics,
                    Viewport viewport,
                    ViewPath viewPath,
                    CircuitSimulation circuitSimulation)
  {
    super.paint(graphics,
                viewport,
                viewPath,
                circuitSimulation);

    Color color = graphics.getColor();
    Stroke stroke = graphics.getStroke();
    Font font = graphics.getFont();

    rectangle.updateGridCache();
    rectangle.paint(graphics, viewport);
    for (SubcircuitPinView pinView : pinViews)
    {
      pinView.paint(graphics, viewport);
    }

    if (typeName != null)
    {
      typeName.paint(graphics, viewport);
    }
    if (comment != null)
    {
      comment.paint(graphics, viewport);
    }
    if (name != null)
    {
      name.paint(graphics, viewport);
    }

    paintPorts(graphics,
               viewport,
               viewPath,
               circuitSimulation);

    graphics.setFont(font);
    graphics.setColor(color);
    graphics.setStroke(stroke);
  }

  public void updateTypeName()
  {
    properties.subcircuitTypeName = instanceSubcircuitView.getTypeName();
  }

  @Override
  public void setRotation(Rotation rotation)
  {
    super.setRotation(rotation);
    updateTextViews();
  }

  private void updateTextViews()
  {
  }

  @Override
  public SubcircuitView getInstanceSubcircuitView()
  {
    return instanceSubcircuitView;
  }

  protected List<SubcircuitInstanceSimulationSimulationData> saveSimulationSubcircuitInstances()
  {
    ArrayList<SubcircuitInstanceSimulationSimulationData> result = new ArrayList<>();
    Set<Map.Entry<ViewPath, Map<CircuitSimulation, SubcircuitInstance>>> entries = simulationSubcircuitInstances.getEntrySet();
    for (Map.Entry<ViewPath, Map<CircuitSimulation, SubcircuitInstance>> pathEntry : entries)
    {
      Map<CircuitSimulation, SubcircuitInstance> circuitSimulations = pathEntry.getValue();
      ViewPath viewPath = pathEntry.getKey();
      for (Map.Entry<CircuitSimulation, SubcircuitInstance> circuitSimulationEntry : circuitSimulations.entrySet())
      {
        SubcircuitInstance subcircuitInstance = circuitSimulationEntry.getValue();
        CircuitSimulation circuitSimulation = circuitSimulationEntry.getKey();
        SubcircuitInstanceSimulation instanceSubcircuitSimulation = subcircuitInstance.getSubcircuitInstanceSimulation();
        SubcircuitSimulation containingSubcircuitSimulation = subcircuitInstance.getContainingSubcircuitSimulation();
        result.add(new SubcircuitInstanceSimulationSimulationData(containingSubcircuitSimulation.getId(),
                                                                  instanceSubcircuitSimulation.getId(),
                                                                  viewPath.getId(),
                                                                  circuitSimulation.getId()));
      }
    }
    return result;
  }

  public SubcircuitSimulation getContainingSubcircuitSimulation(long simulationId)
  {
    for (Map.Entry<ViewPath, Map<CircuitSimulation, SubcircuitInstance>> pathEntry : simulationSubcircuitInstances.getEntrySet())
    {
      Map<CircuitSimulation, SubcircuitInstance> subcircuitInstanceMap = pathEntry.getValue();
      for (SubcircuitInstance subcircuitInstance : subcircuitInstanceMap.values())
      {
        SubcircuitSimulation containingSubcircuitSimulation = subcircuitInstance.getContainingSubcircuitSimulation();
        if (containingSubcircuitSimulation.getId() == simulationId)
        {
          return containingSubcircuitSimulation;
        }
      }
    }
    return null;
  }

  public SubcircuitInstanceSimulation getInstanceSubcircuitSimulation(long simulationId)
  {
    for (Map.Entry<ViewPath, Map<CircuitSimulation, SubcircuitInstance>> pathEntry : simulationSubcircuitInstances.getEntrySet())
    {
      Map<CircuitSimulation, SubcircuitInstance> subcircuitInstanceMap = pathEntry.getValue();
      for (SubcircuitInstance subcircuitInstance : subcircuitInstanceMap.values())
      {
        SubcircuitInstanceSimulation instanceSubcircuitSimulation = subcircuitInstance.getSubcircuitInstanceSimulation();
        if (instanceSubcircuitSimulation.getId() == simulationId)
        {
          return instanceSubcircuitSimulation;
        }
      }
    }
    return null;
  }

  @Override
  public String toDebugString()
  {
    return super.toDebugString() + String.format("Subcircuit Type Name [%s]\nComment [%s]\nWidth [%s]\nHeight [%s]\n",
                                                 properties.subcircuitTypeName,
                                                 properties.comment,
                                                 properties.width,
                                                 properties.height) +
           toSimulationsDebugString(simulationSubcircuitInstances.getSimulations());
  }

  public SubcircuitPinView getSubcircuitPinView(ConnectionView connection)
  {
    for (SubcircuitPinView subcircuitPinView : pinViews)
    {
      if (subcircuitPinView.getConnection() == connection)
      {
        return subcircuitPinView;
      }
    }
    return null;
  }

  public SubcircuitPinView getSubcircuitPinView(PinView pinView)
  {
    for (SubcircuitPinView subcircuitPinView : pinViews)
    {
      if (subcircuitPinView.getPinView() == pinView)
      {
        return subcircuitPinView;
      }
    }
    return null;
  }

  public Set<? extends SubcircuitSimulation> getComponentSubcircuitSimulations()
  {
    return simulationSubcircuitInstances.getSimulations();
  }

  public ViewPaths getViewPaths()
  {
    return getInstanceSubcircuitView().getCircuitEditor().getViewPaths();
  }
}

