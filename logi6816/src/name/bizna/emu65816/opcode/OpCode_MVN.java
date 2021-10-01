package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

import static name.bizna.emu65816.Unsigned.toShort;

public class OpCode_MVN
    extends OpCode
{
  public OpCode_MVN(int mCode, InstructionCycles cycles)
  {
    super("MVN", "Block move next...", mCode, cycles);
  }

  @Override
  public void execute1(Cpu65816 cpu)
  {
    if (!cpu.blockMoveNext())
    {
      cpu.doneInstruction();
    }
  }
}

