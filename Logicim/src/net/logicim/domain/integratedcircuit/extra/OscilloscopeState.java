package net.logicim.domain.integratedcircuit.extra;

import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.Pins;
import net.logicim.domain.common.state.State;

public class OscilloscopeState
    extends State
{
  protected int width;
  protected int inputCount;
  protected float[][] values;
  protected int resolution;
  protected int tickPosition;

  public OscilloscopeState(IntegratedCircuit<? extends Pins, ? extends State> parent, int width, int inputCount, int resolution)
  {
    super(parent);
    this.width = width;
    this.inputCount = inputCount;
    values = new float[inputCount][];
    this.resolution = resolution;
    for (int i = 0; i < values.length; i++)
    {
      values[i] = new float[width * resolution];
    }
    tickPosition = 0;
  }

  public void tick()
  {
    tickPosition++;
    if (tickPosition >= width * resolution)
    {
      tickPosition = 0;
    }
  }

  public void sample(int input, float voltage)
  {
    if (Float.isNaN(voltage))
    {
      voltage = 0;
    }
    values[input][tickPosition] = voltage;
  }

  public float[][] getValues()
  {
    return values;
  }
}

