package net.logicim.ui.integratedcircuit.standard.logic.buffer;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.logic.buffer.InverterData;
import net.logicim.domain.common.propagation.Family;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.common.propagation.VoltageConfiguration;
import net.logicim.domain.integratedcircuit.standard.logic.buffer.BufferPins;
import net.logicim.domain.integratedcircuit.standard.logic.buffer.Inverter;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;

import static net.logicim.domain.common.LongTime.nanosecondsToTime;

public class InverterView
    extends BaseInverterView<Inverter>
{
  public InverterView(CircuitEditor circuitEditor,
                      Int2D position,
                      Rotation rotation,
                      String name,
                      Family family)
  {
    super(circuitEditor,
          position,
          rotation,
          name,
          family);
    createPorts(true);
    finaliseView();
  }

  @Override
  protected Inverter createIntegratedCircuit(FamilyVoltageConfiguration familyVoltageConfiguration)
  {
    return new Inverter(circuitEditor.getCircuit(), "", new BufferPins(familyVoltageConfiguration));
  }

  @Override
  public InverterData save()
  {
    return new InverterData(position,
                            rotation,
                            name,
                            family.getFamily(),
                            saveEvents(),
                            savePorts(),
                            saveState());
  }
}

