package net.logicim.ui.integratedcircuit.standard.logic.xor;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.logic.xor.XnorGateData;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.integratedcircuit.standard.logic.xor.XnorGate;
import net.logicim.domain.integratedcircuit.standard.logic.xor.XorGatePins;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.integratedcircuit.standard.logic.common.LogicGateProperties;

public class XnorGateView
    extends BaseXorGateView<XnorGate>
{
  public XnorGateView(CircuitEditor circuitEditor,
                      Int2D position,
                      Rotation rotation,
                      LogicGateProperties properties)
  {
    super(circuitEditor, position, rotation, properties);
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
  public XnorGateData save(boolean selected)
  {
    return new XnorGateData(position,
                            rotation,
                            properties.name,
                            properties.family.getFamily(),
                            saveEvents(),
                            savePorts(),
                            selected,
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

