package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_BVC
    extends OpCode
{
  public OpCode_BVC(int mCode, InstructionCycles cycles)
  {
    super("BVC", "Branch on Overflow Clear (V=0)", mCode, cycles);
  }
}

