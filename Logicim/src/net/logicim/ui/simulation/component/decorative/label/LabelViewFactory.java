package net.logicim.ui.simulation.component.decorative.label;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.decorative.HorizontalAlignment;
import net.logicim.domain.common.Circuit;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.component.factory.ViewFactory;

public class LabelViewFactory
    extends ViewFactory<LabelView, LabelProperties>
{
  @Override
  public LabelView create(SubcircuitView subcircuitView,
                          Circuit circuit,
                          Int2D position,
                          Rotation rotation)
  {
    return new LabelView(subcircuitView,
                         circuit,
                         position,
                         rotation,
                         new LabelProperties("  ",
                                             HorizontalAlignment.LEFT,
                                             false,
                                             true,
                                             true));
  }

  @Override
  public LabelView create(SubcircuitView subcircuitView,
                          Circuit circuit,
                          Int2D position,
                          Rotation rotation,
                          LabelProperties properties)
  {
    return new LabelView(subcircuitView,
                         circuit,
                         position,
                         rotation,
                         properties);
  }

  @Override
  public Class<LabelView> getViewClass()
  {
    return LabelView.class;
  }
}

