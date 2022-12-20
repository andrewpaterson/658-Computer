package net.logicim.ui.integratedcircuit.standard.power;

import net.logicim.common.type.Int2D;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;

public class GroundView
    extends PowerSourceView
{
  public GroundView(CircuitEditor circuitEditor,
                    Int2D position,
                    Rotation rotation,
                    String name)
  {
    super(circuitEditor, position, rotation, name);
    finaliseView();
  }

  @Override
  public float getSourceVoltage()
  {
    return 0;
  }

  @Override
  protected void createPortViews()
  {
  }
}

