package net.logicim.ui.simulation.component.subcircuit;

import net.logicim.common.SimulatorException;
import net.logicim.common.type.Int2D;
import net.logicim.data.ReflectiveData;
import net.logicim.data.integratedcircuit.common.SubcircuitInstanceData;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.common.subcircuit.SubcircuitInstanceProperties;
import net.logicim.ui.shape.rectangle.RectangleView;
import net.logicim.ui.simulation.component.passive.pin.SubcircuitPinView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SubcircuitInstanceView
    extends StaticView<SubcircuitInstanceProperties>
{
  protected boolean enabled;
  protected boolean subcircuitComponentsCreated;
  protected List<SubcircuitPinView> pinViews;
  protected RectangleView rectangle;

  public SubcircuitInstanceView(SubcircuitView subcircuitView,
                                Circuit circuit,
                                Int2D position,
                                Rotation rotation)
  {
    super(subcircuitView,
          circuit,
          position,
          rotation,
          new SubcircuitInstanceProperties(""));
    this.enabled = false;
    this.subcircuitComponentsCreated = false;
    this.pinViews = new ArrayList<>();
    subcircuitView.addSubcircuitInstanceView(this);
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
    rectangle = new RectangleView(this, 10, 10, true, true);
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
    return new SubcircuitInstanceData(properties.name,
                                      position,
                                      rotation,
                                      boundingBox.save(),
                                      selectionBox.save(),
                                      selected);
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
    return subcircuitView.getTypeName();
  }

  @Override
  public String getDescription()
  {
    return getType() + " " + subcircuitView.getTypeName() + "(" + getName() + ") (" + getPosition() + ")";
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
    rectangle.updateGridCache();
    rectangle.paint(graphics, viewport);
    for (SubcircuitPinView pinView : pinViews)
    {
      pinView.updateGridCache();
      pinView.paint(graphics, viewport);
    }
  }
}

