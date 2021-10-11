package name.bizna.cpu;

import name.bizna.bus.common.Bus;
import name.bizna.bus.common.Tickables;
import name.bizna.bus.common.Trace;
import name.bizna.bus.cpu.TickablePins65816;
import name.bizna.bus.gate.NotGate;
import name.bizna.bus.gate.OrGate;
import name.bizna.bus.gate.Transceiver;
import name.bizna.bus.memory.Memory;
import name.bizna.bus.wiring.ClockOscillator;

import java.io.File;

import static name.bizna.util.FileUtil.readBytes;

public class CpuMain
{
  public static void main(String[] args)
  {
    Tickables tickables = new Tickables(true);

    Bus addressBus = new Bus(16);
    Bus dataAndBankMultiplexedBus = new Bus(8);
    Bus dataBus = new Bus(8);
    Trace rwbTrace = new Trace();
    Trace notRWBTrace = new Trace();
    Trace readTrace = new Trace();
    Trace clockTrace = new Trace();
    Trace notClockTrace = new Trace();
    Trace abortBTrace = new Trace();
    Trace busEnable = new Trace();
    Trace irqBTrace = new Trace();
    Trace nmiBTrace = new Trace();
    Trace resetBTrace = new Trace();
    Trace emulationTrace = new Trace();
    Trace memoryLockBTrace = new Trace();
    Trace mxTrace = new Trace();
    Trace rdyTrace = new Trace();
    Trace vectorPullBTrace = new Trace();
    Trace validProgramAddressTrace = new Trace();
    Trace validDataAddressTrace = new Trace();

    new NotGate(tickables, "NOT Clock", clockTrace, notClockTrace);

    new OrGate(tickables, "(NOT Clock) OR Read", notClockTrace, rwbTrace, readTrace);

    new NotGate(tickables, "NOT RWB", rwbTrace, notRWBTrace);

    new Transceiver(tickables, "Pass Data block Bank", 8, dataAndBankMultiplexedBus, dataBus, notClockTrace, notRWBTrace);

    Memory memory = new Memory(tickables, "", addressBus, dataBus, rwbTrace,
                               readBytes(new File("../Test816/Test816.bin")));

    TickablePins65816 cpuPins = new TickablePins65816(tickables,
                                                      "",
                                                      addressBus,
                                                      dataAndBankMultiplexedBus,
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
    Cpu65816 cpu = new Cpu65816(cpuPins);

    new ClockOscillator(tickables, "", clockTrace);

    int count = 1024;
    while (!cpu.isStopped() && count > 0)
    {
      tickables.run();

      cpu.dump();

      count--;
    }

    byte[] progress = memory.get(0, 18);
    String s = new String(progress);
    System.out.println(s);
  }
}

