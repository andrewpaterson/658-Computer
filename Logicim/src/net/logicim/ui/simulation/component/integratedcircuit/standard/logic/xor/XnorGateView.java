package net.logicim.ui.simulation.component.integratedcircuit.standard.logic.xor;

import net.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.logic.common.LogicGateProperties;
import net.logicim.data.integratedcircuit.standard.logic.xor.XnorGateData;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.integratedcircuit.standard.logic.xor.XnorGate;
import net.logicim.domain.integratedcircuit.standard.logic.xor.XorGatePins;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;

public class XnorGateView
    extends BaseXorGateView<XnorGate>
{
  public XnorGateView(SubcircuitView subcircuitView,
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
    createPortViews(true, 1);
  }

  @Override
  protected XnorGate createIntegratedCircuit(SubcircuitSimulation subcircuitSimulation, FamilyVoltageConfiguration familyVoltageConfiguration)
  {
    return new XnorGate(subcircuitSimulation.getCircuit(),
                        properties.name,
                        new XorGatePins(properties.inputWidth * properties.inputCount,
                                        familyVoltageConfiguration));
  }

  @Override
  public XnorGateData save(boolean selected)
  {
    return new XnorGateData(position,
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
    return "XNOR Gate";
  }
}

