package net.simulation.maintests;

import net.simulation.common.Bus;
import net.simulation.common.Tickables;
import net.simulation.common.Trace;
import net.simulation.memory.Counter;
import net.simulation.memory.CounterTickablePins;
import net.simulation.memory.Memory;
import net.simulation.wiring.ClockOscillatorTickablePins;
import net.simulation.wiring.Constant;
import net.simulation.wiring.ConstantTickablePins;

import java.io.File;

import static net.util.FileUtil.readBytes;

public class BusTest
{
  public static void main(String[] args)
  {
    Tickables tickables = new Tickables();

    Trace clockTrace = new Trace();
    Trace readTrace = new Trace();
    Trace lowTrace = new Trace();
    Bus counterData = new Bus(8);
    Bus dataBus = new Bus(8);
    Bus zeroBus = new Bus(8);
    Bus addressBus = new Bus(counterData, zeroBus);

    new ClockOscillatorTickablePins(tickables, "", clockTrace);
    Counter counter = new Counter(new CounterTickablePins(tickables, "", 8, counterData, clockTrace));
    new Constant(new ConstantTickablePins(tickables, "High", readTrace), true);
    new Constant(new ConstantTickablePins(tickables, "Low", lowTrace), false);
    new Constant(new ConstantTickablePins(tickables, "00000000", 8, zeroBus), 0);

    new Memory(tickables,
               "",
               addressBus, dataBus, readTrace, lowTrace, lowTrace,
               readBytes(new File("../Test816/Test816.bin")));

    long lastCountNumber = 0;
    String oldStringValue = "";
    for (; ; )
    {
      tickables.run();

      long counterNumber = counter.getCounter();
      String addressValue = addressBus.getStringValue();
      String dataValue = dataBus.getStringValue();
      if (!oldStringValue.equals(dataValue))
      {
        if (counterNumber - lastCountNumber > 1)
        {
          System.out.println("...");
        }
        lastCountNumber = counterNumber;
        System.out.println(addressValue + ":" + dataValue);
        oldStringValue = dataValue;
      }

      if (counterNumber == 0xFF)
      {
        break;
      }
    }

    System.out.println("Done");
  }
}

