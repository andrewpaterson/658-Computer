package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_TYX
    extends OpCode
{
  public OpCode_TYX(int mCode, InstructionCycles cycles)
  {
    super("TYX", "Transfer Index Y to Index X", mCode, cycles);
  }
}

