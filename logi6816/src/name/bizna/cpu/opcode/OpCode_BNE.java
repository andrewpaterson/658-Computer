package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_BNE
    extends OpCode
{
  public OpCode_BNE(int mCode, InstructionCycles cycles)
  {
    super("BNE", "Branch if Not Equal (Z=0)", mCode, cycles);
  }
}

