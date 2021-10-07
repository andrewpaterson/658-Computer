package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_PHY
    extends OpCode
{
  public OpCode_PHY(int mCode, InstructionCycles cycles)
  {
    super("PHY", "Push Index Y on Stack", mCode, cycles);
  }
}

