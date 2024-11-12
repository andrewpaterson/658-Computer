package net.logicim.ui.simulation.component.integratedcircuit.standard.logic.and;

import net.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.logic.and.NandGateData;
import net.logicim.data.integratedcircuit.standard.logic.common.LogicGateProperties;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.integratedcircuit.standard.logic.and.AndGatePins;
import net.logicim.domain.integratedcircuit.standard.logic.and.NandGate;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;

public class NandGateView
    extends BaseAndGateView<NandGate>
{
  public NandGateView(SubcircuitView subcircuitView,
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
    createPortViews(true, 0);
  }

  @Override
  protected NandGate createIntegratedCircuit(SubcircuitSimulation subcircuitSimulation, FamilyVoltageConfiguration familyVoltageConfiguration)
  {
    return new NandGate(subcircuitSimulation.getCircuit(),
                        properties.name,
                        new AndGatePins(properties.inputWidth * properties.inputCount,
                                        familyVoltageConfiguration));
  }

  @Override
  public NandGateData save(boolean selected)
  {
    return new NandGateData(position,
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
    return "NAND Gate";
  }
}

