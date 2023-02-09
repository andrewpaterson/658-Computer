package net.logicim.ui.simulation.component.integratedcircuit.standard.logic.common;

import net.logicim.common.type.Int2D;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.component.integratedcircuit.standard.common.StandardIntegratedCircuitView;

import java.util.ArrayList;
import java.util.List;

import static net.logicim.ui.common.integratedcircuit.PropertyClamp.clamp;

public abstract class LogicGateView<IC extends IntegratedCircuit<?, ?>>
    extends StandardIntegratedCircuitView<IC, LogicGateProperties>
{
  public static final float OR_FILL_OFFSET = 0.25f;
  public static final int OR_ARC_RIGHT_START = 354;
  public static final int OR_ARC_SIDE_LENGTH = 66;
  public static final int OR_ARC_LEFT_START = 120;
  public static final int OR_ARC_BOTTOM_START = 61;
  public static final int OR_ARC_BOTTOM_LENGTH = 58;
  public static final int OR_ARC_FILL_OFFSET = 6;

  public LogicGateView(CircuitEditor circuitEditor,
                       Int2D position,
                       Rotation rotation,
                       LogicGateProperties properties)
  {
    super(circuitEditor, position, rotation, properties);
  }

  protected static List<Integer> calculatePortOffsets(int inputCount)
  {
    int start;
    int end;
    boolean skipZero = false;
    end = inputCount / 2;
    if (inputCount % 2 == 0)
    {
      skipZero = true;
    }
    start = -end;

    ArrayList<Integer> integers = new ArrayList<>();
    for (int i = start; i <= end; i++)
    {
      if (!((i == 0) & skipZero))
      {
        integers.add(i);
      }
    }
    return integers;
  }

  protected void createPortViews(boolean negateOutput, int inputOffset)
  {
    List<Integer> portOffsets = calculatePortOffsets(properties.inputCount);
    for (int portNumber = 0; portNumber < portOffsets.size(); portNumber++)
    {
      Integer i = portOffsets.get(portNumber);
      new PortView(this, integratedCircuit.getPort("Input " + portNumber), new Int2D(i, 1 + inputOffset));
    }

    PortView outputPortView = new PortView(this, integratedCircuit.getPort("Output"), new Int2D(0, -2));
    if (negateOutput)
    {
      outputPortView.setInverting(true, Rotation.North);
    }
  }

  @Override
  public void propertyChanged(LogicGateProperties newProperties)
  {
    newProperties.inputCount = clamp(newProperties.inputCount, 1, 13);
  }

  protected float calculateWidth(int inputCount)
  {
    if (inputCount <= 3)
    {
      return 1.5f;
    }
    else
    {
      int i = (inputCount / 2) - 1;
      return 1.5f + i;
    }
  }
}

