package net.simulation.maintests;

import net.simulation.common.Bus;
import net.simulation.common.Tickables;
import net.simulation.common.Trace;
import net.simulation.memory.Memory;
import net.simulation.wiring.ClockOscillator;
import net.wdc65xx.simulation.Cpu65816Pins;
import net.wdc65xx.wdc65816.Cpu65816;
import net.wdc65xx.wdc65816.addressingmode.BusCycle;

import java.io.File;

import static net.util.FileUtil.readBytes;
import static net.util.StringUtil.leftJustify;
import static net.util.StringUtil.rightJustify;

public class CpuTest
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

    System.out.println(new String(memory.get(0, 18)));

    int count = 1024;
    while (!cpu.isStopped() && count > 0)
    {
      tickables.run();

      if (cpu.getPreviousClock())
      {
        BusCycle busCycle = cpu.getBusCycle();
        String addressOffset = busCycle.toAddressOffsetString();
        String operation = busCycle.toOperationString();

        String opCode = cpu.getOpcodeMnemonicString();
        System.out.println(rightJustify("" + (cpu.getCycle() + 1), 2, " ") + " | " + rightJustify(opCode, 12, " ") + " | " + leftJustify(addressOffset, 16, " ") + " | " + operation);
      }

      count--;
    }

    System.out.println(new String(memory.get(0, 18)));
  }
}

