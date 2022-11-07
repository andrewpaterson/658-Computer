package net.logicim.ui.integratedcircuit.standard.logic.xor;

import net.logicim.common.type.Int2D;
import net.logicim.domain.integratedcircuit.standard.logic.xor.XnorGate;
import net.logicim.domain.integratedcircuit.standard.logic.xor.XorGatePins;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;

public class XnorGateView
    extends BaseXorGateView<XnorGate>
{
  public XnorGateView(CircuitEditor circuitEditor,
                      int inputCount,
                      Int2D position,
                      Rotation rotation)
  {
    super(circuitEditor,
          new XnorGate(circuitEditor.getCircuit(), "", new XorGatePins(inputCount)),
          position,
          rotation);
    createLogicGatePorts(inputCount, true, 1);
    finaliseView();
  }
}

