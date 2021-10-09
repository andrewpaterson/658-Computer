package name.bizna.bus;

import name.bizna.bus.common.Bus;
import name.bizna.bus.common.Tickables;
import name.bizna.bus.common.Trace;
import name.bizna.bus.memory.Counter;
import name.bizna.bus.wiring.ClockOscillator;

public class BusTest
{
  public static void main(String[] args)
  {
    Tickables tickables = new Tickables();

    Trace clockTrace = new Trace();
    Bus counterData = new Bus(8);

    new ClockOscillator(tickables, clockTrace);
    Counter counter = new Counter(tickables, 8, counterData, clockTrace);

    for (int i = 0; i < 600; i++)
    {
      tickables.run();
      System.out.println(counter.getCounter());
    }

    System.out.println("Done");
  }
}

