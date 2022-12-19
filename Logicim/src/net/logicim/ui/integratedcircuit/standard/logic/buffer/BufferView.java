package net.logicim.ui.integratedcircuit.standard.logic.buffer;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.logic.buffer.BufferData;
import net.logicim.domain.common.propagation.Family;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.common.propagation.VoltageConfiguration;
import net.logicim.domain.integratedcircuit.standard.logic.buffer.Buffer;
import net.logicim.domain.integratedcircuit.standard.logic.buffer.BufferPins;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;

import static net.logicim.domain.common.LongTime.nanosecondsToTime;

public class BufferView
    extends BaseInverterView<Buffer>
{
  public BufferView(CircuitEditor circuitEditor,
                    Int2D position,
                    Rotation rotation,
                    String name,
                    Family family,
                    boolean explicitPowerPorts)
  {
    super(circuitEditor,
          position,
          rotation,
          name,
          family,
          explicitPowerPorts);
    finaliseView();
  }

  @Override
  protected void createPorts()
  {
    super.createPorts();
    createPorts(false);
  }


  @Override
  protected Buffer createIntegratedCircuit(FamilyVoltageConfiguration familyVoltageConfiguration)
  {
    return new Buffer(circuitEditor.getCircuit(), "", new BufferPins(familyVoltageConfiguration));
  }

  @Override
  public BufferData save()
  {
    return new BufferData(position,
                          rotation,
                          name,
                          family.getFamily(),
                          saveEvents(),
                          savePorts(),
                          saveState(),
                          explicitPowerPorts);
  }
}

