package net.logicim.ui.simulation.component.passive.splitter;

import net.logicim.common.SimulatorException;
import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.data.passive.wire.SplitterAppearance;
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
import net.logicim.ui.shape.text.TextView;
import net.logicim.ui.simulation.CircuitEditor;

import java.awt.*;
import java.util.List;
import java.util.*;

import static net.logicim.data.integratedcircuit.decorative.HorizontalAlignment.LEFT;

public class SplitterView
    extends PassiveView<Splitter, SplitterProperties>
{
  protected List<LineView> lineViews;
  protected RectangleView rectangleView;
  protected List<TextView> textViews;

  protected List<PortView> endPortViews;
  protected PortView startPortView;

  public SplitterView(CircuitEditor circuitEditor,
                      Int2D position,
                      Rotation rotation,
                      SplitterProperties properties)
  {
    super(circuitEditor, position, rotation, properties);

    createGraphics();
    finaliseView();
  }

  @Override
  protected void createPortViews()
  {
    startPortView = new PortView(this, passive.getStartPorts(), new Int2D(createStartPosition()));
    endPortViews = createEndPorts(properties);
  }

  protected List<Port> getPortsForFanOutIndex(int fanIndex)
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

  protected List<Integer> getPortIndicesForFanoutIndex(int fanIndex)
  {
    List<Integer> ports = new ArrayList<>();
    for (int i = 0; i < properties.splitIndices.length; i++)
    {
      int x = properties.splitIndices[i];
      if (x == fanIndex)
      {
        ports.add(i);
      }
    }
    return ports;
  }

  protected List<PortView> createEndPorts(SplitterProperties properties)
  {
    List<PortView> portViews = new ArrayList<>();
    for (int i = 0; i < properties.fanOut; i++)
    {
      List<Port> portsForFanOut = getPortsForFanOutIndex(i);
      if (portsForFanOut.size() > 0)
      {
        Float2D position = createEndPosition(i);
        PortView portView = new PortView(this, portsForFanOut, new Int2D(position));
        portViews.add(portView);
      }
    }
    return portViews;
  }

  private void createGraphics()
  {
    this.lineViews = createLineViews(
        properties.fanOut,
                                     properties.gridSpacing,
                                     properties.appearance);

    rectangleView = createRectangleView(properties.fanOut);

    this.textViews = createTextViews();
  }

  private List<TextView> createTextViews()
  {
    List<TextView> textViews = new ArrayList<>();
    for (int endIndex = 0; endIndex < properties.fanOut; endIndex++)
    {
      List<Integer> portIndicesForFanoutIndex = getPortIndicesForFanoutIndex(endIndex);
      String text = toText(portIndicesForFanoutIndex);
      Float2D position = createMidPosition(endIndex);
      TextView textView = new TextView(this,
                                       position,
                                       text,
                                       7,
                                       false,
                                       LEFT);
      setTextViewThing(textView, position);
      textViews.add(textView);
    }
    return textViews;
  }

  private void setTextViewThing(TextView textView, Float2D position)
  {
    if (rotation.isNorth() || rotation.isCannot())
    {
      position.x += 0.2;
    }
    else if (rotation.isSouth())
    {
      position.x += 0.5;
    }
    else if (rotation.isEast())
    {
      position.x += 0.4;
    }
    else if (rotation.isWest())
    {
      position.x += 0.2;
    }
    else
    {
      throw new SimulatorException("Cannot set position for unknown rotation.");
    }

    double y = 0.35;
    if (properties.appearance == SplitterAppearance.LEFT_HANDED)
    {
      position.y += y;
    }
    else if (properties.appearance == SplitterAppearance.RIGHT_HANDED)
    {
      position.y -= y;
    }

    textView.setPositionRelativeToIC(position.clone());
  }

  private String toText(List<Integer> indices)
  {
    Collections.sort(indices);
    StringBuilder builder = new StringBuilder();
    Integer previous = null;
    int skipped = 0;
    for (Integer index : indices)
    {
      if (previous != null)
      {
        if (index - 1 == previous)
        {
          skipped++;
        }
        else
        {
          appendPreviousDigit(builder, previous, skipped);
          skipped = 0;
          builder.append(",");
          builder.append(index);
        }
      }
      else
      {
        builder.append(index);
      }
      previous = index;
    }
    appendPreviousDigit(builder, previous, skipped);
    return builder.toString();
  }

  private void appendPreviousDigit(StringBuilder builder, Integer previous, int skipped)
  {
    if (skipped == 1)
    {
      builder.append(",");
      builder.append(previous);
    }
    else if (skipped > 1)
    {
      builder.append("-");
      builder.append(previous);
    }
  }

  private List<LineView> createLineViews(int fanOut, int gridSpacing, SplitterAppearance appearance)
  {
    List<LineView> lineViews = new ArrayList<>();

    Float2D startPosition = createStartPosition();
    Float2D endPosition = createEndPosition(fanOut, gridSpacing, startPosition, appearance);
    LineView startLineView = new LineView(this, startPosition, endPosition, 4);
    lineViews.add(startLineView);

    for (int i = 0; i < fanOut; i++)
    {
      Float2D p1 = createEndPosition(i);
      Float2D p2 = createMidPosition(i);

      LineView lineView = new LineView(this, p1, p2);
      lineViews.add(lineView);
    }

    return lineViews;
  }

  private Float2D createEndPosition(int fanOut, int gridSpacing, Float2D startPosition, SplitterAppearance appearance)
  {
    Float2D endPosition;
    if (appearance == SplitterAppearance.LEFT_HANDED)
    {
      if (startPosition.y > 0)
      {
        endPosition = createEndPosition(0);
      }
      else if (startPosition.y < -(fanOut - 1) * gridSpacing)
      {
        endPosition = createEndPosition(fanOut - 1);
      }
      else
      {
        endPosition = createEndPosition(0);
        endPosition.y = startPosition.y;
      }
    }
    else if (appearance == SplitterAppearance.RIGHT_HANDED)
    {
      if (startPosition.y > (fanOut - 1) * gridSpacing)
      {
        endPosition = createEndPosition(fanOut - 1);
      }
      else if (startPosition.y < 0)
      {
        endPosition = createEndPosition(0);
      }
      else
      {
        endPosition = createEndPosition(0);
        endPosition.y = startPosition.y;
      }
    }
    else
    {
      endPosition = createEndPosition(0);
      endPosition.y = startPosition.y;
    }
    endPosition.x = 0;
    return endPosition;
  }

  private RectangleView createRectangleView(int fanOut)
  {
    Float2D p1 = createEndPosition(0);
    p1.x = 0 - 0.1f;
    Float2D p2 = createEndPosition(fanOut - 1);
    p2.x = 0 + 0.1f;
    return new RectangleView(this, p1, p2, true, true);
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport, long time)
  {
    super.paint(graphics, viewport, time);

    Color color = graphics.getColor();
    Stroke stroke = graphics.getStroke();
    Font font = graphics.getFont();

    for (LineView lineView : lineViews)
    {
      lineView.paint(graphics, viewport);
    }
    rectangleView.paint(graphics, viewport);

    paintPorts(graphics, viewport, time);

    for (TextView textView : textViews)
    {
      textView.paint(graphics, viewport);
    }

    graphics.setFont(font);
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

  private Float2D createStartPosition()
  {
    if (properties.appearance == SplitterAppearance.LEFT_HANDED)
    {
      return new Float2D(-1, -properties.endOffset);
    }
    else if (properties.appearance == SplitterAppearance.RIGHT_HANDED)
    {
      return new Float2D(-1, properties.endOffset);
    }
    else
    {
      return new Float2D(-1, ((properties.fanOut / 4.0f) * properties.gridSpacing) - properties.endOffset);
    }
  }

  private Float2D createEndPosition(int endIndex)
  {
    return createPosition(endIndex, 2);
  }

  private Float2D createMidPosition(int endIndex)
  {
    return createPosition(endIndex, 0);
  }

  private Float2D createPosition(int endIndex, int x)
  {
    if (properties.appearance == SplitterAppearance.LEFT_HANDED)
    {
      return new Float2D(x, -endIndex * properties.gridSpacing);
    }
    else if (properties.appearance == SplitterAppearance.RIGHT_HANDED)
    {
      return new Float2D(x, endIndex * properties.gridSpacing);
    }
    else
    {
      return new Float2D(x, (-endIndex + (properties.fanOut / 2.0f)) * properties.gridSpacing);
    }
  }

  @Override
  public void propertyChanged()
  {
    properties.fanOut = PropertyClamp.clamp(properties.fanOut, 1, PropertyClamp.MAX);
    properties.gridSpacing = PropertyClamp.clamp(properties.gridSpacing, 1, 12);
    properties.endOffset = PropertyClamp.clamp(properties.endOffset, -1, properties.fanOut * properties.gridSpacing - 1);
    for (int i = 0; i < properties.splitIndices.length; i++)
    {
      int index = properties.splitIndices[i];
      properties.splitIndices[i] = PropertyClamp.clamp(index, 0, properties.fanOut - 1);
    }
  }

  @Override
  public void simulationStarted(Simulation simulation)
  {
  }

  public Map<Port, Port> getBidirectionalPortMap()
  {
    List<? extends Port> ports = startPortView.getPorts();
    HashMap<Port, Port> result = new HashMap<>();

    int i = 0;
    for (int fanIndex = 0; fanIndex < properties.fanOut; fanIndex++)
    {
      List<Port> portsForFanoutIndex = getPortsForFanOutIndex(fanIndex);
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

  protected void updateTextViews()
  {
    for (int endIndex = 0; endIndex < textViews.size(); endIndex++)
    {
      TextView textView = textViews.get(endIndex);
      Float2D position = createMidPosition(endIndex);
      setTextViewThing(textView, position);
    }
  }

  @Override
  public void rotateRight()
  {
    super.rotateRight();
    updateTextViews();
  }

  @Override
  public void rotateLeft()
  {
    super.rotateLeft();
    updateTextViews();
  }

}

