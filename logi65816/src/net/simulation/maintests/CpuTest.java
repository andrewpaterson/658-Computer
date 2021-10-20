package net.simulation.maintests;

import net.simulation.common.Bus;
import net.simulation.common.Tickables;
import net.simulation.common.Trace;
import net.simulation.memory.Memory;
import net.simulation.specialised.EconoReset;
import net.simulation.wiring.ClockOscillator;
import net.simulation.wiring.Constant;
import net.util.StringUtil;
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
      printDivider();
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
    String mlb = pins.getMemoryLockB().getWireValuesAsString();

    String reset = pins.getResetB().getWireValuesAsString();
    String irq = pins.getIrqB().getWireValuesAsString();
    String nmi = pins.getNmiB().getWireValuesAsString();
    String abort = pins.getAbortB().getWireValuesAsString();
    String busEnable = pins.getAbortB().getWireValuesAsString();

    String opCodeCycle = "| " + cycle + " | " + rightJustify(opCode, 13, " ") + " | " + addressSource + " (" + address + ")" + " | " + dataSource + "(" + data + ")" + " | " + rwb + " | " + vpa + " | " + vda + " | " + vpb + " | " + mlb + " |";
    String inputs = "| " + busEnable + " | " + reset + " | " + nmi + " | " + abort + " | " + irq + " |";

    WDC65C816 cpu = pins.getCpu();
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
    System.out.println("|" + pad(134, "-") + "|" + "   " + "|" + pad(19, "-") + "|"  + "   " + "|" + pad(49, "-") + "|");
  }

  public static void main(String[] args)
  {
    Tickables tickables = new Tickables();

    Bus addressBus = new Bus(16);
    Bus dataBus = new Bus(8);
    Trace rwbTrace = new Trace();
    Trace clockTrace = new Trace();
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
    Trace highTrace = new Trace();

    Constant high = new Constant(tickables, "", true, highTrace);
    abortBTrace.connect(highTrace);
    nmiBTrace.connect(highTrace);
    irqBTrace.connect(highTrace);
    busEnableTrace.connect(highTrace);

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
    WDC65C816 cpu = new WDC65C816(cpuPins);

    System.out.println(new String(memory.get(0, 18)));

    int count = 1024;
    printDivider();
    System.out.println("|Cycle|  Instruction  |         Address         |                                 Data                             |RWB|VPA|VDA|VPB|MLB|   |BE |RES|NMI|ABT|IRQ|   |   PC    |  SP  |  DP  | DB |  A   |  X   |  Y   |");
    printDivider();
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

