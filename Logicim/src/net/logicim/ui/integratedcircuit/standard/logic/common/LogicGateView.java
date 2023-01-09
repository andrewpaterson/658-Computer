package net.logicim.ui.integratedcircuit.standard.logic.common;

import net.logicim.common.type.Int2D;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.integratedcircuit.standard.common.StandardIntegratedCircuitView;

import static net.logicim.ui.common.integratedcircuit.PropertyClamp.clamp;

public abstract class LogicGateView<IC extends IntegratedCircuit<?, ?>>
    extends StandardIntegratedCircuitView<IC, LogicGateProperties>
{

  public LogicGateView(CircuitEditor circuitEditor,
                       Int2D position,
                       Rotation rotation,
                       LogicGateProperties properties)
  {
    super(circuitEditor, position, rotation, properties);
  }

  protected void createPortViews(boolean negateOutput, int inputOffset)
  {
    int start;
    int end;
    boolean skipZero = false;
    end = properties.inputCount / 2;
    if (properties.inputCount % 2 == 0)
    {
      skipZero = true;
    }
    start = -end;

    int portNumber = 0;
    for (int i = start; i <= end; i++)
    {
      if (!((i == 0) & skipZero))
      {
        new PortView(this, integratedCircuit.getPort("Input " + portNumber), new Int2D(i, 1 + inputOffset));
        portNumber++;
      }
    }
    PortView outputPortView = new PortView(this, integratedCircuit.getPort("Output"), new Int2D(0, -2));
    if (negateOutput)
    {
      outputPortView.setInverting(true, Rotation.North);
    }
  }

  @Override
  public void clampProperties()
  {
    properties.inputCount = clamp(properties.inputCount, 1, 8);
  }
}

