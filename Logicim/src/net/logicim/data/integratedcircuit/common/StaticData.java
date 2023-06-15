package net.logicim.data.integratedcircuit.common;

import net.logicim.common.type.Int2D;
import net.logicim.data.common.ViewData;
import net.logicim.data.wire.TraceLoader;
import net.logicim.domain.InstanceCircuitSimulation;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.simulation.SubcircuitEditor;

public abstract class StaticData<T extends StaticView<?>>
    extends ViewData
{
  protected String name;
  protected Int2D position;
  protected Rotation rotation;
  protected long id;
  protected boolean enabled;
  protected boolean selected;

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

  public T createAndEnableStaticView(SubcircuitEditor subcircuitEditor, boolean newComponentPropertyStep)
  {
    T staticView = createStaticView(subcircuitEditor, newComponentPropertyStep);

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

  public abstract T createStaticView(SubcircuitEditor subcircuitEditor, boolean newComponentPropertyStep);

  public abstract void createAndConnectComponent(SubcircuitEditor subcircuitEditor,
                                                 InstanceCircuitSimulation circuit,
                                                 TraceLoader traceLoader,
                                                 T componentView);
}

