package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_PHB
    extends OpCode
{
  public OpCode_PHB(int mCode, InstructionCycles cycles)
  {
    super("PHB", "Push Data Bank Register on Stack", mCode, cycles);
  }
}

