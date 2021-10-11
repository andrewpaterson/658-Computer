package name.bizna.bus;

import name.bizna.bus.common.Bus;
import name.bizna.bus.common.Tickables;
import name.bizna.bus.common.Trace;
import name.bizna.bus.memory.Counter;
import name.bizna.bus.memory.Memory;
import name.bizna.bus.wiring.ClockOscillator;
import name.bizna.bus.wiring.Constant;

import java.io.File;

import static name.bizna.util.FileUtil.readBytes;

public class BusTest
{
  public static void main(String[] args)
  {
    Tickables tickables = new Tickables();

    Trace clockTrace = new Trace();
    Trace readTrace = new Trace();
    Trace outputEnableTrace = new Trace();
    Bus counterData = new Bus(8);
    Bus dataBus = new Bus(8);
    Bus zeroBus = new Bus(8);
    Bus addressBus = new Bus(counterData, zeroBus);

    new ClockOscillator(tickables, "", clockTrace);
    Counter counter = new Counter(tickables, "", 8, counterData, clockTrace);
    new Constant(tickables, "", true, readTrace);
    new Constant(tickables, "", false, outputEnableTrace);
    new Constant(tickables, "", 8, 0, zeroBus);

    new Memory(tickables,
               "",
               addressBus, dataBus, readTrace, outputEnableTrace,
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

