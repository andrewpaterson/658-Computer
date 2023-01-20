package net.logicim.ui.simulation.component.decorative.common;

import net.logicim.common.type.Int2D;
import net.logicim.domain.Simulation;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.simulation.CircuitEditor;

public abstract class DecorativeView<T extends DecorativeProperties>
    extends StaticView<T>
{
  public boolean enabled;

  public DecorativeView(CircuitEditor circuitEditor,
                        Int2D position,
                        Rotation rotation,
                        T properties)
  {
    super(circuitEditor, position, rotation, properties);
    circuitEditor.addDecorativeView(this);
  }

  @Override
  protected void finaliseView()
  {
    finalised = true;
    enabled = false;

    updateBoundingBoxFromShapes(boundingBox);

    selectionBox.copy(boundingBox);
    boundingBox.grow(0.5f);
  }

  @Override
  public boolean isEnabled()
  {
    return enabled;
  }

  @Override
  public PortView getPortInGrid(int x, int y)
  {
    return null;
  }

  @Override
  public ConnectionView getConnectionsInGrid(int x, int y)
  {
    return null;
  }

  @Override
  public Int2D getConnectionGridPosition(ConnectionView connectionView)
  {
    return null;
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
}

