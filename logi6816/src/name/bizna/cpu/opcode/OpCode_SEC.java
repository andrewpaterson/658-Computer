package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_SEC
    extends OpCode
{
  public OpCode_SEC(int mCode, InstructionCycles cycles)
  {
    super("SEC", "Set Carry Flag", mCode, cycles);
  }
}

