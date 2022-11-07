package net.logicim.ui.integratedcircuit.standard.logic.xor;

import net.logicim.common.type.Int2D;
import net.logicim.domain.integratedcircuit.standard.logic.xor.XorGate;
import net.logicim.domain.integratedcircuit.standard.logic.xor.XorGatePins;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;

public class XorGateView
    extends BaseXorGateView<XorGate>
{
  public XorGateView(CircuitEditor circuitEditor,
                     int inputCount,
                     Int2D position,
                     Rotation rotation)
  {
    super(circuitEditor,
          new XorGate(circuitEditor.getCircuit(), "", new XorGatePins(inputCount)),
          position,
          rotation);
    createLogicGatePorts(inputCount, false, 1);
    finaliseView();
  }
}

