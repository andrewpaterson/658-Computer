package net.logicim.data.integratedcircuit.standard.logic.or;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.event.IntegratedCircuitEventData;
import net.logicim.data.integratedcircuit.standard.logic.common.LogicGateData;
import net.logicim.data.port.PortData;
import net.logicim.data.trace.TraceLoader;
import net.logicim.domain.common.propagation.FamilyStore;
import net.logicim.domain.common.state.State;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.integratedcircuit.standard.logic.common.LogicGateProperties;
import net.logicim.ui.integratedcircuit.standard.logic.or.OrGateView;

import java.util.List;

public class OrGateData
    extends LogicGateData<OrGateView>
{
  public OrGateData()
  {
  }

  public OrGateData(Int2D position,
                    Rotation rotation,
                    String name,
                    String family,
                    List<IntegratedCircuitEventData<?>> events,
                    List<PortData> ports,
                    State state,
                    int inputCount,
                    boolean explicitPowerPorts)
  {
    super(position,
          rotation,
          name,
          family,
          events,
          ports,
          state,
          inputCount,
          explicitPowerPorts);
  }

  @Override
  public OrGateView create(CircuitEditor circuitEditor, TraceLoader traceLoader)
  {
    return new OrGateView(circuitEditor,
                          position,
                          rotation,
                          new LogicGateProperties(name,
                                                  FamilyStore.getInstance().get(family),
                                                  explicitPowerPorts,
                                                  inputCount));
  }
}

