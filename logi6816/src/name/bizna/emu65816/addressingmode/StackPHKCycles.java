package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.AddressingMode.Stack;

public class StackPHKCycles
    extends InstructionCycles
{
  //22c
  public StackPHKCycles()
  {
    super(Stack,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(S()), E(Cpu65816::PHK), Write_DataLow(), SP_dec(), DONE()));
  }
}

