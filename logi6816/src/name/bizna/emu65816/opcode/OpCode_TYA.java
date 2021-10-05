package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_TYA
    extends OpCode
{
  public OpCode_TYA(int mCode, InstructionCycles cycles)
  {
    super("TYA", "Transfer Index Y to Accumulator", mCode, cycles);
  }
}

