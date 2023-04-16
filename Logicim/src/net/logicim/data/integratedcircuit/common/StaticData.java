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
  protected boolean selected;

  public StaticData()
  {
  }

  public StaticData(String name,
                    Int2D position,
                    Rotation rotation,
                    long id,
                    boolean selected)
  {
    this.name = name;
    this.position = position.clone();
    this.rotation = rotation;
    this.id = id;
    this.selected = selected;
  }

  public T createAndLoad(SubcircuitEditor subcircuitEditor, boolean newComponentPropertyStep)
  {
    T componentView = createStaticView(subcircuitEditor, newComponentPropertyStep);

    if (selected)
    {
      subcircuitEditor.select(componentView);
    }
    return componentView;
  }

  public abstract T createStaticView(SubcircuitEditor subcircuitEditor, boolean newComponentPropertyStep);
}

