package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;
import name.bizna.emu65816.addressingmode.StackSoftwareInterruptCycles;

public class OpCode_BRK
    extends OpCode
{
  public OpCode_BRK(int code, InstructionCycles instructionCycles)
  {
    super("BRK", "Force break software interrupt.", code, instructionCycles);
  }

  @Override
  public void execute1(Cpu65816 cpu)
  {
    cpu.BRK();
  }
}

