package net.logicim.ui.integratedcircuit.standard.logic.or;

import net.logicim.common.type.Int2D;
import net.logicim.domain.integratedcircuit.standard.logic.or.NorGate;
import net.logicim.domain.integratedcircuit.standard.logic.or.OrGatePins;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;

public class NorGateView
    extends BaseOrGateView<NorGate>
{
  public NorGateView(CircuitEditor circuitEditor,
                     int inputCount,
                     Int2D position,
                     Rotation rotation)
  {
    super(circuitEditor,
          new NorGate(circuitEditor.getCircuit(), "", new OrGatePins(inputCount)),
          position,
          rotation);
    createLogicGatePorts(inputCount, true);
    finaliseView();
  }
}

