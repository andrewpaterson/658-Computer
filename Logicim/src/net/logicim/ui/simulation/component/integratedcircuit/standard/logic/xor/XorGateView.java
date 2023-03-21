package net.logicim.ui.simulation.component.integratedcircuit.standard.logic.xor;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.logic.xor.XorGateData;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.integratedcircuit.standard.logic.xor.XorGate;
import net.logicim.domain.integratedcircuit.standard.logic.xor.XorGatePins;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.data.integratedcircuit.standard.logic.common.LogicGateProperties;

public class XorGateView
    extends BaseXorGateView<XorGate>
{
  public XorGateView(SubcircuitView subcircuitView,
                     Circuit circuit,
                     Int2D position,
                     Rotation rotation,
                     LogicGateProperties properties)
  {
    super(subcircuitView, circuit, position, rotation, properties);
    finaliseView(circuit);
  }

  @Override
  protected void createPortViews()
  {
    super.createPortViews();
    createPortViews(false, 1);
  }

  @Override
  protected XorGate createIntegratedCircuit(Circuit circuit, FamilyVoltageConfiguration familyVoltageConfiguration)
  {
    return new XorGate(circuit,
                       properties.name,
                       new XorGatePins(properties.inputWidth * properties.inputCount,
                                       familyVoltageConfiguration));
  }

  @Override
  public XorGateData save(boolean selected)
  {
    return new XorGateData(position,
                           rotation,
                           properties.name,
                           properties.family,
                           saveEvents(),
                           savePorts(),
                           selected,
                           saveState(),
                           properties.inputCount,
                           properties.inputWidth,
                           properties.explicitPowerPorts);
  }

  @Override
  public String getType()
  {
    return "XOR Gate";
  }
}

