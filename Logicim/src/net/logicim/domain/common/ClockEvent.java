package net.logicim.domain.common;

public class ClockEvent extends Event
{
  protected IntegratedCircuit<? extends Pins> integratedCircuit;

  public ClockEvent(long time, IntegratedCircuit<? extends Pins> integratedCircuit)
  {
    super(time);
    this.integratedCircuit = integratedCircuit;
  }

  public IntegratedCircuit<? extends Pins> getIntegratedCircuit()
  {
    return integratedCircuit;
  }
}

