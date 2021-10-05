package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_TAY
    extends OpCode
{
  public OpCode_TAY(int mCode, InstructionCycles cycles)
  {
    super("TAY", "Transfer Accumulator in Index Y", mCode, cycles);
  }
}

