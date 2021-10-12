package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_ORA
    extends OpCode
{
  public OpCode_ORA(int mCode, InstructionCycles cycles)
  {
    super("ORA", "'OR' memory with A; result in A and update NZ.", mCode, cycles);
  }
}

