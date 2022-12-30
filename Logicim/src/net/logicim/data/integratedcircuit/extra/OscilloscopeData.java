package net.logicim.data.integratedcircuit.extra;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.IntegratedCircuitData;
import net.logicim.data.integratedcircuit.event.IntegratedCircuitEventData;
import net.logicim.data.port.PortData;
import net.logicim.data.trace.TraceLoader;
import net.logicim.domain.common.propagation.FamilyStore;
import net.logicim.domain.integratedcircuit.extra.OscilloscopeState;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.integratedcircuit.extra.OscilloscopeView;

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
                          List<PortData> ports,
                          OscilloscopeState state,
                          int inputCount,
                          int numberOfDivsWide,
                          int samplesPerDiv,
                          int divHeightInGrids,
                          float sampleFrequency)
  {
    super(position, rotation, name, family, events, ports, state);
    this.inputCount = inputCount;
    this.numberOfDivsWide = numberOfDivsWide;
    this.samplesPerDiv = samplesPerDiv;
    this.divHeightInGrids = divHeightInGrids;
    this.sampleFrequency = sampleFrequency;
  }

  @Override
  public OscilloscopeView create(CircuitEditor circuitEditor, TraceLoader traceLoader)
  {
    return new OscilloscopeView(circuitEditor,
                                inputCount,
                                numberOfDivsWide,
                                divHeightInGrids,
                                samplesPerDiv,
                                sampleFrequency,
                                position,
                                rotation,
                                name,
                                FamilyStore.getInstance().get(family));
  }
}

