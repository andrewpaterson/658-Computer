package net.logicim.data.subciruit;

import net.common.SimulatorException;
import net.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.ComponentData;
import net.logicim.data.port.common.SimulationMultiPortData;
import net.logicim.domain.passive.subcircuit.SubcircuitInstanceSimulation;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.CircuitLoaders;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

import java.util.List;

public class SubcircuitInstanceData
    extends ComponentData<SubcircuitInstanceView>
{
  public String subcircuitTypeName;
  public String comment;
  public int width;
  public int height;

  public List<SubcircuitInstanceSimulationSimulationData> subcircuitInstanceSimulations;

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
                                List<SubcircuitInstanceSimulationSimulationData> subcircuitInstanceSimulations,
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
    this.subcircuitTypeName = subcircuitTypeName;
    this.subcircuitInstanceSimulations = subcircuitInstanceSimulations;
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
  public void createAndConnectComponentDuringLoad(SubcircuitSimulation containingSubcircuitSimulation, CircuitLoaders circuitLoaders, SubcircuitInstanceView componentView)
  {
    throw new SimulatorException("SubcircuitInstanceData.createAndConnectComponent() is not implemented.  Call createAndConnectComponent2() instead.");
  }

  public void createAndConnectComponentDuringLoad(SubcircuitSimulation containingSubcircuitSimulation,
                                                  SubcircuitInstanceSimulation subcircuitInstanceSimulation,
                                                  CircuitLoaders circuitLoaders,
                                                  SubcircuitInstanceView componentView)
  {
    componentView.createSubcircuitInstance(containingSubcircuitSimulation, subcircuitInstanceSimulation);

    loadPorts(containingSubcircuitSimulation, circuitLoaders, componentView);
  }

  @Override
  public boolean appliesToSimulation(long id)
  {
    //Might want to optimise this a bit.
    for (SubcircuitInstanceSimulationSimulationData subcircuitInstanceSimulation : subcircuitInstanceSimulations)
    {
      if (subcircuitInstanceSimulation.containingSimulation == id)
      {
        return true;
      }
    }
    return false;
  }
}

