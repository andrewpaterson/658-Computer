package net.logicim.ui.simulation.component.subcircuit;

import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.common.type.Tuple2;
import net.logicim.common.util.StringUtil;
import net.logicim.data.integratedcircuit.decorative.HorizontalAlignment;
import net.logicim.data.subciruit.SubcircuitInstanceData;
import net.logicim.data.subciruit.SubcircuitInstanceProperties;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.port.TracePort;
import net.logicim.domain.passive.subcircuit.SubcircuitInstance;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Colours;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.integratedcircuit.PassiveView;
import net.logicim.ui.shape.common.BoundingBox;
import net.logicim.ui.shape.rectangle.Rectangle;
import net.logicim.ui.shape.rectangle.RectangleView;
import net.logicim.ui.shape.text.TextView;
import net.logicim.ui.simulation.component.passive.pin.PinPropertyHelper;
import net.logicim.ui.simulation.component.passive.pin.PinView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static java.awt.Font.SANS_SERIF;

public class SubcircuitInstanceView
    extends PassiveView<SubcircuitInstance, SubcircuitInstanceProperties>
{
  protected boolean subcircuitComponentsCreated;
  protected List<SubcircuitPinView> pinViews;
  protected SubcircuitView instanceSubcircuitView;

  protected RectangleView rectangle;
  protected TextView typeName;
  protected TextView name;
  protected TextView comment;

  public SubcircuitInstanceView(SubcircuitView subcircuitView,
                                SubcircuitView instanceSubcircuitView,
                                Int2D position,
                                Rotation rotation,
                                SubcircuitInstanceProperties properties)
  {
    super(subcircuitView,
          position,
          rotation,
          properties);
    this.instanceSubcircuitView = instanceSubcircuitView;
    this.subcircuitComponentsCreated = false;
    this.pinViews = new ArrayList<>();

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

  protected void validatePorts(CircuitSimulation circuitSimulation)
  {
  }

  protected void validateComponent(CircuitSimulation circuitSimulation)
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
                                      saveSimulations(),
                                      savePorts(),
                                      properties.comment,
                                      properties.width,
                                      properties.height);
  }

  @Override
  protected SubcircuitInstance createPassive(CircuitSimulation circuitSimulation)
  {
    SubcircuitInstance subcircuitInstance = new SubcircuitInstance(circuitSimulation.getCircuit(), properties.name);
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

    //List<View> allViews = instanceSubcircuitView.getAllViews();

    return subcircuitInstance;
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

  @Override
  public void clampProperties(SubcircuitInstanceProperties newProperties)
  {
  }

  @Override
  public List<ConnectionView> getConnections()
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
  public void disconnect()
  {
    for (SubcircuitPinView pinView : pinViews)
    {
      pinView.disconnect();
    }
  }

  @Override
  protected void createPortViews()
  {
    for (SubcircuitPinView pinView : pinViews)
    {
      pinView.createPortView(getPinPortNames(pinView.getPinView()));
    }
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport, CircuitSimulation simulation)
  {
    super.paint(graphics, viewport, simulation);

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

    paintPorts(graphics, viewport, simulation);

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
}

