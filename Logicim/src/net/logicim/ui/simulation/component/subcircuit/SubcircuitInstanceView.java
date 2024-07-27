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
import net.logicim.domain.passive.subcircuit.SubcircuitSimulations;
import net.logicim.ui.circuit.CircuitInstanceView;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Colours;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.integratedcircuit.ComponentView;
import net.logicim.ui.shape.common.BoundingBox;
import net.logicim.ui.shape.rectangle.Rectangle;
import net.logicim.ui.shape.rectangle.RectangleView;
import net.logicim.ui.shape.text.TextView;
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
  protected boolean subcircuitComponentsCreated;
  protected List<SubcircuitPinView> pinViews;
  protected SubcircuitView instanceSubcircuitView;

  protected RectangleView rectangle;
  protected TextView typeName;
  protected TextView name;
  protected TextView comment;

  protected Map<SubcircuitSimulation, SubcircuitInstance> simulationSubcircuitInstances;  //These are the simulations from the containing subcircuit view.

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
    this.subcircuitComponentsCreated = false;
    this.pinViews = new ArrayList<>();
    this.simulationSubcircuitInstances = new LinkedHashMap<>();

    createPinsAndGraphics();
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

  protected void validatePorts(SubcircuitSimulation subcircuitSimulation)
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
  public SubcircuitInstance createComponent(SubcircuitSimulation containingSubcircuitSimulation)
  {
    validateCanCreateComponent(containingSubcircuitSimulation);

    CircuitSimulation circuitSimulation = containingSubcircuitSimulation.getCircuitSimulation();
    SubcircuitInstance subcircuitInstance = new SubcircuitInstance(circuitSimulation.getCircuit(), properties.name);
    SubcircuitInstanceSimulation subcircuitInstanceSimulation = new SubcircuitInstanceSimulation(circuitSimulation, subcircuitInstance);
    instanceSubcircuitView.addSubcircuitSimulation(subcircuitInstanceSimulation);
    subcircuitInstance.setSubcircuitInstanceSimulation(subcircuitInstanceSimulation);

    createTracePorts(subcircuitInstance);
    putContainingSubcircuitSimulation(containingSubcircuitSimulation, subcircuitInstance);
    postCreateComponent(containingSubcircuitSimulation, subcircuitInstance);

    instanceSubcircuitView.createComponentsForSubcircuitInstanceView(subcircuitInstanceSimulation);
    //instanceSubcircuitView.createTracesForSubcircuitInstanceView(this);

    return subcircuitInstance;
  }

  @Override
  public void createComponents(SubcircuitSimulations simulations)
  {
    for (SubcircuitSimulation subcircuitSimulation : simulations.getSubcircuitSimulations())
    {
      createComponent(subcircuitSimulation);
    }
  }

  //Called from SubcircuitInstanceData.
  public SubcircuitInstance createSubcircuitInstance(SubcircuitSimulation containingSubcircuitSimulation, SubcircuitInstanceSimulation subcircuitInstanceSimulation)
  {
    CircuitSimulation circuitSimulation = containingSubcircuitSimulation.getCircuitSimulation();
    SubcircuitInstance subcircuitInstance = new SubcircuitInstance(circuitSimulation.getCircuit(), properties.name);
    subcircuitInstanceSimulation.setSubcircuitInstance(subcircuitInstance);
    subcircuitInstance.setSubcircuitInstanceSimulation(subcircuitInstanceSimulation);

    createTracePorts(subcircuitInstance);
    putContainingSubcircuitSimulation(containingSubcircuitSimulation, subcircuitInstance);
    postCreateComponent(containingSubcircuitSimulation, subcircuitInstance);

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

  protected void putContainingSubcircuitSimulation(SubcircuitSimulation subcircuitSimulation, SubcircuitInstance subcircuitInstance)
  {
    if (simulationSubcircuitInstances.get(subcircuitSimulation) != null)
    {
      throw new SimulatorException("A subcircuit instance [%s] for simulation [%s] already exists.", subcircuitInstance.getDescription(), subcircuitSimulation.getDescription());
    }

    simulationSubcircuitInstances.put(subcircuitSimulation, subcircuitInstance);
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
    return "Subcircuit";
  }

  public String getTypeName()
  {
    return instanceSubcircuitView.getTypeName();
  }

  @Override
  public String getDescription()
  {
    return getType() + " " + getShortDescription();
  }

  @Override
  public List<SubcircuitInstanceSimulation> getInnerSubcircuitSimulations(CircuitSimulation circuitSimulation)
  {
    List<SubcircuitInstanceSimulation> result = new ArrayList<>();
    for (SubcircuitInstance subcircuitInstance : simulationSubcircuitInstances.values())
    {
      SubcircuitInstanceSimulation subcircuitInstanceSimulation = subcircuitInstance.getSubcircuitInstanceSimulation();
      if (subcircuitInstanceSimulation.getCircuitSimulation() == circuitSimulation)
      {
        result.add(subcircuitInstanceSimulation);
      }
    }
    return result;
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

  public SubcircuitInstance getComponent(SubcircuitSimulation subcircuitSimulation)
  {
    return simulationSubcircuitInstances.get(subcircuitSimulation);
  }

  @Override
  public void destroyComponent(SubcircuitSimulation subcircuitSimulation)
  {
    SubcircuitInstance removed = simulationSubcircuitInstances.get(subcircuitSimulation);
    if (removed == null)
    {
      throw new SimulatorException("[%s] could not find a component for simulation [%s].", getDescription(), subcircuitSimulation.getDescription());
    }
    SubcircuitInstanceSimulation subcircuitInstanceSimulation = removed.getSubcircuitInstanceSimulation();
    instanceSubcircuitView.destroySubcircuitInstanceComponentsAndSimulations(subcircuitInstanceSimulation);

    Circuit circuit = subcircuitSimulation.getCircuit();
    circuit.remove(removed);
    simulationSubcircuitInstances.remove(subcircuitInstanceSimulation);
  }

  @Override
  public void destroyAllComponents()
  {
    for (Map.Entry<SubcircuitSimulation, SubcircuitInstance> entry : simulationSubcircuitInstances.entrySet())
    {
      SubcircuitInstance subcircuitInstance = entry.getValue();
      SubcircuitInstanceSimulation subcircuitInstanceSimulation = subcircuitInstance.getSubcircuitInstanceSimulation();
      instanceSubcircuitView.destroySubcircuitInstanceComponentsAndSimulations(subcircuitInstanceSimulation);
    }

    for (Map.Entry<SubcircuitSimulation, SubcircuitInstance> entry : simulationSubcircuitInstances.entrySet())
    {
      SubcircuitSimulation subcircuitSimulation = entry.getKey();
      SubcircuitInstance removed = entry.getValue();
      Circuit circuit = subcircuitSimulation.getCircuit();
      circuit.remove(removed);
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
    for (SubcircuitInstance passive : simulationSubcircuitInstances.values())
    {
      return passive.getType();
    }
    return "";
  }

  @Override
  public void paint(Graphics2D graphics,
                    Viewport viewport,
                    SubcircuitSimulation subcircuitSimulation)
  {
    super.paint(graphics, viewport, subcircuitSimulation);

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

    paintPorts(graphics, viewport, subcircuitSimulation);

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

  public SubcircuitView getInstanceSubcircuitView()
  {
    return instanceSubcircuitView;
  }

  @Override
  public SubcircuitView getCircuitSubcircuitView()
  {
    return instanceSubcircuitView;
  }

  protected List<SubcircuitInstanceSimulationSimulationData> saveSimulationSubcircuitInstances()
  {
    ArrayList<SubcircuitInstanceSimulationSimulationData> result = new ArrayList<>();
    for (Map.Entry<SubcircuitSimulation, SubcircuitInstance> entry : simulationSubcircuitInstances.entrySet())
    {
      SubcircuitSimulation subcircuitSimulation = entry.getKey();
      SubcircuitInstance subcircuitInstance = entry.getValue();
      result.add(new SubcircuitInstanceSimulationSimulationData(subcircuitSimulation.getId(),
                                                                subcircuitInstance.getSubcircuitInstanceSimulation().getId()));
    }
    return result;
  }

  public SubcircuitSimulation getContainingSubcircuitSimulation(long simulationId)
  {
    for (SubcircuitSimulation subcircuitInstanceSimulation : simulationSubcircuitInstances.keySet())
    {
      if (subcircuitInstanceSimulation.getId() == simulationId)
      {
        return subcircuitInstanceSimulation;
      }
    }
    return null;
  }

  public SubcircuitInstanceSimulation getInstanceSubcircuitSimulation(long simulationId)
  {
    for (SubcircuitInstance subcircuitInstance : simulationSubcircuitInstances.values())
    {
      SubcircuitInstanceSimulation subcircuitInstanceSimulation = subcircuitInstance.getSubcircuitInstanceSimulation();
      if (subcircuitInstanceSimulation.getId() == simulationId)
      {
        return subcircuitInstanceSimulation;
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
           toSimulationsDebugString(simulationSubcircuitInstances.keySet());
  }

  public PinView getPinView(ConnectionView connection)
  {
    SubcircuitPinView subcircuitPinView = getSubcircuitPinView(connection);
    if (subcircuitPinView != null)
    {
      return subcircuitPinView.getPinView();
    }
    else
    {
      return null;
    }
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

  public Set<SubcircuitSimulation> getComponentSubcircuitSimulations()
  {
    return simulationSubcircuitInstances.keySet();
  }
}

