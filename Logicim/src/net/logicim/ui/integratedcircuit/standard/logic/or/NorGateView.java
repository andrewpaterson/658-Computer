package net.logicim.ui.integratedcircuit.standard.logic.or;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.logic.or.NorGateData;
import net.logicim.domain.common.propagation.VoltageConfiguration;
import net.logicim.domain.integratedcircuit.standard.logic.or.NorGate;
import net.logicim.domain.integratedcircuit.standard.logic.or.OrGatePins;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;

import static net.logicim.domain.common.LongTime.nanosecondsToTime;

public class NorGateView
    extends BaseOrGateView<NorGate>
{
  public NorGateView(CircuitEditor circuitEditor,
                     int inputCount,
                     Int2D position,
                     Rotation rotation,
                     String name)
  {
    super(circuitEditor,
          inputCount,
          position,
          rotation,
          name);
    createPorts(true, 0);
    finaliseView();
  }

  @Override
  protected NorGate createIntegratedCircuit()
  {
    return new NorGate(circuitEditor.getCircuit(), "", new OrGatePins(inputCount, new VoltageConfiguration("",
                                                                                                           3.3f, 0.8f,
                                                                                                           2.0f,
                                                                                                           0.0f,
                                                                                                           3.3f,
                                                                                                           nanosecondsToTime(2.5f),
                                                                                                           nanosecondsToTime(2.5f))));
  }

  @Override
  public NorGateData save()
  {
    return new NorGateData(position,
                           rotation,
                           name,
                           saveEvents(),
                           savePorts(),
                           saveState(),
                           inputCount);
  }
}

