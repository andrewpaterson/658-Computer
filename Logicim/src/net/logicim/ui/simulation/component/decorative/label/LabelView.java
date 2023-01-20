package net.logicim.ui.simulation.component.decorative.label;

import net.logicim.common.type.Int2D;
import net.logicim.data.ReflectiveData;
import net.logicim.data.integratedcircuit.decorative.LabelData;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.component.decorative.common.DecorativeView;

public class LabelView
    extends DecorativeView<LabelProperties>
{
  public LabelView(CircuitEditor circuitEditor, Int2D position, Rotation rotation, LabelProperties properties)
  {
    super(circuitEditor, position, rotation, properties);
  }

  @Override
  public void clampProperties()
  {
  }

  @Override
  public ReflectiveData save(boolean selected)
  {
    return new LabelData(properties.name, position, rotation, selected);
  }

  @Override
  public String getType()
  {
    return "Label";
  }
}

