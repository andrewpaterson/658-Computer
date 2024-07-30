package net.logicim.ui.simulation.component.integratedcircuit.wdc;

import net.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.logic.wdc.W65C816Data;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816;
import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816Pins;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.component.integratedcircuit.standard.common.StandardIntegratedCircuitView;

public class W65C816View
    extends StandardIntegratedCircuitView<W65C816, W65C816Properties>
{
  public W65C816View(SubcircuitView subcircuitView,
                     Int2D position,
                     Rotation rotation,
                     W65C816Properties properties)
  {
    super(subcircuitView, position, rotation, properties);
    createGraphics();
    finaliseView();
  }

  private void createGraphics()
  {

  }

  @Override
  public void clampProperties(W65C816Properties newProperties)
  {

  }

  @Override
  protected void createPortViews()
  {
  }

  @Override
  public String getType()
  {
    return "W65C816 Microprocessor";
  }

  @Override
  protected W65C816 createIntegratedCircuit(SubcircuitSimulation subcircuitSimulation, FamilyVoltageConfiguration familyVoltageConfiguration)
  {
    return new W65C816(subcircuitSimulation.getCircuit(),
                       properties.name,
                       new W65C816Pins(familyVoltageConfiguration));
  }

  @Override
  public W65C816Data save(boolean selected)
  {
    return new W65C816Data(position,
                           rotation,
                           properties.name,
                           properties.family,
                           saveEvents(),
                           savePorts(),
                           id,
                           enabled,
                           selected,
                           saveSimulationState(),
                           properties.explicitPowerPorts);
  }
}

