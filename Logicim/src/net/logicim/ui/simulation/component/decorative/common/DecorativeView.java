package net.logicim.ui.simulation.component.decorative.common;

import net.common.type.Int2D;
import net.logicim.data.decorative.common.DecorativeProperties;
import net.logicim.domain.CircuitSimulation;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.circuit.path.ViewPath;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.StaticView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class DecorativeView<T extends DecorativeProperties>
    extends StaticView<T>
{
  public DecorativeView(SubcircuitView containingSubcircuitView,
                        Int2D position,
                        Rotation rotation,
                        T properties)
  {
    super(containingSubcircuitView, position, rotation, properties);
    containingSubcircuitView.addDecorativeView(this);
  }

  @Override
  protected void finaliseView()
  {
    super.finaliseView();
  }

  @Override
  public void validate()
  {
  }

  @Override
  public void simulationStarted(CircuitSimulation circuitSimulation)
  {
  }

  @Override
  public void simulationStarted(ViewPath viewPath, CircuitSimulation circuitSimulation)
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
  public void disconnectViewAndDestroyComponents()
  {
  }

  @Override
  public void destroyComponent(ViewPath viewPath, CircuitSimulation circuitSimulation)
  {
  }

  @Override
  public void destroyAllComponents()
  {
  }

  @Override
  public List<ConnectionView> getOrCreateConnectionViews()
  {
    return new ArrayList<>();
  }
}

