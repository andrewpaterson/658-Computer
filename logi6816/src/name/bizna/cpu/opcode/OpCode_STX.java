package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_STX
    extends OpCode
{
  public OpCode_STX(int mCode, InstructionCycles cycles)
  {
    super("STX", "Store Index X in Memory", mCode, cycles);
  }
}

