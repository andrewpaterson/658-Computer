package net.logicim.ui.simulation.component.decorative.common;

import net.logicim.common.type.Int2D;
import net.logicim.data.decorative.common.DecorativeProperties;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
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
  public DecorativeView(SubcircuitView subcircuitView,
                        Int2D position,
                        Rotation rotation,
                        T properties)
  {
    super(subcircuitView, position, rotation, properties);
    subcircuitView.addDecorativeView(this);
  }

  @Override
  protected void finaliseView()
  {
    finalised = true;
    updateBoundingBoxes();
  }

  @Override
  public void simulationStarted(SubcircuitSimulation circuit)
  {
  }

  @Override
  public List<ConnectionView> getConnectionViews()
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
  public void disconnect()
  {
  }

  @Override
  public List<ConnectionView> getOrCreateConnectionViews(SubcircuitView subcircuitView)
  {
    return new ArrayList<>();
  }
}

