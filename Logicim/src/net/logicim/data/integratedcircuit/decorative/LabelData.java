package net.logicim.data.integratedcircuit.decorative;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.DecorativeData;
import net.logicim.data.wire.TraceLoader;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.component.decorative.label.LabelProperties;
import net.logicim.ui.simulation.component.decorative.label.LabelView;

public class LabelData
    extends DecorativeData<LabelView>
{
  protected HorizontalAlignment alignment;
  protected boolean bold;
  protected boolean fill;
  protected boolean border;

  public LabelData()
  {
  }

  public LabelData(String name,
                   Int2D position,
                   Rotation rotation,
                   boolean selected,
                   HorizontalAlignment alignment,
                   boolean bold,
                   boolean fill,
                   boolean border)
  {
    super(name, position, rotation, selected);
    this.alignment = alignment;
    this.bold = bold;
    this.fill = fill;
    this.border = border;
  }

  @Override
  protected LabelView create(CircuitEditor circuitEditor, TraceLoader traceLoader)
  {
    return new LabelView(circuitEditor,
                         position,
                         rotation,
                         new LabelProperties(name,
                                             alignment,
                                             bold,
                                             fill,
                                             border));
  }
}

