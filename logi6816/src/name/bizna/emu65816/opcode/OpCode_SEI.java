package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_SEI
    extends OpCode
{
  public OpCode_SEI(int mCode, InstructionCycles cycles)
  {
    super("SEI", "Set Interrupt Disable Status", mCode, cycles);
  }
}

