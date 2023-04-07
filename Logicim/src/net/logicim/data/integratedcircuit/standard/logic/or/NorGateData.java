package net.logicim.data.integratedcircuit.standard.logic.or;

import net.logicim.common.type.Int2D;
import net.logicim.data.family.Family;
import net.logicim.data.integratedcircuit.event.IntegratedCircuitEventData;
import net.logicim.data.integratedcircuit.standard.logic.common.LogicGateData;
import net.logicim.data.port.common.SimulationMultiPortData;
import net.logicim.data.wire.TraceLoader;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.propagation.FamilyStore;
import net.logicim.domain.common.state.State;
import net.logicim.ui.common.Rotation;
import net.logicim.data.integratedcircuit.standard.logic.common.LogicGateProperties;
import net.logicim.ui.simulation.SubcircuitEditor;
import net.logicim.ui.simulation.component.integratedcircuit.standard.logic.or.NorGateView;

import java.util.List;

public class NorGateData
    extends LogicGateData<NorGateView>
{
  public NorGateData()
  {
  }

  public NorGateData(Int2D position,
                     Rotation rotation,
                     String name,
                     Family family,
                     List<IntegratedCircuitEventData<?>> events,
                     List<SimulationMultiPortData> ports,
                     boolean selected,
                     State state,
                     int inputCount,
                     int inputWidth,
                     boolean explicitPowerPorts)
  {
    super(position,
          rotation,
          name,
          family,
          events,
          ports,
          selected,
          state,
          inputCount,
          inputWidth,
          explicitPowerPorts);
  }

  @Override
  public NorGateView create(SubcircuitEditor subcircuitEditor, CircuitSimulation simulation, TraceLoader traceLoader, boolean fullLoad)
  {
    return new NorGateView(subcircuitEditor.getSubcircuitView(),
                           simulation,
                           position,
                           rotation,
                           new LogicGateProperties(name,
                                                   FamilyStore.getInstance().get(family),
                                                   explicitPowerPorts,
                                                   inputCount,
                                                   inputWidth));
  }
}

