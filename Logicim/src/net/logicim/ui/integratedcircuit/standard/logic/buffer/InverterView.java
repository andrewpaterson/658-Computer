package net.logicim.ui.integratedcircuit.standard.logic.buffer;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.logic.buffer.InverterData;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.integratedcircuit.standard.logic.buffer.BufferPins;
import net.logicim.domain.integratedcircuit.standard.logic.buffer.Inverter;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.PropertyClamp;

public class InverterView
    extends BaseBufferView<Inverter>
{
  public InverterView(CircuitEditor circuitEditor,
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
    createPortViews(true);
  }

  @Override
  protected Inverter createIntegratedCircuit(FamilyVoltageConfiguration familyVoltageConfiguration)
  {
    return new Inverter(circuitEditor.getCircuit(),
                        properties.name,
                        new BufferPins(properties.bufferCount,
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
                            properties.bufferCount,
                            properties.explicitPowerPorts);
  }

  @Override
  public String getType()
  {
    return "Inverter";
  }

  @Override
  public void clampProperties()
  {
    properties.bufferCount = PropertyClamp.clamp(properties.bufferCount, 1, 32);
  }
}

