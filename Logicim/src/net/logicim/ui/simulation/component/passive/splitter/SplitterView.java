package net.logicim.ui.simulation.component.passive.splitter;

import net.common.SimulatorException;
import net.common.type.Float2D;
import net.common.type.Int2D;
import net.logicim.data.passive.wire.SplitterAppearance;
import net.logicim.data.passive.wire.SplitterData;
import net.logicim.data.passive.wire.SplitterProperties;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.domain.passive.wire.Splitter;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.integratedcircuit.PassiveView;
import net.logicim.ui.common.integratedcircuit.PropertyClamp;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.shape.line.LineView;
import net.logicim.ui.shape.rectangle.RectangleView;
import net.logicim.ui.shape.text.TextView;

import java.awt.*;
import java.util.List;
import java.util.*;

import static java.awt.Font.SANS_SERIF;
import static net.logicim.data.integratedcircuit.decorative.HorizontalAlignment.RIGHT;

public class SplitterView
    extends PassiveView<Splitter, SplitterProperties>
{
  public static int FONT_SIZE = 7;

  protected List<LineView> lineViews;
  protected RectangleView rectangleView;
  protected List<TextView> textViews;

  protected List<PortView> endPortViews;
  protected PortView startPortView;

  protected Map<String, String> bidirectionalPortMap;

  public SplitterView(SubcircuitView subcircuitView,
                      Int2D position,
                      Rotation rotation,
                      SplitterProperties properties)
  {
    super(subcircuitView, position, rotation, properties);
    bidirectionalPortMap = calculateBidirectionalPortMap();
    createGraphics();
    createPortViews();
    finaliseView();
  }

  public List<String> getStartPortNames()
  {
    ArrayList<String> result = new ArrayList<>(properties.bitWidth);
    for (int i = 0; i < properties.bitWidth; i++)
    {
      result.add("Start " + i);
    }
    return result;
  }

  public List<String> getEndPortNames()
  {
    ArrayList<String> result = new ArrayList<>(properties.bitWidth);
    for (int i = 0; i < properties.bitWidth; i++)
    {
      result.add("End " + i);
    }
    return result;
  }

  @Override
  protected void createPortViews()
  {
    startPortView = new PortView(this, getStartPortNames(), new Int2D(createStartPosition()));
    endPortViews = createEndPorts();
  }

  protected List<Integer> getPortIndicesForFanoutIndex(SplitterProperties properties, int fanIndex)
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

  protected List<PortView> createEndPorts()
  {
    List<PortView> portViews = new ArrayList<>();
    int portIndex = 0;
    List<String> endPortNames = getEndPortNames();
    for (int fanOutIndex = 0; fanOutIndex < properties.fanOut; fanOutIndex++)
    {
      List<Integer> portIndicesForFanOutIndex = getPortIndicesForFanoutIndex(properties, fanOutIndex);
      if (portIndicesForFanOutIndex.size() > 0)
      {
        List<String> portNamesForFanOutIndex = new ArrayList<>();
        for (int i = 0; i < portIndicesForFanOutIndex.size(); i++)
        {
          portNamesForFanOutIndex.add(getEndPortName(endPortNames, portIndex));
          portIndex++;
        }

        Float2D position = createEndPosition(fanOutIndex);
        PortView portView = new PortView(this, portNamesForFanOutIndex, new Int2D(position));
        portViews.add(portView);
      }
    }
    return portViews;
  }

  private String getEndPortName(List<String> endPortNames, int endPortIndex)
  {
    return endPortNames.get(endPortIndex);
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
      List<Integer> portIndicesForFanoutIndex = getPortIndicesForFanoutIndex(properties, endIndex);
      String text = toText(portIndicesForFanoutIndex);
      Float2D position = createMidPosition(endIndex);
      TextView textView = new TextView(this,
                                       position,
                                       text,
                                       SANS_SERIF,
                                       FONT_SIZE,
                                       false,
                                       RIGHT);
      adjustTextViewPosition(textView, position, rotation);
      textViews.add(textView);
    }
    return textViews;
  }

  private void adjustTextViewPosition(TextView textView, Float2D position, Rotation rotation)
  {
    if (rotation.isNorth() || rotation.isCannot())
    {
      position.y += 0.3;
    }
    else if (rotation.isSouth())
    {
      position.y += 0.3;
    }
    else if (rotation.isEast())
    {
      position.y += 0.3;
    }
    else if (rotation.isWest())
    {
      position.y += 0.3;
    }
    else
    {
      throw new SimulatorException("Cannot set position for unknown rotation.");
    }

    double x = 0.4;
    if (properties.appearance == SplitterAppearance.LEFT_HANDED)
    {
      position.x += x;
    }
    else if (properties.appearance == SplitterAppearance.RIGHT_HANDED)
    {
      position.x -= x;
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
      if (startPosition.x > 0)
      {
        endPosition = createEndPosition(0);
      }
      else if (startPosition.x < -(fanOut - 1) * gridSpacing)
      {
        endPosition = createEndPosition(fanOut - 1);
      }
      else
      {
        endPosition = createEndPosition(0);
        endPosition.x = startPosition.x;
      }
    }
    else if (appearance == SplitterAppearance.RIGHT_HANDED)
    {
      if (startPosition.x > (fanOut - 1) * gridSpacing)
      {
        endPosition = createEndPosition(fanOut - 1);
      }
      else if (startPosition.x < 0)
      {
        endPosition = createEndPosition(0);
      }
      else
      {
        endPosition = createEndPosition(0);
        endPosition.x = startPosition.x;
      }
    }
    else
    {
      endPosition = createEndPosition(0);
      endPosition.x = startPosition.x;
    }
    endPosition.y = 0;
    return endPosition;
  }

  private RectangleView createRectangleView(int fanOut)
  {
    Float2D p1 = createEndPosition(0);
    p1.y = 0 - 0.1f;
    Float2D p2 = createEndPosition(fanOut - 1);
    p2.y = 0 + 0.1f;
    return new RectangleView(this, p1, p2, true, true);
  }

  @Override
  public void paint(Graphics2D graphics,
                    Viewport viewport,
                    SubcircuitSimulation subcircuitSimulation)
  {
    super.paint(graphics, viewport, subcircuitSimulation);

    Color color = graphics.getColor();
    Stroke stroke = graphics.getStroke();
    Font font = graphics.getFont();

    for (LineView lineView : lineViews)
    {
      lineView.paint(graphics, viewport);
    }
    rectangleView.paint(graphics, viewport);

    paintPorts(graphics, viewport, subcircuitSimulation);

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
                            saveSimulationPassives(),
                            savePorts(),
                            id,
                            enabled,
                            selected,
                            properties.bitWidth,
                            properties.fanOut,
                            properties.gridSpacing,
                            properties.appearance,
                            properties.endOffset,
                            properties.splitIndices);
  }

  @Override
  protected Splitter createPassive(SubcircuitSimulation subcircuitSimulation)
  {
    List<String> startPortNames = getStartPortNames();
    return new Splitter(subcircuitSimulation.getCircuit(),
                        properties.name,
                        startPortNames,
                        getEndPortNames());
  }

  private Float2D createStartPosition()
  {
    if (properties.appearance == SplitterAppearance.LEFT_HANDED)
    {
      return new Float2D(-properties.endOffset, -1);
    }
    else if (properties.appearance == SplitterAppearance.RIGHT_HANDED)
    {
      return new Float2D(properties.endOffset, -1);
    }
    else
    {
      return new Float2D(((properties.fanOut / 4.0f) * properties.gridSpacing) - properties.endOffset, -1);
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

  private Float2D createPosition(int endIndex, int y)
  {
    if (properties.appearance == SplitterAppearance.LEFT_HANDED)
    {
      return new Float2D(-endIndex * properties.gridSpacing, y);
    }
    else if (properties.appearance == SplitterAppearance.RIGHT_HANDED)
    {
      return new Float2D(endIndex * properties.gridSpacing, y);
    }
    else
    {
      return new Float2D((-endIndex + (properties.fanOut / 2.0f)) * properties.gridSpacing, y);
    }
  }

  @Override
  public void clampProperties(SplitterProperties newProperties)
  {
    newProperties.fanOut = PropertyClamp.clamp(newProperties.fanOut, 1, PropertyClamp.MAX_WIDTH);
    newProperties.gridSpacing = PropertyClamp.clamp(newProperties.gridSpacing, 1, 12);
    newProperties.endOffset = PropertyClamp.clamp(newProperties.endOffset, -1, newProperties.fanOut * newProperties.gridSpacing - 1);
    for (int i = 0; i < newProperties.splitIndices.length; i++)
    {
      int index = newProperties.splitIndices[i];
      newProperties.splitIndices[i] = PropertyClamp.clamp(index, 0, newProperties.fanOut - 1);
    }
  }

  public Map<String, String> calculateBidirectionalPortMap()
  {
    HashMap<String, String> result = new HashMap<>();

    List<String> startPortNames = getStartPortNames();
    List<String> endPortNames = getEndPortNames();

    int i = 0;
    for (int fanIndex = 0; fanIndex < properties.fanOut; fanIndex++)
    {
      List<Integer> portsForFanoutIndex = getPortIndicesForFanoutIndex(properties, fanIndex);
      for (Integer endPortIndex : portsForFanoutIndex)
      {
        String endPortName = endPortNames.get(endPortIndex);
        String startPortName = startPortNames.get(i);
        result.put(startPortName, endPortName);
        result.put(endPortName, startPortName);
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
      adjustTextViewPosition(textView, position, rotation);
    }
  }

  @Override
  public void setRotation(Rotation rotation)
  {
    super.setRotation(rotation);
    updateTextViews();
  }

  @Override
  public String toDebugString()
  {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < properties.splitIndices.length; i++)
    {
      int splitIndex = properties.splitIndices[i];
      builder.append("Split [");
      builder.append(i);
      builder.append(" -> ");
      builder.append(splitIndex);
      builder.append("]\n");
    }

    return super.toDebugString() +
           String.format("Bit Width [%s]\nFan Out [%s]\nSpacing [%s]\nAppearance [%s]\nEnd Offset[%s]\n",
                         properties.bitWidth,
                         properties.fanOut,
                         properties.gridSpacing,
                         properties.appearance,
                         properties.endOffset) +
           builder.toString();
  }

  public String getOpposite(String portName)
  {
    return bidirectionalPortMap.get(portName);
  }
}

