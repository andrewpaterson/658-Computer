package net.logicim.ui.simulation.component.integratedcircuit.wdc;

import net.common.type.Int2D;
import net.logicim.domain.common.defaults.DefaultFamily;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.component.factory.ViewFactory;

public class W65C816ViewFactory
    extends ViewFactory<W65C816View, W65C816Properties>
{
  @Override
  public W65C816View create(CircuitEditor circuitEditor,
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
  public W65C816Properties createInitialProperties()
  {
    return new W65C816Properties("",
                                 DefaultFamily.get(),
                                 false);
  }

  @Override
  public W65C816View create(CircuitEditor circuitEditor,
                            SubcircuitView subcircuitView,
                            Int2D position,
                            Rotation rotation,
                            W65C816Properties properties)
  {
    return new W65C816View(subcircuitView,
                           position,
                           rotation,
                           properties);
  }

  @Override
  public Class<W65C816View> getViewClass()
  {
    return W65C816View.class;
  }
}

