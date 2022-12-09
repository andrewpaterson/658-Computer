package net.logicim.data.integratedcircuit.extra;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.IntegratedCircuitData;
import net.logicim.data.integratedcircuit.event.IntegratedCircuitEventData;
import net.logicim.data.port.PortData;
import net.logicim.data.trace.TraceLoader;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.integratedcircuit.extra.OscilloscopeView;

import java.util.List;

public class OscilloscopeData
    extends IntegratedCircuitData<OscilloscopeView>
{
  protected int inputCount;
  protected int numberOfDivsWide;
  protected int samplesPerDiv;
  protected int divHeightInGrids;
  protected float sampleFrequency;

  protected float[][] minVoltage;
  protected float[][] maxVoltage;
  protected int[][] colour;
  protected int tickPosition;

  public OscilloscopeData()
  {
  }

  public OscilloscopeData(Int2D position,
                          Rotation rotation,
                          String name,
                          List<IntegratedCircuitEventData<?>> events,
                          List<PortData> ports,
                          int inputCount,
                          int numberOfDivsWide,
                          int samplesPerDiv,
                          int divHeightInGrids,
                          float sampleFrequency,
                          float[][] minVoltage,
                          float[][] maxVoltage,
                          int[][] colour,
                          int tickPosition)
  {
    super(position, rotation, name, events, ports);
    this.inputCount = inputCount;
    this.numberOfDivsWide = numberOfDivsWide;
    this.samplesPerDiv = samplesPerDiv;
    this.divHeightInGrids = divHeightInGrids;
    this.sampleFrequency = sampleFrequency;
    this.minVoltage = minVoltage;
    this.maxVoltage = maxVoltage;
    this.colour = colour;
    this.tickPosition = tickPosition;
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
                                name);
  }
}

