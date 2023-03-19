package net.logicim.data.integratedcircuit.common;

import net.logicim.common.SimulatorException;
import net.logicim.common.type.Int2D;
import net.logicim.data.wire.TraceLoader;
import net.logicim.data.wire.WireDataHelper;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.SubcircuitEditor;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;

public class SubcircuitInstanceData
    extends StaticData<SubcircuitInstanceView>
{
  public String subcircuitTypeName;

  public SubcircuitInstanceData()
  {
  }

  public SubcircuitInstanceData(String subcircuitTypeName,
                                Int2D position,
                                Rotation rotation,
                                String name,
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
    this.subcircuitTypeName = subcircuitTypeName;
  }

  @Override
  public SubcircuitInstanceView createAndLoad(SubcircuitEditor subcircuitEditor,
                                              TraceLoader traceLoader,
                                              boolean createConnections,
                                              Simulation simulation,
                                              Circuit circuit)
  {
    SubcircuitEditor instanceSubcircuitEditor = subcircuitEditor.getSubcircuitEditor(subcircuitTypeName);
    SubcircuitInstanceView subcircuitInstanceView = new SubcircuitInstanceView(subcircuitEditor.getSubcircuitView(),
                                                                               instanceSubcircuitEditor.getSubcircuitView(),
                                                                               circuit,
                                                                               position,
                                                                               rotation);
//    if (createConnections)
//    {
//      subcircuitInstanceView.createConnections(subcircuitEditor.getSubcircuitView());
//      WireDataHelper.wireConnect(subcircuitEditor,
//                                 simulation,
//                                 traceLoader,
//                                 subcircuitInstanceView,
//                                 traceIds,
//                                 selected);
//    }
    subcircuitInstanceView.enable(simulation);
    return subcircuitInstanceView;

  }

  @Override
  protected SubcircuitInstanceView create(SubcircuitView subcircuitView, Circuit circuit, TraceLoader traceLoader)
  {
    throw new SimulatorException("Create should not be called from TunnelData.");
  }
}

