package net.logicim.data.integratedcircuit.extra;

import net.logicim.common.type.Int2D;
import net.logicim.data.family.Family;
import net.logicim.data.integratedcircuit.common.IntegratedCircuitData;
import net.logicim.data.integratedcircuit.event.SimulationIntegratedCircuitEventData;
import net.logicim.data.port.common.SimulationMultiPortData;
import net.logicim.domain.common.propagation.FamilyStore;
import net.logicim.data.simulation.SimulationStateData;
import net.logicim.domain.integratedcircuit.extra.OscilloscopeState;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.SubcircuitEditor;
import net.logicim.ui.simulation.component.integratedcircuit.extra.OscilloscopeView;

import java.util.List;

public class OscilloscopeData
    extends IntegratedCircuitData<OscilloscopeView, OscilloscopeState>
{
  protected int inputCount;
  protected int numberOfDivsWide;
  protected int samplesPerDiv;
  protected int divHeightInGrids;
  protected float sampleFrequency;

  public OscilloscopeData()
  {
  }

  public OscilloscopeData(Int2D position,
                          Rotation rotation,
                          String name,
                          Family family,
                          SimulationIntegratedCircuitEventData events,
                          List<SimulationMultiPortData> ports,
                          long id,
                          boolean selected,
                          SimulationStateData<OscilloscopeState> simulationState,
                          int inputCount,
                          int numberOfDivsWide,
                          int samplesPerDiv,
                          int divHeightInGrids,
                          float sampleFrequency)
  {
    super(position,
          rotation,
          name,
          family,
          events,
          ports,
          id,
          selected,
          simulationState);
    this.inputCount = inputCount;
    this.numberOfDivsWide = numberOfDivsWide;
    this.samplesPerDiv = samplesPerDiv;
    this.divHeightInGrids = divHeightInGrids;
    this.sampleFrequency = sampleFrequency;
  }

  @Override
  public OscilloscopeView createComponentView(SubcircuitEditor subcircuitEditor, boolean newComponentPropertyStep)
  {
    return new OscilloscopeView(subcircuitEditor.getSubcircuitView(),
                                position,
                                rotation,
                                new OscilloscopeProperties(name,
                                                           FamilyStore.getInstance().get(family),
                                                           inputCount,
                                                           numberOfDivsWide,
                                                           divHeightInGrids,
                                                           samplesPerDiv,
                                                           sampleFrequency));
  }
}

