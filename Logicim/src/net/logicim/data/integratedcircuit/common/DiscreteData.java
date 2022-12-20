package net.logicim.data.integratedcircuit.common;

import net.logicim.data.ReflectiveData;
import net.logicim.data.trace.TraceLoader;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.DiscreteView;

public abstract class DiscreteData
    extends ReflectiveData
{
  public DiscreteData()
  {
  }

  public abstract DiscreteView createAndLoad(CircuitEditor circuitEditor, TraceLoader traceLoader);
}

