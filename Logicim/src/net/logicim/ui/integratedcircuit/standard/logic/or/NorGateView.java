package net.logicim.ui.integratedcircuit.standard.logic.or;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.logic.or.NorGateData;
import net.logicim.domain.common.propagation.Family;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.integratedcircuit.standard.logic.or.NorGate;
import net.logicim.domain.integratedcircuit.standard.logic.or.OrGatePins;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;

public class NorGateView
    extends BaseOrGateView<NorGate>
{
  public NorGateView(CircuitEditor circuitEditor,
                     int inputCount,
                     Int2D position,
                     Rotation rotation,
                     String name,
                     Family family,
                     boolean explicitPowerPorts)
  {
    super(circuitEditor,
          inputCount,
          position,
          rotation,
          name,
          family,
          explicitPowerPorts);
    finaliseView();
  }

  @Override
  protected void createPortViews()
  {
    super.createPortViews();
    createPortViews(true, 0);
  }

  @Override
  protected NorGate createIntegratedCircuit(FamilyVoltageConfiguration familyVoltageConfiguration)
  {
    return new NorGate(circuitEditor.getCircuit(), name, new OrGatePins(inputCount, familyVoltageConfiguration));
  }

  @Override
  public NorGateData save()
  {
    return new NorGateData(position,
                           rotation,
                           name,
                           family.getFamily(),
                           saveEvents(),
                           savePorts(),
                           saveState(),
                           inputCount,
                           explicitPowerPorts);
  }

  @Override
  public String getType()
  {
    return "NOR Gate";
  }
}

