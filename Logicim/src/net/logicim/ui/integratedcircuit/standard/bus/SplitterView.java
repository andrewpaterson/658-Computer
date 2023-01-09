package net.logicim.ui.integratedcircuit.standard.bus;

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

    this.endPorts = null;
    this.startPort = null;

    this.boundingBox = new BoundingBox();
    this.selectionBox = new BoundingBox();

    circuitEditor.addPassiveView(this);
    finaliseView();
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
  public void paint(Graphics2D graphics, Viewport viewport, long time)
  {
    super.paint(graphics, viewport, time);

    Color color = graphics.getColor();
    Stroke stroke = graphics.getStroke();

    paintPorts(graphics, viewport, time);

    graphics.setColor(color);
    graphics.setStroke(stroke);
  }

  @Override
  public String getType()
  {
    return "Splitter";
  }

  public SplitterData save(boolean selected)
  {
    return new SplitterData(position,
                            rotation,
                            properties.name,
                            savePorts(),
                            selected,
                            properties.endCount,
                            properties.endOffset,
                            properties.spacing);
  }

  @Override
  protected Splitter createPassive()
  {
    return new Splitter(circuitEditor.getCircuit(),
                        properties.name,
                        properties.endCount);
  }

  @Override
  protected void createPortViews()
  {
    startPort = new PortView(this, passive.getStartPorts(), new Int2D(-2, -properties.endOffset));
    endPorts = createEndPorts(properties);
  }

  protected List<PortView> createEndPorts(SplitterProperties properties)
  {
    Int2D position = new Int2D(1, 0);
    ArrayList<PortView> portViews = new ArrayList<>(properties.endCount);
    for (int i = 0; i < properties.endCount; i++)
    {
      portViews.add(new PortView(this, passive.getEndPort(i), new Int2D(position)));
      position.subtract(0, properties.spacing);
    }
    return portViews;
  }

  @Override
  public void clampProperties()
  {
  }

  @Override
  public void simulationStarted(Simulation simulation)
  {
  }
}

