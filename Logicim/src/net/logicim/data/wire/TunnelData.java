package net.logicim.data.wire;

import net.logicim.common.SimulatorException;
import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.BoundingBoxData;
import net.logicim.data.integratedcircuit.common.StaticData;
import net.logicim.data.passive.wire.TunnelProperties;
import net.logicim.domain.CircuitSimulation;
import net.logicim.ui.common.Rotation;
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
  public TunnelView createAndLoad(SubcircuitEditor subcircuitEditor,
                                  TraceLoader traceLoader,
                                  boolean fullLoad,
                                  CircuitSimulation simulation)
  {
    TunnelView tunnelView = new TunnelView(subcircuitEditor.getSubcircuitView(),
                                           simulation,
                                           position,
                                           rotation,
                                           boundingBox.create(),
                                           selectionBox.create(),
                                           new TunnelProperties(name, doubleSided));
    if (fullLoad)
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
  protected TunnelView create(SubcircuitEditor subcircuitEditor, CircuitSimulation simulation, TraceLoader traceLoader, boolean fullLoad)
  {
    throw new SimulatorException("Create should not be called from TunnelData.");
  }
}

