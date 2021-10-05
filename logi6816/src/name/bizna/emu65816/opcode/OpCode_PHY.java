package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_PHY
    extends OpCode
{
  public OpCode_PHY(int mCode, InstructionCycles cycles)
  {
    super("PHY", "Push Index Y on Stack", mCode, cycles);
  }
}

