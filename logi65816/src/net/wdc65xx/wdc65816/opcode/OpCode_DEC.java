package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_DEC
    extends OpCode
{
  public OpCode_DEC(int mCode, InstructionCycles busCycles)
  {
    super("DEC", "Decrement memory; result in memory and update NZ.", mCode, busCycles);
  }
}

