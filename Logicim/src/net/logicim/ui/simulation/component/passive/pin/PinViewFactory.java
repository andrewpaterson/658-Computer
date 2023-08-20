package net.logicim.ui.simulation.component.passive.pin;

import net.common.type.Int2D;
import net.logicim.data.circuit.SubcircuitPinAlignment;
import net.logicim.data.circuit.SubcircuitPinAnchour;
import net.logicim.data.common.Radix;
import net.logicim.data.passive.wire.PinProperties;
import net.logicim.domain.common.defaults.DefaultFamily;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
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
                  circuitEditor.getCurrentSubcircuitView(),
                  position,
                  rotation,
                  createDefaultProperties(circuitEditor.getCurrentSubcircuitEditor(),
                                          getViewClass(),
                                          false));
  }

  @Override
  public PinProperties createInitialProperties()
  {
    return new PinProperties("Pin",
                             1,
                             SubcircuitPinAlignment.LEFT,
                             SubcircuitPinAnchour.NEGATIVE,
                             0,
                             false,
                             false,
                             DefaultFamily.get(),
                             false,
                             Radix.BINARY);
  }

  @Override
  public PinView create(CircuitEditor circuitEditor,
                        SubcircuitView subcircuitView,
                        Int2D position,
                        Rotation rotation,
                        PinProperties properties)
  {
    return new PinView(subcircuitView,
                       circuitEditor.getSubcircuitInstanceViewFinder(),
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

