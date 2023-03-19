package net.logicim.ui.simulation.component.passive.power;

import net.logicim.common.type.Int2D;
import net.logicim.domain.common.Circuit;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.component.factory.ViewFactory;

public class PositivePowerViewFactory
    extends ViewFactory<PositivePowerView, PositivePowerProperties>
{
  @Override
  public PositivePowerView create(CircuitEditor circuitEditor,
                                  Int2D position,
                                  Rotation rotation)
  {
    return create(circuitEditor,
                  position,
                  rotation,
                  new PositivePowerProperties(null,
                                              3.3f));
  }

  @Override
  public PositivePowerView create(CircuitEditor circuitEditor,
                                  Int2D position,
                                  Rotation rotation,
                                  PositivePowerProperties properties)
  {
    SubcircuitView subcircuitView = circuitEditor.getCurrentSubcircuitView();
    Circuit circuit = circuitEditor.getCircuit();
    return new PositivePowerView(subcircuitView,
                                 circuit,
                                 position,
                                 rotation,
                                 properties);
  }

  @Override
  public Class<PositivePowerView> getViewClass()
  {
    return PositivePowerView.class;
  }
}

