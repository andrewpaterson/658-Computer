package net.logicim.ui.integratedcircuit.standard.logic.common;

import net.logicim.common.type.Int2D;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.IntegratedCircuitView;
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
}

