package net.logicim.ui.integratedcircuit.standard.bus;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.DiscreteData;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Discrete;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.DiscreteView;

public class SplitterView
    extends DiscreteView<SplitterProperties>
{
  public SplitterView(CircuitEditor circuitEditor,
                      Int2D position,
                      Rotation rotation,
                      SplitterProperties properties)
  {
    super(circuitEditor, position, rotation, properties);
  }

  @Override
  public boolean isEnabled()
  {
    return true;
  }

  @Override
  public void simulationStarted(Simulation simulation)
  {

  }

  @Override
  protected void createPortViews()
  {

  }

  @Override
  public String getType()
  {
    return null;
  }

  @Override
  public DiscreteData save(boolean selected)
  {
    return null;
  }

  @Override
  protected SplitterProperties createProperties()
  {
    return null;
  }

  @Override
  public Discrete getDiscrete()
  {
    return null;
  }

  @Override
  public void clampProperties()
  {

  }

  @Override
  public ConnectionView getConnectionsInGrid(Int2D p)
  {
    return null;
  }

  @Override
  public String getDescription()
  {
    return null;
  }

  @Override
  public void enable(Simulation simulation)
  {

  }

  @Override
  public void disable()
  {

  }
}
