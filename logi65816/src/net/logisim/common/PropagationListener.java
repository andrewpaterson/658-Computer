package net.logisim.common;

import com.cburch.logisim.circuit.Simulator;

public class PropagationListener<T extends LogisimPins>
    implements Simulator.Listener
{
  private T instance;

  public PropagationListener(T instance)
  {
    this.instance = instance;
  }

  @Override
  public void simulatorReset(Simulator.Event e)
  {
  }

  @Override
  public void propagationStarted(Simulator.Event e)
  {
    instance.startPropagation();
  }

  @Override
  public void propagationCompleted(Simulator.Event e)
  {
    instance.donePropagation();
  }

  @Override
  public void simulatorStateChanged(Simulator.Event e)
  {
  }
}

