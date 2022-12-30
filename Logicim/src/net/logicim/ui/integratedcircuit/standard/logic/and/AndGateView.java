package net.logicim.ui.integratedcircuit.standard.logic.and;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.logic.and.AndGateData;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.integratedcircuit.standard.logic.and.AndGate;
import net.logicim.domain.integratedcircuit.standard.logic.and.AndGatePins;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.integratedcircuit.standard.logic.common.LogicGateProperties;

public class AndGateView
    extends BaseAndGateView<AndGate>
{
  public AndGateView(CircuitEditor circuitEditor,
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
    createPortViews(false, 0);
  }

  @Override
  public String getType()
  {
    return "AND Gate";
  }

  @Override
  protected AndGate createIntegratedCircuit(FamilyVoltageConfiguration familyVoltageConfiguration)
  {
    return new AndGate(circuitEditor.getCircuit(),
                       properties.name,
                       new AndGatePins(properties.inputCount, familyVoltageConfiguration));
  }

  @Override
  public AndGateData save()
  {
    return new AndGateData(position,
                           rotation,
                           properties.name,
                           properties.family.getFamily(),
                           saveEvents(),
                           savePorts(),
                           saveState(),
                           properties.inputCount,
                           properties.explicitPowerPorts);
  }
}

