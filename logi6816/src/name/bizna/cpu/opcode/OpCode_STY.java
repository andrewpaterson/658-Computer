package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_STY
    extends OpCode
{
  public OpCode_STY(int mCode, InstructionCycles cycles)
  {
    super("STY", "Store Index Y in Memory", mCode, cycles);
  }
}

