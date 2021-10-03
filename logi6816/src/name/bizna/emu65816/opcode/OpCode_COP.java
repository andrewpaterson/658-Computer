package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;
import name.bizna.emu65816.addressingmode.StackSoftwareInterruptCycles;

public class OpCode_COP
    extends OpCode
{
  public OpCode_COP(int code, InstructionCycles cycles)
  {
    super("COP", "Force co-processor software interrupt.", code, cycles);
  }

  @Override
  public void execute1(Cpu65816 cpu)
  {
    cpu.COP();
  }
}

