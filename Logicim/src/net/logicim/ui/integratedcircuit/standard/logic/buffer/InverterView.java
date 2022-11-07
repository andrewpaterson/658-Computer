package net.logicim.ui.integratedcircuit.standard.logic.buffer;

import net.logicim.common.type.Int2D;
import net.logicim.domain.integratedcircuit.standard.logic.buffer.BufferPins;
import net.logicim.domain.integratedcircuit.standard.logic.buffer.Inverter;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;

public class InverterView
    extends BaseInverterView<Inverter>
{
  public InverterView(CircuitEditor circuitEditor,
                      Int2D position,
                      Rotation rotation)
  {
    super(circuitEditor,
          new Inverter(circuitEditor.getCircuit(), "", new BufferPins()),
          position,
          rotation);
    createPorts(true);
    finaliseView();
  }
}

