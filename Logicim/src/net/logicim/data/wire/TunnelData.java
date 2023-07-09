package net.logicim.data.wire;

import net.logicim.common.SimulatorException;
import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.StaticData;
import net.logicim.data.passive.wire.TunnelProperties;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.wire.TunnelView;
import net.logicim.ui.simulation.CircuitLoaders;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

import java.util.Map;

public class TunnelData
    extends StaticData<TunnelView>
{
  public Map<Long, long[]> simulationTraces;
  public boolean doubleSided;

  public TunnelData()
  {
  }

  public TunnelData(String name,
                    Int2D position,
                    Rotation rotation,
                    long id,
                    boolean selected,
                    Map<Long, long[]> simulationTraces,
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

  public void createAndConnectComponent(SubcircuitSimulation containingSubcircuitSimulation,
                                        CircuitLoaders circuitLoaders,
                                        TunnelView tunnelView)
  {
    long[] traceIDs = simulationTraces.get(containingSubcircuitSimulation.getId());
    if (traceIDs == null)
    {
      throw new SimulatorException("Cannot find trace IDs for Circuit Simulation [%s].", containingSubcircuitSimulation.getDescription());
    }

    tunnelView.wireConnect(containingSubcircuitSimulation,
                           circuitLoaders.getTraceLoader(),
                           traceIDs
    );
  }

  @Override
  public TunnelView createStaticView(SubcircuitEditor subcircuitEditor, boolean newComponentPropertyStep)
  {
    TunnelView componentView = createComponentView(subcircuitEditor);
    componentView.getOrCreateConnectionViews(subcircuitEditor.getCircuitSubcircuitView());
    return componentView;
  }

  public TunnelView createComponentView(SubcircuitEditor subcircuitEditor)
  {
    return new TunnelView(subcircuitEditor.getCircuitSubcircuitView(),
                          position,
                          rotation,
                          new TunnelProperties(name, doubleSided));
  }

  @Override
  public boolean appliesToSimulation(long id)
  {
    return simulationTraces.containsKey(id);
  }
}

