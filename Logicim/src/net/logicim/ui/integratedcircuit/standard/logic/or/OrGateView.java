package net.logicim.ui.integratedcircuit.standard.logic.or;

import net.logicim.common.type.Int2D;
import net.logicim.domain.integratedcircuit.standard.logic.or.OrGate;
import net.logicim.domain.integratedcircuit.standard.logic.or.OrGatePins;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;

public class OrGateView
    extends BaseOrGateView<OrGate>
{
  public OrGateView(CircuitEditor circuitEditor,
                    int inputCount,
                    Int2D position,
                    Rotation rotation)
  {
    super(circuitEditor,
          new OrGate(circuitEditor.getCircuit(), "", new OrGatePins(inputCount)),
          position,
          rotation);
    createLogicGatePorts(inputCount, false, 0);
    finaliseView();
  }
}

