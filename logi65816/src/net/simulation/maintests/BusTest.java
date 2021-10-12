package net.simulation.maintests;

import net.simulation.common.Bus;
import net.simulation.common.Tickables;
import net.simulation.common.Trace;
import net.simulation.memory.Counter;
import net.simulation.memory.Memory;
import net.simulation.wiring.ClockOscillator;
import net.simulation.wiring.Constant;

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

    new ClockOscillator(tickables, "", clockTrace);
    Counter counter = new Counter(tickables, "", 8, counterData, clockTrace);
    new Constant(tickables, "High", true, readTrace);
    new Constant(tickables, "Low", false, lowTrace);
    new Constant(tickables, "00000000", 8, 0, zeroBus);

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

