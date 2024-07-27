package net.logicim.domain.integratedcircuit.extra;

import net.logicim.domain.common.state.State;
import net.logicim.domain.common.voltage.VoltageColour;
import net.logicim.domain.common.voltage.VoltageRepresentation;
import net.logicim.domain.common.wire.Trace;

import java.awt.*;

public class OscilloscopeState
    extends State
{
  protected int sampleCount;
  protected float[][] minVoltage;
  protected float[][] maxVoltage;
  protected int[][] colour;
  protected int tickPosition;

  public OscilloscopeState()
  {
    super();
  }

  public OscilloscopeState(int inputCount, int sampleCount)
  {
    super();
    this.sampleCount = sampleCount;

    minVoltage = new float[inputCount][];
    for (int i = 0; i < minVoltage.length; i++)
    {
      minVoltage[i] = new float[sampleCount];
    }

    maxVoltage = new float[inputCount][];
    for (int i = 0; i < maxVoltage.length; i++)
    {
      maxVoltage[i] = new float[sampleCount];
    }

    colour = new int[inputCount][];
    for (int i = 0; i < colour.length; i++)
    {
      colour[i] = new int[sampleCount];
    }

    tickPosition = 0;
  }

  public void tick()
  {
    tickPosition++;
    if (tickPosition >= sampleCount)
    {
      tickPosition = 0;
    }
  }

  public OscilloscopeState(int sampleCount, float[][] minVoltage, float[][] maxVoltage, int[][] colour, int tickPosition)
  {
    this.sampleCount = sampleCount;
    this.minVoltage = minVoltage;
    this.maxVoltage = maxVoltage;
    this.colour = colour;
    this.tickPosition = tickPosition;
  }

  public void sample(int input, Trace trace, VoltageRepresentation colours, long time)
  {
    Color colour;
    float minimumVoltage;
    float maximumVoltage;
    if ((trace == null) || (time == -1))
    {
      colour = colours.getDisconnectedTrace();
      minimumVoltage = 0;
      maximumVoltage = 0;
    }
    else
    {
      minimumVoltage = trace.getMinimumVoltage(time);
      maximumVoltage = trace.getMaximumVoltage(time);

      colour = VoltageColour.getColourForTrace(colours, trace, time);
      if (Float.isNaN(minimumVoltage))
      {
        minimumVoltage = 0;
      }
      if (Float.isNaN(maximumVoltage))
      {
        maximumVoltage = 0;
      }
    }

    this.minVoltage[input][tickPosition] = minimumVoltage;
    this.maxVoltage[input][tickPosition] = maximumVoltage;
    this.colour[input][tickPosition] = colour.getRGB();
  }

  public float[][] getMinVoltage()
  {
    return minVoltage;
  }

  public float[][] getMaxVoltage()
  {
    return maxVoltage;
  }

  public int[][] getColour()
  {
    return colour;
  }

  public int getTickPosition()
  {
    return tickPosition;
  }
}

