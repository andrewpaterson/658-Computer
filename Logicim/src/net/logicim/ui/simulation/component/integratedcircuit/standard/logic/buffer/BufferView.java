package net.logicim.ui.simulation.component.integratedcircuit.standard.logic.buffer;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.logic.buffer.BufferData;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.integratedcircuit.standard.logic.buffer.Buffer;
import net.logicim.domain.integratedcircuit.standard.logic.buffer.BufferPins;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.PropertyClamp;
import net.logicim.ui.simulation.CircuitEditor;

public class BufferView
    extends BaseBufferView<Buffer>
{
  public BufferView(CircuitEditor circuitEditor,
                    Int2D position,
                    Rotation rotation,
                    BufferProperties properties)
  {
    super(circuitEditor, position, rotation, properties);
    finaliseView();
  }

  @Override
  protected void createPortViews()
  {
    super.createPortViews();
    createPortViews(false);
  }

  @Override
  protected Buffer createIntegratedCircuit(FamilyVoltageConfiguration familyVoltageConfiguration)
  {
    return new Buffer(circuitEditor.getCircuit(),
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
  public void clampProperties(BufferProperties newProperties)
  {
    newProperties.inputCount = PropertyClamp.clamp(newProperties.inputCount, 1, PropertyClamp.MAX);
    newProperties.inputWidth = PropertyClamp.clamp(newProperties.inputWidth, 1, PropertyClamp.MAX);
  }

  @Override
  public String getType()
  {
    return "Buffer";
  }
}

