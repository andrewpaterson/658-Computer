package name.bizna.bus;

import name.bizna.bus.common.Tickables;
import name.bizna.bus.common.Trace;
import name.bizna.bus.logic.AndGate;
import name.bizna.bus.logic.OrGate;
import name.bizna.bus.wiring.ClockOscillator;

public class BusTest
{
  public static void main(String[] args)
  {
    Tickables tickables = new Tickables();

    Trace clockTrace = new Trace();
    Trace orIn1 = new Trace();
    Trace orIn2 = new Trace();
    Trace andIn1 = new Trace();
    Trace andIn2 = new Trace();
    clockTrace.connect(orIn1);
    clockTrace.connect(orIn2);
    Trace orOut = new Trace();
    Trace andOut = new Trace();
    clockTrace.connect(andIn1);
    clockTrace.connect(andIn2);

    new OrGate(tickables, orIn1, orIn2, orOut);
    new AndGate(tickables, andIn1, andIn2, andOut);
    new ClockOscillator(tickables, clockTrace);

    tickables.run();
    System.out.println(clockTrace.toString());
    System.out.println(orOut.toString());
    System.out.println(andOut.toString());

    System.out.println("Done");
  }
}

