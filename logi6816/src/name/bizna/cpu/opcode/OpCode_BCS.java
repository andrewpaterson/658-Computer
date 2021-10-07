package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_BCS
    extends OpCode
{
  public OpCode_BCS(int mCode, InstructionCycles cycles)
  {
    super("BCS", "Branch on Carry Set (C=1)", mCode, cycles);
  }
}

