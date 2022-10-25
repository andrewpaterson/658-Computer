package net.logicim.domain.common.propagation;

import net.logicim.domain.common.Timeline;

public class BistateOutputPropagation
    extends Propagation
    implements OutputPropagation
{
  protected float highVoltageOut;
  protected float lowVoltageOut;
  protected int highToLowPropagationDelay;
  protected int lowToHighPropagationDelay;

  public BistateOutputPropagation(Timeline timeline,
                                  String family,
                                  float lowVoltageOut,
                                  float highVoltageOut,
                                  int highToLowPropagationDelay,
                                  int lowToHighPropagationDelay)
  {
    super(timeline, family);
    this.highVoltageOut = highVoltageOut;
    this.lowVoltageOut = lowVoltageOut;
    this.highToLowPropagationDelay = highToLowPropagationDelay;
    this.lowToHighPropagationDelay = lowToHighPropagationDelay;
  }

  @Override
  public float getLowVoltageOut()
  {
    return lowVoltageOut;
  }

  @Override
  public float getHighVoltageOut()
  {
    return highVoltageOut;
  }

  @Override
  public int getHighToLowPropagationDelay()
  {
    return highToLowPropagationDelay;
  }

  @Override
  public int getLowToHighPropagationDelay()
  {
    return lowToHighPropagationDelay;
  }
}

