package net.logicim.data.wire;

import net.logicim.common.SimulatorException;
import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.BoundingBoxData;
import net.logicim.data.integratedcircuit.common.StaticData;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.wire.TunnelProperties;
import net.logicim.ui.common.wire.TunnelView;
import net.logicim.ui.simulation.SubcircuitEditor;

public class TunnelData
    extends StaticData<TunnelView>
{
  public long[] traceIds;
  public boolean doubleSided;

  public TunnelData()
  {
  }

  public TunnelData(String name,
                    Int2D position,
                    Rotation rotation,
                    BoundingBoxData boundingBox,
                    BoundingBoxData selectionBox,
                    boolean selected,
                    long[] traceIds,
                    boolean doubleSided)
  {
    super(name,
          position,
          rotation,
          boundingBox,
          selectionBox,
          selected);
    this.traceIds = traceIds;
    this.doubleSided = doubleSided;
  }

  @Override
  public TunnelView createAndLoad(SubcircuitEditor subcircuitEditor, TraceLoader traceLoader, boolean createConnections, Simulation simulation, Circuit circuit)
  {
    TunnelView tunnelView = new TunnelView(subcircuitEditor.getSubcircuitView(),
                                           circuit,
                                           position,
                                           rotation,
                                           boundingBox.create(),
                                           selectionBox.create(),
                                           new TunnelProperties(name, doubleSided));
    if (createConnections)
    {
      tunnelView.createConnections(subcircuitEditor.getSubcircuitView());
      WireDataHelper.wireConnect(subcircuitEditor,
                                 simulation,
                                 traceLoader,
                                 tunnelView,
                                 traceIds,
                                 selected);
    }
    tunnelView.enable(simulation);
    return tunnelView;
  }

  @Override
  protected TunnelView create(SubcircuitView subcircuitView, Circuit circuit, TraceLoader traceLoader)
  {
    throw new SimulatorException("Create should not be called from TunnelData.");
  }
}

