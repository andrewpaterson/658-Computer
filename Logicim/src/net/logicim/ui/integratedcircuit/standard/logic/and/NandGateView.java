package net.logicim.ui.integratedcircuit.standard.logic.and;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.logic.and.NandGateData;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.integratedcircuit.standard.logic.and.AndGatePins;
import net.logicim.domain.integratedcircuit.standard.logic.and.NandGate;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.integratedcircuit.standard.logic.common.LogicGateProperties;

public class NandGateView
    extends BaseAndGateView<NandGate>
{
  public NandGateView(CircuitEditor circuitEditor,
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
  protected NandGate createIntegratedCircuit(FamilyVoltageConfiguration familyVoltageConfiguration)
  {
    return new NandGate(circuitEditor.getCircuit(),
                        properties.name,
                        new AndGatePins(properties.inputCount, familyVoltageConfiguration));
  }

  @Override
  public NandGateData save(boolean selected)
  {
    return new NandGateData(position,
                            rotation,
                            properties.name,
                            properties.family.getFamily(),
                            saveEvents(),
                            savePorts(),
                            selected,
                            saveState(),
                            properties.inputCount,
                            properties.explicitPowerPorts);
  }

  @Override
  public String getType()
  {
    return "NAND Gate";
  }
}

