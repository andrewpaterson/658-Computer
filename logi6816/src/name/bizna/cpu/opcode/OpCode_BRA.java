package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_BRA
    extends OpCode
{
  public OpCode_BRA(int mCode, InstructionCycles cycles)
  {
    super("BRA", "Branch Always", mCode, cycles);
  }
}

