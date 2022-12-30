package net.logicim.ui.integratedcircuit.standard.logic.buffer;

import net.logicim.common.type.Int2D;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.defaults.DefaultFamily;
import net.logicim.ui.integratedcircuit.factory.ViewFactory;

public class InverterViewFactory
    extends ViewFactory<InverterView, BufferProperties>
{
  @Override
  public InverterView create(CircuitEditor circuitEditor, Int2D position, Rotation rotation)
  {
    return create(circuitEditor,
                  position,
                  rotation,
                  new BufferProperties("",
                                       DefaultFamily.get(),
                                       true));
  }

  @Override
  public InverterView create(CircuitEditor circuitEditor, Int2D position, Rotation rotation, BufferProperties properties)
  {
    return new InverterView(circuitEditor, position, rotation, properties);
  }

  @Override
  public Class<InverterView> getViewClass()
  {
    return InverterView.class;
  }
}

