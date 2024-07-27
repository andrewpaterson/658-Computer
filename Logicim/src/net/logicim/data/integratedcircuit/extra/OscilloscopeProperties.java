package net.logicim.data.integratedcircuit.extra;

import net.logicim.data.family.Family;
import net.logicim.data.integratedcircuit.common.IntegratedCircuitProperties;

import java.util.Objects;

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

  @Override
  public boolean equals(Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (o == null || getClass() != o.getClass())
    {
      return false;
    }
    OscilloscopeProperties that = (OscilloscopeProperties) o;
    return inputCount == that.inputCount &&
           numberOfDivsWide == that.numberOfDivsWide &&
           divHeightInGrids == that.divHeightInGrids &&
           samplesPerDiv == that.samplesPerDiv &&
           Float.compare(that.samplingFrequency_Hz, samplingFrequency_Hz) == 0 &&
           Objects.equals(name, that.name);
  }
}

