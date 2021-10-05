package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_SEC
    extends OpCode
{
  public OpCode_SEC(int mCode, InstructionCycles cycles)
  {
    super("SEC", "Set Carry Flag", mCode, cycles);
  }
}

