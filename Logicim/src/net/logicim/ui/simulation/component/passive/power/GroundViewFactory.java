package net.logicim.ui.simulation.component.passive.power;

import net.logicim.common.type.Int2D;
import net.logicim.data.passive.power.GroundProperties;
import net.logicim.domain.CircuitSimulation;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.component.factory.ViewFactory;

public class GroundViewFactory
    extends ViewFactory<GroundView, GroundProperties>
{
  @Override
  public GroundView create(CircuitEditor circuitEditor,
                           Int2D position,
                           Rotation rotation)
  {
    return create(circuitEditor,
                  position,
                  rotation,
                  createDefaultProperties(getViewClass()));
  }

  @Override
  public GroundProperties createInitialProperties()
  {
    return new GroundProperties("");
  }

  @Override
  public GroundView create(CircuitEditor circuitEditor,
                           Int2D position,
                           Rotation rotation,
                           GroundProperties properties)
  {
    SubcircuitView subcircuitView = circuitEditor.getCurrentSubcircuitView();
    return new GroundView(subcircuitView,
                          position,
                          rotation,
                          properties);
  }

  @Override
  public Class<GroundView> getViewClass()
  {
    return GroundView.class;
  }
}

