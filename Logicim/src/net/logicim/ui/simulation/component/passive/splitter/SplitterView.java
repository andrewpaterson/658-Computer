package net.logicim.ui.simulation.component.passive.splitter;

import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.data.passive.wire.SplitterData;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.passive.wire.Splitter;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.integratedcircuit.PassiveView;
import net.logicim.ui.common.integratedcircuit.PropertyClamp;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.shape.line.LineView;
import net.logicim.ui.shape.rectangle.RectangleView;
import net.logicim.ui.simulation.CircuitEditor;

import java.awt.*;
import java.util.List;
import java.util.*;

public class SplitterView
    extends PassiveView<Splitter, SplitterProperties>
{
  protected List<LineView> lineViews;
  protected RectangleView rectangleView;

  protected List<PortView> endPortViews;
  protected PortView startPortView;

  public SplitterView(CircuitEditor circuitEditor,
                      Int2D position,
                      Rotation rotation,
                      SplitterProperties properties)
  {
    super(circuitEditor, position, rotation, properties);

    circuitEditor.addPassiveView(this);
    createGraphics();
    finaliseView();
  }

  @Override
  protected void createPortViews()
  {
    startPortView = new PortView(this, passive.getStartPorts(), createStartPosition());
    endPortViews = createEndPorts(properties);
  }

  protected List<Port> getPortsForFanoutIndex(int fanIndex)
  {
    List<Port> ports = new ArrayList<>();
    for (int i = 0; i < properties.splitIndices.length; i++)
    {
      int x = properties.splitIndices[i];
      if (x == fanIndex)
      {
        ports.add(passive.getEndPort(i));
      }
    }
    return ports;
  }

  protected List<PortView> createEndPorts(SplitterProperties properties)
  {
    List<PortView> portViews = new ArrayList<>();
    for (int i = 0; i < properties.fanOut; i++)
    {
      Int2D position = createEndPosition(i);
      PortView portView = new PortView(this, getPortsForFanoutIndex(i), new Int2D(position));
      portViews.add(portView);
    }
    return portViews;
  }

  private void createGraphics()
  {
    lineViews = new ArrayList<>();
    Int2D startPosition = createStartPosition();
    Int2D endPosition;
    if (startPosition.y > 0)
    {
      endPosition = createEndPosition(0);
    }
    else if (startPosition.y < -(properties.fanOut - 1) * properties.gridSpacing)
    {
      endPosition = createEndPosition(properties.fanOut - 1);
    }
    else
    {
      endPosition = createEndPosition(0);
      endPosition.y = startPosition.y;
    }
    int centerX = 0;
    endPosition.x = centerX;
    LineView startLineView = new LineView(this, startPosition, endPosition, 4);
    lineViews.add(startLineView);

    for (int i = 0; i < properties.fanOut; i++)
    {
      Int2D p1 = createEndPosition(i);
      Int2D p2 = p1.clone();
      p1.x = 0;
      LineView lineView = new LineView(this, p1, p2);
      lineViews.add(lineView);
    }

    Float2D p1 = new Float2D(createEndPosition(0));
    p1.x = centerX - 0.1f;
    Float2D p2 = new Float2D(createEndPosition(properties.fanOut - 1));
    p2.x = centerX + 0.1f;
    rectangleView = new RectangleView(this, p1, p2, true, true);
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport, long time)
  {
    super.paint(graphics, viewport, time);

    Color color = graphics.getColor();
    Stroke stroke = graphics.getStroke();

    for (LineView lineView : lineViews)
    {
      lineView.paint(graphics, viewport);
    }
    rectangleView.paint(graphics, viewport);

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
                            properties.bitWidth,
                            properties.fanOut,
                            properties.gridSpacing,
                            properties.appearance,
                            properties.endOffset,
                            properties.splitIndices);
  }

  @Override
  protected Splitter createPassive()
  {
    return new Splitter(circuitEditor.getCircuit(),
                        properties.name,
                        properties.bitWidth);
  }

  private Int2D createStartPosition()
  {
    return new Int2D(-1, -properties.endOffset);
  }

  private Int2D createEndPosition(int endIndex)
  {
    return new Int2D(1, -endIndex * properties.gridSpacing);
  }

  @Override
  public void clampProperties()
  {
    properties.fanOut = PropertyClamp.clamp(properties.fanOut, 1, PropertyClamp.MAX);
    properties.gridSpacing = PropertyClamp.clamp(properties.gridSpacing, 1, 12);
    properties.endOffset = PropertyClamp.clamp(properties.endOffset, -1, properties.fanOut * properties.gridSpacing - 1);
  }

  @Override
  public void simulationStarted(Simulation simulation)
  {
  }

  public Map<Port, Port> getBidirectionalPortMap()
  {
    List<? extends Port> ports = startPortView.getPorts();
    int[] endPortViewPortIndex = new int[properties.fanOut];
    Arrays.fill(endPortViewPortIndex, 0);

    HashMap<Port, Port> result = new HashMap<>();

    int i = 0;
    for (int fanIndex = 0; fanIndex < properties.fanOut; fanIndex++)
    {
      List<Port> portsForFanoutIndex = getPortsForFanoutIndex(fanIndex);
      for (Port endPort : portsForFanoutIndex)
      {
        Port startPort = ports.get(i);
        result.put(startPort, endPort);
        result.put(endPort, startPort);
        i++;
      }
    }

    return result;
  }
}

