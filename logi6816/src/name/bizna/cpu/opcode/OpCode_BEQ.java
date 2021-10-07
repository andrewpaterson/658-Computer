package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_BEQ
    extends OpCode
{
  public OpCode_BEQ(int mCode, InstructionCycles cycles)
  {
    super("BEQ", "Branch if Equal (Z=1)", mCode, cycles);
  }
}

