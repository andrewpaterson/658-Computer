package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_ORA
    extends OpCode
{
  public OpCode_ORA(int mCode, InstructionCycles cycles)
  {
    super("ORA", "Bitwise OR memory with A; result in A and update NZ.", mCode, cycles);
  }

  @Override
  public void execute1(Cpu65816 cpu)
  {
    if (cpu.isMemory8Bit())
    {
      int operand = cpu.getData();
      cpu.setA(cpu.getA() | operand);
    }
  }

  @Override
  public void execute2(Cpu65816 cpu)
  {
    if (cpu.isMemory16Bit())
    {
      int operand = cpu.getData();
      cpu.setA(cpu.getA() | operand);
    }
  }
}

