package net.logicim.ui.integratedcircuit.standard.bus;

import net.logicim.common.SimulatorException;
import net.logicim.common.type.Int2D;
import net.logicim.data.splitter.SplitterData;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Component;
import net.logicim.domain.passive.wiring.Splitter;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.integratedcircuit.PassiveView;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.shape.common.BoundingBox;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SplitterView
    extends PassiveView<SplitterProperties>
{
  protected List<PortView> endPorts;
  protected PortView startPort;
  protected Splitter splitter;

  public SplitterView(CircuitEditor circuitEditor, Int2D position, Rotation rotation, SplitterProperties properties)
  {
    super(circuitEditor, position, rotation, properties);
    this.position = position;
    this.rotation = rotation;
    this.properties = properties;

    this.endPorts = createEndPorts(properties);

    this.boundingBox = new BoundingBox();
    this.selectionBox = new BoundingBox();

    circuitEditor.addPassiveView(this);
    calculateEnds();
  }

  protected List<PortView> createEndPorts(SplitterProperties properties)
  {
    ArrayList<PortView> portViews = new ArrayList<>(properties.outputCount);
    for (int i = 0; i < properties.outputCount; i++)
    {
      portViews.add(new PortView(this, splitter.getEndPort(i)));
    }
    return portViews;
  }

  @Override
  public void paintSelected(Graphics2D graphics, Viewport viewport)
  {
  }

  @Override
  public ConnectionView getConnectionsInGrid(int x, int y)
  {
    if (startPort.getGridPosition().equals(x, y))
    {
      return startPort.getConnection();
    }

    for (PortView portView : endPorts)
    {
      if (portView.getGridPosition().equals(x, y))
      {
        return portView.getConnection();
      }
    }
    return null;
  }

  @Override
  public Int2D getConnectionGridPosition(ConnectionView connectionView)
  {
    if (startPort.getConnection() == connectionView)
    {
      return position;
    }
    for (int i = 0; i < endPorts.size(); i++)
    {
      PortView portView = endPorts.get(i);
      if (portView.getConnection() == connectionView)
      {
        return portView.getConnection().getGridPosition();
      }
    }
    return null;
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport, long time)
  {
  }

  @Override
  public String getType()
  {
    return "Splitter";
  }

  @Override
  protected void createPortViews()
  {

  }

  @Override
  public String getDescription()
  {
    return "Splitter (" + position + ") width [" + properties.outputCount + "]";
  }

  @Override
  public void enable(Simulation simulation)
  {
  }

  @Override
  public void disable()
  {
  }

  @Override
  public void setPosition(int x, int y)
  {
    super.setPosition(x, y);
    calculateEnds();
  }

  private void calculateEnds()
  {
    Int2D position = new Int2D(this.position);
    position.add(3, -properties.outputOffset);
    for (PortView endPort : endPorts)
    {
      Int2D relativePosition = endPort.getRelativePosition();
      rotation.rotate(relativePosition, position);
      position.add(0, properties.spacing);
    }
  }

  public SplitterData save(boolean selected)
  {
    throw new SimulatorException("Not yet implemented");
  }

  @Override
  protected void createComponent()
  {
    splitter = new Splitter();
  }

  @Override
  public void clampProperties()
  {
  }

  @Override
  public boolean isEnabled()
  {
    return false;
  }

  @Override
  public void simulationStarted(Simulation simulation)
  {
  }

  @Override
  public Component getComponent()
  {
    return splitter;
  }
}

