package net.logicim.ui.simulation.component.subcircuit;

import net.logicim.common.SimulatorException;
import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.common.util.StringUtil;
import net.logicim.data.circuit.SubcircuitPinAlignment;
import net.logicim.data.circuit.SubcircuitPinAnchour;
import net.logicim.data.common.ReflectiveData;
import net.logicim.data.integratedcircuit.decorative.HorizontalAlignment;
import net.logicim.data.subciruit.SubcircuitInstanceData;
import net.logicim.data.subciruit.SubcircuitInstanceProperties;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.*;
import net.logicim.ui.common.integratedcircuit.StaticView;
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
    extends StaticView<SubcircuitInstanceProperties>
{
  protected boolean enabled;
  protected boolean subcircuitComponentsCreated;
  protected List<SubcircuitPinView> pinViews;
  protected SubcircuitView instanceSubcircuitView;

  protected RectangleView rectangle;
  protected TextView typeName;
  protected TextView name;
  protected TextView comment;

  public SubcircuitInstanceView(SubcircuitView subcircuitView,
                                SubcircuitView instanceSubcircuitView,
                                Circuit circuit,
                                Int2D position,
                                Rotation rotation,
                                SubcircuitInstanceProperties properties)
  {
    super(subcircuitView,
          circuit,
          position,
          rotation,
          properties);
    this.instanceSubcircuitView = instanceSubcircuitView;
    this.enabled = false;
    this.subcircuitComponentsCreated = false;
    this.pinViews = new ArrayList<>();
    this.subcircuitView.addSubcircuitInstanceView(this);
    finaliseView(circuit);
  }

  @Override
  protected void finaliseView(Circuit circuit)
  {
    finalised = true;

    Rectangle rectangle = createTypeGraphics();
    this.rectangle = new RectangleView(this, rectangle, true, true);

    PinPropertyHelper helper = new PinPropertyHelper(instanceSubcircuitView.findAllPins());
    Map<SubcircuitPinAlignment, Map<SubcircuitPinAnchour, List<PinView>>> pinsLocations = helper.groupPinsByLocation();

    PinViewLists leftViewLists = createPinViewLists(pinsLocations, LEFT);
    PinViewLists rightViewLists = createPinViewLists(pinsLocations, RIGHT);
    PinViewLists topViewLists = createPinViewLists(pinsLocations, TOP);
    PinViewLists bottomViewLists = createPinViewLists(pinsLocations, BOTTOM);

    updateBoundingBoxes();
    createSubcircuitComponents(circuit);
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
    topLeft.x = (float) Math.floor(topLeft.x - 0.5f);
    topLeft.y = (float) Math.floor(topLeft.y - 0.5f);

    Float2D bottomRight = new Float2D(boundingBox.getBottomRight());
    bottomRight.x = (float) Math.ceil(bottomRight.x + 0.5f);
    bottomRight.y = (float) Math.ceil(bottomRight.y + 0.5f);

    return new Rectangle(new Int2D(topLeft), new Int2D(bottomRight));
  }

  private void createSubcircuitComponents(Circuit circuit)
  {
    if (!subcircuitComponentsCreated)
    {
      //This is where you create all the components in a subcircuit and add them into the main circuit simulation.
    }
    else
    {
      throw new SimulatorException("Subcircuit components already created.");
    }

    validateComponent();
    validatePorts();
  }

  private void validatePorts()
  {
  }

  private void validateComponent()
  {
  }

  @Override
  public void clampProperties(SubcircuitInstanceProperties newProperties)
  {
  }

  @Override
  public ReflectiveData save(boolean selected)
  {
    return new SubcircuitInstanceData(getTypeName(),
                                      position,
                                      rotation,
                                      properties.name,
                                      boundingBox.save(),
                                      selectionBox.save(),
                                      selected,
                                      properties.comment,
                                      properties.width,
                                      properties.height);
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
  public void enable(Simulation simulation)
  {
    enabled = true;
  }

  @Override
  public void disable()
  {
    enabled = false;
  }

  @Override
  public boolean isEnabled()
  {
    return enabled;
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
  public void simulationStarted(Simulation simulation)
  {
  }

  @Override
  public void disconnect(Simulation simulation)
  {
  }

  @Override
  public List<ConnectionView> createConnections(SubcircuitView subcircuitView)
  {
    List<ConnectionView> connections = new ArrayList<>();
    for (SubcircuitPinView pinView : pinViews)
    {
      Int2D pinPosition = pinView.getPinPosition();
      ConnectionView connection = subcircuitView.getOrAddConnection(pinPosition, this);
      connections.add(connection);
    }
    return connections;
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport, long time)
  {
    super.paint(graphics, viewport, time);

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

