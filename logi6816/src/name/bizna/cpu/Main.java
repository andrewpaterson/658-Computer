package name.bizna.cpu;

import name.bizna.bus.BusPins65816;
import name.bizna.bus.TickableCpu;
import name.bizna.bus.common.Omniport;
import name.bizna.bus.common.Port;
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
    Omniport addressBus = new Omniport(24);
    Omniport dataBus = new Omniport(8);
    Port rwbTrace = new Port();
    Port readTrace = new Port();
    Port clockTrace = new Port();
    Port notClockTrace = new Port();
    Port abortBTrace = new Port();
    Port busEnable = new Port();
    Port irqBTrace = new Port();
    Port nmiBTrace = new Port();
    Port resetBTrace = new Port();
    Port emulationTrace = new Port();
    Port memoryLockBTrace = new Port();
    Port mxTrace = new Port();
    Port rdyTrace = new Port();
    Port vectorPullBTrace = new Port();
    Port validProgramAddressTrace = new Port();
    Port validDataAddressTrace = new Port();

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
    Cpu65816 cpu65816 = new Cpu65816(pins);
    TickableCpu cpu = new TickableCpu(cpu65816);

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

