package net.integratedcircuits.wdc.wdc65816;

import net.common.Snapshot;
import net.integratedcircuits.wdc.wdc65816.instruction.Instruction;

public class W65C816Snapshot
    implements Snapshot
{
  // Status register
  public boolean zeroFlag;
  public boolean negativeFlag;
  public boolean decimalFlag;
  public boolean interruptDisableFlag;
  public boolean accumulatorWidthFlag;
  public boolean indexWidthFlag;
  public boolean carryFlag;
  public boolean emulationFlag;
  public boolean overflowFlag;
  public boolean breakFlag;

  // Registers
  public int accumulator;
  public int xIndex;
  public int yIndex;
  public int dataBank;
  public int directPage;
  public Address programCounter;
  public int stackPointer;

  public boolean reset;
  public boolean irq;
  public boolean nmi;
  public boolean abort;

  public boolean clock;
  public boolean fallingEdge;
  public boolean risingEdge;

  public int cycle;
  public Instruction opCode;
  public boolean stopped;
  public boolean busEnable;

  //These are not the values on the pins, they are internal data.
  public Address address;
  public int data;
  public int directOffset;
  public Address newProgramCounter;

  public int abortProcessRegister;
  public int abortAccumulator;
  public int abortXIndex;
  public int abortYIndex;
  public int abortDataBank;
  public int abortDirectPage;
  public Address abortProgramCounter;
  public int abortStackPointer;

  public int time;

  public W65C816Snapshot(boolean zeroFlag,
                         boolean negativeFlag,
                         boolean decimalFlag,
                         boolean interruptDisableFlag,
                         boolean accumulatorWidthFlag,
                         boolean indexWidthFlag,
                         boolean carryFlag,
                         boolean emulationFlag,
                         boolean overflowFlag,
                         boolean breakFlag,
                         int accumulator,
                         int xIndex,
                         int yIndex,
                         int dataBank,
                         int directPage,
                         Address programCounter,
                         int stackPointer,
                         boolean clock,
                         boolean fallingEdge,
                         boolean risingEdge,
                         boolean reset,
                         boolean irq,
                         boolean nmi,
                         boolean abort,
                         boolean busEnable,
                         int cycle,
                         Instruction opCode,
                         boolean stopped,
                         Address address,
                         int data,
                         int directOffset,
                         Address newProgramCounter,
                         int abortProcessRegister,
                         int abortAccumulator,
                         int abortXIndex,
                         int abortYIndex,
                         int abortDataBank,
                         int abortDirectPage,
                         Address abortProgramCounter,
                         int abortStackPointer,
                         int time)
  {
    this.zeroFlag = zeroFlag;
    this.negativeFlag = negativeFlag;
    this.decimalFlag = decimalFlag;
    this.interruptDisableFlag = interruptDisableFlag;
    this.accumulatorWidthFlag = accumulatorWidthFlag;
    this.indexWidthFlag = indexWidthFlag;
    this.carryFlag = carryFlag;
    this.emulationFlag = emulationFlag;
    this.overflowFlag = overflowFlag;
    this.breakFlag = breakFlag;
    this.accumulator = accumulator;
    this.xIndex = xIndex;
    this.yIndex = yIndex;
    this.dataBank = dataBank;
    this.directPage = directPage;
    this.programCounter = programCounter;
    this.stackPointer = stackPointer;
    this.clock = clock;
    this.fallingEdge = fallingEdge;
    this.risingEdge = risingEdge;
    this.reset = reset;
    this.irq = irq;
    this.nmi = nmi;
    this.abort = abort;

    this.cycle = cycle;
    this.opCode = opCode;
    this.stopped = stopped;
    this.busEnable = busEnable;
    this.address = address;
    this.data = data;
    this.directOffset = directOffset;
    this.newProgramCounter = newProgramCounter;

    this.abortProcessRegister = abortProcessRegister;
    this.abortAccumulator = abortAccumulator;
    this.abortXIndex = abortXIndex;
    this.abortYIndex = abortYIndex;
    this.abortDataBank = abortDataBank;
    this.abortDirectPage = abortDirectPage;
    this.abortProgramCounter = abortProgramCounter;
    this.abortStackPointer = abortStackPointer;
    this.time = time;
  }
}

