package net.logicim.ui.simulation.component.subcircuit;

import net.logicim.common.SimulatorException;
import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.common.util.StringUtil;
import net.logicim.data.common.ReflectiveData;
import net.logicim.data.subciruit.SubcircuitInstanceData;
import net.logicim.data.integratedcircuit.decorative.HorizontalAlignment;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Colours;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.data.subciruit.SubcircuitInstanceProperties;
import net.logicim.ui.shape.common.BoundingBox;
import net.logicim.ui.shape.rectangle.RectangleView;
import net.logicim.ui.shape.text.TextView;
import net.logicim.ui.simulation.component.passive.pin.SubcircuitPinView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static java.awt.Font.SANS_SERIF;

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
    createGraphics();
    finaliseView(circuit);
  }

  @Override
  protected void finaliseView(Circuit circuit)
  {
    finalised = true;
    updateBoundingBoxes();
    createSubcircuitComponents(circuit);
  }

  private void createGraphics()
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

    rectangle = new RectangleView(this, new Int2D(topLeft), new Int2D(bottomRight), true, true);
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

