package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_PHK
    extends OpCode
{
  public OpCode_PHK(int mCode, InstructionCycles cycles)
  {
    super("PHK", "Push Program Bank Register on Stack", mCode, cycles);
  }
}

