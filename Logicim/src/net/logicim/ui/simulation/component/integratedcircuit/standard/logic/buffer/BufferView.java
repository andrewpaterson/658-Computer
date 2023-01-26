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
  public void propertyChanged()
  {
    properties.bufferCount = PropertyClamp.clamp(properties.bufferCount, 1, PropertyClamp.MAX);
  }

  @Override
  public String getType()
  {
    return "Buffer";
  }
}

