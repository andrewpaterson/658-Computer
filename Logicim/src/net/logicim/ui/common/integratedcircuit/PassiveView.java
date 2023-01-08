package net.logicim.ui.common.integratedcircuit;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.PassiveData;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;

public abstract class PassiveView<PROPERTIES extends ComponentProperties>
    extends ComponentView<PROPERTIES>
{
  public PassiveView(CircuitEditor circuitEditor,
                     Int2D position,
                     Rotation rotation,
                     PROPERTIES properties)
  {
    super(circuitEditor, position, rotation, properties);
  }

  public abstract PassiveData save(boolean selected);
}

