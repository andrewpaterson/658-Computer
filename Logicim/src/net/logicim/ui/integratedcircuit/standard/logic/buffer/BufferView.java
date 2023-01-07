package net.logicim.ui.integratedcircuit.standard.logic.buffer;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.logic.buffer.BufferData;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.integratedcircuit.standard.logic.buffer.Buffer;
import net.logicim.domain.integratedcircuit.standard.logic.buffer.BufferPins;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;

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
                      new BufferPins(properties.bufferCount,
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
                          properties.bufferCount,
                          properties.explicitPowerPorts);
  }

  @Override
  public String getType()
  {
    return "Buffer";
  }
}

