package net.logicim.data.integratedcircuit.common;

import net.common.type.Int2D;
import net.logicim.data.common.ViewData;
import net.logicim.domain.CircuitSimulation;
import net.logicim.ui.circuit.path.ViewPath;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.simulation.CircuitLoaders;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

public abstract class StaticData<STATIC_VIEW extends StaticView<?>>
    extends ViewData
{
  public String name;
  public Int2D position;
  public Rotation rotation;
  public boolean enabled;
  public boolean selected;

  public StaticData()
  {
  }

  public StaticData(String name,
                    Int2D position,
                    Rotation rotation,
                    long id,
                    boolean enabled,
                    boolean selected)
  {
    super(id);
    this.name = name;
    this.position = position.clone();
    this.rotation = rotation;
    this.enabled = enabled;
    this.selected = selected;
  }

  public STATIC_VIEW createAndEnableStaticView(SubcircuitEditor subcircuitEditor, boolean newComponentPropertyStep)
  {
    STATIC_VIEW staticView = createStaticView(subcircuitEditor, newComponentPropertyStep);

    if (enabled)
    {
      staticView.enable();
    }

    if (selected)
    {
      subcircuitEditor.select(staticView);
    }
    return staticView;
  }

  public abstract STATIC_VIEW createStaticView(SubcircuitEditor subcircuitEditor, boolean newComponentPropertyStep);

  public abstract void createAndConnectComponentDuringLoad(ViewPath viewPath,
                                                           CircuitSimulation circuitSimulation,
                                                           CircuitLoaders circuitLoaders,
                                                           STATIC_VIEW componentView);
}

