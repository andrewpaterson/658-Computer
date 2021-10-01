package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.AddressingMode.Stack;

public class StackPERCycles
    extends InstructionCycles
{
  //22f
  public StackPERCycles()
  {
    super(Stack,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_DataLow(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_DataHigh(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(S()), E(Cpu65816::PER), Write_DataHigh(), SP_dec()),
          new BusCycle(Address(S()), Write_DataLow(), SP_dec(), DONE()));
  }
}

