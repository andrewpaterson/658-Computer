package net.logicim.ui.integratedcircuit.standard.logic.xor;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.logic.xor.XorGateData;
import net.logicim.domain.common.propagation.Family;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.integratedcircuit.standard.logic.xor.XorGate;
import net.logicim.domain.integratedcircuit.standard.logic.xor.XorGatePins;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;

public class XorGateView
    extends BaseXorGateView<XorGate>
{
  public XorGateView(CircuitEditor circuitEditor,
                     int inputCount,
                     Int2D position,
                     Rotation rotation,
                     String name,
                     Family family,
                     boolean explicitPowerPorts)
  {
    super(circuitEditor,
          inputCount,
          position,
          rotation,
          name,
          family,
          explicitPowerPorts);
    finaliseView();
  }

  @Override
  protected void createPortViews()
  {
    super.createPortViews();
    createPortViews(false, 1);
  }

  @Override
  protected XorGate createIntegratedCircuit(FamilyVoltageConfiguration familyVoltageConfiguration)
  {
    return new XorGate(circuitEditor.getCircuit(), name, new XorGatePins(inputCount, familyVoltageConfiguration));
  }

  @Override
  public XorGateData save()
  {
    return new XorGateData(position,
                           rotation,
                           name,
                           family.getFamily(),
                           saveEvents(),
                           savePorts(),
                           saveState(),
                           inputCount,
                           explicitPowerPorts);
  }

  @Override
  public String getType()
  {
    return "XOR Gate";
  }
}

