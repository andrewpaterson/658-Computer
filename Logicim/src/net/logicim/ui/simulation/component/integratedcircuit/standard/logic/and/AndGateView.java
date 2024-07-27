package net.logicim.ui.simulation.component.integratedcircuit.standard.logic.and;

import net.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.logic.and.AndGateData;
import net.logicim.data.integratedcircuit.standard.logic.common.LogicGateProperties;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.integratedcircuit.standard.logic.and.AndGate;
import net.logicim.domain.integratedcircuit.standard.logic.and.AndGatePins;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;

public class AndGateView
    extends BaseAndGateView<AndGate>
{
  public AndGateView(SubcircuitView subcircuitView,
                     Int2D position,
                     Rotation rotation,
                     LogicGateProperties properties)
  {
    super(subcircuitView, position, rotation, properties);
    finaliseView();
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
  protected AndGate createIntegratedCircuit(SubcircuitSimulation subcircuitSimulation, FamilyVoltageConfiguration familyVoltageConfiguration)
  {
    return new AndGate(subcircuitSimulation.getCircuit(),
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
}

