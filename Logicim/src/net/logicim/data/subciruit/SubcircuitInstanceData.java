package net.logicim.data.subciruit;

import net.logicim.common.SimulatorException;
import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.BoundingBoxData;
import net.logicim.data.integratedcircuit.common.StaticData;
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
  public String subcircuitTypeName;
  public String comment;
  public int width;
  public int height;

  public SubcircuitInstanceData()
  {
  }

  public SubcircuitInstanceData(String subcircuitTypeName,
                                Int2D position,
                                Rotation rotation,
                                String name,
                                BoundingBoxData boundingBox,
                                BoundingBoxData selectionBox,
                                boolean selected,
                                String comment,
                                int width,
                                int height)
  {
    super(name,
          position,
          rotation,
          boundingBox,
          selectionBox,
          selected);
    this.subcircuitTypeName = subcircuitTypeName;
    this.comment = comment;
    this.width = width;
    this.height = height;
  }

  @Override
  public SubcircuitInstanceView createAndLoad(SubcircuitEditor subcircuitEditor,
                                              TraceLoader traceLoader,
                                              boolean fullLoad,
                                              Simulation simulation,
                                              Circuit circuit)
  {
    SubcircuitEditor instanceSubcircuitEditor = subcircuitEditor.getSubcircuitEditor(subcircuitTypeName);
    SubcircuitInstanceView subcircuitInstanceView = new SubcircuitInstanceView(subcircuitEditor.getSubcircuitView(),
                                                                               instanceSubcircuitEditor.getSubcircuitView(),
                                                                               circuit,
                                                                               position,
                                                                               rotation,
                                                                               new SubcircuitInstanceProperties(name,
                                                                                                                subcircuitTypeName,
                                                                                                                comment,
                                                                                                                width,
                                                                                                                height));
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
  protected SubcircuitInstanceView create(SubcircuitView subcircuitView, Circuit circuit, TraceLoader traceLoader, boolean fullLoad)
  {
    throw new SimulatorException("Create should not be called from TunnelData.");
  }
}

