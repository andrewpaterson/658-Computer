package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_STZ
    extends OpCode
{
  public OpCode_STZ(int mCode, InstructionCycles cycles)
  {
    super("STZ", "Store Zero in Memory", mCode, cycles);
  }
}

