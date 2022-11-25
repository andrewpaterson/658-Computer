package net.logicim.ui.integratedcircuit.standard.logic.and;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.logic.and.NandGateData;
import net.logicim.domain.common.propagation.VoltageConfiguration;
import net.logicim.domain.integratedcircuit.standard.logic.and.AndGatePins;
import net.logicim.domain.integratedcircuit.standard.logic.and.NandGate;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;

import static net.logicim.domain.common.LongTime.nanosecondsToTime;

public class NandGateView
    extends BaseAndGateView<NandGate>
{
  public NandGateView(CircuitEditor circuitEditor,
                      int inputCount,
                      Int2D position,
                      Rotation rotation)
  {
    super(circuitEditor,
          inputCount,
          position,
          rotation);
    createPorts(true, 0);
    finaliseView();
  }

  @Override
  protected NandGate createIntegratedCircuit()
  {
    return new NandGate(circuitEditor.getCircuit(), "", new AndGatePins(inputCount, new VoltageConfiguration("",
                                                                                                             0.8f,
                                                                                                             2.0f,
                                                                                                             0.0f,
                                                                                                             3.3f,
                                                                                                             nanosecondsToTime(2.5f),
                                                                                                             nanosecondsToTime(2.5f))));
  }

  @Override
  public NandGateData save()
  {
    return new NandGateData(position,
                            rotation,
                            saveEvents(),
                            savePorts(),
                            inputCount);
  }
}

