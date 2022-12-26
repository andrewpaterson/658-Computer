package net.logicim.ui.integratedcircuit.standard.logic.xor;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.logic.xor.XnorGateData;
import net.logicim.domain.common.propagation.Family;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.integratedcircuit.standard.logic.xor.XnorGate;
import net.logicim.domain.integratedcircuit.standard.logic.xor.XorGatePins;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;

public class XnorGateView
    extends BaseXorGateView<XnorGate>
{
  public XnorGateView(CircuitEditor circuitEditor,
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
    createPortViews(true, 1);
  }

  @Override
  protected XnorGate createIntegratedCircuit(FamilyVoltageConfiguration familyVoltageConfiguration)
  {
    return new XnorGate(circuitEditor.getCircuit(),
                        properties.name,
                        new XorGatePins(properties.inputCount, familyVoltageConfiguration));
  }

  @Override
  public XnorGateData save()
  {
    return new XnorGateData(position,
                            rotation,
                            properties.name,
                            properties.family.getFamily(),
                            saveEvents(),
                            savePorts(),
                            saveState(),
                            properties.inputCount,
                            properties.explicitPowerPorts);
  }

  @Override
  public String getType()
  {
    return "XNOR Gate";
  }
}

