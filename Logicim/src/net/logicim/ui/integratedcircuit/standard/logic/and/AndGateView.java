package net.logicim.ui.integratedcircuit.standard.logic.and;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.logic.and.AndGateData;
import net.logicim.domain.common.propagation.VoltageConfiguration;
import net.logicim.domain.integratedcircuit.standard.logic.and.AndGate;
import net.logicim.domain.integratedcircuit.standard.logic.and.AndGatePins;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;

import static net.logicim.domain.common.LongTime.nanosecondsToTime;

public class AndGateView
    extends BaseAndGateView<AndGate>
{
  public AndGateView(CircuitEditor circuitEditor,
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
  protected AndGate createIntegratedCircuit()
  {
    return new AndGate(circuitEditor.getCircuit(), "", new AndGatePins(inputCount, new VoltageConfiguration("",
                                                                                                            0.8f,
                                                                                                            2.0f,
                                                                                                            0.0f,
                                                                                                            3.3f,
                                                                                                            nanosecondsToTime(2.5f),
                                                                                                            nanosecondsToTime(2.5f))));
  }

  @Override
  public AndGateData save()
  {
    return new AndGateData(toInt2DData(position),
                           toRotationData(rotation),
                           saveEvents(),
                           savePorts(),
                           inputCount);
  }
}

