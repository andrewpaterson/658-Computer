package net.logicim.ui.integratedcircuit.standard.bus;

import net.logicim.common.SimulatorException;
import net.logicim.common.type.Int2D;
import net.logicim.data.splitter.SplitterData;
import net.logicim.domain.Simulation;
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
    extends PassiveView<Splitter, SplitterProperties>
{
  protected List<PortView> endPorts;
  protected PortView startPort;

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
  }

  protected List<PortView> createEndPorts(SplitterProperties properties)
  {
    Int2D position = new Int2D(this.position);
    position.add(3, -properties.endOffset);
    ArrayList<PortView> portViews = new ArrayList<>(properties.endCount);
    for (int i = 0; i < properties.endCount; i++)
    {
      portViews.add(new PortView(this, passive.getEndPort(i), new Int2D(position)));
      position.add(0, properties.spacing);
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

    for (PortView portView : endPorts)
    {
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
    return "Splitter (" + position + ") width [" + properties.endCount + "]";
  }

  @Override
  public void enable(Simulation simulation)
  {
  }

  @Override
  public void disable()
  {
  }

  public SplitterData save(boolean selected)
  {
    throw new SimulatorException("Not yet implemented");
  }

  @Override
  protected Splitter createPassive()
  {
    return new Splitter(circuitEditor.getCircuit(),
                        properties.name,
                        properties.endCount);
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
}

