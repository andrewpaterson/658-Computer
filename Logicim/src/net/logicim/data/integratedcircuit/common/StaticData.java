package net.logicim.data.integratedcircuit.common;

import net.logicim.common.type.Int2D;
import net.logicim.data.common.ReflectiveData;
import net.logicim.data.wire.TraceLoader;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.simulation.SubcircuitEditor;

public abstract class StaticData<T extends StaticView<?>>
    extends ReflectiveData
{
  protected String name;
  protected Int2D position;
  protected Rotation rotation;
  protected BoundingBoxData boundingBox;  //Huh?
  protected BoundingBoxData selectionBox;  //Huh?
  protected boolean selected;

  public StaticData()
  {
  }

  public StaticData(String name,
                    Int2D position,
                    Rotation rotation,
                    BoundingBoxData boundingBox,
                    BoundingBoxData selectionBox,
                    boolean selected)
  {
    this.name = name;
    this.position = position.clone();
    this.rotation = rotation;
    this.boundingBox = boundingBox;
    this.selectionBox = selectionBox;
    this.selected = selected;
  }

  public abstract T createAndLoad(SubcircuitEditor subcircuitEditor,
                                  TraceLoader traceLoader,
                                  boolean fullLoad,
                                  Simulation simulation,
                                  Circuit circuit);

  protected abstract T create(SubcircuitEditor subcircuitEditor, Circuit circuit, TraceLoader traceLoader, boolean fullLoad);
}

