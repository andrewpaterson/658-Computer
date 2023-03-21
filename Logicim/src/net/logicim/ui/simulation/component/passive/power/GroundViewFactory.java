package net.logicim.ui.simulation.component.passive.power;

import net.logicim.common.type.Int2D;
import net.logicim.domain.common.Circuit;
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
    Circuit circuit = circuitEditor.getCircuit();
    return new GroundView(subcircuitView,
                          circuit,
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

