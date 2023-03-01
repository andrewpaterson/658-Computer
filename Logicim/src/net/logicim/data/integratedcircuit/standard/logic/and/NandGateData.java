package net.logicim.data.integratedcircuit.standard.logic.and;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.event.IntegratedCircuitEventData;
import net.logicim.data.integratedcircuit.standard.logic.common.LogicGateData;
import net.logicim.data.port.MultiPortData;
import net.logicim.data.wire.TraceLoader;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.propagation.FamilyStore;
import net.logicim.domain.common.state.State;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.component.integratedcircuit.standard.logic.and.NandGateView;
import net.logicim.ui.simulation.component.integratedcircuit.standard.logic.common.LogicGateProperties;

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
                      String family,
                      List<IntegratedCircuitEventData<?>> events,
                      List<MultiPortData> ports,
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
  public NandGateView create(SubcircuitView subcircuitView, Circuit circuit, TraceLoader traceLoader)
  {
    return new NandGateView(subcircuitView,
                            circuit,
                            position,
                            rotation,
                            new LogicGateProperties(name,
                                                    FamilyStore.getInstance().get(family),
                                                    explicitPowerPorts,
                                                    inputCount,
                                                    inputWidth));
  }
}

