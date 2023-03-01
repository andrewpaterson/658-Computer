package net.logicim.ui.simulation.component.integratedcircuit.standard.logic.buffer;

import net.logicim.common.type.Int2D;
import net.logicim.domain.common.Circuit;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.defaults.DefaultFamily;
import net.logicim.ui.simulation.component.factory.ViewFactory;

public class BufferViewFactory
    extends ViewFactory<BufferView, BufferProperties>
{
  @Override
  public BufferView create(SubcircuitView subcircuitView,
                           Circuit circuit,
                           Int2D position,
                           Rotation rotation)
  {
    return create(subcircuitView,
                  circuit,
                  position,
                  rotation,
                  new BufferProperties("",
                                       DefaultFamily.get(),
                                       true,
                                       1,
                                       1));
  }

  @Override
  public BufferView create(SubcircuitView subcircuitView,
                           Circuit circuit,
                           Int2D position,
                           Rotation rotation,
                           BufferProperties properties)
  {
    return new BufferView(subcircuitView,
                          circuit,
                          position,
                          rotation,
                          properties);
  }

  @Override
  public Class<BufferView> getViewClass()
  {
    return BufferView.class;
  }
}

