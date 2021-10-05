package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_TYX
    extends OpCode
{
  public OpCode_TYX(int mCode, InstructionCycles cycles)
  {
    super("TYX", "Transfer Index Y to Index X", mCode, cycles);
  }
}

