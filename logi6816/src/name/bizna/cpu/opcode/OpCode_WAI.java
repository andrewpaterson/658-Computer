package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_WAI
    extends OpCode
{
  public OpCode_WAI(int mCode, InstructionCycles cycles)
  {
    super("WAI", "Wait for Interrupt", mCode, cycles);
  }
}

