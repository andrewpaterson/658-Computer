package net.logicim.ui.simulation.component.integratedcircuit.standard.logic.xor;

import net.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.logic.common.LogicGateProperties;
import net.logicim.data.integratedcircuit.standard.logic.xor.XorGateData;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.integratedcircuit.standard.logic.xor.XorGate;
import net.logicim.domain.integratedcircuit.standard.logic.xor.XorGatePins;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.circuit.path.ViewPath;
import net.logicim.ui.common.Rotation;

public class XorGateView
    extends BaseXorGateView<XorGate>
{
  public XorGateView(SubcircuitView subcircuitView,
                     Int2D position,
                     Rotation rotation,
                     LogicGateProperties properties)
  {
    super(subcircuitView,
          position,
          rotation,
          properties);
  }

  @Override
  protected void createPortViews()
  {
    super.createPortViews();
    createPortViews(false, 1);
  }

  @Override
  protected XorGate createIntegratedCircuit(ViewPath viewPath,
                                            CircuitSimulation circuitSimulation,
                                            FamilyVoltageConfiguration familyVoltageConfiguration)
  {
    SubcircuitSimulation containingSubcircuitSimulation = viewPath.getSubcircuitSimulation(circuitSimulation);
    return new XorGate(containingSubcircuitSimulation,
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
                           id,
                           enabled,
                           selected,
                           saveSimulationState(),
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

