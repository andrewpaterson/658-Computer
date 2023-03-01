package net.logicim.ui.simulation.component.integratedcircuit.standard.logic.buffer;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.logic.buffer.InverterData;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.integratedcircuit.standard.logic.buffer.BufferPins;
import net.logicim.domain.integratedcircuit.standard.logic.buffer.Inverter;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.PropertyClamp;

public class InverterView
    extends BaseBufferView<Inverter>
{
  public InverterView(SubcircuitView subcircuitView,
                      Circuit circuit,
                      Int2D position,
                      Rotation rotation,
                      BufferProperties properties)
  {
    super(subcircuitView, circuit, position, rotation, properties);
    finaliseView(circuit);
  }

  @Override
  protected void createPortViews()
  {
    super.createPortViews();
    createPortViews(true);
  }

  @Override
  protected Inverter createIntegratedCircuit(Circuit circuit, FamilyVoltageConfiguration familyVoltageConfiguration)
  {
    return new Inverter(circuit,
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
                            properties.family.getFamily(),
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
    return "Inverter";
  }

  @Override
  public void clampProperties(BufferProperties newProperties)
  {
    newProperties.inputWidth = PropertyClamp.clamp(newProperties.inputWidth, 1, PropertyClamp.MAX);
    newProperties.inputCount = PropertyClamp.clamp(newProperties.inputCount, 1, PropertyClamp.MAX);
  }
}

