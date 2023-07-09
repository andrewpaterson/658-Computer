package net.logicim.ui.simulation.component.integratedcircuit.standard.logic.buffer;

import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.logic.buffer.BufferProperties;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.shape.polygon.PolygonView;
import net.logicim.ui.simulation.component.integratedcircuit.standard.logic.common.BaseGateView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static net.logicim.ui.common.Rotation.North;

public abstract class BaseBufferView<IC extends IntegratedCircuit<?, ?>>
    extends BaseGateView<IC, BufferProperties>
{
  protected List<PolygonView> polygons;

  public BaseBufferView(SubcircuitView subcircuitView,
                        Int2D position,
                        Rotation rotation,
                        BufferProperties properties)
  {
    super(subcircuitView, position, rotation, properties);
    polygons = null;
    createGraphics();
  }

  protected void createGraphics()
  {
    polygons = new ArrayList<>();

    List<Integer> portOffsets = calculatePortOffsets(properties.inputCount);
    float width = (portOffsets.size() <= 2 ? 0.75f : 0.5f);
    for (int portOffset : portOffsets)
    {
      polygons.add(new PolygonView(this,
                                   true,
                                   true,
                                   new Float2D(portOffset, -0.9f),
                                   new Float2D(portOffset + width, 1),
                                   new Float2D(portOffset - width, 1)));
    }
  }

  protected void createPortViews(boolean negateOutput)
  {
    List<Integer> portOffsets = calculatePortOffsets(properties.inputCount);
    for (int portNumber = 0; portNumber < portOffsets.size(); portNumber++)
    {
      int portOffset = portOffsets.get(portNumber);
      new PortView(this,
                   getPortNames("Input ",
                                portNumber,
                                properties.inputWidth),
                   new Int2D(portOffset, 1));
    }

    for (int portNumber = 0; portNumber < portOffsets.size(); portNumber++)
    {
      int portOffset = portOffsets.get(portNumber);
      PortView outputPortView = new PortView(this,
                                             getPortNames("Output ",
                                                          portNumber,
                                                          properties.inputWidth),
                                             new Int2D(portOffset, -1));
      if (negateOutput)
      {
        outputPortView.setInverting(true, North);
      }
    }
  }

  @Override
  public void paint(Graphics2D graphics,
                    Viewport viewport,
                    SubcircuitSimulation subcircuitSimulation)
  {
    super.paint(graphics, viewport, subcircuitSimulation);

    if (polygons != null)
    {
      Stroke stroke = graphics.getStroke();
      Color color = graphics.getColor();

      for (PolygonView polygon : polygons)
      {
        polygon.paint(graphics, viewport);
      }
      paintPorts(graphics, viewport, subcircuitSimulation);

      graphics.setStroke(stroke);
      graphics.setColor(color);
    }
  }

  @Override
  public String toDebugString()
  {
    return super.toDebugString() + String.format("Input Count [%s]\nInput Width [%s]\n", properties.inputCount, properties.inputWidth);
  }
}

