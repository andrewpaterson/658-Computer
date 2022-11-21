package net.logicim.ui.integratedcircuit.standard.logic.and;

import net.logicim.common.type.Int2D;
import net.logicim.domain.integratedcircuit.standard.logic.and.AndGate;
import net.logicim.domain.integratedcircuit.standard.logic.and.AndGatePins;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;

public class AndGateView
    extends BaseAndGateView<AndGate>
{
  public AndGateView(CircuitEditor circuitEditor,
                     int inputCount,
                     Int2D position,
                     Rotation rotation)
  {
    super(circuitEditor,
          inputCount,
          position,
          rotation);
    createPorts(false, 0);
    finaliseView();
  }

  @Override
  protected AndGate createIntegratedCircuit()
  {
    return new AndGate(circuitEditor.getCircuit(), "", new AndGatePins(inputCount));
  }
}

