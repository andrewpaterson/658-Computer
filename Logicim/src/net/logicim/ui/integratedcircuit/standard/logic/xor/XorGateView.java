package net.logicim.ui.integratedcircuit.standard.logic.xor;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.logic.xor.XorGateData;
import net.logicim.domain.common.propagation.VoltageConfiguration;
import net.logicim.domain.integratedcircuit.standard.logic.xor.XorGate;
import net.logicim.domain.integratedcircuit.standard.logic.xor.XorGatePins;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;

import static net.logicim.domain.common.LongTime.nanosecondsToTime;

public class XorGateView
    extends BaseXorGateView<XorGate>
{
  public XorGateView(CircuitEditor circuitEditor,
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
    createPorts(false, 1);
    finaliseView();
  }

  @Override
  protected XorGate createIntegratedCircuit()
  {
    return new XorGate(circuitEditor.getCircuit(), "", new XorGatePins(inputCount, new VoltageConfiguration("",
                                                                                                            0.8f,
                                                                                                            2.0f,
                                                                                                            0.0f,
                                                                                                            3.3f,
                                                                                                            nanosecondsToTime(2.5f),
                                                                                                            nanosecondsToTime(2.5f))));
  }

  @Override
  public XorGateData save()
  {
    return new XorGateData(position,
                           rotation,
                           name,
                           saveEvents(),
                           savePorts(),
                           inputCount);
  }
}

