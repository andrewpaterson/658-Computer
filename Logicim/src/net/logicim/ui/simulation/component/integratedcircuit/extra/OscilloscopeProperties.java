package net.logicim.ui.simulation.component.integratedcircuit.extra;

import net.logicim.domain.common.propagation.Family;
import net.logicim.ui.common.integratedcircuit.IntegratedCircuitProperties;

public class OscilloscopeProperties
    extends IntegratedCircuitProperties
{
  public int inputCount;
  public int numberOfDivsWide;
  public int divHeightInGrids;
  public int samplesPerDiv;
  public float samplingFrequency_Hz;

  public OscilloscopeProperties()
  {
    inputCount = 0;
    numberOfDivsWide = 0;
    divHeightInGrids = 0;
    samplesPerDiv = 0;
    samplingFrequency_Hz = 0;
  }

  public OscilloscopeProperties(String name,
                                Family family,
                                int inputCount,
                                int numberOfDivsWide,
                                int divHeightInGrids,
                                int samplesPerDiv,
                                float samplingFrequency)
  {
    super(name, family);
    this.inputCount = inputCount;
    this.numberOfDivsWide = numberOfDivsWide;
    this.divHeightInGrids = divHeightInGrids;
    this.samplesPerDiv = samplesPerDiv;
    this.samplingFrequency_Hz = samplingFrequency;
  }

  @Override
  public OscilloscopeProperties duplicate()
  {
    return new OscilloscopeProperties(name,
                                      family,
                                      inputCount,
                                      numberOfDivsWide,
                                      divHeightInGrids,
                                      samplesPerDiv,
                                      samplingFrequency_Hz);
  }
}

