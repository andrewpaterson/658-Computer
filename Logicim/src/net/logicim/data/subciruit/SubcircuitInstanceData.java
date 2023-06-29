package net.logicim.data.subciruit;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.ComponentData;
import net.logicim.data.port.common.SimulationMultiPortData;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.CircuitLoaders;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

import java.util.List;
import java.util.Set;

public class SubcircuitInstanceData
    extends ComponentData<SubcircuitInstanceView>
{
  public String subcircuitTypeName;
  public String comment;
  public int width;
  public int height;

  public Set<Long> simulation;

  public SubcircuitInstanceData()
  {
  }

  public SubcircuitInstanceData(String subcircuitTypeName,
                                Int2D position,
                                Rotation rotation,
                                String name,
                                long id,
                                boolean enabled,
                                boolean selected,
                                Set<Long> simulationIDs,
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
          enabled,
          selected);
    this.simulation = simulationIDs;
    this.subcircuitTypeName = subcircuitTypeName;
    this.comment = comment;
    this.width = width;
    this.height = height;
  }

  @Override
  protected SubcircuitInstanceView createComponentView(SubcircuitEditor subcircuitEditor)
  {
    SubcircuitEditor instanceSubcircuitEditor = subcircuitEditor.getSubcircuitEditor(subcircuitTypeName);
    return new SubcircuitInstanceView(subcircuitEditor.getCircuitSubcircuitView(),
                                      instanceSubcircuitEditor.getCircuitSubcircuitView(),
                                      position,
                                      rotation,
                                      new SubcircuitInstanceProperties(name,
                                                                       subcircuitTypeName,
                                                                       comment,
                                                                       width,
                                                                       height));
  }

  @Override
  public void createAndConnectComponent(SubcircuitSimulation subcircuitSimulation, CircuitLoaders circuitLoaders, SubcircuitInstanceView componentView)
  {
    componentView.createComponent(subcircuitSimulation);

    loadPorts(subcircuitSimulation, circuitLoaders, componentView);
  }

  @Override
  public boolean appliesToSimulation(long id)
  {
    return simulation.contains(id);
  }
}

