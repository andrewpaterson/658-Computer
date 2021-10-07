package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_PHK
    extends OpCode
{
  public OpCode_PHK(int mCode, InstructionCycles cycles)
  {
    super("PHK", "Push Program Bank Register on Stack", mCode, cycles);
  }
}

