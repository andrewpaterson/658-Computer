package net.logicim.ui.simulation.component.integratedcircuit.standard.logic.buffer;

import net.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.logic.buffer.BufferProperties;
import net.logicim.data.integratedcircuit.standard.logic.buffer.InverterData;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.integratedcircuit.standard.logic.buffer.BufferPins;
import net.logicim.domain.integratedcircuit.standard.logic.buffer.Inverter;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.PropertyClamp;

public class InverterView
    extends BaseBufferView<Inverter>
{
  public InverterView(SubcircuitView subcircuitView,
                      Int2D position,
                      Rotation rotation,
                      BufferProperties properties)
  {
    super(subcircuitView, position, rotation, properties);
    finaliseView();
  }

  @Override
  protected void createPortViews()
  {
    super.createPortViews();
    createPortViews(true);
  }

  @Override
  protected Inverter createIntegratedCircuit(SubcircuitSimulation subcircuitSimulation, FamilyVoltageConfiguration familyVoltageConfiguration)
  {
    return new Inverter(subcircuitSimulation.getCircuit(),
                        properties.name,
                        new BufferPins(properties.inputWidth * properties.inputCount,
                                       familyVoltageConfiguration));
  }

  @Override
  public InverterData save(boolean selected)
  {
    return new InverterData(position,
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
    return "Inverter";
  }

  @Override
  public void clampProperties(BufferProperties newProperties)
  {
    newProperties.inputWidth = PropertyClamp.clamp(newProperties.inputWidth, 1, PropertyClamp.MAX_WIDTH);
    newProperties.inputCount = PropertyClamp.clamp(newProperties.inputCount, 1, PropertyClamp.MAX_WIDTH);
  }
}

