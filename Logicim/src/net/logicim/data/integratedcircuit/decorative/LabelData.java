package net.logicim.data.integratedcircuit.decorative;

import net.common.type.Int2D;
import net.logicim.data.decorative.label.LabelProperties;
import net.logicim.data.integratedcircuit.common.DecorativeData;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.component.decorative.label.LabelView;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

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
                   long id,
                   boolean enabled,
                   boolean selected,
                   HorizontalAlignment alignment,
                   boolean bold,
                   boolean fill,
                   boolean border)
  {
    super(name,
          position,
          rotation,
          id,
          enabled,
          selected);
    this.alignment = alignment;
    this.bold = bold;
    this.fill = fill;
    this.border = border;
  }

  @Override
  public LabelView createStaticView(SubcircuitEditor subcircuitEditor, boolean newComponentPropertyStep)
  {
    return new LabelView(subcircuitEditor.getInstanceSubcircuitView(),
                         position,
                         rotation,
                         new LabelProperties(name,
                                             alignment,
                                             bold,
                                             fill,
                                             border));
  }
}

