package net.logicim.data.integratedcircuit.common;

import net.logicim.common.type.Int2D;
import net.logicim.data.wire.TraceLoader;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.SubcircuitEditor;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;

public class SubcircuitInstanceData
    extends StaticData<SubcircuitInstanceView>
{
  public SubcircuitInstanceData()
  {
  }

  public SubcircuitInstanceData(String name,
                                Int2D position,
                                Rotation rotation,
                                BoundingBoxData boundingBox,
                                BoundingBoxData selectionBox,
                                boolean selected)
  {
    super(name,
          position,
          rotation,
          boundingBox,
          selectionBox,
          selected);
  }

  @Override
  public SubcircuitInstanceView createAndLoad(SubcircuitEditor subcircuitEditor, TraceLoader traceLoader, boolean createConnections, Simulation simulation, Circuit circuit)
  {
    return null;
  }

  @Override
  protected SubcircuitInstanceView create(SubcircuitView subcircuitView, Circuit circuit, TraceLoader traceLoader)
  {
    return null;
  }
}

