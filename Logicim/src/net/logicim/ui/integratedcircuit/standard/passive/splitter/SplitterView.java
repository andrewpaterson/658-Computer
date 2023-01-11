package net.logicim.ui.integratedcircuit.standard.passive.splitter;

import net.logicim.common.type.Int2D;
import net.logicim.data.passive.wire.SplitterData;
import net.logicim.domain.Simulation;
import net.logicim.domain.passive.wire.Splitter;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.integratedcircuit.PassiveView;
import net.logicim.ui.common.integratedcircuit.PropertyClamp;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.shape.line.LineView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SplitterView
    extends PassiveView<Splitter, SplitterProperties>
{
  protected List<LineView> lineViews;

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
    new PortView(this, passive.getStartPorts(), createStartPosition());
    createEndPorts(properties);
  }

  protected void createEndPorts(SplitterProperties properties)
  {
    for (int i = 0; i < properties.endCount; i++)
    {
      Int2D position = createEndPosition(i);
      new PortView(this, passive.getEndPort(i), new Int2D(position));
    }
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
    else if (startPosition.y < -(properties.endCount - 1) * properties.gridSpacing)
    {
      endPosition = createEndPosition(properties.endCount - 1);
    }
    else
    {
      endPosition = createEndPosition(0);
      endPosition.y = startPosition.y;
    }
    int centerX = 0;
    endPosition.x = centerX;
    LineView startLineView = new LineView(this, startPosition, endPosition, 3);
    lineViews.add(startLineView);

    for (int i = 0; i < properties.endCount; i++)
    {
      Int2D p1 = createEndPosition(i);
      Int2D p2 = p1.clone();
      p1.x = 0;
      LineView lineView = new LineView(this, p1, p2);
      lineViews.add(lineView);
    }

    Int2D p1 = createEndPosition(0);
    p1.x = centerX;
    Int2D p2 = createEndPosition(properties.endCount - 1);
    p2.x = centerX;
    lineViews.add(new LineView(this, p1, p2, 3));
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
                            properties.endCount,
                            properties.endOffset,
                            properties.gridSpacing,
                            properties.splitIndices);
  }

  @Override
  protected Splitter createPassive()
  {
    return new Splitter(circuitEditor.getCircuit(),
                        properties.name,
                        properties.endCount);
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
    properties.endCount = PropertyClamp.clamp(properties.endCount, 1, PropertyClamp.MAX);
    properties.gridSpacing = PropertyClamp.clamp(properties.gridSpacing, 1, 12);
    properties.endOffset = PropertyClamp.clamp(properties.endOffset, -1, properties.endCount * properties.gridSpacing - 1);
  }

  @Override
  public void simulationStarted(Simulation simulation)
  {
  }
}

