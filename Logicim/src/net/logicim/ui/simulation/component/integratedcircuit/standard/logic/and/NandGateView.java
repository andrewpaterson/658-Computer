package net.logicim.ui.simulation.component.integratedcircuit.standard.logic.and;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.logic.and.NandGateData;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.integratedcircuit.standard.logic.and.AndGatePins;
import net.logicim.domain.integratedcircuit.standard.logic.and.NandGate;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.data.integratedcircuit.standard.logic.common.LogicGateProperties;

public class NandGateView
    extends BaseAndGateView<NandGate>
{
  public NandGateView(SubcircuitView subcircuitView,
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
  protected NandGate createIntegratedCircuit(Circuit circuit, FamilyVoltageConfiguration familyVoltageConfiguration)
  {
    return new NandGate(circuit,
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
                            selected,
                            saveState(),
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

