package net.logicim.ui.simulation.component.integratedcircuit.standard.logic.or;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.logic.or.NorGateData;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.integratedcircuit.standard.logic.or.NorGate;
import net.logicim.domain.integratedcircuit.standard.logic.or.OrGatePins;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.data.integratedcircuit.standard.logic.common.LogicGateProperties;

public class NorGateView
    extends BaseOrGateView<NorGate>
{
  public NorGateView(SubcircuitView subcircuitView,
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
    createPortViews(true, 0);
  }

  @Override
  protected NorGate createIntegratedCircuit(Circuit circuit, FamilyVoltageConfiguration familyVoltageConfiguration)
  {
    return new NorGate(circuit,
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
                           selected,
                           saveState(),
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

