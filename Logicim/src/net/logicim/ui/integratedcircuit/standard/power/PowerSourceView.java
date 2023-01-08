package net.logicim.ui.integratedcircuit.standard.power;

import net.logicim.common.type.Int2D;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Component;
import net.logicim.domain.passive.power.PowerSource;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.ComponentProperties;
import net.logicim.ui.common.integratedcircuit.PassiveView;

public abstract class PowerSourceView<PROPERTIES extends ComponentProperties>
    extends PassiveView<PROPERTIES>
{
  protected PowerSource powerSource;

  public PowerSourceView(CircuitEditor circuitEditor,
                         Int2D position,
                         Rotation rotation,
                         PROPERTIES properties)
  {
    super(circuitEditor, position, rotation, properties);
    circuitEditor.addPassiveView(this);
  }

  @Override
  protected void createComponent()
  {
    powerSource = new PowerSource(circuitEditor.getCircuit(),
                                  properties.name,
                                  getVoltageOut());
    powerSource.disable();
  }

  @Override
  public void simulationStarted(Simulation simulation)
  {
  }

  @Override
  public Component getComponent()
  {
    return powerSource;
  }

  @Override
  public ConnectionView getConnectionsInGrid(int x, int y)
  {
    //??
    return null;
  }

  public PowerSource getPowerSource()
  {
    return powerSource;
  }

  @Override
  public void clampProperties()
  {
  }

  public abstract float getVoltageOut();
}

