package net.wdc65xx.wdc65816;

import net.wdc65xx.wdc65816.instruction.Instruction;

public class CpuSnapshot
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

  public boolean clock;
  public boolean fallingEdge;
  public boolean risingEdge;
  public boolean reset;
  public int cycle;
  public Instruction opCode;
  public boolean stopped;

  //These are not the values on the pins, they are internal data.
  public Address address;
  public int data;
  public int directOffset;
  public Address newProgramCounter;
  public boolean read;

  public CpuSnapshot(boolean zeroFlag,
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
                     int cycle,
                     Instruction opCode,
                     boolean stopped,
                     Address address,
                     int data,
                     int directOffset,
                     Address newProgramCounter,
                     boolean read)
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
    this.cycle = cycle;
    this.opCode = opCode;
    this.stopped = stopped;
    this.address = address;
    this.data = data;
    this.directOffset = directOffset;
    this.newProgramCounter = newProgramCounter;
    this.read = read;
  }
}

