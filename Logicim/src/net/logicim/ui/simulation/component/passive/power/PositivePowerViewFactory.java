package net.logicim.ui.simulation.component.passive.power;

import net.common.type.Int2D;
import net.logicim.data.passive.power.PositivePowerProperties;
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
    return create(
        circuitEditor, circuitEditor.getCurrentSubcircuitView(), position,
        rotation,
        createDefaultProperties(circuitEditor.getCurrentSubcircuitEditor(), getViewClass(), true));
  }

  @Override
  public PositivePowerProperties createInitialProperties()
  {
    return new PositivePowerProperties(null, 3.3f);
  }

  @Override
  public PositivePowerView create(CircuitEditor circuitEditor,
                                  SubcircuitView subcircuitView,
                                  Int2D position,
                                  Rotation rotation,
                                  PositivePowerProperties properties)
  {
    return new PositivePowerView(subcircuitView,
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

