package net.logicim.ui.simulation.component.passive.pin;

import net.logicim.common.type.Int2D;
import net.logicim.data.circuit.SubcircuitPinAnchour;
import net.logicim.data.circuit.SubcircuitPinAlignment;
import net.logicim.data.common.Radix;
import net.logicim.domain.common.Circuit;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.defaults.DefaultFamily;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.component.factory.ViewFactory;

public class PinViewFactory
    extends ViewFactory<PinView, PinProperties>
{
  @Override
  public PinView create(CircuitEditor circuitEditor,
                        Int2D position,
                        Rotation rotation)
  {
    return create(circuitEditor,
                  position,
                  rotation,
                  new PinProperties("Pin",
                                    1,
                                    SubcircuitPinAlignment.LEFT,
                                    SubcircuitPinAnchour.POSITIVE,
                                    0,
                                    false,
                                    false,
                                    DefaultFamily.get(),
                                    Radix.BINARY));
  }

  @Override
  public PinView create(CircuitEditor circuitEditor,
                        Int2D position,
                        Rotation rotation,
                        PinProperties properties)
  {
    SubcircuitView subcircuitView = circuitEditor.getCurrentSubcircuitView();
    Circuit circuit = circuitEditor.getCircuit();
    return new PinView(subcircuitView,
                       circuit,
                       position,
                       rotation,
                       properties);
  }

  @Override
  public Class<PinView> getViewClass()
  {
    return PinView.class;
  }
}

