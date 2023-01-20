package net.logicim.data.integratedcircuit.decorative;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.DecorativeData;
import net.logicim.data.wire.TraceLoader;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.component.decorative.common.DecorativeView;
import net.logicim.ui.simulation.component.decorative.label.LabelProperties;
import net.logicim.ui.simulation.component.decorative.label.LabelView;

public class LabelData
    extends DecorativeData
{
  public LabelData()
  {
  }

  public LabelData(String name, Int2D position, Rotation rotation, boolean selected)
  {
    super(name, position, rotation, selected);
  }

  @Override
  protected DecorativeView<?> create(CircuitEditor circuitEditor, TraceLoader traceLoader)
  {
    return new LabelView(circuitEditor,
                         position,
                         rotation,
                         new LabelProperties(name));
  }
}

