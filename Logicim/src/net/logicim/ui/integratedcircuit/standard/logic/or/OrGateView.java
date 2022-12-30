package net.logicim.ui.integratedcircuit.standard.logic.or;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.logic.or.OrGateData;
import net.logicim.domain.common.propagation.Family;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.integratedcircuit.standard.logic.or.OrGate;
import net.logicim.domain.integratedcircuit.standard.logic.or.OrGatePins;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;

public class OrGateView
    extends BaseOrGateView<OrGate>
{
  public OrGateView(CircuitEditor circuitEditor,
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
    createPortViews(false, 0);
  }

  @Override
  protected OrGate createIntegratedCircuit(FamilyVoltageConfiguration familyVoltageConfiguration)
  {
    return new OrGate(circuitEditor.getCircuit(),
                      properties.name,
                      new OrGatePins(properties.inputCount, familyVoltageConfiguration));
  }

  @Override
  public OrGateData save()
  {
    return new OrGateData(position,
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
    return "OR Gate";
  }
}

