package net.logicim.data.integratedcircuit.standard.logic.xor;

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
import net.logicim.ui.integratedcircuit.standard.logic.xor.XnorGateView;

import java.util.List;

public class XnorGateData
    extends LogicGateData<XnorGateView>
{
  public XnorGateData()
  {
  }

  public XnorGateData(Int2D position,
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
  public XnorGateView create(CircuitEditor circuitEditor, TraceLoader traceLoader)
  {
    return new XnorGateView(circuitEditor,
                            position,
                            rotation,
                            new LogicGateProperties(name,
                                                    FamilyStore.getInstance().get(family),
                                                    explicitPowerPorts,
                                                    inputCount));
  }
}

