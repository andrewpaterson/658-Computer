package net.logicim.ui.integratedcircuit.standard.logic.common;

import net.logicim.common.type.Int2D;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.IntegratedCircuitView;
import net.logicim.ui.common.PortView;
import net.logicim.ui.common.Rotation;

public abstract class LogicGateView<IC extends IntegratedCircuit<?, ?>>
    extends IntegratedCircuitView<IC>
{
  public LogicGateView(CircuitEditor circuitEditor,
                       IC integratedCircuit,
                       Int2D position,
                       Rotation rotation)
  {
    super(circuitEditor, integratedCircuit, position, rotation);
  }

  protected void createPorts(int inputCount, boolean negateOutput, int inputOffset)
  {
    int start;
    int end;
    boolean skipZero = false;
    if (inputCount % 2 == 0)
    {
      end = inputCount / 2;
      skipZero = true;
    }
    else
    {
      end = inputCount / 2;
    }
    start = -end;

    int portNumber = 0;
    for (int i = start; i <= end; i++)
    {
      if (!((i == 0) & skipZero))
      {
        new PortView(this, this.integratedCircuit.getPort("Input " + portNumber), new Int2D(i, 1 + inputOffset));
        portNumber++;
      }
    }
    PortView outputPortView = new PortView(this, this.integratedCircuit.getPort("Output"), new Int2D(0, -2));
    if (negateOutput)
    {
      outputPortView.setInverting(true, Rotation.North);
    }
  }
}

