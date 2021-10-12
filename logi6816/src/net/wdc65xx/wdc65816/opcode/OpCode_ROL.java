package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_ROL
    extends OpCode
{
  public OpCode_ROL(int mCode, InstructionCycles cycles)
  {
    super("ROL", "Rotate Memory One Bit Left.", mCode, cycles);
  }
}

