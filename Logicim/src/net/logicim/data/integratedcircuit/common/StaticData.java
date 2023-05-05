package net.logicim.data.integratedcircuit.common;

import net.logicim.common.type.Int2D;
import net.logicim.data.common.ReflectiveData;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.simulation.SubcircuitEditor;

public abstract class StaticData<T extends StaticView<?>>
    extends ReflectiveData
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
    this.name = name;
    this.position = position.clone();
    this.rotation = rotation;
    this.id = id;
    this.enabled = enabled;
    this.selected = selected;
  }

  public T createAndLoad(SubcircuitEditor subcircuitEditor, boolean newComponentPropertyStep)
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
}

