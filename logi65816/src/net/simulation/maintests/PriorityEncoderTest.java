package net.simulation.maintests;

import net.integratedcircuits.ti.ls148.LS148;
import net.simulation.common.Bus;
import net.simulation.common.Tickables;
import net.simulation.common.Trace;
import net.simulation.integratedcircuits.ti.LS148TickablePins;
import net.simulation.wiring.Constant;
import net.simulation.wiring.ConstantTickablePins;

public class PriorityEncoderTest
{
  public static void main(String[] args)
  {
    Tickables tickables = new Tickables();

    Bus inputBus = new Bus(8);
    Trace eiTrace = new Trace();
    Bus aBus = new Bus(3);
    Trace gsTrace = new Trace();
    Trace eoTrace = new Trace();
    new LS148("", new LS148TickablePins(tickables, inputBus, eiTrace, aBus, gsTrace, eoTrace));
    Constant constant = new Constant("", new ConstantTickablePins(tickables, 8, inputBus), 0);
    new Constant("", new ConstantTickablePins(tickables, eiTrace), false);

    for (int i = 0; i <= 8; i++)
    {
      if (i < 8)
      {
        constant.setValue(0xff & (~(1 << i)));
      }
      else
      {
        constant.setValue(0xff);
      }
      tickables.run();

      System.out.println("I[" + inputBus.getStringValue() + "], A[" + aBus.getStringValue() + "], GS[" + gsTrace.getStringValue() + "], EO[" + eoTrace.getStringValue() + "].");
    }
  }
}

