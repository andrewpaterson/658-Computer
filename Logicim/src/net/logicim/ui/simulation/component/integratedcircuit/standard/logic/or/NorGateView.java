package net.logicim.ui.simulation.component.integratedcircuit.standard.logic.or;

import net.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.logic.common.LogicGateProperties;
import net.logicim.data.integratedcircuit.standard.logic.or.NorGateData;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.integratedcircuit.standard.logic.or.NorGate;
import net.logicim.domain.integratedcircuit.standard.logic.or.OrGatePins;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;

public class NorGateView
    extends BaseOrGateView<NorGate>
{
  public NorGateView(SubcircuitView subcircuitView,
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
    createPortViews(true, 0);
  }

  @Override
  protected NorGate createIntegratedCircuit(SubcircuitSimulation subcircuitSimulation, FamilyVoltageConfiguration familyVoltageConfiguration)
  {
    return new NorGate(subcircuitSimulation.getCircuit(),
                       properties.name,
                       new OrGatePins(properties.inputWidth * properties.inputCount,
                                      familyVoltageConfiguration));
  }

  @Override
  public NorGateData save(boolean selected)
  {
    return new NorGateData(position,
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
    return "NOR Gate";
  }
}

