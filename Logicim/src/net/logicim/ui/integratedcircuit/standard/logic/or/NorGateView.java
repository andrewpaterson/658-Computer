package net.logicim.ui.integratedcircuit.standard.logic.or;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.logic.or.NorGateData;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.integratedcircuit.standard.logic.or.NorGate;
import net.logicim.domain.integratedcircuit.standard.logic.or.OrGatePins;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.integratedcircuit.standard.logic.common.LogicGateProperties;

public class NorGateView
    extends BaseOrGateView<NorGate>
{
  public NorGateView(CircuitEditor circuitEditor,
                     Int2D position,
                     Rotation rotation,
                     LogicGateProperties properties)
  {
    super(circuitEditor, position, rotation, properties);
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
    return new NorGate(circuitEditor.getCircuit(),
                       properties.name,
                       new OrGatePins(properties.inputCount, familyVoltageConfiguration));
  }

  @Override
  public NorGateData save()
  {
    return new NorGateData(position,
                           rotation,
                           properties.name,
                           properties.family.getFamily(),
                           saveEvents(),
                           savePorts(),
                           saveState(),
                           properties.inputCount,
                           properties.explicitPowerPorts);
  }

  @Override
  public String getType()
  {
    return "NOR Gate";
  }
}

