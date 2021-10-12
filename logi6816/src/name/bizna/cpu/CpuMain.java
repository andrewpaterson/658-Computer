package name.bizna.cpu;

import name.bizna.bus.common.Bus;
import name.bizna.bus.common.Tickables;
import name.bizna.bus.common.Trace;
import name.bizna.bus.cpu.Cpu65816Pins;
import name.bizna.bus.memory.Memory;
import name.bizna.bus.wiring.ClockOscillator;

import java.io.File;

import static name.bizna.util.FileUtil.readBytes;

public class CpuMain
{
  public static void main(String[] args)
  {
    Tickables tickables = new Tickables();

    Bus addressBus = new Bus(16);
    Bus dataBus = new Bus(8);
    Trace rwbTrace = new Trace();
    Trace clockTrace = new Trace();
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

    new ClockOscillator(tickables, "", clockTrace);

    Memory memory = new Memory(tickables, "", addressBus, dataBus, rwbTrace, clockTrace, clockTrace,
                               readBytes(new File("../Test816/Test816.bin")));

    Cpu65816Pins cpuPins = new Cpu65816Pins(tickables,
                                            "",
                                            addressBus,
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
    Cpu65816 cpu = new Cpu65816(cpuPins);

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

