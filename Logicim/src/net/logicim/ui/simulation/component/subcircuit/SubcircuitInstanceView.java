package net.logicim.ui.simulation.component.subcircuit;

import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.common.type.Tuple2;
import net.logicim.common.util.StringUtil;
import net.logicim.data.circuit.SubcircuitPinAlignment;
import net.logicim.data.circuit.SubcircuitPinAnchour;
import net.logicim.data.integratedcircuit.decorative.HorizontalAlignment;
import net.logicim.data.subciruit.SubcircuitInstanceData;
import net.logicim.data.subciruit.SubcircuitInstanceProperties;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.port.TracePort;
import net.logicim.domain.passive.subcircuit.SubcircuitInstance;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.*;
import net.logicim.ui.common.integratedcircuit.PassiveView;
import net.logicim.ui.shape.common.BoundingBox;
import net.logicim.ui.shape.rectangle.Rectangle;
import net.logicim.ui.shape.rectangle.RectangleView;
import net.logicim.ui.shape.text.TextView;
import net.logicim.ui.simulation.component.passive.pin.PinPropertyHelper;
import net.logicim.ui.simulation.component.passive.pin.PinView;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.awt.Font.SANS_SERIF;
import static net.logicim.data.circuit.SubcircuitPinAlignment.*;

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

    kindaCreateGraphicsAndOtherStuff();
    finaliseView();
  }

  private void kindaCreateGraphicsAndOtherStuff()
  {
    Rectangle rectangle = createTypeGraphics();

    PinPropertyHelper helper = new PinPropertyHelper(instanceSubcircuitView.findAllPins());
    Map<SubcircuitPinAlignment, Map<SubcircuitPinAnchour, List<PinView>>> pinsLocations = helper.groupPinsByLocation();

    PinViewLists leftViewLists = createPinViewLists(pinsLocations, LEFT);
    PinViewLists rightViewLists = createPinViewLists(pinsLocations, RIGHT);
    PinViewLists topViewLists = createPinViewLists(pinsLocations, TOP);
    PinViewLists bottomViewLists = createPinViewLists(pinsLocations, BOTTOM);

    // Top / Bottom

    int maxGridWidth = calculateMaxSize(topViewLists.getExtendedSize(), bottomViewLists.getExtendedSize());

    int left = -maxGridWidth / 2;
    int right = maxGridWidth + left;

    List<PinViewOffset> topPinViewOffsets = calculatePinViewOffsets(topViewLists, left, right);
    List<PinViewOffset> bottomPinViewOffsets = calculatePinViewOffsets(bottomViewLists, left, right);

    int top = (int) Math.ceil(rectangle.getTopLeft().getY() - calculateMaxSize(topPinViewOffsets));
    int bottom = (int) Math.floor(rectangle.getBottomRight().getY() + calculateMaxSize(bottomPinViewOffsets));

    rectangle.getTopLeft().setMinY(top);
    rectangle.getBottomRight().setMaxY(bottom);

    int heightOffset = (maxGridWidth / 2);
    rectangle.getTopLeft().setMinX(-heightOffset);
    rectangle.getBottomRight().setMaxX(maxGridWidth - heightOffset);

    //Left / Right

    int maxGridHeight = calculateMaxSize(leftViewLists.getExtendedSize(), rightViewLists.getExtendedSize());
    bottom = rectangle.getBottomRight().getIntY();
    top = rectangle.getTopLeft().getIntY();

    List<PinViewOffset> leftPinViewOffsets;
    List<PinViewOffset> rightPinViewOffsets;
    int size = bottom - top;
    if (maxGridHeight < size)
    {
      leftPinViewOffsets = calculatePinViewOffsets(leftViewLists, top, bottom);
      rightPinViewOffsets = calculatePinViewOffsets(rightViewLists, top, bottom);
    }
    else
    {
      int height = maxGridHeight - size;
      top -= height / 2;
      bottom += height - (height / 2);

      leftPinViewOffsets = calculatePinViewOffsets(leftViewLists, top, bottom);
      rightPinViewOffsets = calculatePinViewOffsets(rightViewLists, top, bottom);
    }

    int leftPos = (int) Math.ceil(rectangle.getBottomRight().getX() + calculateMaxSize(leftPinViewOffsets));
    int rightPos = (int) Math.floor(rectangle.getTopLeft().getX() - calculateMaxSize(rightPinViewOffsets));

    rectangle.getBottomRight().setMaxX(leftPos);
    rectangle.getTopLeft().setMinX(rightPos);

    rectangle.getTopLeft().setMinY(top);
    rectangle.getBottomRight().setMaxY(bottom);

    // Pins

    bottom = rectangle.getBottomRight().getIntY();
    top = rectangle.getTopLeft().getIntY();

    createTopBottomSubcircuitPinViews(topPinViewOffsets, top, HorizontalAlignment.LEFT);
    createTopBottomSubcircuitPinViews(bottomPinViewOffsets, bottom, HorizontalAlignment.RIGHT);

    createLeftRightSubcircuitPinViews(leftPinViewOffsets, leftPos, HorizontalAlignment.LEFT);
    createLeftRightSubcircuitPinViews(rightPinViewOffsets, rightPos, HorizontalAlignment.RIGHT);

    // Done

    this.rectangle = new RectangleView(this,
                                       new Int2D(rectangle.getTopLeft().getY(), rectangle.getTopLeft().getX()),
                                       new Int2D(rectangle.getBottomRight().getY(), rectangle.getBottomRight().getX()),
                                       true,
                                       true);
  }

  private int calculateMaxSize(int firstSize, int secondSize)
  {
    firstSize = firstSize != 0 ? firstSize - 1 : 0;
    secondSize = secondSize != 0 ? secondSize - 1 : 0;
    return Math.max(firstSize, secondSize);
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

  private List<PinViewOffset> calculatePinViewOffsets(PinViewLists pinViewLists, int offset, int oppositeOffset)
  {
    int missing = (oppositeOffset - offset) - pinViewLists.getPinSize();

    ArrayList<PinViewOffset> pinViewOffsets = new ArrayList<>();
    int firstTopPinOffset = offset + 1;
    int lastTopPinOffset = createPinViewOffsets(pinViewOffsets, firstTopPinOffset, pinViewLists.negativePinViews);

    int firstBottomPinOffset = oppositeOffset - pinViewLists.positivePinViews.size();
    createPinViewOffsets(pinViewOffsets, firstBottomPinOffset, pinViewLists.positivePinViews);

    int firstCenterPinOffset = lastTopPinOffset + missing / 2;
    createPinViewOffsets(pinViewOffsets, firstCenterPinOffset, pinViewLists.centerPinViews);

    return pinViewOffsets;
  }

  private int createPinViewOffsets(ArrayList<PinViewOffset> pinViewOffsets, int offset, List<PinView> pinViews)
  {
    if (pinViews.size() > 0)
    {
      for (PinView pinView : pinViews)
      {
        pinViewOffsets.add(new PinViewOffset(pinView, offset));
        offset++;
      }
      offset++;
    }

    return offset;
  }

  private PinViewLists createPinViewLists(Map<SubcircuitPinAlignment, Map<SubcircuitPinAnchour, List<PinView>>> pinsLocations, SubcircuitPinAlignment alignment)
  {
    Map<SubcircuitPinAnchour, List<PinView>> anchourMap = pinsLocations.get(alignment);

    List<PinView> centerPinViews = orderCenterPins(findAndSortPins(anchourMap, SubcircuitPinAnchour.CENTER));
    List<PinView> negativePinViews = orderNegativePins(findAndSortPins(anchourMap, SubcircuitPinAnchour.NEGATIVE));
    List<PinView> positivePinViews = orderPositivePins(findAndSortPins(anchourMap, SubcircuitPinAnchour.POSITIVE));

    Font font = Fonts.getInstance().getFont(SANS_SERIF, 0, 10, false);

    float maxWidest = 0;
    float widest = getWidest(centerPinViews, font);
    if (widest > maxWidest)
    {
      maxWidest = widest;
    }
    widest = getWidest(negativePinViews, font);
    if (widest > maxWidest)
    {
      maxWidest = widest;
    }
    widest = getWidest(positivePinViews, font);
    if (widest > maxWidest)
    {
      maxWidest = widest;
    }

    return new PinViewLists(negativePinViews, centerPinViews, positivePinViews, maxWidest);
  }

  private float getWidest(List<PinView> pinViews, Font font)
  {
    float widest = 0;
    for (PinView pinView : pinViews)
    {
      String text = pinView.getName();
      Float2D textDimension = TextView.calculateTextDimension(font, text);
      if (textDimension.x > widest)
      {
        widest = textDimension.x;
      }
    }
    return widest;
  }

  private List<PinView> findAndSortPins(Map<SubcircuitPinAnchour, List<PinView>> anchourMap, SubcircuitPinAnchour anchour)
  {
    List<PinView> pinViews;
    if (anchourMap != null)
    {
      pinViews = anchourMap.get(anchour);
    }
    else
    {
      pinViews = null;
    }

    List<PinView> sortedPinViews;
    if (pinViews != null)
    {
      sortedPinViews = new ArrayList<>(pinViews);
      Collections.sort(sortedPinViews);
    }
    else
    {
      sortedPinViews = new ArrayList<>();
    }
    return sortedPinViews;
  }

  private List<PinView> orderNegativePins(List<PinView> sortedPinViews)
  {
    return sortedPinViews;
  }

  private List<PinView> orderPositivePins(List<PinView> sortedPinViews)
  {
    Collections.reverse(sortedPinViews);
    return sortedPinViews;
  }

  private List<PinView> orderCenterPins(List<PinView> sortedPinViews)
  {
    if (sortedPinViews.size() > 0)
    {
      PinView[] orderedPinViews = new PinView[sortedPinViews.size()];

      int yEven = (sortedPinViews.size() - 1) / 2;
      int yOdd = yEven + 1;
      boolean even = true;
      for (PinView pinView : sortedPinViews)
      {
        int index;
        if (even)
        {
          index = yEven;
          yEven--;
        }
        else
        {
          index = yOdd;
          yOdd++;
        }
        orderedPinViews[index] = pinView;
        even = !even;
      }

      for (int i = 0; i < orderedPinViews.length; i++)
      {
        PinView orderedPinView = orderedPinViews[i];
        sortedPinViews.set(i, orderedPinView);
      }
    }
    return sortedPinViews;
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
    return getType() + " " + getTypeName() + "(" + getName() + ") (" + getPosition() + ")";
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
}

