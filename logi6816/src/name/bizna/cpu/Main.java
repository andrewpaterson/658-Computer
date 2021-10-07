package name.bizna.cpu;

import name.bizna.bus.BusPins65816;
import name.bizna.bus.TickableCpu;
import name.bizna.bus.common.Omnibus;
import name.bizna.bus.common.Single;
import name.bizna.bus.logic.NotGate;
import name.bizna.bus.logic.OrGate;
import name.bizna.bus.logic.Tickable;
import name.bizna.bus.memory.Memory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static name.bizna.util.FileUtil.readBytes;

public class Main
{
  public static void main(String[] args)
  {
    Omnibus addressBus = new Omnibus(24);
    Omnibus dataBus = new Omnibus(8);
    Single rwbTrace = new Single();
    Single readTrace = new Single();
    Single clockTrace = new Single();
    Single notClockTrace = new Single();
    Single abortBTrace = new Single();
    Single busEnable = new Single();
    Single irqBTrace = new Single();
    Single nmiBTrace = new Single();
    Single resetBTrace = new Single();
    Single emulationTrace = new Single();
    Single memoryLockBTrace = new Single();
    Single mxTrace = new Single();
    Single rdyTrace = new Single();
    Single vectorPullBTrace = new Single();
    Single validProgramAddressTrace = new Single();
    Single validDataAddressTrace = new Single();

    NotGate notGate = new NotGate(clockTrace, notClockTrace);
    OrGate orGate = new OrGate(notClockTrace, rwbTrace, readTrace);

    Memory memory = new Memory(addressBus, dataBus, rwbTrace,
                               readBytes(new File("../Test816/Test816.bin")));

    BusPins65816 pins = new BusPins65816(addressBus,
                                         dataBus,
                                         rwbTrace,
                                         clockTrace,
                                         abortBTrace,
                                         busEnable,
                                         irqBTrace,
                                         nmiBTrace,
                                         resetBTrace,
                                         emulationTrace,
                                         memoryLockBTrace,
                                         mxTrace,
                                         rdyTrace,
                                         vectorPullBTrace,
                                         validProgramAddressTrace,
                                         validDataAddressTrace);

    TickableCpu cpu = new TickableCpu(pins);
    ClockOscillator clockOscillator = new ClockOscillator(clockTrace);

    List<Tickable> tickables = new ArrayList<>();
    tickables.add(memory);
    tickables.add(cpu);
    tickables.add(notGate);
    tickables.add(orGate);
    tickables.add(clockOscillator);

    int count = 1024;
    while (!cpu.isStopped() && count > 0)
    {
      runTickables(tickables);

      cpu.dump();

//      if (!clock)
//      {
//        if (pins.isValidDataAddress() || pins.isValidProgramAddress())
//        {
//          Address address = cpu.getAddress();  //This should probably use pins not cpu.
//          if (pins.isRead())
//          {
//            pins.setData(memory.readByte(address));
//          }
//          else
//          {
//            memory.writeByte(address, pins.getData());
//          }
//        }
//      }

      count--;
    }

    byte[] progress = memory.get(0, 18);
    String s = new String(progress);
    System.out.println(s);
  }

  private static void runTickables(List<Tickable> tickables)
  {
    boolean settled;
    do
    {
      settled = true;
      for (Tickable tickable : tickables)
      {
        settled &= tickable.propagate();
      }
    }
    while (!settled);

    for (Tickable tickable : tickables)
    {
      tickable.donePropagation();
    }
  }
}

