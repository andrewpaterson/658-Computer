package net.logicim.data.integratedcircuit.extra;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.IntegratedCircuitData;
import net.logicim.data.integratedcircuit.event.IntegratedCircuitEventData;
import net.logicim.data.port.MultiPortData;
import net.logicim.data.wire.TraceLoader;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.propagation.FamilyStore;
import net.logicim.domain.integratedcircuit.extra.OscilloscopeState;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.component.integratedcircuit.extra.OscilloscopeProperties;
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
                          String family,
                          List<IntegratedCircuitEventData<?>> events,
                          List<MultiPortData> ports,
                          boolean selected,
                          OscilloscopeState state,
                          int inputCount,
                          int numberOfDivsWide,
                          int samplesPerDiv,
                          int divHeightInGrids,
                          float sampleFrequency)
  {
    super(position, rotation, name, family, events, ports, selected, state);
    this.inputCount = inputCount;
    this.numberOfDivsWide = numberOfDivsWide;
    this.samplesPerDiv = samplesPerDiv;
    this.divHeightInGrids = divHeightInGrids;
    this.sampleFrequency = sampleFrequency;
  }

  @Override
  public OscilloscopeView create(SubcircuitView subcircuitView, Circuit circuit, TraceLoader traceLoader)
  {
    return new OscilloscopeView(subcircuitView,
                                circuit,
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

