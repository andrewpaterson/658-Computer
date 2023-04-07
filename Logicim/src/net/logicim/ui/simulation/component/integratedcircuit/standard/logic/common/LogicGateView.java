package net.logicim.ui.simulation.component.integratedcircuit.standard.logic.common;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.logic.common.LogicGateProperties;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.PropertyClamp;
import net.logicim.ui.common.port.PortView;

import java.util.List;

import static net.logicim.ui.common.integratedcircuit.PropertyClamp.clamp;

public abstract class LogicGateView<IC extends IntegratedCircuit<?, ?>>
    extends BaseGateView<IC, LogicGateProperties>
{
  public static final float OR_FILL_OFFSET = 0.25f;
  public static final int OR_ARC_RIGHT_START = 354;
  public static final int OR_ARC_SIDE_LENGTH = 66;
  public static final int OR_ARC_LEFT_START = 120;
  public static final int OR_ARC_BOTTOM_START = 61;
  public static final int OR_ARC_BOTTOM_LENGTH = 58;
  public static final int OR_ARC_FILL_OFFSET = 6;

  public LogicGateView(SubcircuitView subcircuitView,
                       Int2D position,
                       Rotation rotation,
                       LogicGateProperties properties)
  {
    super(subcircuitView, position, rotation, properties);
  }

  protected void createPortViews(boolean negateOutput, int inputOffset)
  {
    List<Integer> portOffsets = calculatePortOffsets(properties.inputCount);
    for (int portNumber = 0; portNumber < portOffsets.size(); portNumber++)
    {
      Integer i = portOffsets.get(portNumber);
      new PortView(this,
                   getPortNames("Input ",
                                portNumber,
                                properties.inputWidth),
                   new Int2D(i, 1 + inputOffset));
    }

    PortView outputPortView = new PortView(this, "Output", new Int2D(0, -2));
    if (negateOutput)
    {
      outputPortView.setInverting(true, Rotation.North);
    }
  }

  @Override
  public void clampProperties(LogicGateProperties newProperties)
  {
    newProperties.inputCount = clamp(newProperties.inputCount, 1, PropertyClamp.MAX_WIDTH);
    newProperties.inputWidth = clamp(newProperties.inputWidth, 1, PropertyClamp.MAX_WIDTH);
  }
}

