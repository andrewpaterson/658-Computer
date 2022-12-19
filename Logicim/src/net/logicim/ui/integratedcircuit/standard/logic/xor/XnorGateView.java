package net.logicim.ui.integratedcircuit.standard.logic.xor;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.logic.xor.XnorGateData;
import net.logicim.domain.common.propagation.Family;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
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
                      String name,
                      Family family)
  {
    super(circuitEditor,
          inputCount,
          position,
          rotation,
          name,
          family);
    finaliseView();
  }

  @Override
  protected void createPorts()
  {
    createPorts(true, 1);
  }


  @Override
  protected XnorGate createIntegratedCircuit(FamilyVoltageConfiguration familyVoltageConfiguration)
  {
    return new XnorGate(circuitEditor.getCircuit(), "", new XorGatePins(inputCount, familyVoltageConfiguration));
  }

  @Override
  public XnorGateData save()
  {
    return new XnorGateData(position,
                            rotation,
                            name,
                            family.getFamily(),
                            saveEvents(),
                            savePorts(),
                            saveState(),
                            inputCount);
  }
}

