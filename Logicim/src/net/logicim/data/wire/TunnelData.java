package net.logicim.data.wire;

import net.logicim.common.SimulatorException;
import net.logicim.common.type.Int2D;
import net.logicim.data.common.LongArrayData;
import net.logicim.data.common.LongData;
import net.logicim.data.integratedcircuit.common.StaticData;
import net.logicim.data.passive.wire.TunnelProperties;
import net.logicim.domain.CircuitSimulation;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.wire.TunnelView;
import net.logicim.ui.simulation.SubcircuitEditor;

import java.util.Map;

public class TunnelData
    extends StaticData<TunnelView>
{
  public Map<LongData, LongArrayData> simulationTraces;
  public boolean doubleSided;

  public TunnelData()
  {
  }

  public TunnelData(String name,
                    Int2D position,
                    Rotation rotation,
                    long id,
                    boolean selected,
                    Map<LongData, LongArrayData> simulationTraces,
                    boolean enabled,
                    boolean doubleSided)
  {
    super(name,
          position,
          rotation,
          id,
          enabled,
          selected);
    this.simulationTraces = simulationTraces;
    this.doubleSided = doubleSided;
  }

  public void createAndConnectComponent(SubcircuitEditor subcircuitEditor,
                                        CircuitSimulation circuitSimulation,
                                        TraceLoader traceLoader,
                                        TunnelView tunnelView)
  {
    LongArrayData traceIDs = simulationTraces.get(new LongData(circuitSimulation.getId()));
    if (traceIDs == null)
    {
      throw new SimulatorException("Cannot find trace IDs for Circuit Simulation [%s].", circuitSimulation.getDescription());
    }

    WireDataHelper.wireConnect(subcircuitEditor,
                               circuitSimulation,
                               traceLoader,
                               tunnelView,
                               traceIDs.array,
                               selected);
  }

  @Override
  public TunnelView createStaticView(SubcircuitEditor subcircuitEditor, boolean newComponentPropertyStep)
  {
    return new TunnelView(subcircuitEditor.getSubcircuitView(),
                          position,
                          rotation,
                          new TunnelProperties(name, doubleSided));
  }
}

