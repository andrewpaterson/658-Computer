package net.simulation.maintests;

import net.maxim.ds1813.DS1813;
import net.maxim.simulation.DS1813TickablePins;
import net.simulation.common.Bus;
import net.simulation.common.Tickables;
import net.simulation.common.Trace;
import net.simulation.gate.NotGate;
import net.simulation.gate.NotGateTickablePins;
import net.simulation.memory.Memory;
import net.simulation.memory.MemoryTickablePins;
import net.simulation.wiring.ClockOscillator;
import net.simulation.wiring.ClockOscillatorTickablePins;
import net.simulation.wiring.Constant;
import net.simulation.wiring.ConstantTickablePins;
import net.util.StringUtil;
import net.wdc.simulation.WDC65816TickablePins;
import net.wdc.wdc65816.WDC65816;
import net.wdc.wdc65816.instruction.BusCycle;
import net.wdc.wdc65816.instruction.Instruction;

import java.io.File;

import static net.util.FileUtil.readBytes;
import static net.util.StringUtil.*;

public class CpuTest
{
  private static void print(WDC65816TickablePins pins, BusCycle busCycle, Instruction instruction)
  {
    String addressOffset = busCycle.toAddressOffsetString();

    String opCode = instruction.getName();
    boolean clock = pins.getClock().getBoolAfterRead();
    if (busCycle.isFetchOpCode() && !clock)
    {
      printDivider();
    }
    String cycle = ("" + busCycle.getCycle()) + "-" + ("" + (clock ? 'H' : 'L'));
    String addressSource = leftJustify(addressOffset, 16, " ");

    String operation;
    if (clock)
    {
      operation = busCycle.toOperationString();
    }
    else
    {
      operation = "";
    }
    String operationSource = leftJustify(operation, 55, " ");

    String data;
    if (clock)
    {
      data = busCycle.toDataString();
    }
    else
    {
      data = "Write(BA)";
    }
    String dataSource = leftJustify(data, 14, " ");

    String addressBus = pins.getAddressBus().getWireValuesAsString();
    String dataBus = pins.getDataBus().getWireValuesAsString();
    String rwb = pins.getRwB().getWireValuesAsString();
    String vpa = pins.getValidProgramAddress().getWireValuesAsString();
    String vda = pins.getValidDataAddress().getWireValuesAsString();
    String vpb = pins.getVectorPullB().getWireValuesAsString();
    String mlb = pins.getMemoryLockB().getWireValuesAsString();

    String reset = pins.getResetB().getWireValuesAsString();
    String irq = pins.getIrqB().getWireValuesAsString();
    String nmi = pins.getNmiB().getWireValuesAsString();
    String abort = pins.getAbortB().getWireValuesAsString();
    String busEnable = pins.getAbortB().getWireValuesAsString();

    String opCodeCycle = "| " + cycle + " | " + rightJustify(opCode, 13, " ") + " | " + addressSource + " (" + addressBus + ")" + " | " + dataSource + "(" + dataBus + ")" + " | " + operationSource + " | " + rwb + " | " + vpa + " | " + vda + " | " + vpb + " | " + mlb + " |";
    String inputs = "| " + busEnable + " | " + reset + " | " + nmi + " | " + abort + " | " + irq + " |";

    WDC65816 cpu = pins.getCpu();
    String accumulator = StringUtil.rightJustify(cpu.isMemory16Bit() ? to16BitHex(cpu.getA()) : to8BitHex(cpu.getA()), 4, " ");
    String xIndex = StringUtil.rightJustify(cpu.isIndex16Bit() ? to16BitHex(cpu.getX()) : to8BitHex(cpu.getX()), 4, " ");
    String yIndex = StringUtil.rightJustify(cpu.isIndex16Bit() ? to16BitHex(cpu.getY()) : to8BitHex(cpu.getY()), 4, " ");
    String dataBank = to8BitHex(cpu.getDataBank());
    String directPage = to16BitHex(cpu.getDirectPage());
    String programCounter = to8BitHex(cpu.getProgramCounter().getBank()) + ":" + to16BitHex(cpu.getProgramCounter().getOffset());
    String stackPointer = to16BitHex(cpu.getStackPointer());

    String internal = "| " + programCounter + " | " + stackPointer + " | " + directPage + " | " + dataBank + " | " + accumulator + " | " + xIndex + " | " + yIndex + " |";
    System.out.println(opCodeCycle + "   " + inputs + "   " + internal);
  }

  private static void printDivider()
  {
    System.out.println("|" + pad(146, "-") + "|" + "   " + "|" + pad(19, "-") + "|" + "   " + "|" + pad(49, "-") + "|");
  }

  public static void main(String[] args)
  {
    Tickables tickables = new Tickables();

    Bus addressBus = new Bus(16);
    Bus dataBus = new Bus(8);
    Trace rwbTrace = new Trace();
    Trace clockTrace = new Trace();
    Trace notClockTrace = new Trace();
    Trace abortBTrace = new Trace();
    Trace busEnableTrace = new Trace();
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

    Constant busEnable = new Constant("", new ConstantTickablePins(tickables, busEnableTrace), true);
    Constant irqB = new Constant("", new ConstantTickablePins(tickables, irqBTrace), true);
    Constant nmiB = new Constant("", new ConstantTickablePins(tickables, nmiBTrace), true);
    Constant abortB = new Constant("", new ConstantTickablePins(tickables, abortBTrace), true);
    busEnableTrace.connect(busEnableTrace);

    new ClockOscillator("", new ClockOscillatorTickablePins(tickables, clockTrace));
    new NotGate("", new NotGateTickablePins(tickables, clockTrace, notClockTrace));

    DS1813 econoReset = new DS1813("", new DS1813TickablePins(tickables, resetBTrace));

    Memory memory = new Memory("",
                               new MemoryTickablePins(tickables,
                                                      addressBus, dataBus, rwbTrace, notClockTrace, notClockTrace),
                               readBytes(new File("../Test816/Test816.bin")));

    WDC65816TickablePins cpuPins = new WDC65816TickablePins(tickables,
                                                            "",
                                                            addressBus,
                                                            dataBus,
                                                            rwbTrace,
                                                            clockTrace,
                                                            abortBTrace,
                                                            busEnableTrace,
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
    WDC65816 cpu = new WDC65816("", cpuPins);

    InterruptTrigger irqTrigger = new InterruptTrigger("[..............BRK.COP]", 200);
    InterruptTrigger nmiTrigger = new InterruptTrigger("[IRQ...........BRK.COP]", 200);
    InterruptTrigger abortTrigger = new InterruptTrigger("[IRQ.NMI.......BRK.COP]", 200);

    System.out.println(new String(memory.get(0, 23)));

    int count = 3072;
    printDivider();
    System.out.println("|Cycle|  Instruction  |         Address         |        Data        |                     Operation                           |RWB|VPA|VDA|VPB|MLB|   |BE |RES|NMI|ABT|IRQ|   |   PC    |  SP  |  DP  | DB |  A   |  X   |  Y   |");
    printDivider();
    while (!cpu.isStopped() && count > 0)
    {
      BusCycle busCycle = cpu.getBusCycle();
      Instruction instruction = cpu.getOpCode();
      tickables.run();
      print(cpuPins, busCycle, instruction);

      String s = new String(memory.get(0, 23));
      irqB.setValue(irqTrigger.test(s));
      nmiB.setValue(nmiTrigger.test(s));
      abortB.setValue(abortTrigger.test(s));

      count--;
    }

    System.out.println(new String(memory.get(0, 23)));
  }
}

