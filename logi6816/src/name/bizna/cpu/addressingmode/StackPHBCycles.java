package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;

import static name.bizna.cpu.AddressingMode.Stack;

public class StackPHBCycles
    extends InstructionCycles
{
  //22c
  public StackPHBCycles()
  {
    super(Stack,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(S()), E(Cpu65816::PHB), Write_DataLow(), SP_dec(), DONE()));
  }
}

