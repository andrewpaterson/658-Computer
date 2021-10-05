package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_TAX
    extends OpCode
{
  public OpCode_TAX(int mCode, InstructionCycles cycles)
  {
    super("TAX", "Transfer Accumulator in Index X", mCode, cycles);
  }
}

