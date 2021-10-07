package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_PEI
    extends OpCode
{
  public OpCode_PEI(int mCode, InstructionCycles cycles)
  {
    super("PEI", "Push Indirect Address", mCode, cycles);
  }
}

