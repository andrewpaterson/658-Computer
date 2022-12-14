package net.logicim.ui.integratedcircuit.standard.logic.and;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.logic.and.NandGateData;
import net.logicim.domain.common.propagation.Family;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.common.propagation.VoltageConfiguration;
import net.logicim.domain.integratedcircuit.standard.logic.and.AndGatePins;
import net.logicim.domain.integratedcircuit.standard.logic.and.NandGate;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;

import static net.logicim.domain.common.LongTime.nanosecondsToTime;

public class NandGateView
    extends BaseAndGateView<NandGate>
{
  public NandGateView(CircuitEditor circuitEditor,
                      int inputCount,
                      Int2D position,
                      Rotation rotation,
                      String name,
                      Family family)
  {
    super(circuitEditor,
          inputCount,
          position,
          rotation,
          name,
          family);
    createPorts(true, 0);
    finaliseView();
  }

  @Override
  protected NandGate createIntegratedCircuit(FamilyVoltageConfiguration familyVoltageConfiguration)
  {
    return new NandGate(circuitEditor.getCircuit(), "", new AndGatePins(inputCount, familyVoltageConfiguration));
  }

  @Override
  public NandGateData save()
  {
    return new NandGateData(position,
                            rotation,
                            name,
                            family.getFamily(),
                            saveEvents(),
                            savePorts(),
                            saveState(),
                            inputCount);
  }
}

