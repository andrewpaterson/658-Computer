package net.logicim.ui.simulation.component.integratedcircuit.standard.constant;

import net.common.type.Int2D;
import net.logicim.data.common.Radix;
import net.logicim.domain.common.defaults.DefaultFamily;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.component.factory.ViewFactory;

public class ConstantViewFactory
    extends ViewFactory<ConstantView, ConstantProperties>
{
  @Override
  public ConstantView create(CircuitEditor circuitEditor,
                             Int2D position,
                             Rotation rotation)
  {
    return create(circuitEditor,
                  circuitEditor.getCurrentSubcircuitView(),
                  position,
                  rotation,
                  createDefaultProperties(circuitEditor.getCurrentSubcircuitEditor(),
                                          getViewClass(),
                                          true));
  }

  @Override
  public ConstantProperties createInitialProperties()
  {
    return new ConstantProperties("",
                                  DefaultFamily.get(),
                                  2,
                                  Radix.BINARY);
  }

  @Override
  public ConstantView create(CircuitEditor circuitEditor,
                             SubcircuitView subcircuitView,
                             Int2D position,
                             Rotation rotation,
                             ConstantProperties properties)
  {
    return new ConstantView(subcircuitView,
                            position,
                            rotation,
                            properties);
  }

  @Override
  public Class<ConstantView> getViewClass()
  {
    return ConstantView.class;
  }
}

