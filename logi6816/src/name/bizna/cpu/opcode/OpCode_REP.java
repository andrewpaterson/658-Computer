package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_REP
    extends OpCode
{
  public OpCode_REP(int mCode, InstructionCycles cycles)
  {
    super("REP", "Reset Status Bits", mCode, cycles);
  }
}

