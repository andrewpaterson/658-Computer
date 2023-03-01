package net.logicim.ui.simulation.component.integratedcircuit.standard.logic.and;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.logic.and.AndGateData;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.integratedcircuit.standard.logic.and.AndGate;
import net.logicim.domain.integratedcircuit.standard.logic.and.AndGatePins;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.component.integratedcircuit.standard.logic.common.LogicGateProperties;

public class AndGateView
    extends BaseAndGateView<AndGate>
{
  public AndGateView(SubcircuitView subcircuitView,
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
    createPortViews(false, 0);
  }

  @Override
  public String getType()
  {
    return "AND Gate";
  }

  @Override
  protected AndGate createIntegratedCircuit(Circuit circuit, FamilyVoltageConfiguration familyVoltageConfiguration)
  {
    return new AndGate(circuit,
                       properties.name,
                       new AndGatePins(properties.inputWidth * properties.inputCount,
                                       familyVoltageConfiguration));
  }

  @Override
  public AndGateData save(boolean selected)
  {
    return new AndGateData(position,
                           rotation,
                           properties.name,
                           properties.family.getFamily(),
                           saveEvents(),
                           savePorts(),
                           selected,
                           saveState(),
                           properties.inputCount,
                           properties.inputWidth,
                           properties.explicitPowerPorts);
  }
}

