package net.logicim.ui.integratedcircuit.standard.logic.and;

import net.logicim.common.type.Int2D;
import net.logicim.domain.integratedcircuit.standard.logic.and.AndGatePins;
import net.logicim.domain.integratedcircuit.standard.logic.and.NandGate;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;

public class NandGateView
    extends BaseAndGateView<NandGate>
{
  public NandGateView(CircuitEditor circuitEditor,
                      int inputCount,
                      Int2D position,
                      Rotation rotation)
  {
    super(circuitEditor,
          new NandGate(circuitEditor.getCircuit(), "", new AndGatePins(inputCount)),
          position,
          rotation);
    createPorts(inputCount, true, 0);
    finaliseView();
  }
}
