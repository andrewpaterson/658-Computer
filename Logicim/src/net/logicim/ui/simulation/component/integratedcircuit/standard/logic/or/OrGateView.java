package net.logicim.ui.simulation.component.integratedcircuit.standard.logic.or;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.logic.common.LogicGateProperties;
import net.logicim.data.integratedcircuit.standard.logic.or.OrGateData;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.integratedcircuit.standard.logic.or.OrGate;
import net.logicim.domain.integratedcircuit.standard.logic.or.OrGatePins;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;

public class OrGateView
    extends BaseOrGateView<OrGate>
{
  public OrGateView(SubcircuitView subcircuitView,
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
  protected OrGate createIntegratedCircuit(SubcircuitSimulation subcircuitSimulation, FamilyVoltageConfiguration familyVoltageConfiguration)
  {
    return new OrGate(subcircuitSimulation.getCircuit(),
                      properties.name,
                      new OrGatePins(properties.inputWidth * properties.inputCount,
                                     familyVoltageConfiguration));
  }

  @Override
  public OrGateData save(boolean selected)
  {
    return new OrGateData(position,
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
    return "OR Gate";
  }
}

