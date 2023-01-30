package net.logicim.data.wire;

import net.logicim.common.SimulatorException;
import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.BoundingBoxData;
import net.logicim.data.integratedcircuit.common.StaticData;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.wire.TunnelProperties;
import net.logicim.ui.common.wire.TunnelView;
import net.logicim.ui.simulation.CircuitEditor;

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
  public void createAndLoad(CircuitEditor circuitEditor, TraceLoader traceLoader)
  {
    TunnelView tunnelView = new TunnelView(circuitEditor,
                                           position,
                                           rotation,
                                           boundingBox.create(),
                                           selectionBox.create(),
                                           new TunnelProperties(name, doubleSided));
    WireDataHelper.wireConnect(circuitEditor,
                               traceLoader,
                               tunnelView,
                               traceIds,
                               selected);
  }

  @Override
  protected TunnelView create(CircuitEditor circuitEditor, TraceLoader traceLoader)
  {
    throw new SimulatorException("Create should not be called from TunnelData.");
  }
}

