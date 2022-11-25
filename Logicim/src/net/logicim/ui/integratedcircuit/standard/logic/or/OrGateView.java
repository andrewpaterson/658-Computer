package net.logicim.ui.integratedcircuit.standard.logic.or;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.logic.or.OrGateData;
import net.logicim.domain.common.propagation.VoltageConfiguration;
import net.logicim.domain.integratedcircuit.standard.logic.or.OrGate;
import net.logicim.domain.integratedcircuit.standard.logic.or.OrGatePins;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;

import static net.logicim.domain.common.LongTime.nanosecondsToTime;

public class OrGateView
    extends BaseOrGateView<OrGate>
{
  public OrGateView(CircuitEditor circuitEditor,
                    int inputCount,
                    Int2D position,
                    Rotation rotation)
  {
    super(circuitEditor,
          inputCount,
          position,
          rotation);
    createPorts(false, 0);
    finaliseView();
  }

  @Override
  protected OrGate createIntegratedCircuit()
  {
    return new OrGate(circuitEditor.getCircuit(), "", new OrGatePins(inputCount, new VoltageConfiguration("",
                                                                                                          0.8f,
                                                                                                          2.0f,
                                                                                                          0.0f,
                                                                                                          3.3f,
                                                                                                          nanosecondsToTime(2.5f),
                                                                                                          nanosecondsToTime(2.5f))));
  }

  @Override
  public OrGateData save()
  {
    return new OrGateData(position,
                          rotation,
                          saveEvents(),
                          savePorts(),
                          inputCount);
  }
}

