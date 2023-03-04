package net.logicim.ui.simulation.component.decorative.common;

import net.logicim.common.type.Int2D;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.Component;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.StaticView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class DecorativeView<T extends DecorativeProperties>
    extends StaticView<T>
{
  protected boolean enabled;

  public DecorativeView(SubcircuitView subcircuitView,
                        Circuit circuit,
                        Int2D position,
                        Rotation rotation,
                        T properties)
  {
    super(subcircuitView, circuit, position, rotation, properties);
    subcircuitView.addDecorativeView(this);
  }

  @Override
  protected void finaliseView(Circuit circuit)
  {
    finalised = true;
    enabled = false;

    updateBoundingBox();
  }

  @Override
  public boolean isEnabled()
  {
    return enabled;
  }

  @Override
  public void simulationStarted(Simulation simulation)
  {
  }

  @Override
  public List<ConnectionView> getConnections()
  {
    return Collections.emptyList();
  }

  @Override
  public String getName()
  {
    return "";
  }

  @Override
  public String getDescription()
  {
    return getType() + " " + getName() + " (" + getPosition() + ")";
  }

  @Override
  public void enable(Simulation simulation)
  {
    enabled = true;
  }

  @Override
  public void disable()
  {
    enabled = false;
  }

  @Override
  public void disconnect(Simulation simulation)
  {
  }

  @Override
  public void disconnect(Simulation simulation, ConnectionView connection)
  {
  }

  @Override
  public List<ConnectionView> createConnections(SubcircuitView subcircuitView)
  {
    return new ArrayList<>();
  }
}

