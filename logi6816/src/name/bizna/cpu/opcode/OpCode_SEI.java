package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_SEI
    extends OpCode
{
  public OpCode_SEI(int mCode, InstructionCycles cycles)
  {
    super("SEI", "Set Interrupt Disable Status", mCode, cycles);
  }
}

