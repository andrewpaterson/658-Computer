package net.logicim.ui.integratedcircuit.standard.logic.xor;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.logic.xor.XnorGateData;
import net.logicim.domain.common.propagation.VoltageConfiguration;
import net.logicim.domain.integratedcircuit.standard.logic.xor.XnorGate;
import net.logicim.domain.integratedcircuit.standard.logic.xor.XorGatePins;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;

import static net.logicim.domain.common.LongTime.nanosecondsToTime;

public class XnorGateView
    extends BaseXorGateView<XnorGate>
{
  public XnorGateView(CircuitEditor circuitEditor,
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
    createPorts(true, 1);
    finaliseView();
  }

  @Override
  protected XnorGate createIntegratedCircuit()
  {
    return new XnorGate(circuitEditor.getCircuit(), "", new XorGatePins(inputCount, new VoltageConfiguration("",
                                                                                                             3.3f, 0.8f,
                                                                                                             2.0f,
                                                                                                             0.0f,
                                                                                                             3.3f,
                                                                                                             nanosecondsToTime(2.5f),
                                                                                                             nanosecondsToTime(2.5f))));
  }

  @Override
  public XnorGateData save()
  {
    return new XnorGateData(position,
                            rotation,
                            name,
                            saveEvents(),
                            savePorts(),
                            saveState(),
                            inputCount);
  }
}

