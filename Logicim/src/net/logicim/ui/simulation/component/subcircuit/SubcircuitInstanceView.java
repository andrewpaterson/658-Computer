package net.logicim.ui.simulation.component.subcircuit;

import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
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
    this.subcircuitView.addPassiveView(this);

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

    rectangle = calculateRequiredRectangle(rectangle,
                                           leftViewLists,
                                           rightViewLists,
                                           topViewLists,
                                           bottomViewLists);

    leftRightPinPlacement(rectangle, leftViewLists, (int) rectangle.getBottomRight().getX(), HorizontalAlignment.LEFT);
    leftRightPinPlacement(rectangle, rightViewLists, (int) rectangle.getTopLeft().getX(), HorizontalAlignment.RIGHT);

    this.rectangle = new RectangleView(this,
                                       new Int2D(rectangle.getTopLeft().getY(), rectangle.getTopLeft().getX()),
                                       new Int2D(rectangle.getBottomRight().getY(), rectangle.getBottomRight().getX()),
                                       true,
                                       true);
  }

  private void leftRightPinPlacement(Rectangle rectangle, PinViewLists leftViewLists, int x, HorizontalAlignment alignment)
  {
    int yNegative = (int) (rectangle.getTopLeft().getY() + 1);
    for (PinView pinView : leftViewLists.negativePinViews)
    {
      pinViews.add(new SubcircuitPinView(pinView, this, new Int2D(yNegative, x), SANS_SERIF, 10, alignment));
      yNegative++;
    }

    int yPositive = (int) (rectangle.getBottomRight().getY() - 1);
    for (PinView pinView : leftViewLists.positivePinViews)
    {
      pinViews.add(new SubcircuitPinView(pinView, this, new Int2D(yPositive, x), SANS_SERIF, 10, alignment));
      yPositive--;
    }

    int yCenter = -(leftViewLists.centerPinViews.size() / 2) + 1;
    int yCenterEnd = yCenter + leftViewLists.centerPinViews.size() - 1;
    if (yCenterEnd >= yPositive)
    {
      yCenter--;
    }

    for (PinView pinView : leftViewLists.centerPinViews)
    {
      pinViews.add(new SubcircuitPinView(pinView, this, new Int2D(yCenter, x), SANS_SERIF, 10, alignment));
      yCenter++;
    }
  }

  private Rectangle calculateRequiredRectangle(Rectangle rectangle,
                                               PinViewLists leftViewLists,
                                               PinViewLists rightViewLists,
                                               PinViewLists topViewLists,
                                               PinViewLists bottomViewLists)
  {
    int topX = (int) Math.floor(rectangle.getTopLeft().getX() - (topViewLists.widestText));
    int topY = (int) Math.floor(rectangle.getTopLeft().getY() - (leftViewLists.widestText));
    int bottomX = (int) Math.ceil(rectangle.getBottomRight().getX() + (bottomViewLists.widestText));
    int bottomY = (int) Math.ceil(rectangle.getBottomRight().getY() + (rightViewLists.widestText));

    boolean updateMaxHeight = false;
    int maxHeight = bottomY - topY;
    if (leftViewLists.size > maxHeight)
    {
      updateMaxHeight = true;
      maxHeight = leftViewLists.size;
    }
    if (rightViewLists.size > maxHeight)
    {
      updateMaxHeight = true;
      maxHeight = rightViewLists.size;
    }
    if (properties.height > maxHeight)
    {
      updateMaxHeight = true;
      maxHeight = properties.height;
    }

    boolean updateMaxWidth = false;
    int maxWidth = bottomX - topX;
    if (topViewLists.size > maxWidth)
    {
      updateMaxWidth = true;
      maxWidth = topViewLists.size;
    }
    if (bottomViewLists.size > maxWidth)
    {
      updateMaxWidth = true;
      maxWidth = bottomViewLists.size;
    }
    if (properties.width > maxWidth)
    {
      updateMaxWidth = true;
      maxWidth = properties.width;
    }

    if (updateMaxHeight)
    {
      int newTop = -(maxHeight / 2);
      int newBottom = maxHeight / 2 + maxHeight % 2;
      if (topY > newTop)
      {
        topY = newTop;
      }
      if (bottomY < newBottom)
      {
        bottomY = newBottom;
      }
    }

    if (updateMaxWidth)
    {
      int newLeft = -(maxWidth / 2);
      int newRight = maxWidth / 2 + maxWidth % 2;
      if (topX > newLeft)
      {
        topX = newLeft;
      }
      if (bottomX < newRight)
      {
        bottomX = newRight;
      }
    }

    rectangle = new Rectangle(new Int2D(topX, topY), new Int2D(bottomX - 1, bottomY - 1));
    return rectangle;
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

  private void printPins(List<PinView> pinViews)
  {
    for (PinView pinView : pinViews)
    {
      System.out.println(pinView.getName());
    }
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
    topLeft.x = (float) Math.floor(topLeft.x);
    topLeft.y = (float) Math.floor(topLeft.y - 0.5f);

    Float2D bottomRight = new Float2D(boundingBox.getBottomRight());
    bottomRight.x = (float) Math.ceil(bottomRight.x);
    bottomRight.y = (float) Math.ceil(bottomRight.y + 0.5f);

    Rectangle rectangle = new Rectangle(new Int2D(topLeft), new Int2D(bottomRight));
    rectangle.rotateRight();
    return rectangle;
  }

  protected void validatePorts(CircuitSimulation simulation)
  {
  }

  protected void validateComponent(CircuitSimulation simulation)
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
                                      selected,
                                      savePorts(),
                                      properties.comment,
                                      properties.width,
                                      properties.height);
  }

  @Override
  protected SubcircuitInstance createPassive(CircuitSimulation simulation)
  {
    SubcircuitInstance subcircuitInstance = new SubcircuitInstance(simulation.getCircuit(), properties.name);
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
  public void simulationStarted(CircuitSimulation simulation)
  {
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

