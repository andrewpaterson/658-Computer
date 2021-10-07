package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_TRB
    extends OpCode
{
  public OpCode_TRB(int mCode, InstructionCycles cycles)
  {
    super("TRB", "Test and Reset Bit", mCode, cycles);
  }
}

