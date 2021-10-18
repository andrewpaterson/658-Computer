package net.simulation.maintests;

import net.simulation.common.Bus;
import net.simulation.common.Tickables;
import net.simulation.common.Trace;
import net.simulation.memory.Memory;
import net.simulation.specialised.EconoReset;
import net.simulation.wiring.ClockOscillator;
import net.wdc65xx.simulation.Cpu65816Pins;
import net.wdc65xx.wdc65816.WDC65C816;
import net.wdc65xx.wdc65816.instruction.BusCycle;
import net.wdc65xx.wdc65816.instruction.Instruction;

import java.io.File;

import static net.util.FileUtil.readBytes;
import static net.util.StringUtil.*;

public class CpuTest
{
  private static void print(Cpu65816Pins pins, BusCycle busCycle, Instruction instruction)
  {
    String addressOffset = busCycle.toAddressOffsetString();
    String operation = busCycle.toOperationString();

    String opCode = instruction.getName();
    boolean clock = pins.getClock().getBoolAfterRead();
    if (busCycle.isFetchOpCode() && clock)
    {
      System.out.println("|" + pad(130, "-") + "|");
    }
    String cycle = ("" + busCycle.getCycle()) + "-" + ("" + (clock ? 'H' : 'L'));
    String addressSource = leftJustify(addressOffset, 16, " ");
    String dataSource = leftJustify(operation, 60, " ");

    String address = pins.getAddressBus().getWireValuesAsString();
    String data = pins.getDataBus().getWireValuesAsString();
    String rwb = pins.getRwB().getWireValuesAsString();
    String vpa = pins.getValidProgramAddress().getWireValuesAsString();
    String vda = pins.getValidDataAddress().getWireValuesAsString();
    String vpb = pins.getVectorPullB().getWireValuesAsString();
    System.out.println("| " + cycle + " | " + rightJustify(opCode, 13, " ") + " | " + addressSource + " (" + address + ")" + " | " + dataSource + "(" + data + ")" + " | " + rwb + " | " + vpa + " | " + vda + " | " + vpb + " |");
  }

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

    ClockOscillator clock = new ClockOscillator(tickables, "", clockTrace);

    EconoReset econoReset = new EconoReset(tickables, "", resetBTrace);

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
    WDC65C816 cpu = new WDC65C816(cpuPins);

    System.out.println(new String(memory.get(0, 18)));

    int count = 1024;
    System.out.println("|" + pad(130, "-") + "|");
    System.out.println("|Cycle|  Instruction  |         Address         |                                 Data                             |RWB|VPA|VDA|VPB|");
    System.out.println("|" + pad(130, "-") + "|");
    while (!cpu.isStopped() && count > 0)
    {
      BusCycle busCycle = cpu.getBusCycle();
      Instruction instruction = cpu.getOpCode();
      tickables.run();
      print(cpuPins, busCycle, instruction);

      count--;
    }

    System.out.println(new String(memory.get(0, 18)));
  }
}

