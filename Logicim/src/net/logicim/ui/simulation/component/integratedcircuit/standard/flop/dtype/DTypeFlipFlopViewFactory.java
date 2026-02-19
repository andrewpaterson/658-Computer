package net.logicim.ui.simulation.component.integratedcircuit.standard.flop.dtype;

import net.common.type.Int2D;
import net.logicim.domain.common.defaults.DefaultFamily;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.component.factory.ViewFactory;

public class DTypeFlipFlopViewFactory
    extends ViewFactory<DTypeFlipFlopView, DTypeFlipFlopProperties>
{
  @Override
  public DTypeFlipFlopView create(CircuitEditor circuitEditor,
                                  Int2D position,
                                  Rotation rotation)
  {
    return create(circuitEditor, circuitEditor.getCurrentSubcircuitView(), position,
                  rotation,
                  createDefaultProperties(circuitEditor.getCurrentSubcircuitEditor(), getViewClass(), true));
  }

  @Override
  public DTypeFlipFlopProperties createInitialProperties()
  {
    return new DTypeFlipFlopProperties("",
                                       DefaultFamily.get(),
                                       false,
                                       true,
                                       true);
  }

  @Override
  public DTypeFlipFlopView create(CircuitEditor circuitEditor,
                                  SubcircuitView subcircuitView,
                                  Int2D position,
                                  Rotation rotation,
                                  DTypeFlipFlopProperties properties)
  {
    return new DTypeFlipFlopView(subcircuitView,
                                 position,
                                 rotation,
                                 properties);
  }

  @Override
  public Class<DTypeFlipFlopView> getViewClass()
  {
    return DTypeFlipFlopView.class;
  }
}

