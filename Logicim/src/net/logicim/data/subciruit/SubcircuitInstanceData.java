package net.logicim.data.subciruit;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.PassiveData;
import net.logicim.data.port.common.SimulationMultiPortData;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.SubcircuitEditor;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;

import java.util.List;

public class SubcircuitInstanceData
    extends PassiveData<SubcircuitInstanceView>
{
  public String subcircuitTypeName;
  public String comment;
  public int width;
  public int height;

  public SubcircuitInstanceData()
  {
  }

  public SubcircuitInstanceData(String subcircuitTypeName,
                                Int2D position,
                                Rotation rotation,
                                String name,
                                long id,
                                boolean selected,
                                List<SimulationMultiPortData> ports,
                                String comment,
                                int width,
                                int height)
  {
    super(position,
          rotation,
          name,
          ports,
          id,
          selected);
    this.subcircuitTypeName = subcircuitTypeName;
    this.comment = comment;
    this.width = width;
    this.height = height;
  }

  @Override
  protected SubcircuitInstanceView createComponentView(SubcircuitEditor subcircuitEditor, boolean newComponentPropertyStep)
  {
    SubcircuitEditor instanceSubcircuitEditor = subcircuitEditor.getSubcircuitEditor(subcircuitTypeName);
    return new SubcircuitInstanceView(subcircuitEditor.getSubcircuitView(),
                                      instanceSubcircuitEditor.getSubcircuitView(),
                                      position,
                                      rotation,
                                      new SubcircuitInstanceProperties(name,
                                                                       subcircuitTypeName,
                                                                       comment,
                                                                       width,
                                                                       height));
  }
}

