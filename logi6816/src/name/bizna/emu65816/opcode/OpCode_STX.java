package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_STX
    extends OpCode
{
  public OpCode_STX(int mCode, InstructionCycles cycles)
  {
    super("STX", "Store Index X in Memory", mCode, cycles);
  }
}

