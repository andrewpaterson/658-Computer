package net.logicim.data.integratedcircuit.standard.logic.and;

import net.logicim.common.type.Int2D;
import net.logicim.data.family.Family;
import net.logicim.data.integratedcircuit.event.IntegratedCircuitEventData;
import net.logicim.data.integratedcircuit.event.SimulationIntegratedCircuitEventData;
import net.logicim.data.integratedcircuit.standard.logic.common.LogicGateData;
import net.logicim.data.port.common.SimulationMultiPortData;
import net.logicim.data.wire.TraceLoader;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.propagation.FamilyStore;
import net.logicim.domain.common.state.SimulationState;
import net.logicim.domain.common.state.State;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.SubcircuitEditor;
import net.logicim.ui.simulation.component.integratedcircuit.standard.logic.and.NandGateView;
import net.logicim.data.integratedcircuit.standard.logic.common.LogicGateProperties;

import java.util.List;

public class NandGateData
    extends LogicGateData<NandGateView>
{
  public NandGateData()
  {
  }

  public NandGateData(Int2D position,
                      Rotation rotation,
                      String name,
                      Family family,
                      SimulationIntegratedCircuitEventData simulationEvents,
                      List<SimulationMultiPortData> ports,
                      boolean selected,
                      SimulationState<State> simulationState,
                      int inputCount,
                      int inputWidth,
                      boolean explicitPowerPorts)
  {
    super(position,
          rotation,
          name,
          family,
          simulationEvents,
          ports,
          selected,
          simulationState,
          inputCount,
          inputWidth,
          explicitPowerPorts);
  }

  @Override
  public NandGateView create(SubcircuitEditor subcircuitEditor, CircuitSimulation simulation, TraceLoader traceLoader, boolean fullLoad)
  {
    return new NandGateView(subcircuitEditor.getSubcircuitView(),
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

