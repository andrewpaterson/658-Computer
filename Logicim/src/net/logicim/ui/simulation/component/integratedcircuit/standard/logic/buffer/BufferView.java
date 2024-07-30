package net.logicim.ui.simulation.component.integratedcircuit.standard.logic.buffer;

import net.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.logic.buffer.BufferData;
import net.logicim.data.integratedcircuit.standard.logic.buffer.BufferProperties;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.integratedcircuit.standard.logic.buffer.Buffer;
import net.logicim.domain.integratedcircuit.standard.logic.buffer.BufferPins;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.PropertyClamp;

public class BufferView
    extends BaseBufferView<Buffer>
{
  public BufferView(SubcircuitView subcircuitView,
                    Int2D position,
                    Rotation rotation,
                    BufferProperties properties)
  {
    super(subcircuitView, position, rotation, properties);
    createPortViews();
    finaliseView();
  }

  @Override
  protected void createPortViews()
  {
    super.createPortViews();
    createPortViews(false);
  }

  @Override
  protected Buffer createIntegratedCircuit(SubcircuitSimulation subcircuitSimulation, FamilyVoltageConfiguration familyVoltageConfiguration)
  {
    return new Buffer(subcircuitSimulation.getCircuit(),
                      properties.name,
                      new BufferPins(properties.inputWidth * properties.inputCount,
                                     familyVoltageConfiguration));
  }

  @Override
  public BufferData save(boolean selected)
  {
    return new BufferData(position,
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
  public void clampProperties(BufferProperties newProperties)
  {
    newProperties.inputCount = PropertyClamp.clamp(newProperties.inputCount, 1, PropertyClamp.MAX_WIDTH);
    newProperties.inputWidth = PropertyClamp.clamp(newProperties.inputWidth, 1, PropertyClamp.MAX_WIDTH);
  }

  @Override
  public String getType()
  {
    return "Buffer";
  }
}

