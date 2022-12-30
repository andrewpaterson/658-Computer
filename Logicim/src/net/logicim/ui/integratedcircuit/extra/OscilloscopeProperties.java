package net.logicim.ui.integratedcircuit.extra;

import net.logicim.domain.common.propagation.Family;
import net.logicim.ui.common.integratedcircuit.IntegratedCircuitProperties;

public class OscilloscopeProperties
    extends IntegratedCircuitProperties
{
  protected int inputCount;
  protected int numberOfDivsWide;
  protected int divHeightInGrids;
  protected int samplesPerDiv;
  protected float samplingFrequency_Hz;

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
}

